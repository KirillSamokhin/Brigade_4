package view

import CellField.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class CellView {
    private val colors = longArrayOf( 0xff649500, 0xff87cefa, 0xff696969 )
    private val nameCells = listOf("Клетка травы", "Клетка воды", "Клетка камня")
    var start = false
    var finish = false

    fun cellStart () {
        start = true
    }

    fun cellFinish () {
        finish = true
    }

    @Composable
    fun makeBox (cell: Cell) {
        Box (
            modifier = Modifier
                .padding(all = 1.dp)
                .aspectRatio(ratio = 1f)
                .background(color = getColor(cell))
                .clickable {
                    when {
                        start -> {
                            changeEdge(cell, "START")
                            start = false
                        }
                        finish -> {
                            changeEdge(cell, "FINISH")
                            finish = false
                        }
                        else -> changeBase(cell)
                    }
                },
            contentAlignment = Alignment.Center
        ){
            Text(text=" " + cell.g.toString(),
                color=Color(0xff08086b),
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.TopStart))
            Text(text= cell.h.toString() + " ",
                color=Color(0xff6b0505),
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.TopEnd))
            Text(text=" " + cell.f.toString(),
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.BottomStart))
        }
    }

    @Composable
    fun screenInformationAboutTypes () {
        Column {
            for (i in (colors.indices)) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box (
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .size(size = 95.dp)
                            .background(color = Color.Black)
                            .padding(all = 2.dp)
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

    fun getColor (cell: Cell): Color {
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
                changeColor(average, 3, 0.1.toFloat(), cur)
            }
            Status.VIEWED -> {
                changeColor(average, 2, 0.15.toFloat(), cur)
            }
            else -> cur
        }
        return cur
    }

    fun changeColor (average: Float, d: Int, coeff: Float, cur: Color): Color {
        val r = cur.red - (cur.red - average)/d - coeff
        val g = cur.green - (cur.green - average)/d - coeff
        val b = cur.blue - (cur.blue - average)/d - coeff
        return Color(r, g, b)
    }

    fun changeBase (cell: Cell) {
        cell.base = when (cell.base) {
            Base.GRASS -> Base.WATER
            Base.WATER -> Base.STONE
            Base.STONE -> Base.GRASS
        }
    }

    fun changeEdge (cell: Cell, string: String) {
        when (string) {
            "START" -> cell.edge = Edge.START
            "FINISH" -> cell.edge = Edge.FINISH
            else -> cell.edge = Edge.NONE
        }
    }
}

class FieldView {
    @Composable
    fun drawField (x: Int, y: Int, cellView: CellView) {
        Row {
            Box (
                modifier = Modifier
                    .weight(1f) // цена
                    .background(color = Color.Black)
                    .padding(all = 16.dp)
                    .fillMaxHeight()
            ) {
                MaterialTheme {
                    val field = Field(x, y)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(x),
                        content = {
                            items(x * y){ i ->
                                cellView.makeBox(field.field[i/x][i%x])
                            }
                        }
                    )
                }
            }
        }
    }
}