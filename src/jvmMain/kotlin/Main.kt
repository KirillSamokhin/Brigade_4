import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

class CellView {
    @Composable
    fun make_box (cell: Cell) {
        Box (
            modifier = Modifier
                .padding(1.dp)
                .aspectRatio(1f)
                .background(Color(get_color(cell.type))),
            contentAlignment = Center
        ) {
            Text (text=cell.get_info().toString(), fontSize = 30.sp)
        }
    }
    fun get_color (type: Triple<Base, Function, State>): Long {
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
    var h: Int = 0,
    var g: Int = 0,
    var f: Int = 0
) {
    fun get_info(): Triple<Int, Int, Int> {
        return Triple(h, g, f)
    }
    fun set_f(a: Int) {
        f = a
    }
}

class FieldView {
    /* Метод рисования поля */
    @Composable
    fun drawField (x: Int, y: Int) {
        Row {
            /* Пространство слева */
            Box(
                modifier = Modifier
                    .weight(1f) // цена
                    .padding(16.dp)
                    .fillMaxHeight()
            ) {
                val field = Array(20) { Cell() }
                field[3].type = Triple(Base.Water, Function.Simple, State.Simple)
                field[6].type = Triple(Base.Water, Function.Simple, State.Simple)
                field[8].type = Triple(Base.Water, Function.Simple, State.Simple)
                field[0].type = Triple(Base.Stone, Function.Simple, State.Simple)
                val cellview = CellView()
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    content = {
                        items(20) { i ->
                            cellview.make_box(field[i])
                        }
                    }
                )
            }
            /* Пространство справа */
            Box(
                modifier = Modifier
                    .weight(0.5f) // цена
                    .background(Color.Gray) // цвет
                    .fillMaxHeight()
            ) {
                var inputValueX by remember { mutableStateOf("") }
                var inputValueY by remember { mutableStateOf("") }
                val isVisibleX by remember {
                    derivedStateOf {
                        inputValueX.isNotBlank()
                    }
                }
                val isVisibleY by remember {
                    derivedStateOf {
                        inputValueY.isNotBlank()
                    }
                }
                /* Окошко с вводом данных */
                Column (
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Введите размеры поля",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                    /* Ввод X */
                    OutlinedTextField (
                        value = inputValueX,
                        onValueChange = { newValue -> inputValueX = newValue },
                        modifier = Modifier
                            .padding(5.dp)
                            .width(200.dp)
                            .height(65.dp),
                        label = { Text(text = "Значение X") },
                        placeholder = { Text(text = "Введите данные") },
                        singleLine = true,
                        trailingIcon = {
                            if (isVisibleX) {
                                IconButton (
                                    onClick = { inputValueX = "" }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        }
                    )
                    /* Ввод Y */
                    OutlinedTextField (
                        value = inputValueY,
                        onValueChange = { newValue -> inputValueY = newValue },
                        modifier = Modifier
                            .padding(5.dp)
                            .width(200.dp)
                            .height(65.dp),
                        label = { Text(text = "Значение Y") },
                        placeholder = { Text(text = "Введите данные") },
                        maxLines = 1,
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
                        }
                    )
                    /* кнопка отправить */
                    Button (
                        onClick = {
                            /* Тут как то нужно отправлять координаты в функцию рисования */
                            /* Потом подумаем */
                        },
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Text (text = "Отправить")
                    }
                }
            }
        }
    }
}

/* На будущее */
//class Controller {
//    val fieldView = FieldView()
//    @Composable
//    fun userInput() {
//        var inputValueX by remember { mutableStateOf("") }
//        var inputValueY by remember { mutableStateOf("") }
//        val isVisibleX by remember {
//            derivedStateOf {
//                inputValueX.isNotBlank()
//            }
//        }
//        val isVisibleY by remember {
//            derivedStateOf {
//                inputValueY.isNotBlank()
//            }
//        }
//        Surface (
//            color = MaterialTheme.colors.background
//        ) {
//            Row {
//                fieldView.drawField(10, 10)
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .background(Color.Red)
//                        .fillMaxHeight()
//                ) {
//                    // Пусто
//                }
//            }
//        }
//    }
//}

/* Main с созданием окна */
fun main() = application {
    val controller = FieldView()
    Window (
        title = "Algorithm A*",
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(1600.dp, 900.dp) // размеры экрана
        )
    ) {
        controller.drawField(10, 10)
    }
}