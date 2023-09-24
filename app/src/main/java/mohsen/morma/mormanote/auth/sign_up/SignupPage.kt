package mohsen.morma.mormanote.auth.sign_up

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.auth.AuthTitle
import mohsen.morma.mormanote.auth.ButtonInBox
import mohsen.morma.mormanote.auth.ErrorText
import mohsen.morma.mormanote.auth.Loader
import mohsen.morma.mormanote.auth.OptionalText
import mohsen.morma.mormanote.auth.TitleInBox
import mohsen.morma.mormanote.auth.errorSnackBar
import mohsen.morma.mormanote.auth.isValidEmail
import mohsen.morma.mormanote.auth.snackState
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.gesturesEnabled
import mohsen.morma.mormanote.note.SheetSpacer
import mohsen.morma.mormanote.profileEmail
import mohsen.morma.mormanote.profileImage
import mohsen.morma.mormanote.profileName
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.dosis
import mohsen.morma.mormanote.ui.theme.ysabeauMedium


var signupErrorFullName by mutableStateOf(false)
var signupErrorEmail by mutableStateOf(false)
var signupErrorPassword by mutableStateOf(false)
var signupLoading by mutableStateOf(false)
var showDialog by mutableStateOf(false)

val profileImageList = listOf(
    R.drawable.avatar1,
    R.drawable.avatar2,
    R.drawable.avatar3,
    R.drawable.avatar4,
    R.drawable.avatar5,
    R.drawable.avatar6,
)

var imageSelected by mutableIntStateOf(R.drawable.avatar4)


@Composable
fun SignupPage(navController: NavHostController) {
    gesturesEnabled = false


    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            SheetSpacer()

            SnackbarHost(hostState = snackState) {
                Snackbar(
                    snackbarData = it,
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.error
                )
            }

        }

        Box(
            Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 100.dp)
        ) {

            AuthTitle(title = "Welcome", subTitle = "Signup into your account")

            SignupScreen(navController = navController)

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp)
                    .align(Alignment.BottomCenter),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {

                OptionalText(
                    title = R.string.already_have_account,
                    clickableText = R.string.login_now
                ) {
                    navController.navigate(Screen.SignInScreen.route) { popUpTo(0) }
                }


            }
        }


        if (signupLoading)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) { Loader() }


    }
}

@Composable
private fun SignupScreen(
    datastoreVM: DatastoreVM = hiltViewModel(),
    navController: NavHostController
) {

    var isShowPassword by remember { mutableStateOf(false) }
    var emailTextField by remember { mutableStateOf(TextFieldValue("")) }
    var passwordTextField by remember { mutableStateOf(TextFieldValue("")) }
    var fullNameTextField by remember { mutableStateOf(TextFieldValue("")) }

    val scope = rememberCoroutineScope()

    val focus = LocalFocusManager.current


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 88.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .padding(top = 70.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 64.dp,
                            topEnd = 64.dp,
                            bottomEnd = 64.dp,
                            bottomStart = 12.dp
                        )
                    )
                    .background(DarkBlue)
            ) {

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp, top = 48.dp)
                ) {
                    TitleInBox(text = "Sign Up")

                    SheetSpacer(48.dp)

                    OutlinedTextField(
                        value = fullNameTextField,
                        onValueChange = {
                            fullNameTextField = it
                            signupErrorFullName =
                                !(fullNameTextField.text.isNotEmpty() && fullNameTextField.text.length >= 3)
                        },
                        label = {
                            Text(
                                text = "Full Name",
                                color = Color.LightGray,
                                fontSize = 20.sp,
                                fontFamily = Font(ysabeauMedium).toFontFamily()
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.person),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        isError = signupErrorFullName,
                        textStyle = TextStyle(fontFamily = Font(dosis).toFontFamily()),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            Color.White,
                            backgroundColor = Color.Transparent,
                            cursorColor = Color.White,
                            leadingIconColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    ErrorText("Full name must be at least 3 characters", signupErrorFullName)

                    SheetSpacer()

                    OutlinedTextField(
                        value = emailTextField,
                        onValueChange = {
                            emailTextField = it
                            signupErrorEmail =
                                !(emailTextField.text.isNotEmpty() && isValidEmail(emailTextField.text))
                        },
                        label = {
                            Text(
                                text = "E-mail",
                                color = Color.LightGray,
                                fontSize = 20.sp,
                                fontFamily = Font(ysabeauMedium).toFontFamily()
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.email),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        isError = signupErrorEmail,
                        textStyle = TextStyle(fontFamily = Font(dosis).toFontFamily()),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            Color.White,
                            backgroundColor = Color.Transparent,
                            cursorColor = Color.White,
                            leadingIconColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.LightGray,
                            trailingIconColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )


                    ErrorText(stringResource(R.string.valid_email), signupErrorEmail)

                    SheetSpacer()

                    OutlinedTextField(
                        value = passwordTextField,
                        onValueChange = {
                            passwordTextField = it
                            signupErrorPassword =
                                !(passwordTextField.text.isNotEmpty() && passwordTextField.text.length >= 6)
                        },
                        label = {
                            Text(
                                text = "Password",
                                color = Color.LightGray,
                                fontSize = 20.sp,
                                fontFamily = Font(ysabeauMedium).toFontFamily()
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.pass),
                                contentDescription = "",
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = if (isShowPassword) R.drawable.blind else R.drawable.eye),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .clickable { isShowPassword = !isShowPassword }
                            )
                        },
                        isError = signupErrorPassword,
                        textStyle = TextStyle(fontFamily = Font(dosis).toFontFamily()),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            registerByFirebase(
                                fullNameTextField,
                                emailTextField,
                                passwordTextField,
                                scope,
                                datastoreVM,
                                navController
                            )
                            focus.clearFocus(true)
                        }),
                        visualTransformation = if (isShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            Color.White,
                            backgroundColor = Color.Transparent,
                            cursorColor = Color.White,
                            leadingIconColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.LightGray,
                            trailingIconColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    ErrorText(stringResource(R.string.valid_pass), signupErrorPassword)


                    SheetSpacer(36.dp)

                    ButtonInBox("Sign Up") {
                        registerByFirebase(
                            fullNameTextField,
                            emailTextField,
                            passwordTextField,
                            scope,
                            datastoreVM,
                            navController
                        )
                    }

                }
            }


            Box(modifier = Modifier.size(110.dp), contentAlignment = Alignment.BottomEnd) {

                Image(
                    painterResource(id = imageSelected),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Inside
                )

                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(46.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_edit),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = DarkBlue
                    )
                }
            }
        }

    }
    ProfileSelectedDialog()
}

@Composable
fun ProfileSelectedDialog() {
    if (showDialog)
        Dialog(onDismissRequest = { showDialog = false }) {
            ProfileDialogContent()
        }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileDialogContent() {
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
                            imageSelected = profile
                            showDialog = false
                        })
            }

        }
    }
}


private fun registerByFirebase(
    fullNameTextField: TextFieldValue,
    emailTextField: TextFieldValue,
    passwordTextField: TextFieldValue,
    scope: CoroutineScope,
    datastoreVM: DatastoreVM,
    navController: NavHostController
) {

    if (fullNameTextField.text.isNotEmpty() && fullNameTextField.text.length >= 3) {
        signupErrorFullName = false
        if (emailTextField.text.isNotEmpty() && isValidEmail(emailTextField.text)) {
            signupErrorEmail = false
            if (passwordTextField.text.isNotEmpty() && passwordTextField.text.length >= 6) {
                signupLoading = true
                signupErrorPassword = false
                Firebase.auth.createUserWithEmailAndPassword(
                    emailTextField.text,
                    passwordTextField.text
                )
                    .addOnSuccessListener {
                    val user = Firebase.auth.currentUser


                    val profileUpdates = userProfileChangeRequest {
                        displayName = fullNameTextField.text
                        photoUri = imageSelected.toString().toUri()
                    }

                    user!!.updateProfile(profileUpdates).addOnSuccessListener {
                        user.uid.let { uid -> datastoreVM.putUserId(uid) }
                        user.email?.let { email ->
                            profileEmail = email
                            datastoreVM.putEmail(email)
                        }
                        user.displayName?.let { name ->
                            profileName = name
                            datastoreVM.putFullName(name)
                        }
                        user.photoUrl?.path?.let { img ->
                            profileImage = img.toInt()
                            datastoreVM.putImgUri(img)
                        }
                        datastoreVM.putFirstTime(false)
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(0)
                        }
                    }.addOnFailureListener { Log.e("3636",it.message.toString()) }

                }
                    .addOnFailureListener {
                            signupLoading = false
                            Log.e("3636", "Signup : ${it.message}")

                            if (it.message.equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred."))
                                errorSnackBar(scope, "Please check your network connection.")
                            else if (it.message?.contains("403") == true)
                                errorSnackBar(scope, "Sorry, This app isn't Available in your region.")
                            else if (it.message.equals("The email address is already in use by another account."))
                                errorSnackBar(scope, "The email address is already in use by another account.")
                            else
                                errorSnackBar(scope, "Please check your network.")
                        }
            } else {
                signupErrorPassword = true
            }
        } else {
            signupErrorEmail = true
        }
    } else {
        signupErrorFullName = true
    }
}



