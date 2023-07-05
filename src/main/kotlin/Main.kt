val scan = java.util.Scanner(System.`in`)

fun main(args: Array<String>){
    val test: Field = Field(4, 3)
    for(i in 0..2){
        for(j in 0..3){
            var c = scan.next()
            when(c){
                "G" -> test.field[i][j].base = Base.GRASS
                "M" -> test.field[i][j].base = Base.STONE
                "W" -> test.field[i][j].base = Base.WATER
                "S" -> test.field[i][j].edge = Edge.START
                "F" -> test.field[i][j].edge = Edge.FINISH
            }
        }
    }
    val al = Algorythm(test)
    val roots = al.Astar(0, 1, 3, 2)
    val ans = al.recoverPath(roots, test.field[2][3])
    for(i in ans){
        println("${i.x} ${i.y}")
    }
}