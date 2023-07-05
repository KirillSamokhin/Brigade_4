import kotlin.math.abs

class Algorythm(var field: Field){
    fun heuristic(x: Int, y: Int, fx: Int, fy: Int): Int{
        return 20*(abs(fx-x) + abs(fy-y))
    }

    fun Astar(sx: Int, sy: Int, fx: Int, fy: Int){

    }
}