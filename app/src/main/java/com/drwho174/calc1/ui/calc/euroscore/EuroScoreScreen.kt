package com.drwho174.calc1.ui.calc.euroscore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R
import com.drwho174.calc1.domain.EjectionFraction
import com.drwho174.calc1.domain.EuroScore2Input
import com.drwho174.calc1.domain.NyhaClass
import com.drwho174.calc1.domain.ProcedureWeight
import com.drwho174.calc1.domain.PulmonaryArteryPressure
import com.drwho174.calc1.domain.RenalDisease
import com.drwho174.calc1.domain.Urgency
import com.drwho174.calc1.domain.euroScore2PredictedMortality
import com.drwho174.calc1.domain.renalDiseaseFor
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.components.BottomResultBar
import com.drwho174.calc1.ui.components.ChoiceGroup
import com.drwho174.calc1.ui.components.CkdEpiSheet
import com.drwho174.calc1.ui.components.NumberField
import com.drwho174.calc1.ui.components.SectionCard
import com.drwho174.calc1.ui.theme.Calc1Theme

/**
 * EuroSCORE II.
 *
 * Порядок и цветовые блоки повторяют прежнюю версию: возраст, пол, диабет,
 * лёгкие, мобильность, почечная дисфункция (с кнопкой СКФ внутри блока),
 * критическое состояние, NYHA, стенокардия, поражение артерий, повторность,
 * эндокардит, фракция выброса, ИМ, давление в лёгочной артерии, срочность,
 * сложность вмешательства, аорта.
 */
@Composable
fun EuroScoreScreen(modifier: Modifier = Modifier) {
    var age by rememberSaveable { mutableStateOf("") }
    var isFemale by rememberSaveable { mutableStateOf(false) }
    var diabetes by rememberSaveable { mutableStateOf(false) }
    var pulmonaryDysfunction by rememberSaveable { mutableStateOf(false) }
    var poorMobility by rememberSaveable { mutableStateOf(false) }
    var criticalState by rememberSaveable { mutableStateOf(false) }
    var anginaClass4 by rememberSaveable { mutableStateOf(false) }
    var extracardiacArteriopathy by rememberSaveable { mutableStateOf(false) }
    var previousCardiacSurgery by rememberSaveable { mutableStateOf(false) }
    var activeEndocarditis by rememberSaveable { mutableStateOf(false) }
    var recentMyocardialInfarction by rememberSaveable { mutableStateOf(false) }
    var aortaSurgery by rememberSaveable { mutableStateOf(false) }
    var showCkdSheet by rememberSaveable { mutableStateOf(false) }

    var renalDisease by remember { mutableStateOf(RenalDisease.NONE) }
    var nyha by remember { mutableStateOf(NyhaClass.I) }
    var ejectionFraction by remember { mutableStateOf(EjectionFraction.GOOD) }
    var pulmonaryPressure by remember { mutableStateOf(PulmonaryArteryPressure.LOW) }
    var urgency by remember { mutableStateOf(Urgency.ELECTIVE) }
    var procedureWeight by remember { mutableStateOf(ProcedureWeight.CABG) }

    val ageYears = age.toDoubleOrNull()

    val mortality = if (ageYears != null) {
        euroScore2PredictedMortality(
            EuroScore2Input(
                ageYears = ageYears,
                isFemale = isFemale,
                diabetes = diabetes,
                pulmonaryDysfunction = pulmonaryDysfunction,
                poorMobility = poorMobility,
                renalDisease = renalDisease,
                criticalPreoperativeState = criticalState,
                nyha = nyha,
                anginaClass4 = anginaClass4,
                extracardiacArteriopathy = extracardiacArteriopathy,
                previousCardiacSurgery = previousCardiacSurgery,
                activeEndocarditis = activeEndocarditis,
                ejectionFraction = ejectionFraction,
                recentMyocardialInfarction = recentMyocardialInfarction,
                pulmonaryArteryPressure = pulmonaryPressure,
                urgency = urgency,
                procedureWeight = procedureWeight,
                aortaSurgery = aortaSurgery,
            )
        )
    } else {
        null
    }

    val maleLabel = stringResource(R.string.Male)
    val femaleLabel = stringResource(R.string.Female)

    val renalLabels = mapOf(
        RenalDisease.NONE to stringResource(R.string.noRenalDisfunction),
        RenalDisease.MILD to stringResource(R.string.mildRenalDisfunction),
        RenalDisease.SEVERE to stringResource(R.string.severeRenalDisfunction),
        RenalDisease.DIALYSIS to stringResource(R.string.dialisysRenalDisfunction),
    )
    val nyhaLabels = mapOf(
        NyhaClass.I to stringResource(R.string.NYHA1),
        NyhaClass.II to stringResource(R.string.NYHA2),
        NyhaClass.III to stringResource(R.string.NYHA3),
        NyhaClass.IV to stringResource(R.string.NYHA4),
    )
    val ejectionLabels = mapOf(
        EjectionFraction.GOOD to stringResource(R.string.goodEF),
        EjectionFraction.MODERATE to stringResource(R.string.moderateEF),
        EjectionFraction.POOR to stringResource(R.string.poorEF),
        EjectionFraction.VERY_POOR to stringResource(R.string.veryPoorEF),
    )
    val pressureLabels = mapOf(
        PulmonaryArteryPressure.LOW to stringResource(R.string.noPulmonaryHipertension),
        PulmonaryArteryPressure.MODERATE to stringResource(R.string.moderatePulmonaryHipertension),
        PulmonaryArteryPressure.HIGH to stringResource(R.string.severePulmonaryHipertension),
    )
    val urgencyLabels = mapOf(
        Urgency.ELECTIVE to stringResource(R.string.elective),
        Urgency.URGENT to stringResource(R.string.urgent),
        Urgency.EMERGENCY to stringResource(R.string.emergency),
        Urgency.SALVAGE to stringResource(R.string.salvage),
    )
    val procedureLabels = mapOf(
        ProcedureWeight.CABG to stringResource(R.string.cabg),
        ProcedureWeight.NON_CABG to stringResource(R.string.nonCabg),
        ProcedureWeight.TWO_PROCEDURES to stringResource(R.string.twoProcedures),
        ProcedureWeight.THREE_PROCEDURES to stringResource(R.string.threeProcedures),
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NumberField(
                value = age,
                onValueChange = { age = it },
                label = stringResource(R.string.age),
                suffix = stringResource(R.string.unit_years),
            )

            ChoiceGroup(
                title = stringResource(R.string.Sex),
                options = listOf(false, true),
                selected = isFemale,
                onSelect = { isFemale = it },
                label = { female -> if (female) femaleLabel else maleLabel },
                horizontal = true,
            )

            YesNoGroup(
                title = stringResource(R.string.insulin_dependent_diabetis_melitus),
                checked = diabetes,
                onCheckedChange = { diabetes = it },
            )
            YesNoGroup(
                title = stringResource(R.string.pulmonaryDisfunction),
                checked = pulmonaryDysfunction,
                onCheckedChange = { pulmonaryDysfunction = it },
            )
            YesNoGroup(
                title = stringResource(R.string.poorMobility),
                checked = poorMobility,
                onCheckedChange = { poorMobility = it },
            )

            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.renalDisfunction),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = { showCkdSheet = true }) {
                        Text(stringResource(R.string.creatinineClearance))
                    }
                }
                ChoiceGroup(
                    options = RenalDisease.entries,
                    selected = renalDisease,
                    onSelect = { renalDisease = it },
                    label = { disease -> renalLabels.getValue(disease) },
                )
            }

            YesNoGroup(
                title = stringResource(R.string.criticalPreopState),
                checked = criticalState,
                onCheckedChange = { criticalState = it },
            )

            SectionCard {
                ChoiceGroup(
                    title = stringResource(R.string.NYHAclass),
                    options = NyhaClass.entries,
                    selected = nyha,
                    onSelect = { nyha = it },
                    label = { value -> nyhaLabels.getValue(value) },
                )
            }

            YesNoGroup(
                title = stringResource(R.string.anginaClass4),
                checked = anginaClass4,
                onCheckedChange = { anginaClass4 = it },
            )
            YesNoGroup(
                title = stringResource(R.string.extracardiacArtheriopatia),
                checked = extracardiacArteriopathy,
                onCheckedChange = { extracardiacArteriopathy = it },
            )
            YesNoGroup(
                title = stringResource(R.string.previousCardiacSurgery),
                checked = previousCardiacSurgery,
                onCheckedChange = { previousCardiacSurgery = it },
            )
            YesNoGroup(
                title = stringResource(R.string.activeEndocarditis),
                checked = activeEndocarditis,
                onCheckedChange = { activeEndocarditis = it },
            )

            SectionCard {
                ChoiceGroup(
                    title = stringResource(R.string.ejectionFraction),
                    options = EjectionFraction.entries,
                    selected = ejectionFraction,
                    onSelect = { ejectionFraction = it },
                    label = { value -> ejectionLabels.getValue(value) },
                )
            }

            YesNoGroup(
                title = stringResource(R.string.recentMI),
                checked = recentMyocardialInfarction,
                onCheckedChange = { recentMyocardialInfarction = it },
            )

            SectionCard {
                ChoiceGroup(
                    title = stringResource(R.string.pulmonaryArteryPressure),
                    options = PulmonaryArteryPressure.entries,
                    selected = pulmonaryPressure,
                    onSelect = { pulmonaryPressure = it },
                    label = { value -> pressureLabels.getValue(value) },
                )
            }

            SectionCard {
                ChoiceGroup(
                    title = stringResource(R.string.urgency),
                    options = Urgency.entries,
                    selected = urgency,
                    onSelect = { urgency = it },
                    label = { value -> urgencyLabels.getValue(value) },
                )
            }

            SectionCard {
                ChoiceGroup(
                    title = stringResource(R.string.procedureWeight),
                    options = ProcedureWeight.entries,
                    selected = procedureWeight,
                    onSelect = { procedureWeight = it },
                    label = { value -> procedureLabels.getValue(value) },
                )
            }

            YesNoGroup(
                title = stringResource(R.string.aortaSurgery),
                checked = aortaSurgery,
                onCheckedChange = { aortaSurgery = it },
            )

            // Место под закреплённую панель результата, чтобы последние поля
            // не прятались за ней при прокрутке.
            Spacer(Modifier.height(150.dp))
        }

        BottomResultBar(
            title = stringResource(R.string.name_euroscore_2),
            value = mortality?.format(2) ?: "—",
            caption = stringResource(
                R.string.result_with_unit,
                stringResource(R.string.predicted_mortality),
                "%",
            ),
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }

    if (showCkdSheet) {
        CkdEpiSheet(
            onDismiss = { showCkdSheet = false },
            onResult = { gfr ->
                renalDisease = renalDiseaseFor(gfr, dialysis = false)
            },
        )
    }
}

@Composable
private fun YesNoGroup(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val yesLabel = stringResource(R.string.positive)
    val noLabel = stringResource(R.string.negative)

    ChoiceGroup(
        title = title,
        options = listOf(true, false),
        selected = checked,
        onSelect = onCheckedChange,
        label = { value -> if (value) yesLabel else noLabel },
        horizontal = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun EuroScoreScreenPreview() {
    Calc1Theme {
        EuroScoreScreen()
    }
}
