package mohsen.morma.mormanote.bottombar.setup

sealed class Screen(val route:String){
    object HomeScreen: Screen(route = "HomePage")
    object SettingScreen: Screen(route = "SettingPage")
}
