package com.drwho174.calc1.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private const val EPS = 1e-9

private fun sampleHbInput(
    heightCm: Double = 170.0,
    weightKg: Double = 70.0,
    isFemale: Boolean = false,
) = HbPerfusionInput(
    heightCm = heightCm,
    weightKg = weightKg,
    isFemale = isFemale,
    hemoglobinGPerL = 120.0,
    infusionMl = 1000.0,
    exfusionMl = 500.0,
    diuresisMl = 500.0,
    cpbExfusionMl = 300.0,
    primeMl = 1500.0,
)

class HbPerfusionTest {

    @Test
    fun `male 170 cm 70 kg reference scenario`() {
        val result = calculateHbPerfusion(sampleHbInput())

        assertEquals(108.39171237913595, result.beforeCpbAllen, EPS)
        assertEquals(82.86263541553463, result.afterCpbAllen, EPS)
        assertEquals(107.12742200236814, result.beforeCpbNadler, EPS)
        assertEquals(79.7107377472973, result.afterCpbNadler, EPS)
    }

    @Test
    fun `after cpb is always lower than before cpb`() {
        val male = calculateHbPerfusion(sampleHbInput())
        val female = calculateHbPerfusion(sampleHbInput(isFemale = true))

        assertTrue(male.afterCpbAllen < male.beforeCpbAllen)
        assertTrue(male.afterCpbNadler < male.beforeCpbNadler)
        assertTrue(female.afterCpbAllen < female.beforeCpbAllen)
        assertTrue(female.afterCpbNadler < female.beforeCpbNadler)
    }

    @Test
    fun `allen and nadler produce different blood volumes`() {
        val result = calculateHbPerfusion(sampleHbInput())
        assertFalse(result.beforeCpbAllen == result.beforeCpbNadler)
    }

    @Test
    fun `female blood volume is lower than male for the same body size`() {
        val male = calculateHbPerfusion(sampleHbInput(isFemale = false))
        val female = calculateHbPerfusion(sampleHbInput(isFemale = true))

        assertTrue(female.beforeCpbAllen < male.beforeCpbAllen)
        assertTrue(female.beforeCpbNadler < male.beforeCpbNadler)
    }

    @Test
    fun `zero volume changes leave hemoglobin unchanged`() {
        val noLoss = HbPerfusionInput(
            heightCm = 170.0,
            weightKg = 70.0,
            isFemale = false,
            hemoglobinGPerL = 120.0,
            infusionMl = 0.0,
            exfusionMl = 0.0,
            diuresisMl = 0.0,
            cpbExfusionMl = 0.0,
            primeMl = 0.0,
        )

        val result = calculateHbPerfusion(noLoss)
        assertEquals(120.0, result.beforeCpbAllen, EPS)
        assertEquals(120.0, result.afterCpbAllen, EPS)
    }
}

class OxygenDeliveryTest {

    private val sample = OxygenDeliveryInput(
        bsaM2 = 1.8,
        cardiacOutputLMin = 5.0,
        hemoglobinGPerL = 120.0,
        spo2Percent = 98.0,
        pao2MmHg = 100.0,
        etco2Percent = 35.0,
        freshGasFlowLMin = 2.0,
    )

    @Test
    fun `reference scenario`() {
        val result = calculateOxygenDelivery(sample)

        assertEquals(16.0584, result.arterialOxygenContent, EPS)
        assertEquals(802.92, result.oxygenDelivery, EPS)
        assertEquals(446.0666666666668, result.oxygenDeliveryIndex, EPS)
        assertEquals(51.16959064327485, result.co2ProductionIndex, EPS)
        assertEquals(8.717417142857146, result.do2ToVco2Ratio, EPS)
    }

    @Test
    fun `thresholds mark adequate delivery`() {
        val result = calculateOxygenDelivery(sample)
        assertTrue(result.oxygenDeliveryIsAdequate)
        assertTrue(result.ratioIsAdequate)
    }

    @Test
    fun `low delivery is flagged as inadequate`() {
        val result = calculateOxygenDelivery(
            sample.copy(cardiacOutputLMin = 1.0, hemoglobinGPerL = 60.0)
        )

        assertFalse(result.oxygenDeliveryIsAdequate)
        assertFalse(result.ratioIsAdequate)
    }

    @Test
    fun `do2i threshold is strict greater than 270`() {
        // Собираем доставку так, чтобы DO2i был чуть ниже и чуть выше порога.
        val below = sample.copy(cardiacOutputLMin = 5.0, bsaM2 = 5.0)
        val above = sample.copy(cardiacOutputLMin = 5.0, bsaM2 = 1.5)

        val belowIndex = calculateOxygenDelivery(below).oxygenDeliveryIndex
        val aboveIndex = calculateOxygenDelivery(above).oxygenDeliveryIndex

        assertFalse(calculateOxygenDelivery(below.copy(bsaM2 = 5.0)).oxygenDeliveryIsAdequate)
        assertTrue(calculateOxygenDelivery(above).oxygenDeliveryIsAdequate)
        assertTrue(belowIndex < 270.0)
        assertTrue(aboveIndex > 270.0)
    }
}
