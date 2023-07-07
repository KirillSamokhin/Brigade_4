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
    private val cellView = CellView()
    private val fieldView = FieldView()
    private var x by mutableStateOf(value = 0)
    private var y by mutableStateOf(value = 0)
    var flagToAlgorithm by mutableStateOf(value = false)
    var flagToStartAlgorithm by mutableStateOf(value = true)
    private var flagToBlockInputs by mutableStateOf(value = true)

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
                var inputValueX by remember { mutableStateOf(value = "") }
                val isVisibleX by remember {
                    derivedStateOf {
                        inputValueX.isNotBlank()
                    }
                }
                var inputValueY by remember { mutableStateOf(value = "") }
                val isVisibleY by remember {
                    derivedStateOf {
                        inputValueY.isNotBlank()
                    }
                }
                Text (
                    text = "Введите размеры поля",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(all = 5.dp)
                )
                OutlinedTextField (
                    value = inputValueX,
                    onValueChange = { inputValueX = it },
                    modifier = Modifier
                        .padding(all = 5.dp),
                    label = { Text (text = "Значение X") },
                    placeholder = { Text (text = "Введите данные") },
                    singleLine = true,
                    trailingIcon = {
                        if (isVisibleX && flagToBlockInputs) {
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
                    enabled = flagToBlockInputs
                )
                OutlinedTextField (
                    value = inputValueY,
                    onValueChange = { inputValueY = it },
                    modifier = Modifier
                        .padding(all = 5.dp),
                    label = { Text (text = "Значение Y") },
                    placeholder = { Text (text = "Введите данные") },
                    singleLine = true,
                    trailingIcon = {
                        if (isVisibleY && flagToBlockInputs) {
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
                    enabled = flagToBlockInputs
                )
                Button (
                    onClick = {
                        x = inputValueX.toIntOrNull() ?: 0
                        y = inputValueY.toIntOrNull() ?: 0
                        flagToAlgorithm = true
                        flagToBlockInputs = false
                    },
                    modifier = Modifier
                        .padding(all = 5.dp),
                    enabled = flagToBlockInputs
                ) {
                    Text (text = "Отправить")
                }
                Button (
                    onClick = {
                        /* Работа с файлами */
                    },
                    modifier = Modifier
                        .padding(all = 5.dp)
                ) {
                    Text (text = "Открыть файл")
                }
            }
        }
    }

    @Composable
    fun algorithmScreen() {
        Row {
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
                                    cellView.cellEdge("START")
                                }
                        ) { }
                        Text (
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
                                    cellView.cellEdge("FINISH")
                                }
                        ) { }
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
                                val algorithm = Algorithm(fieldView.field)
                                if (cellView.cellStart == null || cellView.cellFinish == null) {
                                    /* Вывод, что: либо финиша, либо старта нет */
                                }
                                else {
                                    val answer = algorithm.aStar(cellView.cellStart!!.x, cellView.cellStart!!.y, cellView.cellFinish!!.x, cellView.cellFinish!!.y)
                                    algorithm.recoverPath(answer, cellView.cellFinish!!)
                                }
                            }
                        ) {
                            Text (text = "Начать")
                        }
                        Button (
                            onClick = {
                                cellView.default()
                                defaultSettings()
                            }
                        ) {
                            Text (text = "Заново")
                        }
                    }
                }
            }
        }
    }

    private fun defaultSettings () {
        fieldView.field.default()
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