import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Singleton private constructor() {
    var message by mutableStateOf(value = "")
    companion object {
        @Volatile
        private var instance: Singleton? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: Singleton().also { instance = it }
            }
    }
}