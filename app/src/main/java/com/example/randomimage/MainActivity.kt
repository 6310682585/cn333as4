package com.example.randomimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil.compose.rememberImagePainter
import com.example.randomimage.ui.theme.RandomImageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomImageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Screen()
                }
            }
        }
    }
}

@Composable
fun Screen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(15.dp))

//  Dropdown category list
        var expanded by remember { mutableStateOf(false) }
        val suggestions = listOf("movie", "game", "album", "book", "face", "fashion", "shoes", "watch", "furniture")
        var selectedCategory by remember { mutableStateOf(" ") }
        var textfieldSize by remember { mutableStateOf(Size.Zero) }

        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown


        Column(Modifier.padding(20.dp)) {
            OutlinedTextField(
                readOnly = true,
                value = selectedCategory,
                onValueChange = { selectedCategory = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textfieldSize = coordinates.size.toSize()
                    },
                label = {Text("choose category")},
                trailingIcon = {
                    Icon(
                        icon,"click for choose category",
                        Modifier.clickable { expanded = !expanded })
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current){textfieldSize.width.toDp()})
            ) {
                suggestions.forEach { label ->
                    DropdownMenuItem(onClick = {
                        selectedCategory = label
                        expanded = false
                    }) {
                        Text(text = label)
                    }
                }
            }
        }

//  width & height slider
        var width by remember { mutableStateOf(8) }
        var height by remember { mutableStateOf(8) }

        for (i in 0..1) {
            var sliderValue by remember { mutableStateOf(8f) }
            var value by remember { mutableStateOf(8) }
            var sideList = mutableListOf<Int>(R.string.width, R.string.height)

            Text(
                text = stringResource(sideList[i], value.toString())
            )
            Slider(
                modifier = Modifier.padding(horizontal = 20.dp),
                value = sliderValue,
                onValueChange = { sliderValue_ ->
                    sliderValue = sliderValue_
                    value = sliderValue.toInt()
                },
                onValueChangeFinished = {
                    if (i == 0) {
                        width = value
                    } else if (i == 1) {
                        height = value
                    }
                },
                valueRange = 8f..2000f,
                steps = 1991
            )
        }

//  submit
        SubmitValue( width, height,selectedCategory)
    }
}


@Composable
fun SubmitValue(

    width: Int,
    height: Int,
    selectedCategory: String,
    modifier: Modifier = Modifier,
) {
//    check link
    Text(
        text = stringResource(R.string.link, width, height, selectedCategory),
        textAlign = TextAlign.Center
    )

    var isDialogVisible by remember { mutableStateOf(false) }

    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = { isDialogVisible = false },
            title = { Text(text = "Random Image ${width} x ${height} pixels") },
            text = {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = modifier
                        .width(300.dp)
                        .height(300.dp),
                        painter = rememberImagePainter(data = "https://loremflickr.com/${width}/${height}/${selectedCategory}"),
                        contentDescription = null
                    )
                }
            },
            confirmButton = {
                Button(onClick = { isDialogVisible = false }) {
                    Text(text = "OK")
                }
            }
        )
    }

    if (selectedCategory != " ") {
        Button(onClick = {
            isDialogVisible = true
        },
            modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.submit),
                fontSize = 15.sp
            )
        }
    } else {
        Text(
            text = stringResource(R.string.notSelect),
            fontSize = 15.sp,
            color = Color.Red
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RandomImageTheme {
        Screen()
    }
}