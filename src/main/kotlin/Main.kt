val scan = java.util.Scanner(System.`in`)

fun main(args: Array<String>){
    val reader = FileReader()
    val test: Field = reader.readMap()
    var sx = 0
    var sy = 0
    var fx = 0
    var fy = 0
    sx = reader.readStart(test.x, test.y).first
    sy = reader.readStart(test.x, test.y).second
    fx = reader.readFinish(test.x, test.y).first
    fy = reader.readFinish(test.x, test.y).second
    val al = Algorythm(test)
    val roots = al.Astar(sx, sy, fx, fy)
    val ans = al.recoverPath(roots, test.field[fy][fx])
}