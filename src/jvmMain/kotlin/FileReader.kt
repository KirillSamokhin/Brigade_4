import java.io.File

class FileReader(file: String = "config.txt"){
    private val source = File(file)

    fun readMap(): Field {
        val lines = source.readLines()
        val X = lines[0].split(" ")[0].toInt()
        val Y = lines[0].split(" ")[1].toInt()
        var sx = lines[1].split(" ")[0].toInt()
        var sy = lines[1].split(" ")[1].toInt()
        var fx = lines[2].split(" ")[0].toInt()
        var fy = lines[2].split(" ")[1].toInt()
        when{
            sx < 0 -> sx = 0
            sx >= X -> sx = X-1
        }
        when {
            sy < 0 -> sy = 0
            sy >= Y -> sy = Y - 1
        }
        when{
            fx < 0 -> fx = 1
            fx >= X -> fx = X-1
        }
        when{
            fy < 0 -> fy = 1
            fy >= Y -> fy = Y-1
        }
        val field = Field(X, Y)
        field.field[sy][sx].edge = Edge.START
        field.startCord = Pair(sx, sy)
        field.field[sy][sx].g = 0
        field.field[fy][fx].edge = Edge.FINISH
        field.finishCord = Pair(fx, fy)
        var counter = 0
        var i = 3
        while(counter < Y && i < lines.size){
            if(lines[i] == ""){
                i+=1
                continue
            }
            val line = lines[i].split(" ")
            if(line.size < X){
                i+=1
                continue
            }
            for(j in 0 until X){
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