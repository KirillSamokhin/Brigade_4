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
}