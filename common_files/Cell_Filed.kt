package Cell_Field

enum class Status {
    NOTVIEWED,
    VIEWED,
    CHECK,
    INPATH
}

enum class Base{
    GRASS,
    WATER,
    STONE
}

enum class Edge {
    START,
    FINISH,
    NONE
}

class Cell(status: Status = Status.NOTVIEWED, base: Base = Base.GRASS, edge: Edge = Edge.NONE, var x: Int = 0, var y: Int = 0, var f: Int = 0, var g: Int = 0, var h: Int = 0){
    var status: Status = status
        set(value){
            field = value
        }
        get() = field
    var base: Base = base
        set(value){
            field = value
        }
        get() = field
    var edge: Edge = edge
        set(value){
            field = value
        }
        get() = field
    fun setCoord(X: Int, Y: Int){
        this.x = X
        this.y = Y
    }
    fun getCoord(): Pair<Int, Int>{
        return Pair(this.x, this.y)
    }
    fun setParams(g: Int, h: Int){
        this.g = g
        this.h = h
        this.f = g+h
    }
    fun getParams(): IntArray{
        var tmp = 0
        when(this.base){
            Base.GRASS -> tmp=10
            Base.WATER -> tmp=30
            Base.STONE -> tmp=-1
        }
        return intArrayOf(tmp, f, g, h)
    }
}

class Field(val x: Int, val y: Int){
    val field = Array(this.y){Array<Cell>(this.x){Cell(x=0, y=0)}}
    init{
        for(i in 0 until y){
            for(j in 0 until x){
                this.field[i][j].setCoord(j, i)
            }
        }
    }
}