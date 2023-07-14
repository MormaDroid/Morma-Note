package mohsen.morma.mormanote.bottombar.setup

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mohsen.morma.mormanote.home.HomePage
import mohsen.morma.mormanote.setting.SettingPage

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route){
        composable(Screen.HomeScreen.route){
            HomePage(navController)
        }

        composable(Screen.SettingScreen.route){
            SettingPage(navController)
        }
    }
}