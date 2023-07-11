import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class Cell {
    var base by mutableStateOf(value = Base.GRASS)
    var status by mutableStateOf(value = Status.NOTVIEWED)
    var edge by mutableStateOf(value = Edge.NONE)
    var f by mutableStateOf(value = 0)
    var g by mutableStateOf(value = -1)
    var h by mutableStateOf(value = 0)
    var x: Int = 0
    var y: Int = 0
    var parent: Pair<Int, Int> = Pair(-1, -1)

    fun setParams (g: Int, h: Int) {
        this.g = g
        this.h = h
        this.f = g+h
    }

    fun getWeight(): Int {
        return when(this.base){
            Base.GRASS -> 1
            Base.WATER -> 3
            Base.STONE -> -1
        }
    }

    fun changeBase(){
        base = when (base){
            Base.GRASS -> Base.WATER
            Base.WATER -> Base.STONE
            Base.STONE -> Base.GRASS
        }
    }

    fun changeEdge (string: String) {
        edge = when (string) {
            "START" -> {
                this.g = 0
                Edge.START
            }
            "FINISH" -> {
                Edge.FINISH
            }
            else -> {
                this.g = -1
                Edge.NONE
            }
        }
    }
}