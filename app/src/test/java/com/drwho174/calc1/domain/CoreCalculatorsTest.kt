package com.drwho174.calc1.domain

import org.junit.Assert.assertEquals
import org.junit.Test

private const val EPS = 1e-9

class CkdEpiTest {

    @Test
    fun `male 60 years with creatinine 88_4055 umol per l`() {
        assertEquals(
            86.16262077966914,
            estimateCkdEpi2021(creatinineUmolPerL = 88.4055, ageYears = 60.0, isFemale = false),
            EPS,
        )
    }

    @Test
    fun `female 70 years with creatinine 70 umol per l`() {
        assertEquals(
            80.19983361063514,
            estimateCkdEpi2021(creatinineUmolPerL = 70.0, ageYears = 70.0, isFemale = true),
            EPS,
        )
    }

    @Test
    fun `same creatinine gives sex specific coefficients`() {
        // У женщин и мужчин разные и a, и b, и половой коэффициент — поэтому
        // даже одинаковый креатинин даёт разные значения.
        assertEquals(
            106.4741528113815,
            estimateCkdEpi2021(60.0, 60.0, isFemale = false),
            EPS,
        )
        assertEquals(
            99.68827599336512,
            estimateCkdEpi2021(60.0, 60.0, isFemale = true),
            EPS,
        )
    }

    @Test
    fun `creatinine boundary for female switches exponent at 0_7 mg per dl`() {
        val atBoundary = estimateCkdEpi2021(0.7 * CREATININE_UMOL_PER_MG_DL, 40.0, isFemale = true)
        val above = estimateCkdEpi2021(0.71 * CREATININE_UMOL_PER_MG_DL, 40.0, isFemale = true)

        assertEquals(112.05430679879757, atBoundary, EPS)
        assertEquals(110.16310870388523, above, EPS)
    }

    @Test
    fun `creatinine boundary for male switches exponent at 0_9 mg per dl`() {
        val atBoundary = estimateCkdEpi2021(0.9 * CREATININE_UMOL_PER_MG_DL, 40.0, isFemale = false)
        val above = estimateCkdEpi2021(0.91 * CREATININE_UMOL_PER_MG_DL, 40.0, isFemale = false)

        assertEquals(110.7255996035549, atBoundary, EPS)
        assertEquals(109.26709105693676, above, EPS)
    }
}

class BsaTest {

    @Test
    fun `mosteller 170 cm 70 kg`() {
        assertEquals(1.8217549231441645, Bsa.mosteller(170.0, 70.0), EPS)
    }

    @Test
    fun `dubois 170 cm 70 kg`() {
        assertEquals(1.8097078017532471, Bsa.dubois(170.0, 70.0), EPS)
    }

    @Test
    fun `haycock 170 cm 70 kg`() {
        assertEquals(1.8256771247769754, Bsa.haycock(170.0, 70.0), EPS)
    }
}

class IdealBodyMassTest {

    @Test
    fun `male 180 cm`() {
        assertEquals(75.116, idealBodyMassKg(180.0, isFemale = false), EPS)
    }

    @Test
    fun `female 180 cm`() {
        assertEquals(70.616, idealBodyMassKg(180.0, isFemale = true), EPS)
    }

    @Test
    fun `152_4 cm gives base value only`() {
        assertEquals(50.0, idealBodyMassKg(152.4, isFemale = false), EPS)
        assertEquals(45.5, idealBodyMassKg(152.4, isFemale = true), EPS)
    }
}

class RespiratoryIndexTest {

    @Test
    fun `gradient ri and ratios for fio2 60 pao2 100 paco2 40`() {
        assertEquals(277.8, aaGradient(60.0, 100.0, 40.0), EPS)
        assertEquals(2.778, respiratoryIndex(60.0, 100.0, 40.0), EPS)
        assertEquals(166.66666666666666, pfRatio(60.0, 100.0), EPS)
        assertEquals(163.33333333333334, sfRatio(60.0, 98.0), EPS)
    }
}

class PerfusiologistTest {

    @Test
    fun `bsa index and speed for 170 cm 70 kg rate 2_4`() {
        val bsa = Perfusiologist.bodySurfaceArea(170.0, 70.0)
        assertEquals(1.8097078017532471, bsa, EPS)

        val index = Perfusiologist.perfusionIndex(2.4, bsa)
        assertEquals(4.343298724207793, index, EPS)
        assertEquals(4.343298724207793, Perfusiologist.perfusionSpeed(index, 100.0), EPS)
        assertEquals(92.09589885461055, Perfusiologist.percentForSpeed(index, 4.0), EPS)
    }

    @Test
    fun `slider snapping matches original behaviour`() {
        // 92.096 / 5 = 18.419 → 18 * 5 = 90
        assertEquals(90, Perfusiologist.snapPercent(92.09589885461055))
        // 4.3433 → 43 → 4.3
        assertEquals(4.3, Perfusiologist.snapSpeed(4.343298724207793)!!, EPS)
        assertEquals(null, Perfusiologist.snapPercent(300.0))
        assertEquals(null, Perfusiologist.snapSpeed(9.0))
    }
}
