class Singleton private constructor() {
    var message = ""

    companion object {
        @Volatile
        private var instance: Singleton? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: Singleton().also { instance = it }
            }
    }
}