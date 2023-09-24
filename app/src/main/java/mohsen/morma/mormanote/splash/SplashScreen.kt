package mohsen.morma.mormanote.splash

import android.util.Log
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.auth.Loader
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.bottombar.setup.startPage
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.isShowSplash
import mohsen.morma.mormanote.profileEmail
import mohsen.morma.mormanote.profileImage
import mohsen.morma.mormanote.profileName
import mohsen.morma.mormanote.setting.appThemeSelected
import mohsen.morma.mormanote.setting.checkedState

var isGoToAnotherPage by mutableStateOf(false)

@Composable
fun SplashScreen(datastoreVM: DatastoreVM = hiltViewModel()) {

    datastoreVM.getFingerprint()
    datastoreVM.getAppColor()
    datastoreVM.getEmail()
    datastoreVM.getImgUri()
    datastoreVM.getFullName()

    appThemeSelected = Color(datastoreVM.appColor.collectAsState().value)
    checkedState = datastoreVM.isUseFingerprint.collectAsState().value

    profileImage =
        if (datastoreVM.imgUri.collectAsState().value != null) datastoreVM.imgUri.collectAsState().value.toString()
            .toInt() else R.drawable.avatar4
    Log.e("3636", "MainActivity: $profileImage")

    profileName =
        if (datastoreVM.fullName.collectAsState().value != null) datastoreVM.fullName.collectAsState().value.toString() else ""
    profileEmail =
        if (datastoreVM.email.collectAsState().value != null) datastoreVM.email.collectAsState().value.toString() else ""

    var isShowLoader by remember {
        mutableStateOf(false)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(appThemeSelected),
        Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )

        LaunchedEffect(key1 = isShowLoader) {
            delay(500)
            isShowLoader = true
        }

        AnimatedVisibility(
            visible = isShowLoader,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 48.dp), Alignment.BottomCenter
            ) {
                Loader(96, loadingFile = R.raw.white_loading)
            }
        }
    }

    datastoreVM.getFirstTime()
    startPage = if (datastoreVM.firstTime.collectAsState().value) Screen.SignUpScreen.route else Screen.HomeScreen.route



    if (checkedState) Fingerprint() else isGoToAnotherPage = true

    LaunchedEffect(key1 = isGoToAnotherPage ){
        if (isGoToAnotherPage){
            delay(2500)
            isShowLoader = false
            isShowSplash = false
        }
    }
}

@Composable
fun Fingerprint() {
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val executor = ContextCompat.getMainExecutor(activity)

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Morma Note Fingerprint")
        .setSubtitle("Use fingerprint to use the app.")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    val biometricPrompt =BiometricPrompt(activity,executor,
        object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                isGoToAnotherPage = true
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.e("3636","ErrorFingerprint${errString}")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        }
        )

    LaunchedEffect(Unit){
        delay(500)
        biometricPrompt.authenticate(promptInfo)
    }

}