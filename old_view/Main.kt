

//package view

//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.background
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.prob.ui.theme.ProbTheme

import Cell_Field.Base
import Cell_Field.Cell
import Cell_Field.Field
import Cell_Field.Status
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application



//enum class Base{
//    Grass,
//    Water,
//    Stone
//}
//
//enum class Function{
//    Simple,
//    Start,
//    Finish
//}
//
//enum class State{
//    Simple,
//    InOpenList,
//    InClosedList
//}
//
//class Cell(
//    var type: Triple<Base, Function, State> = Triple(Base.Grass, Function.Simple, State.Simple),
//    var h: Int = 0,
//    var g: Int = 0,
//    var f: Int = 0){
//    fun get_info(): Triple<Int, Int, Int>{
//        return Triple(h, g, f)
//    }
//    fun set_f(a: Int){
//        f = a
//    }
//}

class CellView{
    @Composable
    fun make_box(cell: Cell){
        Box(
            modifier = Modifier
                .padding(1.dp)
                .aspectRatio(1f)
                .background(get_color(cell)),
            contentAlignment = Alignment.Center
        ){
            Text(text=" " + cell.getParams()[2].toString(),
                color=Color(0xff08086b),
                //fontFamily = FontFamily.Monospace,
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.TopStart))
            Text(text= cell.getParams()[3].toString() + " ",
                color=Color(0xff6b0505),
                //fontFamily = FontFamily.Monospace,
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.TopEnd))
            Text(text=" " + cell.getParams()[1].toString(),
                //fontFamily = FontFamily.Monospace,
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.BottomStart))
        }
    }

    //fun get_color(type: Triple<Base, Function, State>): Long{
    fun get_color(cell: Cell): Color{
        var res: Long = 0x00000000
        res = when(cell.base){
            Base.GRASS -> 0xff689808  //Alpha_Red_Green_Blue (0 - min, f - max)
            Base.WATER -> 0xff88cffb
            Base.STONE -> 0xff696969
        }

        var cur = Color(res)
        val average = (cur.red + cur.green + cur.blue)/3
        cur = when(cell.status){
            Status.CHECK -> {
//                val coeff = 0.1.toFloat()
//                val r = cur.red - (cur.red - average)/3 - coeff
//                val g = cur.green - (cur.green - average)/3- coeff
//                val b = cur.blue - (cur.blue - average)/3 - coeff
//                Color(r, g, b)
                change_color(average, 3, 0.1.toFloat(), cur)

//                val int_color = 0xff << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff)

//                println("check founded")
//                val res_s = res.toString().chunked(2)
//                //var a = LongArray(3, {0})
//                println("create a")
//                val a = Array(4, {i -> res_s[i].toLong()})
//                val average = a.average()
//                println("for")
//                for (i in 1..3){
//                    val coef = (a[i]-average)/2
//                    a[i] -= coef.toLong()
//                }
//                print("joining")
//                a.joinToString(separator = "").toLong()
            }
            Status.VIEWED -> {
//                val coeff = 0.15.toFloat()
//                val r = cur.red - (cur.red - average)/2 - coeff
//                val g = cur.green - (cur.green - average)/2 - coeff
//                val b = cur.blue - (cur.blue - average)/2 - coeff
//                Color(r, g, b)
                change_color(average, 2, 0.15.toFloat(), cur)
            }
            else -> cur
        }
        return cur
    }

    fun change_color(average: Float, d: Int, coeff: Float, cur: Color): Color{
        val r = cur.red - (cur.red - average)/d - coeff
        val g = cur.green - (cur.green - average)/d - coeff
        val b = cur.blue - (cur.blue - average)/d - coeff
        return Color(r, g, b)

    }
}


@Composable
@Preview
fun App() {
    //var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        //var field: Array<Cell> = arrayOf()
        var field = Field(5, 4)
//        var field = Array(20, {Cell()})
//        field[1].status = Status.CHECK
//        field[5].status = Status.CHECK
//        field[7].status = Status.CHECK
//        field[11].status = Status.CHECK
//        field[3].base = Base.WATER
//        field[8].base = Base.WATER
//        field[7].base = Base.WATER
//        field[6].status = Status.VIEWED
//        field[6].h = 10
//        field[6].g = 5
//        field[6].f = 15
//        field[8].type = Triple(Base.Water, Function.Simple, State.Simple)
//        field[0].type = Triple(Base.Stone, Function.Simple, State.Simple)
        val cellview = CellView()
        //var a = Cell()
        LazyVerticalGrid(
            columns = GridCells.Fixed(field.x),
            content={
                items(field.x * field.y){ i ->
                    cellview.make_box(field.field[i/field.x][i%field.x])
                }
            })
//        field[0].type = Triple(Base.Water, Function.Simple, State.Simple)
//        field[0].f = 4
    }
}



fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}


//class MainActivity: ComponentActivity(){
//    override fun onCreate(savedInstanceState: Bundle?){
//        super.onCreate(savedInstanceState)
//        setContent{
//            //var field: Array<Cell> = arrayOf()
//            var field = Array(20, {Cell()})
//            field[3].type = Triple(Base.Water, Function.Simple, State.Simple)
//            field[6].type = Triple(Base.Water, Function.Simple, State.Simple)
//            field[8].type = Triple(Base.Water, Function.Simple, State.Simple)
//            field[0].type = Triple(Base.Stone, Function.Simple, State.Simple)
//            val cellview = CellView()
//            //var a = Cell()
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(5),
//                content={
//                    items(20){ i ->
//                        cellview.make_box(field[i])
//                    }
//                })
//            field[0].type = Triple(Base.Water, Function.Simple, State.Simple)
//            field[0].f = 4
//        }
//    }
//}


