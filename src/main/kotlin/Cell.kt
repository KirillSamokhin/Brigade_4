class Cell{

    var status: Status = Status.NOTVIEWED
    var base: Base = Base.GRASS
    var edge: Edge = Edge.NONE
    var x: Int = 0
    var y: Int = 0
    var f: Int  = 0
    var g: Int = -1
    var h: Int = 0
    var parent: Pair<Int, Int> = Pair(-1, -1)
    fun setParams(g: Int, h: Int){
        this.g = g
        this.h = h
        this.f = g+h
    }
    fun getWeight(): Int{
        when(this.base){
            Base.GRASS -> return 10
            Base.WATER -> return 30
            Base.STONE -> return -1
        }
    }
}