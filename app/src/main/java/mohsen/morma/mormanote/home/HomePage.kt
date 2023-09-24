package mohsen.morma.mormanote.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.razaghimahdi.library.core.CardDrawerState
import kotlinx.coroutines.delay
import mohsen.morma.mormanote.auth.snackState
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.gesturesEnabled
import mohsen.morma.mormanote.home.top_bar.TopHomeBarScreen
import mohsen.morma.mormanote.home.vertical_staggered.VerticalStaggered
import mohsen.morma.mormanote.model.NoteEntity


var isShowLoader by mutableStateOf(true)

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(
    navController: NavHostController,
    drawerState: CardDrawerState,
    noteVM: NoteVM = hiltViewModel(),
    datastoreVM: DatastoreVM = hiltViewModel()
) {

    datastoreVM.putFirstTime(false)

    Firebase.firestore
        .collection(Firebase.auth.uid.toString())
        .get()
        .addOnSuccessListener {
            for (note in it.toObjects<NoteEntity>()) {
                noteVM.insertNote(note)
            }
        }





    gesturesEnabled = true


    /**Get Notes From Offline Repo*/
    noteVM.getAllNotes()
    val noteList: List<NoteEntity> =
        noteVM.notesList.collectAsState().value.sortedByDescending { it.id }

    val scope = rememberCoroutineScope()

    FirebaseFirestore.setLoggingEnabled(true)

    for (note in noteList) {
        Firebase.firestore
            .collection(Firebase.auth.uid.toString())
            .document(note.id.toString())
            .set(note)
    }



    Column(
        Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {

            SnackbarHost(hostState = snackState) {
                Snackbar(
                    snackbarData = it,
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.error
                )
            }

            /**
             * Top Home Page bar
             */
            TopHomeBarScreen(navController, drawerState, scope)

            VerticalStaggered(navController, noteList)
        }

        LaunchedEffect(true) {
            delay(5000)
            isShowLoader = false
        }
    }






