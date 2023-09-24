package mohsen.morma.mormanote.bottombar.setup

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.razaghimahdi.library.core.CardDrawerState
import mohsen.morma.mormanote.auth.sign_in.ForgotPassword
import mohsen.morma.mormanote.auth.sign_in.SigninPage
import mohsen.morma.mormanote.auth.sign_up.SignupPage
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.home.HomePage
import mohsen.morma.mormanote.home.search.SearchPage
import mohsen.morma.mormanote.model.NoteEntity
import mohsen.morma.mormanote.note.NotePage
import mohsen.morma.mormanote.setting.SettingPage
import mohsen.morma.mormanote.splash.SplashScreen

var startPage by mutableStateOf(Screen.SignUpScreen.route)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    drawerState: CardDrawerState,
    datastoreVM: DatastoreVM = hiltViewModel(),
    noteVM: NoteVM = hiltViewModel()
) {

    Firebase.firestore
        .collection(Firebase.auth.uid.toString())
        .get()
        .addOnSuccessListener {
            for (note in it.toObjects<NoteEntity>()) {
                Log.e("3636", "Get Data: $note")
                noteVM.insertNote(note)
            }
        }

    datastoreVM.getFirstTime()
    NavHost(navController = navController, startDestination = startPage ) {

        composable(Screen.Splash.route){ SplashScreen() }

        composable(Screen.HomeScreen.route) { HomePage(navController, drawerState) }

        composable(Screen.SettingScreen.route) { SettingPage() }

        composable(
            Screen.NoteScreen.route + "?noteId={noteId}?bgImg={bgImg}?link={link}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("bgImg") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("link") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            it.arguments?.let { args ->
                NotePage(
                    navController,
                    noteId = args.getInt("noteId"),
                    startWithBgImg = args.getString("bgImg").toString(),
                    link = args.getString("link").toString()
                )
            }
        }

        composable(Screen.SearchScreen.route) { SearchPage(navController = navController) }

        composable(Screen.SignUpScreen.route){ SignupPage(navController) }
        composable(Screen.SignInScreen.route){ SigninPage(navController) }
        composable(Screen.ForgotPasswordScreen.route){ ForgotPassword(navController = navController)}

    }
}