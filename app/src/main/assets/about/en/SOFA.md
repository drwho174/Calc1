# SOFA — Sequential Organ Failure Assessment

The score assesses the severity of organ dysfunction in an intensive care
patient. For each of the six systems the **worst value of the day** is taken and
scored from 0 to 4 points; the total ranges from 0 to 24.

### Systems and thresholds

| System | 0 | 1 | 2 | 3 | 4 |
|---|---|---|---|---|---|
| Respiration, PaO2/FiO2 | ≥ 400 | < 400 | < 300 | < 200 on ventilation | < 100 on ventilation |
| Coagulation, platelets ×10⁹/L | ≥ 150 | < 150 | < 100 | < 50 | < 20 |
| Liver, bilirubin µmol/L | < 20 | 20–32 | 33–101 | 102–204 | > 204 |
| Haemodynamics | MAP ≥ 70 | MAP < 70 | dopamine ≤ 5 or dobutamine | dopamine > 5 or adrenaline ≤ 0.1 | dopamine > 15 or adrenaline > 0.1 |
| CNS, GCS | 15 | 13–14 | 10–12 | 6–9 | < 6 |
| Kidneys, creatinine µmol/L | < 110 | 110–170 | 171–299 | 300–440 or urine output < 500 mL | > 440 or urine output < 200 mL |

Vasopressor doses are given in µg/kg/min.

### Interpretation

A SOFA increase of 2 points or more in a patient with infection corresponds to
organ dysfunction in the Sepsis-3 definition of sepsis. The approximate
hospital mortality rises with the total score: below 9 % at 0–1 points and above
73 % at 16 and higher. In the app the mortality ranges are shown as intervals
rather than as an exact prognosis for an individual patient.

### Sources

- Vincent JL, Moreno R, Takala J, et al. The SOFA (Sepsis-related Organ
  Failure Assessment) score to describe organ dysfunction/failure.
  *Intensive Care Med.* 1996;22(7):707–710.
  [PubMed 8844239](https://pubmed.ncbi.nlm.nih.gov/8844239/)
- Ferreira FL, Bota DP, Bross A, Mélot C, Vincent JL. Serial evaluation of the
  SOFA score to predict outcome in critically ill patients.
  *JAMA.* 2001;286(14):1754–1758.
  [PubMed 11594901](https://pubmed.ncbi.nlm.nih.gov/11594901/)
- Singer M, Deutschman CS, Seymour CW, et al. The Third International Consensus
  Definitions for Sepsis and Septic Shock (Sepsis-3).
  *JAMA.* 2016;315(8):801–810.
  [PubMed 26903338](https://pubmed.ncbi.nlm.nih.gov/26903338/)
