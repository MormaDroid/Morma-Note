package mohsen.morma.mormanote

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.razaghimahdi.library.CardDrawer
import com.razaghimahdi.library.core.CardDrawerValue
import com.razaghimahdi.library.core.rememberCardDrawerState
import dagger.hilt.android.AndroidEntryPoint
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.bottombar.setup.SetupNavGraph
import mohsen.morma.mormanote.bottombar.ui.BottomBar
import mohsen.morma.mormanote.ui.drawer.DrawerContent
import mohsen.morma.mormanote.ui.theme.MormaNoteTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
            val drawerState = rememberCardDrawerState(initialValue = CardDrawerValue.Closed)
            val navController = rememberNavController()

            val backStackEntry = navController.currentBackStackEntryAsState()

            // Control TopBar and BottomBar
            when (backStackEntry.value?.destination?.route) {
                Screen.HomeScreen.route -> bottomBarState.value = true
                Screen.SettingScreen.route -> bottomBarState.value = true
                Screen.NoteScreen.route -> bottomBarState.value = false
                Screen.AllNoteScreen.route -> bottomBarState.value = false
            }

            MormaNoteTheme(isOpen = drawerState.isOpen) {
                CardDrawer(
                    gesturesEnabled = true,
                    contentCornerSize = 16.dp,
                    drawerContent = { DrawerContent(drawerState) },
                    drawerBackgroundColor = Color(0xFF1F2F98),  //Todo: Remember Theming
                    drawerState = drawerState,
                ) {
                    Scaffold(bottomBar = {
                        AnimatedVisibility(
                            visible = bottomBarState.value,
                            enter = slideInVertically(initialOffsetY = {it}),
                            exit = slideOutVertically(targetOffsetY = {it})
                        ) {
                            BottomBar(navController)
                        }
                    }) {
                        SetupNavGraph(navController = navController, drawerState,this,cacheDir)
                    }
                }
            }
        }
    }
}


