package mohsen.morma.mormanote.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.room.Entity
import androidx.room.PrimaryKey
import mohsen.morma.mormanote.ui.theme.DarkBlue

@Entity
data class NoteEntity(
    val title: String,
    val content: String,
    val font : Int = 800007,
    val date : String,
    val color: Int = DarkBlue.toArgb(),
    val backgroundColor: Int = Color.White.toArgb(),
    val backgroundPic: Int = 0,
    val iconTintSelected: Int = DarkBlue.toArgb(),
    val alignment : String = TextAlign.Start.toString(),
    val gallery: String = "",
    val voice : String = "",
    @PrimaryKey(true) val id  :Int=0
)
