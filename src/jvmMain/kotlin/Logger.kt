class Logger {
    private val singleton = Singleton.getInstance()

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

    fun cellProcesses (cur: Cell) {
        singleton.message = "Вершина (${cur.x}, ${cur.y}) добавлена в очередь. Установлены следующие числовые характеристики:\n" +
                "g = ${cur.g}\n" +
                "h = ${cur.h}\n" +
                "f = g + h = ${cur.f}"
    }

    fun betterWay (cur: Cell) {
        singleton.message = "Найден более короткий путь до вершины (${cur.x}, ${cur.y}). Теперь g = ${cur.g}, а f = ${cur.f}."
    }

    fun outOfBounds(x: Int, y: Int){
        singleton.message = "Клетка с координатами ($x, $y) выходит за границы поля."
    }

    fun cellViewed(x: Int, y: Int){
        singleton.message = "Клетка с координатами ($x, $y) уже рассмотрена."
    }
}