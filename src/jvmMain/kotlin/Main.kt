import androidx.compose.foundation.background
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

/* Это база :)*/
enum class Base {
    Grass,
    Water,
    Stone
}

/* Это я не знаю что */
enum class Function {
    Simple,
    Start,
    Finish
}

/* Состояния */
enum class State {
    Simple,
    InOpenList,
    InClosedList
}

/* Окна */
enum class WindowTypes {
    Initialization,
    Algorithm
}

class CellView {
    @Composable
    fun makeBox (cell: Cell) {
        Box (
            modifier = Modifier
                .padding(1.dp)
                .aspectRatio(1f)
                .background(Color(getColor(cell.type))),
            contentAlignment = Center
        ) {
            Text (
                text = cell.getInfo().toString(),
                fontSize = 30.sp
            )
        }
    }
    fun getColor (type: Triple<Base, Function, State>): Long {
        var res: Long = 0x00000000
        res = when(type.first) {
            Base.Grass -> 0xff649500
            Base.Water -> 0xff87cefa
            Base.Stone -> 0xff696969
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
        Row {
            /* Пространство слева */
            Box (
                modifier = Modifier
                    .weight(1f) // цена
                    .padding(16.dp)
                    .fillMaxHeight()
            ) {
                val field = Array(x) { Cell() }
                field[3].type = Triple(Base.Water, Function.Simple, State.Simple)
                field[6].type = Triple(Base.Water, Function.Simple, State.Simple)
                field[8].type = Triple(Base.Water, Function.Simple, State.Simple)
                field[0].type = Triple(Base.Stone, Function.Simple, State.Simple)
                val cellView = CellView()
                LazyVerticalGrid(
                    columns = GridCells.Fixed(y),
                    content = {
                        items(y) { i ->
                            cellView.makeBox(field[i])
                        }
                    }
                )
            }
            /* Пространство справа */
            Box(
                modifier = Modifier
                    .weight(0.5f) // цена
                    .background(Color.LightGray) // цвет
                    .fillMaxHeight()
            ) {

            }
        }
    }
}

/* На будущее */
class Controller {
    var x by mutableStateOf(0)
    var y by mutableStateOf(0)
    @Composable
    fun userInputCord () {
        Surface(
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
                        if (isVisibleX) {
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                        if (isVisibleY) {
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Button (
                    onClick = {
                        x = inputValueX.toIntOrNull() ?: 0
                        y = inputValueY.toIntOrNull() ?: 0
                    },
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text (text = "Сохранить")
                }
                Button (
                    onClick = {
                    },
                    modifier = Modifier.padding(15.dp)
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
}

/* Main с созданием окна */
fun main() = application {
    val controller = Controller()
    Window (
        title = "Algorithm A*",
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(1600.dp, 900.dp) // размеры экрана
        )
    ) {
        controller.userInputCord()
    }
}