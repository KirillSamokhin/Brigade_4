import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
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

enum class Base {
    Grass,
    Water,
    Stone
}

enum class Function {
    Simple,
    Start,
    Finish
}

enum class State {
    Simple,
    InOpenList,
    InClosedList
}

class CellView {
    private val colors = longArrayOf( 0xff649500, 0xff87cefa, 0xff696969 )
    private val nameCells = listOf("Клетка травы", "Клетка воды", "Клетка камня")
    @Composable
    fun makeBox (cell: Cell) {
        Box (
            modifier = Modifier
                .padding(all = 1.dp)
                .aspectRatio(ratio = 1f)
                .background(color = Color(getColor(cell.type)))
                .clickable {
                           /* Меняется */
                },
            contentAlignment = Center
        ) {
            Text (
                text = cell.getInfo().toString(),
                fontSize = 30.sp
            )
        }
    }

    @Composable
    fun screenInformationAboutTypes () {
        Column {
            for (i in (colors.indices)) {
                Row (
                    verticalAlignment = CenterVertically
                ) {
                    Box (
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .size(size = 95.dp)
                            .background(color = Color.Black)
                            .padding(all = 5.dp)
                            .background(color = Color(color = colors[i]))
                    ) {
                    }
                    Text (
                        text = nameCells[i],
                        modifier = Modifier
                            .padding(all = 10.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    fun getColor (type: Triple<Base, Function, State>): Long {
        var res: Long = 0x00000000
        res = when(type.first) {
            Base.Grass -> colors[0]
            Base.Water -> colors[1]
            Base.Stone -> colors[2]
        }

        res += when(type.third) {
            State.InOpenList -> 0xccd3d3d3
            State.InClosedList -> 0xcca9a9a9
            else -> 0
        }
        return res
    }
}

class Cell (
    var type: Triple<Base, Function, State> = Triple(Base.Grass, Function.Simple, State.Simple),
    var hFunction: Int = 0,
    var gFunction: Int = 0,
    var fFunction: Int = 0
) {
    fun getInfo (): Triple<Int, Int, Int> {
        return Triple(hFunction, gFunction, fFunction)
    }
    fun setF (a: Int) {
        fFunction = a
    }
}

class FieldView {
    /* Метод рисования поля */
    @Composable
    fun drawField (x: Int, y: Int) {
        val field = Array(x) { Cell() }
        field[3].type = Triple(Base.Water, Function.Simple, State.Simple)
        field[6].type = Triple(Base.Water, Function.Simple, State.Simple)
        field[8].type = Triple(Base.Water, Function.Simple, State.Simple)
        field[0].type = Triple(Base.Stone, Function.Simple, State.Simple)
        val cellView = CellView()
        LazyVerticalGrid (
            columns = GridCells.Fixed(y),
            content = {
                items(x) { i ->
                    cellView.makeBox(field[i])
                }
            }
        )
    }
}

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