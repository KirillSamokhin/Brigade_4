import java.io.File


class FileReader(file: String = "config.txt"){
    private val source = File(file)

    fun readMap(): Field? {
        val lines = source.readLines()
        val x = lines[0].split(" ")[0].toIntOrNull() ?: 0
        val y = lines[0].split(" ")[1].toIntOrNull() ?: 0
        var sx = lines[1].split(" ")[0].toInt()
        var sy = lines[1].split(" ")[1].toInt()
        var fx = lines[2].split(" ")[0].toInt()
        var fy = lines[2].split(" ")[1].toInt()
        if (x <= 1 || y <= 1) {
            return null
        }
        when {
            sx < 0 -> sx = 0
            sx >= x -> sx = x-1
        }
        when {
            sy < 0 -> sy = 0
            sy >= y -> sy = y - 1
        }
        when {
            fx < 0 -> fx = 1
            fx >= x -> fx = x-1
        }
        when {
            fy < 0 -> fy = 1
            fy >= y -> fy = y-1
        }
        val field = Field(x, y)
        field.field[sy][sx].edge = Edge.START
        field.startCord = Pair(sx, sy)
        field.field[sy][sx].g = 0
        field.field[fy][fx].edge = Edge.FINISH
        field.finishCord = Pair(fx, fy)
        var counter = 0
        var i = 3
        while(counter < y && i < lines.size){
            if(lines[i] == ""){
                i+=1
                continue
            }
            val line = lines[i].split(" ")
            if(line.size < x){
                i+=1
                continue
            }
            for(j in 0 until x){
                when (line[j]) {
                    "Т" -> field.field[counter][j].base = Base.GRASS
                    "В" -> field.field[counter][j].base = Base.WATER
                    "К" -> field.field[counter][j].base = Base.STONE
                }
            }
            i+=1
            counter += 1
        }
        return field
    }
}