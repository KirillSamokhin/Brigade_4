val scan = java.util.Scanner(System.`in`)

fun main(args: Array<String>){
    val reader = FileReader()
    val test: Field = reader.readMap()
    val al = Algorythm(test)
    val roots = al.Astar()
    val ans = al.recoverPath(roots)
}