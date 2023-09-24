package mohsen.morma.mormanote

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.HiltAndroidApp
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.setting.appThemeSelected
import mohsen.morma.mormanote.setting.checkedState

@HiltAndroidApp
class G : Application() {
    init {
        @Composable fun Hello(datastoreVM:DatastoreVM = hiltViewModel()){

            datastoreVM.getFingerprint()
            checkedState = datastoreVM.isUseFingerprint.collectAsState().value

            datastoreVM.getAppColor()
            appThemeSelected = Color(datastoreVM.appColor.collectAsState().value)
        }
    }

}