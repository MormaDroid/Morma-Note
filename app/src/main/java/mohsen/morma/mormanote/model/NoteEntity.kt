package mohsen.morma.mormanote.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.room.Entity
import androidx.room.PrimaryKey
import mohsen.morma.mormanote.ui.theme.DarkBlue

@Entity
data class NoteEntity(
    var title: String="",
    var content: String="",
    var font: Int = 2131230728,
    var fontCounter: Int = -1,
    var fontSize: Int = -1,
    var date: String="",
    var charCount: Int = 0,
    var link: String="",
    var color: Int = DarkBlue.toArgb(),
    var backgroundColor: Int = Color.White.toArgb(),
    var backgroundPic: Int = 0,
    var iconTintSelected: Int = DarkBlue.toArgb(),
    var alignment: String = TextAlign.Start.toString(),
    var iconAlign: Int = 0,
    @PrimaryKey(true) var id: Int = 0,
    var uid: String = ""
) 
