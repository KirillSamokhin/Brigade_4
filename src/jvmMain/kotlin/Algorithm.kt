import kotlin.math.abs


class Algorithm (private var field: Field) {
    private val log = Logger()
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
        return 2 * (abs(fx-x) + abs(fy-y))
    }

    fun cellProcess () {
        var xp = x
        var yp = y
        when (offset) {
            1 -> xp = x-1
            2 -> yp = y-1
            3 -> xp = x+1
            4 -> yp = y+1
        }
        offset = (offset + 1) % 5
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
            if (a == -1) {
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
        offset = (offset + 1) % 5
        if (queue.size() == 0) return roots
        cur = queue.extractMin()
        cur.status = Status.VIEWED
        log.curCell(cur)
        x = cur.x
        y = cur.y
        if(cur == end){
            log.finishReached()
            return roots
        }
        return null
    }

    fun fullIteration (): MutableMap<Cell, Cell?> {
        while (offset != 0){
            cellProcess()
        }
        while (queue.size() != 0) {
            offset = (offset + 1) % 5
            cur = queue.extractMin()
            cur.status = Status.VIEWED
            x = cur.x
            y = cur.y
            if (cur == end) {
                log.finishReached()
                return roots
            }
            repeat (times = 4) {
                cellProcess()
            }
        }
        return roots
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