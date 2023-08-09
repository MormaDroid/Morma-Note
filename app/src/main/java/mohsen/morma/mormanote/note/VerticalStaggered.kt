package mohsen.morma.mormanote.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.model.NoteEntity
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.ysabeauBold
import mohsen.morma.mormanote.ui.theme.ysabeauMedium

@Composable
fun VerticalStaggered(noteVM: NoteVM = hiltViewModel()) {

    noteVM.getAllNotes()
    val allNotes = noteVM.notesList.collectAsState().value.sortedByDescending { it.id }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalItemSpacing = 8.dp
    ){
        items(allNotes){note->
            NoteCard(note)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteCard(note:NoteEntity) {
    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier.wrapContentHeight(),
        backgroundColor = DarkBlue,
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = Font(ysabeauBold).toFontFamily(),
                    fontSize = 30.sp,
                    maxLines = 2,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = note.content,
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Medium,
                    fontFamily = Font(ysabeauMedium).toFontFamily(),
                    fontSize = 22.sp,
                    maxLines = 15,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }
    }
}
