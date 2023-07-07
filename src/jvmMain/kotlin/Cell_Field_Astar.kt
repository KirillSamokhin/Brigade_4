import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.abs

enum class Status {
    NOTVIEWED,
    VIEWED,
    CHECK,
    INPATH
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
    var base by mutableStateOf(Base.GRASS)
    var status by mutableStateOf(Status.NOTVIEWED)
    var edge by mutableStateOf(Edge.NONE)
    var x: Int = 0
    var y: Int = 0
    var f by mutableStateOf(0)
    var g by mutableStateOf(-1)
    var h by mutableStateOf(0)
    var parent: Pair<Int, Int> = Pair(-1, -1)
    fun setParams(g: Int, h: Int){
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


class Field (val x: Int, val y: Int) {
    val field = Array(this.y){Array<Cell>(this.x){ Cell() }}
    init{
        for(i in 0 until y){
            for(j in 0 until x){
                this.field[i][j].x = j
                this.field[i][j].y = i
            }
        }
    }
    fun defaultSettings(){
        field.forEach{
            it.forEach {
                it.g = -1
                it.h = 0
                it.f = 0
                it.status = Status.NOTVIEWED
                it.base = Base.GRASS
                it.changeEdge("NONE")
            }
        }
    }
}

class Heap(){
    var queue = emptyList<Cell>().toMutableList()

    fun siftUp(index: Int){
        if(index < 0 || index >= this.queue.size){
            return
        }
        var tmpIndex = index
        var parent = (index - 1)/2
        while(tmpIndex > 0 && this.queue[parent].f >= this.queue[tmpIndex].f){
            val tmp = this.queue[parent]
            this.queue[parent] = this.queue[tmpIndex]
            this.queue[tmpIndex] = tmp
            val buf = tmpIndex
            tmpIndex = parent
            parent = (buf-1)/2
        }
    }

    fun siftDown(index: Int){
        if (index < 0 || index >= this.queue.size) {
            return
        }
        var minIndex = index
        var tmpIndex = index
        var left: Int
        var right: Int
        while(true) {
            left = 2*tmpIndex+1
            right = 2*tmpIndex+2
            if(right < this.queue.size && this.queue[right].f < this.queue[minIndex].f)
                minIndex = right
            if(left < this.queue.size && this.queue[left].f < this.queue[minIndex].f)
                minIndex = left
            if(minIndex == tmpIndex)
                return
            else{
                this.queue[tmpIndex]; this.queue[minIndex] = this.queue[minIndex]; this.queue[tmpIndex]
                tmpIndex = minIndex
            }
        }
    }

    fun extractMin(): Cell {
        val min_element = this.queue[0]
        this.queue[0] = this.queue[this.queue.size-1]
        this.queue.removeAt(this.queue.size - 1)
        this.siftDown(0)
        return min_element
    }

    fun put(element: Cell){
        this.queue.add(element)
        this.siftUp(this.size() - 1)
    }

    fun size(): Int{
        return this.queue.size
    }
}


class Algorythm (var field: Field) {
    fun heuristic(x: Int, y: Int, fx: Int, fy: Int): Int{
        return 20*(abs(fx-x) + abs(fy-y))
    }

    fun cellProcess(x: Int, y: Int, queue: Heap, roots: MutableMap<Cell, Cell?>, parent: Cell, fx: Int, fy: Int){
        if(x < 0 || y < 0 || x >= this.field.x || y >= this.field.y){
            return
        }
        if (this.field.field[y][x].base == Base.STONE || this.field.field[y][x].status == Status.VIEWED) {
            return
        }
        val node = this.field.field[y][x]
        val temp_dist = parent.g + this.field.field[y][x].getWeight()
        if(node.g == -1 || node.g > temp_dist){
            roots[node] = parent
            node.setParams(temp_dist, heuristic(x, y, fx, fy))
            node.parent = Pair<Int, Int>(parent.x, parent.y)
            node.status = Status.CHECK
            queue.put(node)
        }
    }

    fun Astar (sx: Int, sy: Int, fx: Int, fy: Int): MutableMap<Cell, Cell?> {
        var roots: MutableMap<Cell, Cell?> = mutableMapOf(field.field[sy][sx] to null)
        var queue = Heap()
        val end = field.field[fy][fx]
        queue.put(field.field[sy][sx])
        while(queue.size() != 0){
            val cur = queue.extractMin()
            cur.status = Status.VIEWED
            val x = cur.x
            val y = cur.y
            if(cur == end){
                break
            }
            for(i in listOf(-1, 1)){
                cellProcess(x+i, y, queue, roots, cur, fx, fy)
                cellProcess(x, y+i, queue, roots, cur, fx, fy)
            }
        }
        return roots
    }

    fun recoverPath (roots: MutableMap<Cell, Cell?>, end: Cell){
        var curr: Cell? = end
        while(curr != null){
            curr.status = Status.INPATH
            curr = roots[curr]
        }
    }
}