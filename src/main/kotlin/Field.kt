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