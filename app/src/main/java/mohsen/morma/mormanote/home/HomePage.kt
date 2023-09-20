package mohsen.morma.mormanote.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razaghimahdi.library.core.CardDrawerState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mohsen.morma.mormanote.auth.errorSnackBar
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.gesturesEnabled
import mohsen.morma.mormanote.home.top_bar.TopHomeBarScreen
import mohsen.morma.mormanote.home.vertical_staggered.VerticalStaggered
import mohsen.morma.mormanote.model.NoteEntity


@OptIn(DelicateCoroutinesApi::class)
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


    gesturesEnabled = true


    /**Get Notes From Offline Repo*/
    noteVM.getAllNotes()
    val noteList: List<NoteEntity> =
        noteVM.notesList.collectAsState().value.sortedByDescending { it.id }

    val scope = rememberCoroutineScope()

    GlobalScope.launch (Dispatchers.IO){
        for (note in noteList){
            Firebase.firestore
                .collection(Firebase.auth.uid.toString())
                .document(note.id.toString())
                .set(note)
                .addOnSuccessListener {
                    Log.e("3636", "Adding note to firebase: $note" )
                }
                .addOnFailureListener {
                    errorSnackBar(scope,it.message.toString())
                }
        }
    }

    val context = LocalContext.current
    Toast.makeText(context,noteList.size.toString(),Toast.LENGTH_LONG).show()


    Column(
        Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {

        /**
         * Top Home Page bar
         */
        TopHomeBarScreen(navController, drawerState, scope)

        VerticalStaggered(navController, noteList)

    }
}






