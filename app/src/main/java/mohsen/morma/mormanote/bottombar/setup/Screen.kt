package mohsen.morma.mormanote.bottombar.setup

sealed class Screen(val route:String){
    object HomeScreen: Screen(route = "HomePage")
    object SettingScreen: Screen(route = "SettingPage")
    object NoteScreen : Screen(route = "NotePage")
    object SearchScreen : Screen(route = "SearchPage")
    object SignUpScreen  : Screen(route = "SignUpPage")
    object SignInScreen  : Screen(route = "SignInPage")
    object Splash  : Screen(route = "SplashPage")
    object ForgotPasswordScreen  : Screen(route = "ForgotPasswordPage")

}
