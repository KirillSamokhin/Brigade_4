import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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


class Controller {
    private val fieldView = FieldView()
    private var x by mutableStateOf(value = 0)
    private var y by mutableStateOf(value = 0)
    var flagToAlgorithm by mutableStateOf(value = false)
    private var blockInputs by mutableStateOf(value = true)
    private var flagToStart by mutableStateOf(value = true)
    private var flagToFinish by mutableStateOf(value = true)

    @Composable
    fun userInputCord () {
        Surface (
            color = MaterialTheme.colors.background
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = "Введите размеры поля",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(all = 5.dp)
                )
                var inputValueX by remember { mutableStateOf(value = "") }
                val isVisibleX by remember {
                    derivedStateOf {
                        inputValueX.isNotBlank()
                    }
                }
                OutlinedTextField (
                    value = inputValueX,
                    onValueChange = { inputValueX = it },
                    modifier = Modifier
                        .padding(all = 5.dp),
                    label = {
                        Text (text = "Значение X")
                    },
                    placeholder = {
                        Text (text = "Введите данные")
                    },
                    singleLine = true,
                    trailingIcon = {
                        if (isVisibleX && blockInputs) {
                            IconButton (
                                onClick = {
                                    inputValueX = ""
                                }
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
                var inputValueY by remember { mutableStateOf(value = "") }
                val isVisibleY by remember {
                    derivedStateOf {
                        inputValueY.isNotBlank()
                    }
                }
                OutlinedTextField (
                    value = inputValueY,
                    onValueChange = { inputValueY = it },
                    modifier = Modifier
                        .padding(all = 5.dp),
                    label = {
                        Text(text = "Значение Y")
                    },
                    placeholder = {
                        Text(text = "Введите данные")
                    },
                    singleLine = true,
                    trailingIcon = {
                        if (isVisibleY && blockInputs) {
                            IconButton (
                                onClick = {
                                    inputValueY = ""
                                }
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
                        flagToAlgorithm = true
                        blockInputs = false
                    },
                    modifier = Modifier
                        .padding(all = 5.dp),
                    enabled = blockInputs
                ) {
                    Text (text = "Отправить")
                }
                Button (
                    onClick = {
                        /* Функция вставки из файла */
                    },
                    modifier = Modifier
                        .padding(all = 5.dp),
                    enabled = blockInputs
                ) {
                    Text (text = "Открыть файл")
                }
            }
        }
    }

    @Composable
    fun algorithmScreen () {
        Row {
            val cellView = CellView()
            /* Пространство слева */
            Box (
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(all = 16.dp)
                    .fillMaxHeight()
            ) {
                fieldView.drawField(x, y, cellView)
            }
            /* Пространство справа */
            Box (
                modifier = Modifier
                    .weight(weight = 0.3f)
                    .background(color = Color.LightGray)
                    .fillMaxHeight()
            ) {
                Column {
                    cellView.screenInformationAboutTypes()
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box (
                            modifier = Modifier
                                .padding(all = 10.dp)
                                .size(size = 95.dp)
                                .background(color = Color.Blue)
                                .clickable {
                                    if (flagToStart) {
                                        flagToStart = false
                                        cellView.cellStart()
                                    }
                                }
                        )
                        Text(
                            text = "Клетка начала",
                            modifier = Modifier
                                .padding(all = 10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box (
                            modifier = Modifier
                                .padding(all = 10.dp)
                                .size(size = 95.dp)
                                .background(color = Color.Yellow)
                                .clickable {
                                    if (flagToFinish) {
                                        flagToFinish = false
                                        cellView.cellFinish()
                                    }
                                }
                        )
                        Text (
                            text = "Клетка финиша",
                            modifier = Modifier
                                .padding(all = 10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        Button (
                            onClick = {
                                /* Функция начала алгоритма А* */
                            }
                        ) {
                            Text (text = "Начать")
                        }
                        Button (
                            onClick = {
                                defaultSettings()
                            }
                        ) {
                            Text (text = "Сброс")
                        }
                    }
                }
            }
        }
    }

    private fun defaultSettings () {
        flagToFinish = true
        flagToStart = true
        fieldView.field.defaultSettings()
    }
}

fun main() = application {
    val controller = Controller()
    Window (
        title = "Algorithm A*",
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(width = 1600.dp, height = 900.dp)
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
            size = DpSize(width = 600.dp, height = 500.dp)
        ),
        resizable = false
    ) {
        controller.userInputCord()
    }
}