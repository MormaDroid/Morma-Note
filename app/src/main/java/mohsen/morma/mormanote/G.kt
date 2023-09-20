package mohsen.morma.mormanote

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.HiltAndroidApp
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.setting.appFontSelected
import mohsen.morma.mormanote.setting.appThemeSelected

@HiltAndroidApp
class G : Application() {
    init {
        @Composable fun Hello(datastoreVM:DatastoreVM = hiltViewModel()){


            datastoreVM.getAppFont()
            datastoreVM.getAppColor()

            appFontSelected = datastoreVM.appFont.collectAsState().value
            appThemeSelected = Color(datastoreVM.appColor.collectAsState().value)
        }
    }

}