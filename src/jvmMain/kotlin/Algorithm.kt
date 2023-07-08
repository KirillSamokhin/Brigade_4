import kotlin.math.abs

class Algorithm(private var field: Field){
    private val log = Logger()
    private var stop = false
    private var sx: Int = 0
    private var sy: Int = 0
    private var fx: Int = 0
    private var fy: Int = 0
    private lateinit var roots: MutableMap<Cell, Cell?>
    private var queue = Heap()
    private lateinit var end: Cell
    private var x: Int = 0
    private var y: Int = 0
    private lateinit var cur: Cell
    private var offset = 0

    private fun heuristic (x: Int, y: Int, fx: Int, fy: Int): Int {
        return 20*(abs(fx-x) + abs(fy-y))
    }

    fun cellProcess () {
        var xp = x
        var yp = y
        when (offset) {
            0 -> xp = x-1
            1 -> yp = y-1
            2 -> xp = x+1
            3 -> yp = y+1
        }
        offset = (offset + 1) % 4
        val parent = cur
        if (xp < 0 || yp < 0 || xp >= this.field.x || yp >= this.field.y) {
            log.outOfBounds(xp, yp)
            return
        }
        if (this.field.field[yp][xp].base == Base.STONE) {
            log.stone(xp, yp)
            return
        }
        val node = this.field.field[yp][xp]
        val tempDist = parent.g + this.field.field[yp][xp].getWeight()
        if (node.g == -1 || node.g > tempDist) {
            val a = node.g
            roots[node] = parent
            node.setParams(tempDist, heuristic(xp, yp, fx, fy))
            node.parent = Pair(parent.x, parent.y)
            node.status = Status.CHECK
            queue.put(node)
            if(a == -1){
                log.cellProcesses(node)
            } else {
                log.betterWay(node)
            }
        } else {
            log.cellViewed(xp, yp)
        }
    }

    fun aStar () {
        sx = field.startCord.first
        sy = field.startCord.second
        fx = field.finishCord.first
        fy = field.finishCord.second
        roots = mutableMapOf(field.field[sy][sx] to null)
        end = field.field[fy][fx]
        field.field[sy][sx].setParams(0, heuristic(sx, sy, fx, fy))
        queue.put(field.field[sy][sx])
    }

    fun iteration (): MutableMap<Cell, Cell?>? {
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