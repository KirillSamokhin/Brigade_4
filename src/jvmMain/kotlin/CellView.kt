import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp


class CellView {
    private val pictures = listOf( "Grass.jpg", "Water.jpg", "Stone.jpg" )
    private val nameCells = listOf( "Клетка травы", "Клетка воды", "Клетка камня" )
    var clickable = true
    var start = false
    var finish = false
    var cellStart: Cell? = null
    var cellFinish: Cell? = null

    @Composable
    fun makeBox (cell: Cell) {
        BoxWithConstraints (
            modifier = Modifier
                .padding(all = 1.dp)
                .aspectRatio(ratio = 1f)
                .clickable {
                    if (clickable) {
                        when {
                            start -> {
                                cellStart?.changeEdge("NONE")
                                cell.changeEdge("START")
                                cellStart = cell
                                start = false
                                if (cellStart == cellFinish) {
                                    cellFinish = null
                                }
                            }
                            finish -> {
                                if (cellStart != cell) {
                                    cellFinish?.changeEdge("NONE")
                                    cell.changeEdge("FINISH")
                                    cellFinish = cell
                                    finish = false
                                }
                            }
                            else -> cell.changeBase()
                        }
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Image (
                painter = painterResource(getImageName(cell)),
                contentDescription = "Cell pictures",
                modifier = Modifier
                    .fillMaxSize()
            )
            Box (
                modifier = Modifier
                    .matchParentSize()
                    .background(getColor(cell))
            ) { }
            val size = min(maxWidth, maxHeight) * 0.3f
            when (cell.edge) {
                Edge.START -> Icon (
                        Icons.Default.Face,
                        contentDescription = "Start icon",
                        modifier = Modifier
                            .size(size = size),
                        tint = Color.Black,
                    )
                Edge.FINISH -> Icon (
                        Icons.Default.Home,
                        contentDescription = "Finish icon",
                        modifier = Modifier
                            .size(size = size),
                        tint = Color.Black,
                    )
                else -> { }
            }
            Text (
                text = " " + cell.g.toString(),
                color = Color(color = 0xff08086b),
                fontSize = LocalDensity.current.run{ size.toSp() },
                modifier = Modifier
                    .align(Alignment.TopStart)
            )
            Text (
                text = " " + cell.h.toString(),
                color = Color(color = 0xff6b0505),
                fontSize = LocalDensity.current.run{ size.toSp() },
                modifier = Modifier.align(Alignment.CenterStart))

            Text (
                text = " " + cell.f.toString(),
                fontSize = LocalDensity.current.run{ size.toSp() },
                modifier = Modifier
                    .align(Alignment.BottomStart)
            )
        }
    }

    @Composable
    fun screenInformationAboutTypes () {
        Column {
            for (i in (pictures.indices)) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box (
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .size(size = 95.dp)
                            .background(color = Color.Black)
                            .padding(all = 2.dp)
                    ) {
                        Image (
                            painter = painterResource(pictures[i]),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                        )
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

    fun cellEdge (edge: String) {
        when(edge){
            "START" -> {
                start = true
                finish = false
            }
            "FINISH" -> {
                start = false
                finish = true
            }
        }
    }

    fun default(){
        clickable = true
        cellStart = null
        cellFinish = null
        start = false
        finish = false

    }

    private fun getImageName (cell: Cell): String {
        if (cell.status == Status.PATH) return "Gravel.png"
        return when (cell.base) {
            Base.GRASS -> "Grass.jpg"
            Base.WATER -> "Water.jpg"
            Base.STONE -> "Stone.jpg"
        }
    }

    private fun getColor (cell: Cell): Color {
        return when(cell.status){
            Status.CHECK -> Color(0x88696969)
            Status.VIEWED -> Color(0xaa161616)
            else -> Color(0x00000000)
        }
    }
}