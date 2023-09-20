package mohsen.morma.mormanote

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.razaghimahdi.library.CardDrawer
import com.razaghimahdi.library.core.CardDrawerState
import com.razaghimahdi.library.core.CardDrawerValue
import com.razaghimahdi.library.core.rememberCardDrawerState
import dagger.hilt.android.AndroidEntryPoint
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.bottombar.setup.SetupNavGraph
import mohsen.morma.mormanote.bottombar.ui.BottomBar
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.setting.appFontSelected
import mohsen.morma.mormanote.setting.appThemeSelected
import mohsen.morma.mormanote.ui.drawer.DrawerContent
import mohsen.morma.mormanote.ui.theme.MormaNoteTheme

var gesturesEnabled by mutableStateOf(true)

var profileImage by mutableIntStateOf(R.drawable.avatar4)
var profileName by mutableStateOf("")
var profileEmail by mutableStateOf("")


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Log.e("3636", "SingleProf: $profileImage" )

            val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
            val drawerState = rememberCardDrawerState(initialValue = CardDrawerValue.Closed)
            val navController = rememberNavController()
            val backStackEntry = navController.currentBackStackEntryAsState()
            val datastoreVM by viewModels<DatastoreVM>()

            datastoreVM.getAppFont()
            datastoreVM.getAppColor()
            datastoreVM.getEmail()
            datastoreVM.getImgUri()
            datastoreVM.getFullName()

            appFontSelected = datastoreVM.appFont.collectAsState().value
            appThemeSelected = Color(datastoreVM.appColor.collectAsState().value)

            profileImage = if(datastoreVM.imgUri.collectAsState().value != null) datastoreVM.imgUri.collectAsState().value.toString().toInt() else R.drawable.avatar4
            Log.e("3636", "MainActivity: $profileImage" )

            profileName = if(datastoreVM.fullName.collectAsState().value != null) datastoreVM.fullName.collectAsState().value.toString() else ""
            profileEmail = if(datastoreVM.email.collectAsState().value != null) datastoreVM.email.collectAsState().value.toString() else ""

            // Control TopBar and BottomBar
            ControlTopAndBottomBar(backStackEntry, bottomBarState)

            MormaNoteTheme(isOpen = drawerState.isOpen) {
                datastoreVM.getFirstTime()
                bottomBarState.value = !datastoreVM.firstTime.collectAsState().value
                MyUI(drawerState, bottomBarState, navController)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun MyUI(
        drawerState: CardDrawerState,
        bottomBarState: MutableState<Boolean>,
        navController: NavHostController,
    ) {
        CardDrawer(
            gesturesEnabled = gesturesEnabled,
            contentCornerSize = 16.dp,
            drawerContent = { DrawerContent(drawerState) },
            drawerBackgroundColor = appThemeSelected,
            drawerState = drawerState,
        ) {
            MainContent(bottomBarState, navController, drawerState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    private fun MainContent(
        bottomBarState: MutableState<Boolean>,
        navController: NavHostController,
        drawerState: CardDrawerState
    ) {
        Scaffold(bottomBar = {
            AnimatedVisibility(
                visible = bottomBarState.value,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BottomBar(navController)
            }
        }) { SetupNavGraph(navController = navController, drawerState) }
    }

    @Composable
    private fun ControlTopAndBottomBar(
        backStackEntry: State<NavBackStackEntry?>,
        bottomBarState: MutableState<Boolean>
    ) { when (backStackEntry.value?.destination?.route) {
            Screen.HomeScreen.route -> bottomBarState.value = true
            Screen.SettingScreen.route -> bottomBarState.value = true
            Screen.NoteScreen.route + "?noteId={noteId}?gallery={gallery}?link={link}" -> bottomBarState.value = false
            Screen.SearchScreen.route -> bottomBarState.value = false
            Screen.SignInScreen.route -> bottomBarState.value = false
            Screen.SignUpScreen.route -> bottomBarState.value = false
        } }
}


