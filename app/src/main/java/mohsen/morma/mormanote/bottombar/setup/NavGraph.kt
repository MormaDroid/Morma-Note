package mohsen.morma.mormanote.bottombar.setup

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.razaghimahdi.library.core.CardDrawerState
import mohsen.morma.mormanote.home.HomePage
import mohsen.morma.mormanote.note.AllNotes
import mohsen.morma.mormanote.note.NotePage
import mohsen.morma.mormanote.setting.SettingPage
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    drawerState: CardDrawerState,
    activity: Activity,
    cacheDir: File?
) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomePage(navController, drawerState)
        }

        composable(Screen.SettingScreen.route) {
            SettingPage(navController)
        }

        composable(Screen.NoteScreen.route) {
            NotePage(
                navController,
                activity = activity,
                cacheDir = cacheDir
            )
        }

        composable(Screen.AllNoteScreen.route) {
            AllNotes(navController)
        }

    }
}