package mohsen.morma.mormanote.model

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.room.Entity
import androidx.room.PrimaryKey
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.ysabeauMedium
import java.io.File

@Entity
data class NoteEntity(
    val title: String,
    val content: String,
    val font : FontFamily = ysabeauMedium,
    val date : String,
    val color: Color = DarkBlue,
    val backgroundColor: Color = Color.White,
    val backgroundPic: Int? = null,
    val iconTintSelected: Color = DarkBlue,
    val alignment : TextAlign = TextAlign.Start,
    val gallery: Uri?,
    val voice : File? = null,
    @PrimaryKey(true) val id  :Int=0
)
