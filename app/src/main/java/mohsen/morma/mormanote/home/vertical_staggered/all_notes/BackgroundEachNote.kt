package mohsen.morma.mormanote.home.vertical_staggered.all_notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.util.RippleIcon

@Composable
fun BackgroundEachNote() {
    Card(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color(0xFFC70000),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.End,
            Alignment.CenterVertically
        ) { RippleIcon(icon = R.drawable.delete, tint = Color.White) }
    }
}