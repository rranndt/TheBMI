package dev.rranndt.bmicalculator

data class BMIScreenState(
    val weightValue: String = "60",
    val heightValue: String = "170",
    val weightValueStage: WeightValueStage = WeightValueStage.ACTIVE,
    val heightValueStage: HeightValueStage = HeightValueStage.INACTIVE,
    val shouldBMICardShow: Boolean = false,
    val sheetTitle: String = "",
    val sheetItemsList: List<String> = emptyList(),
    val weightUnit: String = "Kilograms",
    val heightUnit: String = "Centimeter",
    val bmi: Double = 0.0,
    val bmiStage: String = "",
    val error: String? = null
)

enum class WeightValueStage {
    INACTIVE,
    ACTIVE,
    RUNNING
}

enum class HeightValueStage {
    INACTIVE,
    ACTIVE,
    RUNNING
}