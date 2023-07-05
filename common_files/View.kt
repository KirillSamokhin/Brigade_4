

package view


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

import androidx.compose.foundation.layout.*




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
                change_color(average, 3, 0.1.toFloat(), cur)
            }
            Status.VIEWED -> {
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
                MaterialTheme {
                    val field = Field(5, 4)
                    val cellview = CellView()
                    //var a = Cell()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(field.x),
                        content={
                            items(field.x * field.y){ i ->
                                cellview.make_box(field.field[i/field.x][i%field.x])
                            }
                        })
                }
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



fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        //App()
    }
}

