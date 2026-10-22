package com.drwho174.calc1.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private const val EPS = 1e-9

class EuroScore2Test {

    @Test
    fun `70 year old male with no risk factors`() {
        val input = EuroScore2Input(ageYears = 70.0, isFemale = false)

        assertEquals(-5.0108379, euroScore2FactorsSum(input), EPS)
        assertEquals(0.6621183814400584, euroScore2PredictedMortality(input), 1e-6)
    }

    @Test
    fun `80 year old female with combined risk factors`() {
        val input = EuroScore2Input(
            ageYears = 80.0,
            isFemale = true,
            diabetes = true,
            renalDisease = RenalDisease.SEVERE,
            nyha = NyhaClass.III,
            ejectionFraction = EjectionFraction.POOR,
            urgency = Urgency.URGENT,
            procedureWeight = ProcedureWeight.TWO_PROCEDURES,
            aortaSurgery = true,
        )

        assertEquals(-0.665932, euroScore2FactorsSum(input), EPS)
        assertEquals(33.94083316407266, euroScore2PredictedMortality(input), 1e-6)
    }

    @Test
    fun `age factor is flat up to 60 then grows`() {
        assertEquals(1.0, euroScore2AgeFactor(60.0), EPS)
        assertEquals(1.0, euroScore2AgeFactor(30.0), EPS)
        assertEquals(11.0, euroScore2AgeFactor(70.0), EPS)
    }

    @Test
    fun `renal disease mapping follows the ckd dialog thresholds`() {
        assertEquals(RenalDisease.NONE, renalDiseaseFor(86.0, dialysis = false))
        assertEquals(RenalDisease.MILD, renalDiseaseFor(85.0, dialysis = false))
        assertEquals(RenalDisease.MILD, renalDiseaseFor(51.0, dialysis = false))
        assertEquals(RenalDisease.SEVERE, renalDiseaseFor(50.0, dialysis = false))
        assertEquals(RenalDisease.DIALYSIS, renalDiseaseFor(10.0, dialysis = true))
    }
}

class MehranScoreTest {

    private fun emptyInput(): MehranScoreInput = MehranScoreInput(
        contrastVolumeMl = 0.0,
        egfrCategory = EgfrCategory.ABOVE_60,
    )

    @Test
    fun `contrast volume gives one point per 100 ml`() {
        val base = emptyInput()
        assertEquals(2, mehranPoints(base.copy(contrastVolumeMl = 200.0)))
        assertEquals(3, mehranPoints(base.copy(contrastVolumeMl = 250.0)))
        assertEquals(0, mehranPoints(base.copy(contrastVolumeMl = 40.0)))
    }

    @Test
    fun `egfr category contributes its table points`() {
        val base = emptyInput()
        assertEquals(0, mehranPoints(base.copy(egfrCategory = EgfrCategory.ABOVE_60)))
        assertEquals(2, mehranPoints(base.copy(egfrCategory = EgfrCategory.FROM_40_TO_60)))
        assertEquals(4, mehranPoints(base.copy(egfrCategory = EgfrCategory.FROM_20_TO_40)))
        assertEquals(6, mehranPoints(base.copy(egfrCategory = EgfrCategory.BELOW_20)))
    }

    @Test
    fun `risk tables match the published thresholds`() {
        assertEquals(7.5, mehranCinRiskPercent(5), EPS)
        assertEquals(0.04, mehranDialysisRiskPercent(5), EPS)

        assertEquals(14.0, mehranCinRiskPercent(6), EPS)
        assertEquals(14.0, mehranCinRiskPercent(10), EPS)
        assertEquals(0.12, mehranDialysisRiskPercent(10), EPS)

        assertEquals(26.1, mehranCinRiskPercent(11), EPS)
        assertEquals(26.1, mehranCinRiskPercent(15), EPS)
        assertEquals(1.09, mehranDialysisRiskPercent(15), EPS)

        assertEquals(57.3, mehranCinRiskPercent(16), EPS)
        assertEquals(12.6, mehranDialysisRiskPercent(16), EPS)
    }

    @Test
    fun `egfr category boundaries`() {
        assertEquals(EgfrCategory.ABOVE_60, egfrCategoryFor(60.1))
        assertEquals(EgfrCategory.FROM_40_TO_60, egfrCategoryFor(60.0))
        assertEquals(EgfrCategory.FROM_40_TO_60, egfrCategoryFor(40.0))
        assertEquals(EgfrCategory.FROM_20_TO_40, egfrCategoryFor(20.0))
        assertEquals(EgfrCategory.BELOW_20, egfrCategoryFor(19.9))
    }

    @Test
    fun `risk grows monotonically with points`() {
        var previous = 0.0
        for (points in 0..20) {
            val current = mehranCinRiskPercent(points)
            assertTrue(current >= previous)
            previous = current
        }
        assertFalse(mehranDialysisRiskPercent(0) > mehranDialysisRiskPercent(20))
    }
}
