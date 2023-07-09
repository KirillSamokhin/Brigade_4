import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import javax.swing.JFileChooser


class Controller {
    private val singleton = Singleton.getInstance()
    private val cellView = CellView()
    private val fieldView = FieldView()
    private lateinit var field: Field
    private lateinit var algorithm: Algorithm
    private var x by mutableStateOf(value = 0)
    private var y by mutableStateOf(value = 0)
    private var flagToBlockInputs by mutableStateOf(value = true)
    private var flagToStartAlgorithm by mutableStateOf(value = true)
    private var flagToEndAlgorithm by mutableStateOf(value = true)
    private var count = 0
    var flagToAlgorithm by mutableStateOf(value = false)

    @Composable
    fun userInputCord () {
        val showHelp = remember { mutableStateOf(value = false) }
        if (showHelp.value) {
            helpDialog (onDismiss = {showHelp.value = false})
        }
        val showError1 = remember { mutableStateOf(value = false) }
        if (showError1.value) {
            errorAlert(
                onDismiss = { showError1.value = false },
                message = "Отправить можно только положительные числа больше нуля"
            )
        }
        val showError2 = remember { mutableStateOf(value = false) }
        if (showError2.value) {
            errorAlert(
                onDismiss = { showError2.value = false },
                message = "В файле должны быть указаны положительные размеры поля. Пример файла:\n" +
                        "7 4\n" +
                        "0 0\n" +
                        "6 3\n" +
                        "Т Т Т Т В К Т\n" +
                        "Т К Т В Т Т К\n" +
                        "Т В Т К К К К\n" +
                        "Т В Т Т В Т Т"
            )
        }
        Surface (
            color = MaterialTheme.colors.background
        ) {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .size(size = 30.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon (
                    Icons.Default.Info,
                    contentDescription = "Help icon",
                    modifier = Modifier
                        .size(size = 30.dp)
                        .clickable {
                            showHelp.value = true
                        },
                    tint = Color.Black,
                )
            }
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
                var selectedFileName by remember { mutableStateOf(value = "") }
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
                        if (x > 0 && y > 0) {
                            field = Field(x, y)
                            flagToAlgorithm = true
                            flagToBlockInputs = false
                        } else {
                            showError1.value = true
                        }
                    },
                    modifier = Modifier
                        .padding(all = 5.dp),
                    enabled = flagToBlockInputs
                ) {
                    Text (text = "Отправить")
                }
                Button (
                    onClick = {
                        val fileChooser = JFileChooser()
                        fileChooser.dialogTitle = "Выберите файл"
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val selectedFile = fileChooser.selectedFile
                            selectedFileName = selectedFile.path
                        }
                        val fileReader = FileReader(selectedFileName)
                        if (fileReader.readMap() != null) {
                            field = fileReader.readMap()!!
                            flagToBlockInputs = false
                            flagToAlgorithm = true
                        } else {
                            showError2.value = true
                        }
                    },
                    modifier = Modifier
                        .padding(all = 5.dp),
                    enabled = flagToBlockInputs
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
                fieldView.drawField(field, cellView)
            }
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
                                .background(color = Color.Black)
                                .padding(all = 2.dp)
                                .background(color = Color.LightGray)
                                .clickable {
                                    if (flagToStartAlgorithm) {
                                        cellView.cellEdge("START")
                                    }
                                }
                        ) {
                            Icon (
                                Icons.Default.Face,
                                contentDescription = "Start icon",
                                modifier = Modifier
                                    .size(size = 50.dp)
                                    .align(Alignment.Center),
                                tint = Color.Black,
                            )
                        }
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
                                .background(color = Color.Black)
                                .padding(all = 2.dp)
                                .background(color = Color.LightGray)
                                .clickable {
                                    if (flagToStartAlgorithm) {
                                        cellView.cellEdge("FINISH")
                                    }
                                }
                        ) {
                            Icon (
                                Icons.Default.Home,
                                contentDescription = "Finish icon",
                                modifier = Modifier
                                    .size(size = 50.dp)
                                    .align(Alignment.Center),
                                tint = Color.Black,
                            )
                        }
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
                                algorithm = Algorithm(field)
                                field.startCord = Pair(cellView.cellStart?.x ?: 0, cellView.cellStart?.y ?: 0)
                                if (field.startCord.first == 0 && field.startCord.second == 0) {
                                    field.field[0][0].changeEdge("START")
                                }
                                field.finishCord = Pair(cellView.cellFinish?.x ?: (field.x - 1), cellView.cellFinish?.y ?: (field.y - 1))
                                if (field.finishCord.first == field.x - 1 && field.finishCord.second == field.y - 1) {
                                    field.field[field.y - 1][field.x - 1].changeEdge("FINISH")
                                }
                                cellView.clickable = false
                                flagToStartAlgorithm = false
                                algorithm.aStar()

                            },
                            enabled = flagToStartAlgorithm
                        ) {
                            Text (text = "Начать")
                        }
                        Button (
                            onClick = {
                                var answer:  MutableMap<Cell, Cell?>? = null
                                when (count % 5) {
                                    0 -> {
                                        answer = algorithm.iteration()
                                    }
                                    else -> algorithm.cellProcess()
                                }
                                if (answer != null) {
                                    algorithm.recoverPath(answer)
                                    flagToEndAlgorithm = false
                                }
                                count += 1
                            },
                            enabled = !flagToStartAlgorithm && flagToEndAlgorithm
                        ) {
                            Text (text = "Далее")
                        }
                        Button (
                            onClick = {
                                defaultSettings()
                            }
                        ) {
                            Text (text = "Сброс")
                        }
                        Button (
                            onClick = {
                                val answer = algorithm.fullIteration()
                                algorithm.recoverPath(answer)
                                flagToEndAlgorithm = false
                            },
                            enabled = !flagToStartAlgorithm && flagToEndAlgorithm
                        ) {
                            Text (text = "Конец")
                        }
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp),
                    ) {
                        Text (
                            text = singleton.message,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun helpDialog (onDismiss: () -> Unit) {
        val option = "Алгоритм А*: Нахождение кратчайшего пути в графе.\nВ данной визуализации алгоритма мы используем Манхеттенскую эвристику и представляем" +
                " граф в виде карты состоящей из клеток:\n- Клетка Травы: клетка с ценой прохода 1.\n- Клетка Воды: клетка " +
                "с ценой прохода 3.\n- Клетка Камня: клетка, которую нельзя пройти.\nНа поле должно быть установлено Начало " +
                "и Финиш. Их расположение задаётся пользователем, но если пользователь не выберет ничего, то по умолчанию " +
                "Начало будет слева сверху, а Финиш справа снизу. Во время работы алгоритма будут происходить следующие " +
                "действия:\n- Клетка становится бледного оттенка, если она находится на рассмотрении.\n- Клетка становится " +
                "тёмного оттенка, если она возможно входит в минимальный путь.\n- Клетка окрашивается в жёлтый цвет, если " +
                "кратчайший путь найден (все клетки кратчайшего пути окрашены в этот цвет). \nВвод размера поля можно задать " +
                "самому с последующей настройкой поля, либо из файла."
        AlertDialog (
            onDismissRequest = {},
            modifier = Modifier
                .size(size = 500.dp),
            title = {
                Text (text = "Описание алгоритма")
            },
            confirmButton = {
                Button (
                    onClick = onDismiss
                ) {
                    Text (text = "Понятно")
                }
            },
            text = {
                Text (text = option)
            }
        )
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun errorAlert (onDismiss: () -> Unit, message: String) {
        AlertDialog (
            onDismissRequest = {},
            modifier = Modifier
                .size(size = 300.dp),
            title = {
                Row {
                    Icon (
                        Icons.Default.Warning,
                        contentDescription = "Warning icon",
                        modifier = Modifier
                            .padding(all = 5.dp)
                            .size(size = 30.dp),
                        tint = Color.Black,
                    )
                    Text (text = "Ошибка!")
                }
            },
            confirmButton = {
                Button (
                    onClick = onDismiss
                ) {
                    Text (text = "Понятно")
                }
            },
            text = {
                Text (text = message)
            }
        )
    }

    private fun defaultSettings () {
        singleton.message = ""
        field.default()
        cellView.default()
        flagToStartAlgorithm = true
        flagToEndAlgorithm = true
        count = 0
    }
}