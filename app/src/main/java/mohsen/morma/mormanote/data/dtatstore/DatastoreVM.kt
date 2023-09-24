package mohsen.morma.mormanote.data.dtatstore

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import mohsen.morma.mormanote.ui.theme.DarkBlue
import javax.inject.Inject

@HiltViewModel
class DatastoreVM @Inject constructor(private val datastore: DatastoreRepository) : ViewModel() {

    companion object {
        const val AUTH_UID_KEY = "AUTH_UID_KEY"
        const val AUTH_NAME_KEY = "AUTH_NAME_KEY"
        const val AUTH_EMAIL_KEY = "AUTH_EMAIL_KEY"
        const val AUTH_IMG_URI_KEY = "AUTH_IMG_URI_KEY"
        const val FIRST_TIME = "FIRST_TIME"
        const val APP_THEME = "APP_THEME"
        const val APP_FONT = "APP_FONT"
        const val USE_FINGERPRINT = "USE_FINGERPRINT"
    }

    fun putThemeColor(color: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            datastore.putInt(APP_THEME, color)
        }
    }

    fun putFont(font: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            datastore.putInt(APP_FONT, font)
        }
    }

    fun putUserId(uid: String) {
        viewModelScope.launch(Dispatchers.IO) { datastore.putString(AUTH_UID_KEY, uid) }
    }

    fun putFullName(fullName: String) {
        viewModelScope.launch(Dispatchers.IO) { datastore.putString(AUTH_NAME_KEY, fullName) }
    }

    fun putEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) { datastore.putString(AUTH_EMAIL_KEY, email) }
    }

    fun putImgUri(imgUri: String) {
        viewModelScope.launch(Dispatchers.IO) { datastore.putString(AUTH_IMG_URI_KEY, imgUri) }
    }

    var imgUri = MutableStateFlow<String?>(null)
    fun getImgUri() {
        viewModelScope.launch(Dispatchers.IO) {
            imgUri.value = (datastore.getString(AUTH_IMG_URI_KEY))
        }
    }

    val uid = MutableStateFlow<String?>(null)
    fun getUserId() {
        viewModelScope.launch(Dispatchers.IO) { uid.emit(datastore.getString(AUTH_UID_KEY)) }
    }

    val fullName = MutableStateFlow<String?>(null)
    fun getFullName() {
        viewModelScope.launch(Dispatchers.IO) { fullName.emit(datastore.getString(AUTH_NAME_KEY)) }
    }

    val email = MutableStateFlow<String?>(null)
    fun getEmail() {
        viewModelScope.launch(Dispatchers.IO) { email.emit(datastore.getString(AUTH_EMAIL_KEY)) }
    }

    fun putFirstTime(firstTime: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            datastore.putBool(FIRST_TIME, firstTime)
        }
    }

    val firstTime = MutableStateFlow(true)
    fun getFirstTime() = viewModelScope.launch(Dispatchers.IO) {
        datastore.getBool(FIRST_TIME)?.let { firstTime.emit(it) }
    }

    val appColor = MutableStateFlow(DarkBlue.toArgb())
    fun getAppColor() {
        viewModelScope.launch(Dispatchers.IO) {
            datastore.getInt(APP_THEME)?.let {
                appColor.value = it
            }
        }
    }

    fun putFingerprint(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { datastore.putBool(USE_FINGERPRINT, value) }
    }

    var isUseFingerprint = MutableStateFlow(false)

    fun getFingerprint(){
        viewModelScope.launch (Dispatchers.IO){
            datastore.getBool(USE_FINGERPRINT)?.let { isUseFingerprint.emit(it) }
        }
    }

}
