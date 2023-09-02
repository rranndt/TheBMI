package dev.rranndt.bmicalculator

import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.rranndt.bmicalculator.ui.theme.CustomBlue
import dev.rranndt.bmicalculator.ui.theme.CustomGreen
import dev.rranndt.bmicalculator.ui.theme.CustomOrange
import dev.rranndt.bmicalculator.ui.theme.CustomRed
import dev.rranndt.bmicalculator.ui.theme.GrayBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMIScreen(
    state: BMIScreenState,
    userAction: (UserAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(skipPartiallyExpanded = true)
    )
    val context = LocalContext.current

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Hey Guys! Checkout my Body Mass Index: ${state.bmi} BMI, which is considered ${state.bmiStage}"
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetContent(
                sheetTitle = state.sheetTitle,
                sheetTitleList = state.sheetItemsList,
                onItemClicked = {
                    coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                    userAction(UserAction.OnSheetItemClicked(it))
                },
                onCancelClicked = {
                    coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                }
            )
        },
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        ),
        sheetPeekHeight = 0.dp,
    ) { paddingValues ->
        ScreenContent(
            state = state,
            onWeightUnitClicked = {
                userAction(UserAction.OnWeightTextClicked)
                coroutineScope.launch { scaffoldState.bottomSheetState.show() }
            },
            onHeightUnitClicked = {
                userAction(UserAction.OnHeightTextClicked)
                coroutineScope.launch { scaffoldState.bottomSheetState.show() }
            },
            onWeightValueClicked = { userAction(UserAction.OnWeightValueClicked) },
            onHeightValueClicked = { userAction(UserAction.OnHeightValueClicked) },
            onGoButtonClicked = { userAction(UserAction.OnGoButtonClicked(context)) },
            onACButtonClicked = { userAction(UserAction.OnAllClearButtonClicked) },
            onDeleteButtonClicked = { userAction(UserAction.OnDeleteButtonClicked) },
            onShareButtonClicked = { context.startActivity(shareIntent) },
            onNumberClicked = { userAction(UserAction.OnNumberClicked(it)) },
            modifier = Modifier.padding(paddingValues = paddingValues)
        )
    }
}

@Composable
fun ScreenContent(
    state: BMIScreenState,
    onWeightUnitClicked: () -> Unit,
    onHeightUnitClicked: () -> Unit,
    onWeightValueClicked: () -> Unit,
    onHeightValueClicked: () -> Unit,
    onGoButtonClicked: () -> Unit,
    onACButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
    onShareButtonClicked: () -> Unit,
    onNumberClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .background(GrayBackground)
            .padding(15.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "BMI Calculator",
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                UnitItem(
                    text = "Weight",
                    onClick = onWeightUnitClicked
                )
                InputUnitValue(
                    inputValue = state.weightValue,
                    inputUnit = state.weightUnit,
                    inputColor = if (state.weightValueStage != WeightValueStage.INACTIVE) CustomOrange
                    else Color.Black,
                    onUnitValueClicked = onWeightValueClicked
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                UnitItem(
                    text = "Height",
                    onClick = onHeightUnitClicked
                )
                InputUnitValue(
                    inputValue = state.heightValue,
                    inputUnit = state.heightUnit,
                    inputColor = if (state.heightValueStage != HeightValueStage.INACTIVE) CustomOrange
                    else Color.Black,
                    onUnitValueClicked = onHeightValueClicked
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Crossfade(
                targetState = state.shouldBMICardShow,
                label = ""
            ) {
                if (it) {
                    Column(verticalArrangement = Arrangement.SpaceBetween) {
                        BMIResultCard(
                            bmi = state.bmi,
                            bmiStage = state.bmiStage,
                            bmiStageColor = when (state.bmiStage) {
                                "Underweight" -> CustomBlue
                                "Normal" -> CustomGreen
                                else -> CustomRed
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ShareButton(
                            onClick = onShareButtonClicked,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    Divider()
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxSize()) {
                        NumberKeyboard(
                            onNumberClick = onNumberClicked,
                            modifier = Modifier.weight(7f)
                        )
                        Column(modifier = Modifier.weight(3f)) {
                            SymbolButton(
                                symbol = "AC",
                                onClick = onACButtonClicked
                            )
                            SymbolButtonWithIcon(onClick = onDeleteButtonClicked)
                            SymbolButton(
                                symbol = "GO",
                                onClick = onGoButtonClicked
                            )
                        }
                    }
                }
            }
        }
    }
}