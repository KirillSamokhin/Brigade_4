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


class Field (var x: Int, var y: Int) {
    val field = Array(this.y){Array(this.x){ Cell() }}

    init {
        for (i in 0 until y) {
            for (j in 0 until x) {
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


class Algorithm (private var field: Field) {
    private fun heuristic (x: Int, y: Int, fx: Int, fy: Int): Int {
        return 20 * (abs(fx-x) + abs(fy-y))
    }

    private fun cellProcess (x: Int, y: Int, queue: Heap, roots: MutableMap<Cell, Cell?>, parent: Cell, fx: Int, fy: Int) {
        if (x < 0 || y < 0 || x >= this.field.x || y >= this.field.y) {
            return
        }
        if (this.field.field[y][x].base == Base.STONE) {
            return
        }
        val node = this.field.field[y][x]
        val tempDist = parent.g + this.field.field[y][x].getWeight()
        if (node.g == -1 || node.g > tempDist) {
            roots[node] = parent
            node.setParams(tempDist, heuristic(x, y, fx, fy))
            node.parent = Pair(parent.x, parent.y)
            node.status = Status.CHECK
            queue.put(node)
        }
    }

    fun aStar (sx: Int, sy: Int, fx: Int, fy: Int): MutableMap<Cell, Cell?> {
        val roots: MutableMap<Cell, Cell?> = mutableMapOf(field.field[sy][sx] to null)
        val queue = Heap()
        val end = field.field[fy][fx]
        field.field[sy][sx].f = heuristic(sx, sy, fx, fy)
        queue.put(field.field[sy][sx])
        while (queue.size() != 0) {
            val cur = queue.extractMin()
            cur.status = Status.VIEWED
            val x = cur.x
            val y = cur.y
            if (cur == end) {
                break
            }
            for (i in listOf(-1, 1)) {
                cellProcess(x+i, y, queue, roots, cur, fx, fy)
                cellProcess(x, y+i, queue, roots, cur, fx, fy)
            }
        }
        return roots
    }

    fun recoverPath (roots: MutableMap<Cell, Cell?>, end: Cell) {
        var curr: Cell? = end
        when (roots[end] != null) {
            true -> {
                while(curr != null){
                    curr.status = Status.PATH
                    curr = roots[curr]
                }
            }
            false -> { }
        }
    }
}