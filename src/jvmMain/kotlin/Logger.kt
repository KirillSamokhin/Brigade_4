class Logger {
    private val singleton = Singleton.getInstance()

    fun startCreated (x: Int, y: Int) {
        singleton.message = "Старт установлен в клетку с координатами ($x, $y)."
    }

    fun finishCreated (x: Int, y: Int) {
        singleton.message = "Финиш установлен в клетку с координатами ($x, $y)."
    }

    fun finishUnreachable () {
        singleton.message = "Финиш недостижим!"
    }

    fun finishReached () {
        singleton.message = "Финиш достигнут!"
    }

    fun curCell (cur: Cell) {
        singleton.message = "Пометим клетку (${cur.x}, ${cur.y}) со значением эвристической функции f = ${cur.f} как просмотренную."
    }

    fun stone (x: Int, y: Int) {
        singleton.message = "В клетке с координатами ($x, $y) расположен камень, переход невозможен."
    }

    fun cellProc (cur: Cell) {
        singleton.message = "Вершина (${cur.x}, ${cur.y}) добавлена в очередь. Установлены следующие числовые характеристики:\n" +
                "g = ${cur.g}\n" +
                "h = ${cur.h}\n" +
                "f = g + h = ${cur.f}"
    }

    fun betterWay (cur: Cell) {
        singleton.message = "Найден более короткий путь до вершины (${cur.x}, ${cur.y}). Теперь g = ${cur.g}, а f = ${cur.f}."
    }

    fun viewPath(root: MutableList<Cell>){
        println("\nИтоговый путь имеет вид:")
        print("\t(${root[0].x}, ${root[0].y})")
        for(i in 1 until root.size){
            print(" -> (${root[i].x}, ${root[i].y})")
        }
    }

    fun outOfBounds(x: Int, y: Int){
        singleton.message = "Клетка с координатами ($x, $y) выходит за границы поля."
    }

    fun cellViewed(x: Int, y: Int){
        singleton.message = "Клетка с координатами ($x, $y) уже рассмотрена."
    }
}