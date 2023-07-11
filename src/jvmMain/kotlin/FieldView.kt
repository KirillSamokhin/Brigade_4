import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


class FieldView {
    @Composable
    fun drawField (field: Field, cellView: CellView) {
        Row {
            Box (
                modifier = Modifier
                    .background(color = Color.Black)
                    .padding(all = 16.dp)
                    .fillMaxHeight()
            ) {
                MaterialTheme {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(field.x),
                        content = {
                            items(count = field.x * field.y){ i ->
                                cellView.makeBox(field.field[i / field.x][i % field.x])
                            }
                        }
                    )
                }
            }
        }
    }
}