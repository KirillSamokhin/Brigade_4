class Logger{
    fun startCreated(x: Int, y: Int){
        println("Старт установлен в клетку с координатами ($x, $y).")
    }
    fun finishCreated(x: Int, y: Int){
        println("Финиш установлен в клетку с координатами ($x, $y).")
    }
    fun finishUnreachable(){
        println("\nФиниш недостижим!")
    }
    fun finishReached(){
        println("\nФиниш достигнут!")
    }
    fun curCell(cur: Cell){
        println("\nПометим клетку (${cur.x}, ${cur.y}) со значением эвристической функции f = ${cur.f} как просмотренную.")
    }
    fun stone(x: Int, y: Int){
        println("\tВ клетке с координатами ($x, $y) расположен камень, переход невозможен.")
    }
    fun cellProc(cur: Cell){
        println("\tВершина (${cur.x}, ${cur.y}) добавлена в очередь. Установлены следующие числовые характеристики:\n" +
                "\tg = ${cur.g}\n" +
                "\th = ${cur.h}\n" +
                "\tf = g + h = ${cur.f}")
    }
    fun betterWay(cur: Cell){
        println("\tНайден более короткий путь до вершины (${cur.x}, ${cur.y}). Теперь g = ${cur.g}, а f = ${cur.f}.")
    }
    fun viewPath(root: MutableList<Cell>){
        println("\nИтоговый путь имеет вид:")
        print("\t(${root[0].x}, ${root[0].y})")
        for(i in 1 until root.size){
            print(" -> (${root[i].x}, ${root[i].y})")
        }
    }
}