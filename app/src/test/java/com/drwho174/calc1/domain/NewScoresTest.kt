package com.drwho174.calc1.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class CharlsonComorbidityIndexTest {

    @Test
    fun agePointsStartAtFifty() {
        assertEquals(0, charlsonAgePoints(49))
        assertEquals(1, charlsonAgePoints(50))
        assertEquals(2, charlsonAgePoints(60))
        assertEquals(4, charlsonAgePoints(80))
        assertEquals(5, charlsonAgePoints(90))
    }

    @Test
    fun indexSumsConditionsAndAge() {
        val input = CharlsonInput(
            conditions = setOf(
                CharlsonCondition.MYOCARDIAL_INFARCTION,
                CharlsonCondition.MODERATE_RENAL_DISEASE,
                CharlsonCondition.METASTATIC_TUMOR,
            ),
            age = 65,
        )

        // 1 + 2 + 6 состояния, плюс 2 балла за возраст.
        assertEquals(11, charlsonIndex(input))
    }

    @Test
    fun emptyHistoryUsesOnlyAge() {
        assertEquals(1, charlsonIndex(CharlsonInput(emptySet(), age = 55)))
        assertEquals(0, charlsonIndex(CharlsonInput(emptySet(), age = 30)))
    }

    @Test
    fun tenYearSurvivalFollowsPublishedTable() {
        assertEquals(99.0, charlsonTenYearSurvival(0), 0.001)
        assertEquals(96.0, charlsonTenYearSurvival(1), 0.001)
        assertEquals(90.0, charlsonTenYearSurvival(2), 0.001)
        assertEquals(77.0, charlsonTenYearSurvival(3), 0.001)
        assertEquals(53.0, charlsonTenYearSurvival(4), 0.001)
        assertEquals(21.0, charlsonTenYearSurvival(5), 0.001)
        assertEquals(2.0, charlsonTenYearSurvival(6), 0.001)
        assertEquals(2.0, charlsonTenYearSurvival(12), 0.001)
    }

    @Test
    fun severityBands() {
        assertEquals(CharlsonSeverity.LOW, charlsonSeverity(2))
        assertEquals(CharlsonSeverity.MODERATE, charlsonSeverity(3))
        assertEquals(CharlsonSeverity.HIGH, charlsonSeverity(5))
    }
}

class SofaScoreTest {

    @Test
    fun startsAtZeroWhenNothingSelected() {
        assertEquals(0, sofaScore(emptyMap()))
        assertEquals(0, sofaScore(SofaOrgan.entries.associateWith { 0 }))
    }

    @Test
    fun sumsOrgansAndClampsEachToFour() {
        val points = mapOf(
            SofaOrgan.RESPIRATION to 4,
            SofaOrgan.COAGULATION to 3,
            SofaOrgan.LIVER to 2,
            SofaOrgan.CARDIOVASCULAR to 1,
            SofaOrgan.CENTRAL_NERVOUS to 0,
            SofaOrgan.RENAL to 4,
        )

        assertEquals(14, sofaScore(points))
        assertEquals(SOFA_MAX_POINTS, sofaScore(SofaOrgan.entries.associateWith { 99 }))
    }

    @Test
    fun mortalityRangesCoverWholeScale() {
        assertEquals("< 9", sofaMortalityRange(0))
        assertEquals("< 9", sofaMortalityRange(1))
        assertEquals("9–16", sofaMortalityRange(2))
        assertEquals("44–53", sofaMortalityRange(11))
        assertEquals("> 73", sofaMortalityRange(16))
        assertEquals("> 73", sofaMortalityRange(SOFA_MAX_POINTS))
    }

    @Test
    fun severityBands() {
        assertEquals(SofaSeverity.LOW, sofaSeverity(0))
        assertEquals(SofaSeverity.MODERATE, sofaSeverity(4))
        assertEquals(SofaSeverity.HIGH, sofaSeverity(8))
        assertEquals(SofaSeverity.VERY_HIGH, sofaSeverity(14))
    }
}

class ElGanzouriIndexTest {

    @Test
    fun bestProfileScoresZero() {
        assertEquals(0, elGanzouriScore(ElGanzouriInput()))
    }

    @Test
    fun worstProfileScoresEleven() {
        val input = ElGanzouriInput(
            mallampati = MallampatiClass.FOURTH,
            thyromental = ThyromentalDistance.BELOW_SIX,
            mouthOpening = MouthOpening.BELOW_FIVE,
            neckMovement = NeckMovement.BELOW_EIGHTY,
            prognathia = Prognathia.IMPOSSIBLE,
            weight = BodyWeightBand.ABOVE_120,
            difficultIntubation = DifficultIntubationHistory.PRESENT,
        )

        assertEquals(11, elGanzouriScore(input))
    }

    @Test
    fun riskClassesFollowThresholds() {
        assertEquals(AirwayRiskClass.LOW, elGanzouriRiskClass(0))
        assertEquals(AirwayRiskClass.LOW, elGanzouriRiskClass(2))
        assertEquals(AirwayRiskClass.MODERATE, elGanzouriRiskClass(3))
        assertEquals(AirwayRiskClass.MODERATE, elGanzouriRiskClass(4))
        assertEquals(AirwayRiskClass.HIGH, elGanzouriRiskClass(5))
        assertEquals(AirwayRiskClass.HIGH, elGanzouriRiskClass(11))
    }
}

class AriscatScoreTest {

    @Test
    fun bestProfileScoresZero() {
        assertEquals(0, ariscatScore(AriscatInput()))
    }

    @Test
    fun worstProfileScoresOneHundredTwentyThree() {
        val input = AriscatInput(
            age = AriscatAge.ABOVE_80,
            saturation = AriscatSaturation.UP_TO_90,
            respiratoryInfectionLastMonth = true,
            anemia = true,
            incision = AriscatIncision.INTRATHORACIC,
            duration = AriscatDuration.ABOVE_THREE_HOURS,
            emergency = true,
        )

        assertEquals(123, ariscatScore(input))
    }

    @Test
    fun riskClassesFollowThresholds() {
        assertEquals(AriscatRisk.LOW, ariscatRisk(25))
        assertEquals(AriscatRisk.MODERATE, ariscatRisk(26))
        assertEquals(AriscatRisk.MODERATE, ariscatRisk(44))
        assertEquals(AriscatRisk.HIGH, ariscatRisk(45))
        assertEquals(AriscatRisk.HIGH, ariscatRisk(123))
    }
}

class LeeRiskIndexTest {

    @Test
    fun countsPredictors() {
        assertEquals(0, leeRiskScore(LeeRiskInput()))
        assertEquals(
            3,
            leeRiskScore(
                LeeRiskInput(
                    highRiskSurgery = true,
                    ischemicHeartDisease = true,
                    insulinTherapy = true,
                ),
            ),
        )
        assertEquals(
            6,
            leeRiskScore(
                LeeRiskInput(
                    highRiskSurgery = true,
                    ischemicHeartDisease = true,
                    heartFailure = true,
                    cerebrovascularDisease = true,
                    insulinTherapy = true,
                    creatinineAbove177 = true,
                ),
            ),
        )
    }

    @Test
    fun maceRiskFollowsValidationCohort() {
        assertEquals(0.4, leeMaceRiskPercent(0), 0.001)
        assertEquals(0.9, leeMaceRiskPercent(1), 0.001)
        assertEquals(7.0, leeMaceRiskPercent(2), 0.001)
        assertEquals(11.0, leeMaceRiskPercent(3), 0.001)
        assertEquals(11.0, leeMaceRiskPercent(6), 0.001)
    }

    @Test
    fun riskClasses() {
        assertEquals(LeeRiskClass.LOW, leeRiskClass(0))
        assertEquals(LeeRiskClass.MODERATE, leeRiskClass(1))
        assertEquals(LeeRiskClass.HIGH, leeRiskClass(2))
        assertEquals(LeeRiskClass.VERY_HIGH, leeRiskClass(3))
    }
}
