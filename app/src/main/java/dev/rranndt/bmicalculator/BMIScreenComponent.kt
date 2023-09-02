package dev.rranndt.bmicalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.rranndt.bmicalculator.ui.theme.BMICalculatorTheme
import dev.rranndt.bmicalculator.ui.theme.CustomBlue
import dev.rranndt.bmicalculator.ui.theme.CustomGray
import dev.rranndt.bmicalculator.ui.theme.CustomGreen
import dev.rranndt.bmicalculator.ui.theme.CustomOrange
import dev.rranndt.bmicalculator.ui.theme.CustomRed

@Composable
fun UnitItem(
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 22.sp
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Select Icon"
        )
    }
}

@Composable
fun InputUnitValue(
    inputValue: String,
    inputUnit: String,
    inputColor: Color,
    onUnitValueClicked: () -> Unit
) {
    Column(horizontalAlignment = Alignment.End) {
        Text(
            text = inputValue,
            fontSize = 40.sp,
            color = inputColor,
            modifier = Modifier.clickable { onUnitValueClicked() }
        )
        Text(
            text = inputUnit,
            fontSize = 12.sp
        )
    }
}

@Composable
fun NumberKeyboard(
    onNumberClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val numberButtonList = listOf(
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "", "0", "."
        )
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(numberButtonList) { item ->
                NumberButton(
                    number = item,
                    onClick = onNumberClick,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun NumberButton(
    number: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(10.dp)
            .clip(CircleShape)
            .clickable { onClick(number) }
    ) {
        Text(
            text = number,
            fontSize = 40.sp
        )
    }
}

@Composable
fun ColumnScope.SymbolButton(
    symbol: String,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(CustomGray)
            .clickable { onClick() }
            .padding(15.dp)
            .weight(1f)
            .aspectRatio(1f)
    ) {
        Text(
            text = symbol,
            fontSize = 26.sp,
            color = CustomOrange
        )
    }
}

@Composable
fun ColumnScope.SymbolButtonWithIcon(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(CustomGray)
            .clickable { onClick() }
            .padding(15.dp)
            .weight(1f)
            .aspectRatio(1f)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_backspace),
            contentDescription = "Delete Icon",
            tint = CustomOrange
        )
    }
}

@Composable
fun BMIResultCard(
    bmi: Double,
    bmiStage: String = "Normal",
    bmiStageColor: Color = CustomGreen
) {
    Column(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(15.dp)
            )
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "$bmi",
                fontSize = 70.sp,
                color = CustomOrange
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "BMI",
                    fontSize = 40.sp,
                    color = Color.Gray
                )
                Text(
                    text = bmiStage,
                    fontSize = 18.sp,
                    color = bmiStageColor
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Divider(
            color = Color.Gray,
            thickness = 5.dp,
            modifier = Modifier.shadow(elevation = 5.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Information",
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Underweight",
                color = CustomBlue
            )
            Text(
                text = "Normal",
                color = CustomGreen
            )
            Text(
                text = "Overweight",
                color = CustomRed
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Divider(
                color = CustomBlue,
                thickness = 5.dp,
                modifier = Modifier.weight(1f)
            )
            Divider(
                color = CustomGreen,
                thickness = 5.dp,
                modifier = Modifier.weight(1f)
            )
            Divider(
                color = CustomRed,
                thickness = 5.dp,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "16.0",
                fontSize = 18.sp,
                color = Color.DarkGray
            )
            Text(
                text = "18.5",
                fontSize = 18.sp,
                color = Color.DarkGray
            )
            Text(
                text = "25.0",
                fontSize = 18.sp,
                color = Color.DarkGray
            )
            Text(
                text = "40.0",
                fontSize = 18.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun ShareButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = CustomOrange,
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier
    ) {
        Text(
            text = "Share",
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun BottomSheetContent(
    sheetTitle: String,
    sheetTitleList: List<String>,
    onItemClicked: (String) -> Unit,
    onCancelClicked: () -> Unit
) {
    Text(
        text = sheetTitle,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
    sheetTitleList.forEach { item ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClicked(item) }
        ) {
            Text(
                text = item,
                modifier = Modifier.padding(15.dp)
            )
        }
    }
    Button(
        onClick = onCancelClicked,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black
        ),
        shape = MaterialTheme.shapes.extraSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(text = "Cancel")
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    BMICalculatorTheme {

    }
}