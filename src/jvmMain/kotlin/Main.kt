import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

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
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text (
                text = "Введите размеры поля",
                fontSize = 30.sp,
                modifier = Modifier.padding(5.dp)
            )
            OutlinedTextField (
                value = inputValueX,
                onValueChange = { newValue -> inputValueX = newValue },
                modifier = Modifier.padding(5.dp),
                label = { Text(text = "Значение X") },
                placeholder = { Text(text = "Введите данные") },
                maxLines = 1,
                trailingIcon = {
                    if (isVisibleX) {
                        IconButton(
                            onClick = { inputValueX = "" }
                        ) {
                            Icon (
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                }
            )
            OutlinedTextField (
                value = inputValueY,
                onValueChange = { newValue -> inputValueY = newValue },
                modifier = Modifier.padding(5.dp),
                label = { Text(text = "Значение Y") },
                placeholder = { Text(text = "Введите данные") },
                maxLines = 1,
                trailingIcon = {
                    if (isVisibleY) {
                        IconButton(
                            onClick = { inputValueY = "" }
                        ) {
                            Icon (
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
            println("$inputValueX $inputValueY")
        }
    }
}

@Preview
fun main() = application {
    val controller = Controller()
    Window (
        title = "Initialisation",
        onCloseRequest = ::exitApplication
    ) {
        controller.project()
    }
//    Window(onCloseRequest = ::exitApplication) {
//        app()
//    }
}
