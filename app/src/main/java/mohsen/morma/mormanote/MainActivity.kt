package mohsen.morma.mormanote

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.razaghimahdi.library.CardDrawer
import com.razaghimahdi.library.core.CardDrawerValue
import com.razaghimahdi.library.core.rememberCardDrawerState
import dagger.hilt.android.AndroidEntryPoint
import mohsen.morma.mormanote.bottombar.setup.SetupNavGraph
import mohsen.morma.mormanote.bottombar.ui.BottomBar
import mohsen.morma.mormanote.ui.drawer.DrawerContent
import mohsen.morma.mormanote.ui.theme.MormaNoteTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val drawerState = rememberCardDrawerState(initialValue = CardDrawerValue.Closed)
            val navController = rememberNavController()

            MormaNoteTheme(isOpen = drawerState.isOpen) {
                CardDrawer(
                    gesturesEnabled = true,
                    contentCornerSize = 16.dp,
                    drawerContent = { DrawerContent(drawerState) },
                    drawerBackgroundColor = Color(0xFF1F2F98),  //Todo: Remember Theming
                    drawerState = drawerState,
                ) {
                    Scaffold(bottomBar = {BottomBar(navController)}) {
                        SetupNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}


