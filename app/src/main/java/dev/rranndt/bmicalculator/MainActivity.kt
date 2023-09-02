package dev.rranndt.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.rranndt.bmicalculator.ui.theme.BMICalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: BMIViewModel = viewModel()
            val state by viewModel.state.collectAsState()

            BMICalculatorTheme {
                BMIScreen(
                    state = state,
                    userAction = viewModel::onAction
                )
            }
        }
    }
}
