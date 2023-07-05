

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import view.CellView
import view.FieldView


/* На будущее */
class Controller {
    private var x by mutableStateOf(0)
    private var y by mutableStateOf(0)
    var flagToAlgorithm by mutableStateOf(false)
    private var blockInputs by mutableStateOf(true)
    @Composable
    fun userInputCord () {
        Surface (
            color = MaterialTheme.colors.background
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = "Введите размеры поля",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)
                )
                var inputValueX by remember { mutableStateOf("") }
                val isVisibleX by remember {
                    derivedStateOf {
                        inputValueX.isNotBlank()
                    }
                }
                OutlinedTextField (
                    value = inputValueX,
                    onValueChange = { inputValueX = it },
                    modifier = Modifier
                        .padding(5.dp),
                    label = { Text(text = "Значение X") },
                    placeholder = { Text(text = "Введите данные") },
                    singleLine = true,
                    trailingIcon = {
                        if (isVisibleX && blockInputs) {
                            IconButton (
                                onClick = { inputValueX = "" }
                            ) {
                                Icon (
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = blockInputs
                )
                var inputValueY by remember { mutableStateOf("") }
                val isVisibleY by remember {
                    derivedStateOf {
                        inputValueY.isNotBlank()
                    }
                }
                OutlinedTextField (
                    value = inputValueY,
                    onValueChange = { inputValueY = it },
                    modifier = Modifier
                        .padding(5.dp),
                    label = { Text(text = "Значение Y") },
                    placeholder = { Text(text = "Введите данные") },
                    singleLine = true,
                    trailingIcon = {
                        if (isVisibleY && blockInputs) {
                            IconButton (
                                onClick = { inputValueY = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = blockInputs
                )
                Button (
                    onClick = {
                        x = inputValueX.toIntOrNull() ?: 0
                        y = inputValueY.toIntOrNull() ?: 0
                    },
                    modifier = Modifier.padding(15.dp),
                    enabled = blockInputs
                ) {
                    Text (text = "Сохранить")
                }
                Button (
                    onClick = { flagToAlgorithm = true; blockInputs = false },
                    modifier = Modifier.padding(5.dp),
                    enabled = blockInputs
                ) {
                    Text (text = "Отправить")
                }
                Text (
                    text = "Вы ввели: $inputValueX $inputValueY",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text (
                    text = "Вы сохранили: $x $y",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }

    @Composable
    fun algorithmScreen() {
        Row {
            /* Пространство слева */
            Box (
                modifier = Modifier
                    .weight(1f) // цена
                    .padding(16.dp)
                    .fillMaxHeight()
            ) {
                val fieldView = FieldView()
                fieldView.drawField(x, y)
            }
            /* Пространство справа */
            Box (
                modifier = Modifier
                    .weight(0.3f) // цена
                    .background(color = Color.LightGray) // цвет
                    .fillMaxHeight()
            ) {
                val cellView = CellView()
                cellView.screenInformationAboutTypes()
            }
        }
    }

    @Composable
    fun setStart() {
        /* Установка Start */
    }

    @Composable
    fun setFinish() {
        /* Установка Finish */
    }
}

fun main() = application {
    val controller = Controller()
    Window (
        title = "Algorithm A*",
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(1600.dp, 900.dp)
        )
    ) {
        if (controller.flagToAlgorithm) {
            controller.algorithmScreen()
        }
    }
    Window (
        title = "Initialization",
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(600.dp, 500.dp)
        ),
        resizable = false
    ) {
        controller.userInputCord()
    }
}