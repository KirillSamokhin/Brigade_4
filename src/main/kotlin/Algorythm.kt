import kotlin.math.abs

class Algorythm(var field: Field){
    fun heuristic(x: Int, y: Int, fx: Int, fy: Int): Int{
        return 20*(abs(fx-x) + abs(fy-y))
    }

    fun cellProcess(x: Int, y: Int, queue: Heap, roots: MutableMap<Cell, Cell?>, parent: Cell, fx: Int, fy: Int){
        if(x < 0 || y < 0 || x >= this.field.x || y >= this.field.y){
            return
        }
        val log = Logger()
        if(this.field.field[y][x].base == Base.STONE){
            log.stone(x, y)
            return
        }
        val node = this.field.field[y][x]
        val temp_dist = parent.g + this.field.field[y][x].getWeight()
        if(node.g == -1 || node.g > temp_dist){
            val a = node.g
            roots[node] = parent
            node.setParams(temp_dist, heuristic(x, y, fx, fy))
            node.parent = Pair<Int, Int>(parent.x, parent.y)
            node.status = Status.CHECK
            queue.put(node)
            if(a == -1){
                log.cellProc(node)
            }
            else{
                log.betterWay(node)
            }
//            Thread.sleep(1000)
        }
    }

    fun Astar(): MutableMap<Cell, Cell?>{
        val log = Logger()
        val sx = field.startCord.first
        val sy = field.startCord.second
        val fx = field.finishCord.first
        val fy = field.finishCord.second
        var roots: MutableMap<Cell, Cell?> = mutableMapOf(field.field[sy][sx] to null)
        var queue = Heap()
        val end = field.field[fy][fx]
        field.field[sy][sx].f = heuristic(sx, sy, fx, fy)
        queue.put(field.field[sy][sx])
        while(queue.size() != 0){
            val cur = queue.extractMin()
            cur.status = Status.VIEWED
            log.curCell(cur)
            val x = cur.x
            val y = cur.y
            if(cur == end){
                log.finishReached()
                break
            }
            for(i in listOf(-1, 1)){
                cellProcess(x+i, y, queue, roots, cur, fx, fy)
                cellProcess(x, y+i, queue, roots, cur, fx, fy)
            }
        }
        return roots
    }

    fun recoverPath(roots: MutableMap<Cell, Cell?>): MutableList<Cell>{
        val log = Logger()
        val end = field.field[field.finishCord.second][field.finishCord.first]
        var path = emptyList<Cell>().toMutableList()
        var curr: Cell? = end
        if(roots[end] == null){
            log.finishUnreachable()
            return path
        }
        while(curr != null){
            path.add(curr)
            curr = roots[curr]
        }
        path.reverse()
        log.viewPath(path)
        return path
    }
}