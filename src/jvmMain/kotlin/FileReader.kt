import java.io.File

class FileReader (file: String = "config.txt") {
    private val source = File(file)

    fun readStart (x: Int, y: Int): Pair<Int, Int> {
        val lines = source.readLines()
        var sx = lines[1].split(" ")[0].toInt()
        var sy = lines[1].split(" ")[1].toInt()
        when {
            sx < 0 -> sx = 0
            sx >= x -> sx = x-1
        }
        when {
            sy < 0 -> sy = 0
            sy >= y -> sy = y-1
        }
        return Pair(sx, sy)
    }

    fun readFinish (x: Int, y: Int): Pair<Int, Int> {
        val lines = source.readLines()
        var fx = lines[2].split(" ")[0].toInt()
        var fy = lines[2].split(" ")[1].toInt()
        when {
            fx < 0 -> fx = 1
            fx >= x -> fx = x-1
        }
        when {
            fy < 0 -> fy = 1
            fy >= y -> fy = y-1
        }
        return Pair(fx, fy)
    }

    fun readMap (): Field {
        val lines = source.readLines()
        val x = lines[0].split(" ")[0].toInt()
        val y = lines[0].split(" ")[1].toInt()
        val sx = readStart(x, y).first
        val sy = readStart(x, y).second
        val fx = readFinish(x, y).first
        val fy = readFinish(x, y).second
        val field = Field(x, y)
        field.field[sy][sx].edge = Edge.START
        field.field[sy][sx].g = 0
        field.field[fy][fx].edge = Edge.FINISH
        var counter = 0
        var i = 3
        while (counter < y && i < lines.size) {
            if (lines[i] == "") {
                i+=1
                continue
            }
            val line = lines[i].split(" ")
            println(lines[i])
            if(line.size < x-1){
                i+=1
                continue
            }
            for (j in 0 until x) {
                when(line[j]){
                    "G" -> field.field[counter][j].base = Base.GRASS
                    "W" -> field.field[counter][j].base = Base.WATER
                    "M" -> field.field[counter][j].base = Base.STONE
                }
            }
            i+=1
            counter += 1
        }
        return field
    }
}