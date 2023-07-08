import kotlin.math.abs

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
            log.outOfBounds(xp, yp)
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
                log.cellProcesses(node)
            }
            else{
                log.betterWay(node)
            }
        }
        else{
            log.cellViewed(xp, yp)
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
    }

    fun iteration(): MutableMap<Cell, Cell?>?{
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