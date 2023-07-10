import java.io.File


class FileReader(file: String = "config.txt"){
    private val source = File(file)

    fun readMap(): Field? {
        val lines = source.readLines()
        for (i in 0..2) {
            if (lines[i].split(" ").size != 2) {
                return null
            }
        }
        val x = lines[0].split(" ")[1].toIntOrNull() ?: 0
        val y = lines[0].split(" ")[0].toIntOrNull() ?: 0
        if (x !in 2..50 || y !in 2..50) {
            return null
        }
        val sx = lines[1].split(" ")[0].toIntOrNull() ?: 0
        val sy = lines[1].split(" ")[1].toIntOrNull() ?: 0
        if (sx !in 0 until x || sy !in 0 until y) {
            return null
        }
        val fx = lines[2].split(" ")[0].toIntOrNull() ?: 0
        val fy = lines[2].split(" ")[1].toIntOrNull() ?: 0
        if (fx !in 0 until x || fy !in 0 until y) {
            return null
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