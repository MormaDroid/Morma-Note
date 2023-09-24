package mohsen.morma.mormanote.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.auth.Loader
import mohsen.morma.mormanote.auth.errorSnackBar
import mohsen.morma.mormanote.auth.sign_up.profileImageList
import mohsen.morma.mormanote.auth.snackState
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.note.SheetSpacer
import mohsen.morma.mormanote.profileEmail
import mohsen.morma.mormanote.profileImage
import mohsen.morma.mormanote.profileName
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.DarkRed
import mohsen.morma.mormanote.ui.theme.Gray
import mohsen.morma.mormanote.ui.theme.dosis
import mohsen.morma.mormanote.util.RippleIcon
import java.text.DateFormat

val colorList = listOf(
    DarkBlue,
    DarkRed,
    Gray
)
var appThemeSelected by mutableStateOf(colorList[0])

var showProfileImageDialog by mutableStateOf(false)

var checkedState by mutableStateOf(false)


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingPage(datastoreVM: DatastoreVM = hiltViewModel()) {

    datastoreVM.getFingerprint()
    checkedState = datastoreVM.isUseFingerprint.collectAsState().value

    val scope = rememberCoroutineScope()

    var fullNameTextField by remember { mutableStateOf(TextFieldValue(profileName)) }
    var fullNameEnabled by remember { mutableStateOf(false) }
    var haveError by remember { mutableStateOf(false) }
    var fullNameIsLoading by remember { mutableStateOf(false) }

    var profileImageLoading by remember {
        mutableStateOf(false)
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appThemeSelected)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.34f), Alignment.TopCenter
        ) {


            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(128.dp), Alignment.BottomEnd) {
                    if (profileImageLoading) {
                        Loader(
                            128,
                            if (appThemeSelected == Gray) R.raw.circle_loader else R.raw.white_loading
                        )
                    } else {
                        Image(
                            painter = painterResource(id = profileImage),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }


                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White), Alignment.Center
                    ) {
                        RippleIcon(icon = R.drawable.profile_edit, appThemeSelected, size = 28.dp) {
                            showProfileImageDialog = true
                        }
                    }
                }

                SheetSpacer()

                AccountInfo(profileName, 30)

                SheetSpacer(8.dp)

                AccountInfo(profileEmail, 15)

            }

            SnackbarHost(hostState = snackState) {
                Snackbar(
                    snackbarData = it,
                    shape = RoundedCornerShape(16.dp),
                    containerColor = if (haveError) MaterialTheme.colorScheme.error else Color(
                        0xFF058254
                    ),
                    modifier = Modifier.padding(top = 36.dp)
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 36.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Color.White), Alignment.TopCenter
            ) {

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 64.dp),
                    Arrangement.Top,
                    Alignment.Start
                ) {

                    SectionTitle(text = "Full Name")

                    Row(
                        Modifier.fillMaxWidth(),
                        Arrangement.SpaceBetween,
                        Alignment.CenterVertically
                    ) {
                        TextField(
                            value = fullNameTextField,
                            onValueChange = { fullNameTextField = it },
                            enabled = fullNameEnabled,
                            textStyle = TextStyle(
                                color = appThemeSelected,
                                fontFamily = Font(dosis).toFontFamily(),
                                fontSize = 18.sp
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color.White
                            )
                        )


                        if (fullNameIsLoading) {
                            Loader(48)
                        } else {
                            RippleIcon(
                                icon = if (fullNameEnabled) R.drawable.check else R.drawable.name_edit,
                                appThemeSelected,
                                28.dp
                            ) {

                                if (fullNameEnabled) {
                                    fullNameIsLoading = true
                                    Firebase.auth.currentUser?.updateProfile(
                                        userProfileChangeRequest {
                                            displayName = fullNameTextField.text
                                        }
                                    )
                                        ?.addOnSuccessListener {

                                            val user = Firebase.auth.currentUser?.displayName

                                            profileName = user.toString()
                                            datastoreVM.putFullName(user.toString())

                                            haveError = false
                                            errorSnackBar(scope, "Full name changed.")
                                            fullNameEnabled = !fullNameEnabled
                                            fullNameIsLoading = false

                                        }
                                        ?.addOnFailureListener {
                                            haveError = true
                                            fullNameEnabled = !fullNameEnabled
                                            fullNameIsLoading = false
                                            if (it.message.equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred."))
                                                errorSnackBar(
                                                    scope,
                                                    "Please check your network connection."
                                                )
                                            else if (it.message?.contains("403") == true)
                                                errorSnackBar(
                                                    scope,
                                                    "Sorry, This app isn't Available in your region."
                                                )
                                            else
                                                errorSnackBar(scope, "Please check tour network.")
                                        }
                                } else {
                                    fullNameEnabled = !fullNameEnabled
                                }

                            }
                        }


                    }

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(appThemeSelected)
                    )

                    SheetSpacer()

                    SectionTitle(text = "App Theme")

                    Row(
                        Modifier.fillMaxWidth(),
                        Arrangement.SpaceEvenly,
                        Alignment.CenterVertically
                    ) { EditAppTheme() }

                    SheetSpacer(12.dp)

                    SectionTitle(text = "Fingerprint")

                    Row(
                        Modifier.fillMaxWidth(),
                        Arrangement.SpaceBetween,
                        Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Do you want to use fingerprint?",
                            color = appThemeSelected,
                            fontFamily = Font(dosis).toFontFamily(),
                            fontSize = 22.sp,
                        )

                        Switch(
                            checked = checkedState,
                            onCheckedChange = {
                                checkedState = it
                                datastoreVM.putFingerprint(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = if (appThemeSelected == Gray) Color.Black else appThemeSelected,
                                uncheckedThumbColor = Color.LightGray
                            )
                        )


                    }

                }

            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.13f)
                    .padding(horizontal = 28.dp),
                shape = RoundedCornerShape(24.dp),
                backgroundColor = Color.White,
                elevation = 12.dp
            ) {
                NotesInfo()
            }
        }


    }

    if (showProfileImageDialog)
        Dialog(onDismissRequest = { showProfileImageDialog = false }) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.3f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                FlowRow(
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.Center
                ) {

                    for (profile in profileImageList) {
                        Image(
                            painter = painterResource(id = profile),
                            contentDescription = null,
                            modifier = Modifier
                                .size(96.dp)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    profileImageLoading = true

                                    Firebase.auth.currentUser
                                        ?.updateProfile(
                                            userProfileChangeRequest {
                                                photoUri = profile
                                                    .toString()
                                                    .toUri()
                                            }
                                        )
                                        ?.addOnSuccessListener {
                                            profileImage = profile
                                            datastoreVM.putImgUri(profile.toString())
                                            haveError = false
                                            errorSnackBar(scope, "Image profile is changed.")
                                            profileImageLoading = false
                                        }
                                        ?.addOnFailureListener {
                                            profileImageLoading = false
                                            haveError = true
                                            if (it.message.equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred."))
                                                errorSnackBar(
                                                    scope,
                                                    "Please check your network connection."
                                                )
                                            else if (it.message?.contains("403") == true)
                                                errorSnackBar(
                                                    scope,
                                                    "Sorry, This app isn't Available in your region."
                                                )
                                            else
                                                errorSnackBar(scope, "Please check tour network.")
                                        }


                                    showProfileImageDialog = false
                                })
                    }

                }
            }

        }

}


@Composable
fun NotesInfo(noteVM: NoteVM = hiltViewModel()) {

    noteVM.getAllNotes()
    val notes = noteVM.notesList.collectAsState().value

    var allCharCount = 0

    for (note in notes) {
        allCharCount += note.charCount
    }

    Row(Modifier.fillMaxSize(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {

        NotesInfoText(notes.size.toString(), "Notes")
        Spacer(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .width(2.dp)
                .background(appThemeSelected)
        )
        NotesInfoText(allCharCount.toString(), "letters")

        Spacer(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .width(2.dp)
                .background(appThemeSelected)
        )


        val signupData = Firebase.auth.currentUser?.metadata?.creationTimestamp?.let {
            DateFormat.getDateInstance().format(it)
        }

        NotesInfoText("$signupData")

    }

}

@Composable
fun NotesInfoText(title: String, content: String = "") {

    Row(Modifier.fillMaxHeight(), Arrangement.Center, Alignment.CenterVertically) {

        Text(
            text = title,
            color = appThemeSelected,
            fontFamily = Font(dosis).toFontFamily(),
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )

        SheetSpacer(8.dp)

        Text(
            text = content,
            color = appThemeSelected,
            fontFamily = Font(dosis).toFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

    }

}


@Composable
fun EditAppTheme(datastoreVM: DatastoreVM = hiltViewModel()) {
    for (color in colorList) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(16.dp))
                .selectable(
                    selected = (appThemeSelected == color),
                    onClick = {
                        appThemeSelected = color
                        datastoreVM.putThemeColor(color.toArgb())
                    }
                ),
            Alignment.TopEnd
        ) {

            Image(
                painter = painterResource(id = R.drawable.water_color),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(color)
            )

            if (appThemeSelected == color)
                Image(
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = null,
                    Modifier
                        .size(48.dp)
                        .padding(top = 20.dp, end = 20.dp),
                    colorFilter = ColorFilter.tint(appThemeSelected)
                )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {

    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        color = appThemeSelected.copy(0.7f),
        fontFamily = Font(dosis).toFontFamily(),
        fontSize = 16.sp,
        textAlign = TextAlign.Start
    )
}

@Composable
fun AccountInfo(text: String, size: Int) {
    Text(
        text = text,
        color = if (appThemeSelected == Gray) Color.Black else Color.White,
        fontFamily = Font(dosis).toFontFamily(),
        fontSize = size.sp
    )
}



