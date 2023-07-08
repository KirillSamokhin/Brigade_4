import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.abs

enum class Status {
    NOTVIEWED,
    VIEWED,
    CHECK,
    PATH
}

enum class Base {
    GRASS,
    WATER,
    STONE
}

enum class Edge {
    START,
    FINISH,
    NONE
}

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
            Base.GRASS -> 10
            Base.WATER -> 30
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



class Field(val x: Int, val y: Int){
    val field = Array(this.y){Array<Cell>(this.x){Cell()}}
    var startCord = Pair(-1, -1)
    var finishCord = Pair(-1, -1)

    init{
        for(i in 0 until y){
            for(j in 0 until x){
                this.field[i][j].x = j
                this.field[i][j].y = i
            }
        }
    }

    fun default () {
        field.forEach { it ->
            it.forEach {
                it.g = -1
                it.h = 0
                it.f = 0
                it.status = Status.NOTVIEWED
                it.base = Base.GRASS
                it.edge = Edge.NONE
            }
        }
    }
}

class Heap {
    private var queue = emptyList<Cell>().toMutableList()

    private fun siftUp (index: Int) {
        if (index < 0 || index >= this.queue.size) {
            return
        }
        var tmpIndex = index
        var parent = (index - 1) / 2
        while (tmpIndex > 0 && this.queue[parent].f >= this.queue[tmpIndex].f) {
            val tmp = this.queue[parent]
            this.queue[parent] = this.queue[tmpIndex]
            this.queue[tmpIndex] = tmp
            val buf = tmpIndex
            tmpIndex = parent
            parent = (buf-1)/2
        }
    }

    private fun siftDown (index: Int) {
        if (index < 0 || index >= this.queue.size) {
            return
        }
        var minIndex = index
        var tmpIndex = index
        var left: Int
        var right: Int
        while (true) {
            left = 2 * tmpIndex + 1
            right = 2 * tmpIndex + 2
            if (right < this.queue.size && this.queue[right].f < this.queue[minIndex].f)
                minIndex = right
            if( left < this.queue.size && this.queue[left].f < this.queue[minIndex].f)
                minIndex = left
            if (minIndex == tmpIndex)
                return
            else {
                this.queue[tmpIndex]; this.queue[minIndex] = this.queue[minIndex]; this.queue[tmpIndex]
                tmpIndex = minIndex
            }
        }
    }

    fun extractMin (): Cell {
        val minElement = this.queue[0]
        this.queue[0] = this.queue[this.queue.size-1]
        this.queue.removeAt(this.queue.size - 1)
        this.siftDown(0)
        return minElement
    }

    fun put (element: Cell) {
        this.queue.add(element)
        this.siftUp(this.size() - 1)
    }

    fun size (): Int {
        return this.queue.size
    }
}


class Algorithm(var field: Field){
    private val log = Logger()
    private var stop = false
    var sx: Int = 0
    var sy: Int = 0
    var fx: Int = 0
    var fy: Int = 0
    lateinit var roots: MutableMap<Cell, Cell?>
    var queue = Heap()
    lateinit var end: Cell
    var x: Int = 0
    var y: Int = 0
    lateinit var cur: Cell
    var offset = 0


    fun heuristic(x: Int, y: Int, fx: Int, fy: Int): Int{
        return 20*(abs(fx-x) + abs(fy-y))
    }

    fun cellProcess(){
        var xp = x
        var yp = y

        when (offset){
            0 -> xp = x-1
            1 -> yp = y-1
            2 -> xp = x+1
            3 -> yp = y+1
        }
        offset = (offset + 1) % 4
        val parent = cur
        if(xp < 0 || yp < 0 || xp >= this.field.x || yp >= this.field.y){
            return
        }
        if(this.field.field[yp][xp].base == Base.STONE){
            log.stone(xp, yp)
            return
        }
        val node = this.field.field[yp][xp]
        val temp_dist = parent.g + this.field.field[yp][xp].getWeight()
        if(node.g == -1 || node.g > temp_dist){
            val a = node.g
            roots[node] = parent
            node.setParams(temp_dist, heuristic(xp, yp, fx, fy))
            node.parent = Pair<Int, Int>(parent.x, parent.y)
            node.status = Status.CHECK
            queue.put(node)
            if(a == -1){
                log.cellProc(node)
            }
            else{
                log.betterWay(node)
            }
        }
    }

    fun Astar(){
        sx = field.startCord.first
        sy = field.startCord.second
        fx = field.finishCord.first
        fy = field.finishCord.second
        roots = mutableMapOf(field.field[sy][sx] to null)
        end = field.field[fy][fx]
        field.field[sy][sx].setParams(0, heuristic(sx, sy, fx, fy))
        queue.put(field.field[sy][sx])
        //iteration()
    }

    fun iteration(): MutableMap<Cell, Cell?>?{
        println("iteration")
        if (queue.size() == 0) return roots
        cur = queue.extractMin()
        cur.status = Status.VIEWED
        log.curCell(cur)
        x = cur.x
        y = cur.y
        if(cur == end){
            log.finishReached()
            stop = true
            return roots
        }
        //for(i in listOf(-1, 1)){
            //cellProcess()
            //cellProcess(x+i, y, queue, roots, cur, fx, fy)
            //cellProcess()
            //cellProcess(x, y+i, queue, roots, cur, fx, fy) }
        return null
    }

    fun recoverPath (roots: MutableMap<Cell, Cell?>) {
        val end = field.field[field.finishCord.second][field.finishCord.first]
        var curr: Cell? = end
        when (roots[end] != null) {
            true -> {
                while(curr != null) {
                    curr.status = Status.PATH
                    curr = roots[curr]
                }
            }
            false -> {
                log.finishUnreachable()
            }
        }
    }
}