class Field (val x: Int, val y: Int) {
    val field = Array(this.y){Array(this.x){Cell()}}
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