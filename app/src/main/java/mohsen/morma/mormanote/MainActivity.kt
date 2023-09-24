package mohsen.morma.mormanote

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.razaghimahdi.library.CardDrawer
import com.razaghimahdi.library.core.CardDrawerState
import com.razaghimahdi.library.core.CardDrawerValue
import com.razaghimahdi.library.core.rememberCardDrawerState
import dagger.hilt.android.AndroidEntryPoint
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.bottombar.setup.SetupNavGraph
import mohsen.morma.mormanote.bottombar.ui.BottomBar
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.model.NoteEntity
import mohsen.morma.mormanote.setting.appThemeSelected
import mohsen.morma.mormanote.splash.SplashScreen
import mohsen.morma.mormanote.ui.drawer.DrawerContent
import mohsen.morma.mormanote.ui.theme.MormaNoteTheme

var gesturesEnabled by mutableStateOf(true)

var profileImage by mutableIntStateOf(R.drawable.avatar4)
var profileName by mutableStateOf("")
var profileEmail by mutableStateOf("")

var isShowBottomBar by mutableStateOf(false)
var isShowSplash by mutableStateOf(true)


@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val drawerState = rememberCardDrawerState(initialValue = CardDrawerValue.Closed)
            val navController = rememberNavController()
            val backStackEntry = navController.currentBackStackEntryAsState()
            val noteVM by viewModels<NoteVM>()

            Firebase.firestore
                .collection(Firebase.auth.uid.toString())
                .get()
                .addOnSuccessListener {
                    for (note in it.toObjects<NoteEntity>()) {
                        Log.e("3636", "Get Data: $note")
                        noteVM.insertNote(note)
                    }
                }

                // Control TopBar and BottomBar
                ControlTopAndBottomBar(backStackEntry)

                MormaNoteTheme(isOpen = drawerState.isOpen) {
                    if (isShowSplash)
                        SplashScreen()
                    else
                        MyUI(drawerState, navController)
                }

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun MyUI(
        drawerState: CardDrawerState,
        navController: NavHostController,
    ) {
        CardDrawer(
            gesturesEnabled = gesturesEnabled,
            contentCornerSize = 16.dp,
            drawerContent = { DrawerContent(drawerState,navController) },
            drawerBackgroundColor = appThemeSelected,
            drawerState = drawerState,
        ) {
            MainContent(navController, drawerState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    private fun MainContent(
        navController: NavHostController,
        drawerState: CardDrawerState
    ) {
        Scaffold(bottomBar = {
            AnimatedVisibility(
                visible = isShowBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BottomBar(navController)
            }
        }) { SetupNavGraph(navController = navController, drawerState) }
    }

    @Composable
    private fun ControlTopAndBottomBar(
        backStackEntry: State<NavBackStackEntry?>
    ) {
        when (backStackEntry.value?.destination?.route) {
            Screen.HomeScreen.route -> isShowBottomBar = true
            Screen.SettingScreen.route -> isShowBottomBar = true
            Screen.Splash.route -> isShowBottomBar = false
            Screen.NoteScreen.route + "?noteId={noteId}?bgImg={bgImg}?link={link}" -> isShowBottomBar = false
            Screen.SearchScreen.route -> isShowBottomBar = false
            Screen.SignInScreen.route -> isShowBottomBar = false
            Screen.SignUpScreen.route -> isShowBottomBar = false

        }
    }
}


