package mohsen.morma.mormanote.home.vertical_staggered

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.OutlinedButton
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.home.vertical_staggered.all_notes.BackgroundEachNote
import mohsen.morma.mormanote.home.vertical_staggered.all_notes.NotePreview
import mohsen.morma.mormanote.model.NoteEntity
import mohsen.morma.mormanote.setting.appFontSelected
import mohsen.morma.mormanote.setting.appThemeSelected
import mohsen.morma.mormanote.ui.theme.ysabeauBold
import mohsen.morma.mormanote.ui.theme.ysabeauMedium
import kotlin.properties.Delegates

var isEmptyList by Delegates.notNull<Boolean>()


@Composable
fun VerticalStaggered(
    navController: NavHostController,
    allNotes: List<NoteEntity>
) {

    isEmptyList = allNotes.isEmpty()
    val heightScreen = LocalConfiguration.current.screenHeightDp - 300

    if (isEmptyList) {
        EmptyNoteText(heightScreen)
    } else {
        VerticalStaggeredNote(allNotes, navController)
    }
}

@Composable
private fun EmptyNoteText(heightScreen: Int) {
    Column(
        Modifier
            .fillMaxWidth()
            .height(heightScreen.dp)
            .padding(horizontal = 16.dp), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.nothing_note),
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = Font(appFontSelected).toFontFamily(),
            color = appThemeSelected,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun VerticalStaggeredNote(
    allNotes: List<NoteEntity>,
    navController: NavHostController
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalItemSpacing = 8.dp
    ) {
        items(allNotes, key = { it.id }) { note ->
            NoteCard(note, navController)
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteCard(note: NoteEntity, navController: NavHostController, noteVM: NoteVM = hiltViewModel()) {

    /**A boolean for check delete note or not*/
    var deleteNote by remember { mutableStateOf(false) }

    /**A boolean for check to show the delete note dialog*/
    var isShowDeleteDialog by remember { mutableStateOf(false) }

    /**Coroutine Scope*/
    val scope = rememberCoroutineScope()

    /**a variable to remember swipe state*/
    val swipeState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToStart) isShowDeleteDialog = true
        true
    })

    /**Delete note dialog*/
    if (isShowDeleteDialog)
        AlertDialog(
            onDismissRequest = {
                isShowDeleteDialog = false
                scope.launch { swipeState.reset() }
            },
            confirmButton = {
                Button(
                    onClick = {
                        deleteNote = true
                        noteVM.deleteNote(note)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = stringResource(R.string.yes),
                        fontWeight = FontWeight.Medium,
                        fontFamily = Font(appFontSelected).toFontFamily(),
                        fontSize = 16.sp,
                        color = appThemeSelected
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        isShowDeleteDialog = false
                        scope.launch { swipeState.reset() }
                    },
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text(
                        text = stringResource(R.string.no),
                        fontWeight = FontWeight.Medium,
                        fontFamily = Font(appFontSelected).toFontFamily(),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            },
            title = { TitleDeleteDialog() },
            text = { TextDeleteDialog() },
            backgroundColor = appThemeSelected,
            shape = RoundedCornerShape(16.dp)
        )

    /**update notes list from repo*/
    noteVM.getAllNotes()
    if (noteVM.notesList.collectAsState().value.isEmpty()) isEmptyList = true

    /**All notes Section*/
    AnimatedVisibility(visible = !deleteNote, exit = fadeOut(spring())) {
        SwipeToDismiss(
            state = swipeState,
            dismissThresholds = { FractionalThreshold(0.4f) },
            background = { BackgroundEachNote() },
            directions = setOf(DismissDirection.EndToStart)
        ) {
            NotePreview(navController, note)
        }
    }

    /**Delete Note Action*/
    LaunchedEffect(deleteNote) {
        if (deleteNote) {
            delay(1500)
            noteVM.deleteNote(note)
        }
    }


}


@Composable
private fun TextDeleteDialog() {
    Text(
        text = stringResource(R.string.is_delete_note),
        fontWeight = FontWeight.Medium,
        fontFamily = Font(ysabeauMedium).toFontFamily(),
        fontSize = 18.sp,
        color = Color.White
    )
}

@Composable
private fun TitleDeleteDialog() {
    Text(
        text = stringResource(R.string.delete_note),
        fontWeight = FontWeight.ExtraBold,
        fontFamily = Font(ysabeauBold).toFontFamily(),
        fontSize = 30.sp,
        color = Color.White
    )
}





