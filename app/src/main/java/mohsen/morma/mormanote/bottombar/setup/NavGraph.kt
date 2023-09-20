package mohsen.morma.mormanote.bottombar.setup

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.razaghimahdi.library.core.CardDrawerState
import mohsen.morma.mormanote.auth.sign_in.ForgotPassword
import mohsen.morma.mormanote.auth.sign_in.SigninPage
import mohsen.morma.mormanote.auth.sign_up.SignupPage
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.home.HomePage
import mohsen.morma.mormanote.home.search.SearchPage
import mohsen.morma.mormanote.note.NotePage
import mohsen.morma.mormanote.setting.SettingPage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    drawerState: CardDrawerState,
    datastoreVM: DatastoreVM = hiltViewModel()
) {
    datastoreVM.getFirstTime()
    NavHost(navController = navController, startDestination = if (datastoreVM.firstTime.collectAsState().value) Screen.SignUpScreen.route else Screen.HomeScreen.route ) {

        composable(Screen.HomeScreen.route) { HomePage(navController, drawerState) }

        composable(Screen.SettingScreen.route) { SettingPage() }

        composable(
            Screen.NoteScreen.route + "?noteId={noteId}?gallery={gallery}?link={link}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("gallery") {
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
                    startWithGallery = args.getString("gallery").toString(),
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