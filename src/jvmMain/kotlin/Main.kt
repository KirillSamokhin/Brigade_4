import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main () = application {
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
        state = WindowState (
            size = DpSize(600.dp, 500.dp)
        ),
        resizable = false
    ) {
        controller.userInputCord()
    }
}

//fun main () = application {
//    Window (
//        onCloseRequest = ::exitApplication
//    ) {
//        Image (
//            painter = painterResource("sample.png"),
//            contentDescription = "Sample",
//            modifier = Modifier
//                .fillMaxSize()
//        )
//    }
//}