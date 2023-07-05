val scan = java.util.Scanner(System.`in`)
fun main(args: Array<String>){
    val test = Heap()
    test.put(Cell(x=0, y=0, f = 30, g = 20, h = 10))
    test.put(Cell(x=1, y=0, f = 20, g = 10, h = 10))
    println(test.queue[1].f)
}