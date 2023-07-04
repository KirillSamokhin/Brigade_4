import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application


//fun app() {
//    var text by remember { mutableStateOf("Hello, World!") }
//
//    MaterialTheme {
//        Button(onClick = {
//            text = "Hello, Desktop!"
//        }) {
//            Text(text)
//        }
//    }
//}

private class Controller {
    var flagAnswer = false
    @Composable
    fun project () {
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
        Box (
            modifier = Modifier.fillMaxSize(),
        ) {
            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        top = 20.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Введите размеры поля",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                    OutlinedTextField(
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
                                IconButton(
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
                    OutlinedTextField(
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
                                IconButton(
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
                    Button(
                        onClick = {

                        },
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Text(text = "Отправить")
                    }
                }
            }
        }
    }
}

@Preview
fun main() = application {
    val controller = Controller()
    Window (
        title = "Initialisation",
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(1600.dp, 900.dp)
        )
    ) {
        controller.project()
    }
//    Window(onCloseRequest = ::exitApplication) {
//        app()
//    }
}