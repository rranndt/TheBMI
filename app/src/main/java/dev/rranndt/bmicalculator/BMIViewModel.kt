package dev.rranndt.bmicalculator

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

class BMIViewModel : ViewModel() {
    private val _state = MutableStateFlow(BMIScreenState())
    val state = _state.asStateFlow()

    fun onAction(userAction: UserAction) {
        when (userAction) {
            UserAction.OnWeightValueClicked -> {
                _state.update { currentState ->
                    currentState.copy(
                        weightValueStage = WeightValueStage.ACTIVE,
                        heightValueStage = HeightValueStage.INACTIVE,
                        shouldBMICardShow = false
                    )
                }
            }

            UserAction.OnHeightValueClicked -> {
                _state.update { currentState ->
                    currentState.copy(
                        heightValueStage = HeightValueStage.ACTIVE,
                        weightValueStage = WeightValueStage.INACTIVE,
                        shouldBMICardShow = false
                    )
                }
            }

            UserAction.OnWeightTextClicked -> {
                _state.update { currentState ->
                    currentState.copy(
                        sheetTitle = "Weight",
                        sheetItemsList = listOf("Kilograms", "Pounds")
                    )
                }
            }

            UserAction.OnHeightTextClicked -> {
                _state.update { currentState ->
                    currentState.copy(
                        sheetTitle = "Height",
                        sheetItemsList = listOf("Centimeter", "Meter", "Feet", "Inches")
                    )
                }
            }

            is UserAction.OnGoButtonClicked -> calculateBMI(userAction.context)

            is UserAction.OnNumberClicked -> enterNumber(userAction.number)

            UserAction.OnAllClearButtonClicked -> allClearToZero()

            UserAction.OnDeleteButtonClicked -> deleteLastDigit()

            is UserAction.OnSheetItemClicked -> changeWeightOrHeightUnit(userAction.sheetItem)
        }
    }

    private fun calculateBMI(context: Context) {
        val weightInKgs: Double = when (_state.value.weightUnit) {
            "Pounds" -> _state.value.weightValue.toDouble().times(0.4536)
            else -> _state.value.weightValue.toDouble()
        }
        val heightInMeters: Double = when (_state.value.heightUnit) {
            "Centimeter" -> _state.value.heightValue.toDouble().times(0.01)
            "Feet" -> _state.value.heightValue.toDouble().times(0.3048)
            "Inches" -> _state.value.heightValue.toDouble().times(0.0254)
            else -> _state.value.heightValue.toDouble()
        }
        try {
            val bmiValue = weightInKgs / (heightInMeters * heightInMeters)
            val bmiValueWithDecimal = (bmiValue * 10).roundToInt() / 10.0
            val bmiStage = when (bmiValueWithDecimal) {
                in 0.0..18.5 -> "Underweight"
                in 18.5..25.0 -> "Normal"
                in 25.0..100.0 -> "Overweight"
                else -> "Invalid"
            }
            _state.update { currentState ->
                currentState.copy(
                    shouldBMICardShow = true,
                    bmi = if (bmiValueWithDecimal > 100) 0.0 else bmiValueWithDecimal,
                    bmiStage = bmiStage
                )
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "This BMI does not look good, check again the height and weight value.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun changeWeightOrHeightUnit(sheetItem: String) {
        if (_state.value.sheetTitle == "Weight") {
            _state.update { currentState ->
                currentState.copy(weightUnit = sheetItem)
            }
        } else if (_state.value.sheetTitle == "Height") {
            _state.update { currentState ->
                currentState.copy(heightUnit = sheetItem)
            }
        }
    }

    private fun enterNumber(number: String) {
        when {
            _state.value.weightValueStage == WeightValueStage.ACTIVE -> {
                _state.update { currentState ->
                    currentState.copy(
                        weightValue = if (number == ".") "0." else number,
                        weightValueStage = WeightValueStage.RUNNING
                    )
                }
            }

            _state.value.weightValueStage == WeightValueStage.RUNNING -> {
                if (_state.value.weightValue.contains(".").not() &&
                    _state.value.weightValue.length <= 3
                ) {
                    if (_state.value.weightValue.length <= 2 && number != ".") {
                        _state.update { currentState ->
                            currentState.copy(
                                weightValue = _state.value.weightValue + number,
                                weightValueStage = WeightValueStage.RUNNING
                            )
                        }
                    } else if (number == ".") {
                        _state.update { currentState ->
                            currentState.copy(
                                weightValue = _state.value.weightValue + number,
                                weightValueStage = WeightValueStage.RUNNING
                            )
                        }
                    }
                } else if (
                    _state.value.weightValue.contains(".") &&
                    _state.value.weightValue.reversed().indexOf(".") < 2
                ) {
                    _state.update { currentState ->
                        currentState.copy(
                            weightValue = _state.value.weightValue + number,
                            weightValueStage = WeightValueStage.RUNNING
                        )
                    }
                }
            }

            _state.value.heightValueStage == HeightValueStage.ACTIVE -> {
                _state.update { currentState ->
                    currentState.copy(
                        heightValue = if (number == ".") "0." else number,
                        heightValueStage = HeightValueStage.RUNNING
                    )
                }
            }

            _state.value.heightValueStage == HeightValueStage.RUNNING -> {
                if (_state.value.heightValue.contains(".").not() &&
                    _state.value.heightValue.length <= 3
                ) {
                    if (_state.value.heightValue.length <= 2 && number != ".") {
                        _state.update { currentState ->
                            currentState.copy(
                                heightValue = _state.value.heightValue + number,
                                heightValueStage = HeightValueStage.RUNNING
                            )
                        }
                    } else if (number == ".") {
                        _state.update { currentState ->
                            currentState.copy(
                                heightValue = _state.value.heightValue + number,
                                heightValueStage = HeightValueStage.RUNNING
                            )
                        }
                    }
                } else if (
                    _state.value.heightValue.contains(".") &&
                    _state.value.heightValue.reversed().indexOf(".") < 2
                ) {
                    _state.update { currentState ->
                        currentState.copy(
                            heightValue = _state.value.heightValue + number,
                            heightValueStage = HeightValueStage.RUNNING
                        )
                    }
                }
            }
        }
    }

    private fun allClearToZero() {
        if (_state.value.weightValueStage != WeightValueStage.INACTIVE) {
            _state.update { currentState ->
                currentState.copy(
                    weightValue = "0",
                    weightValueStage = WeightValueStage.ACTIVE
                )
            }
        } else if (_state.value.heightValueStage != HeightValueStage.INACTIVE) {
            _state.update { currentState ->
                currentState.copy(
                    heightValue = "0",
                    heightValueStage = HeightValueStage.ACTIVE
                )
            }
        }
    }

    private fun deleteLastDigit() {
        if (_state.value.weightValueStage != WeightValueStage.INACTIVE) {
            _state.update { currentState ->
                currentState.copy(
                    weightValue = if (_state.value.weightValue.length == 1) "0"
                    else _state.value.weightValue.dropLast(1)
                )
            }
        } else if (_state.value.heightValueStage != HeightValueStage.INACTIVE) {
            _state.update { currentState ->
                currentState.copy(
                    heightValue = if (_state.value.heightValue.length == 1) "0"
                    else _state.value.heightValue.dropLast(1)
                )
            }
        }
    }
}

sealed class UserAction {
    object OnWeightValueClicked : UserAction()
    object OnHeightValueClicked : UserAction()
    object OnWeightTextClicked : UserAction()
    object OnHeightTextClicked : UserAction()
    data class OnGoButtonClicked(val context: Context) : UserAction()
    data class OnNumberClicked(val number: String) : UserAction()
    object OnAllClearButtonClicked : UserAction()
    object OnDeleteButtonClicked : UserAction()
    data class OnSheetItemClicked(val sheetItem: String) : UserAction()
}