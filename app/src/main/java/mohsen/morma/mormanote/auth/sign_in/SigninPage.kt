package mohsen.morma.mormanote.auth.sign_in

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
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
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.gesturesEnabled
import mohsen.morma.mormanote.model.NoteEntity
import mohsen.morma.mormanote.note.SheetSpacer
import mohsen.morma.mormanote.profileEmail
import mohsen.morma.mormanote.profileImage
import mohsen.morma.mormanote.profileName
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.dosis
import mohsen.morma.mormanote.ui.theme.ysabeauMedium


var signinErrorEmail by mutableStateOf(false)
var signinErrorPassword by mutableStateOf(false)

var signinLoading by mutableStateOf(false)


@Composable
fun SigninPage(navController: NavHostController) {



    gesturesEnabled = false

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


        Box(modifier = Modifier.fillMaxSize()) {
            AuthTitle(title = "Welcome back", subTitle = "Signin into your account")

            SigninScreen(navController)


            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp), Arrangement.Center, Alignment.CenterVertically
            ) {
                OptionalText(
                    title = R.string.dont_have_account,
                    clickableText = R.string.register
                ) { navController.navigate(Screen.SignUpScreen.route) { popUpTo(0) } }

            }

            if (signinLoading)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) { Loader() }
        }

    }
}

@Composable
private fun SigninScreen(
    navController: NavHostController,
    noteVM: NoteVM = hiltViewModel(),
    datastoreVM: DatastoreVM = hiltViewModel()
) {

    var isShowPassword by remember { mutableStateOf(false) }
    var emailTextField by remember { mutableStateOf(TextFieldValue("")) }
    var passwordTextField by remember { mutableStateOf(TextFieldValue("")) }
    val focus = LocalFocusManager.current

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
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
                TitleInBox(text = "Sign In")

                SheetSpacer(48.dp)

                OutlinedTextField(
                    value = emailTextField,
                    onValueChange = {
                        emailTextField = it
                        signinErrorEmail =
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
                    isError = signinErrorEmail,
                    textStyle = TextStyle(fontFamily = Font(dosis).toFontFamily()),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email,
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

                ErrorText(text = stringResource(R.string.valid_email), boolean = signinErrorEmail)

                SheetSpacer()

                OutlinedTextField(
                    value = passwordTextField,
                    onValueChange = {
                        passwordTextField = it
                        signinErrorPassword =
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
                    isError = signinErrorPassword,
                    keyboardActions = KeyboardActions(onDone = {
                        loginByFirebase(
                            emailTextField.text,
                            passwordTextField.text,
                            scope,
                            navController,
                            noteVM,
                            datastoreVM
                        )
                        focus.clearFocus(true)
                    }),
                    textStyle = TextStyle(fontFamily = Font(dosis).toFontFamily()),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
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

                ErrorText(
                    text = stringResource(id = R.string.valid_pass),
                    boolean = signinErrorPassword
                )

                SheetSpacer(46.dp)

                ButtonInBox("Sign In") {
                    loginByFirebase(
                        emailTextField.text,
                        passwordTextField.text,
                        scope,
                        navController,
                        noteVM,
                        datastoreVM
                    )
                }

                SheetSpacer(16.dp)

                Text(
                    text = stringResource(id = R.string.forgot_pass),
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = Font(dosis).toFontFamily(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { navController.navigate(Screen.ForgotPasswordScreen.route) }
                )
            }
        }
    }
}


fun loginByFirebase(
    emailTextField: String,
    passwordTextField: String,
    scope: CoroutineScope,
    navController: NavHostController,
    noteVM: NoteVM,
    datastoreVM: DatastoreVM
) {

    if (emailTextField.isNotEmpty() && isValidEmail(emailTextField)) {
        signinErrorEmail = false
        if (passwordTextField.isNotEmpty() && passwordTextField.length >= 6) {
            signinLoading = true
            signinErrorPassword = false
            Firebase.auth.signInWithEmailAndPassword(emailTextField, passwordTextField)
                .addOnSuccessListener {
                    val user = Firebase.auth.currentUser

                    Firebase.firestore
                        .collection(Firebase.auth.uid.toString())
                        .get()
                        .addOnSuccessListener {

                            for (note in it.toObjects<NoteEntity>()) {
                                Log.e("3636", "Get Data: $note")
                                noteVM.insertNote(note)
                            }

                            user?.uid?.let { uid -> datastoreVM.putUserId(uid) }
                            user?.email?.let { email ->
                                profileEmail = email
                                datastoreVM.putEmail(email)
                            }
                            user?.displayName?.let { name ->
                                profileName = name
                                datastoreVM.putFullName(name)
                            }
                            user?.photoUrl?.let { img ->
                                datastoreVM.putImgUri(img.toString())
                                profileImage = img.toString().toInt()
                            }
                            datastoreVM.putFirstTime(false)

                            navController.navigate(Screen.HomeScreen.route){popUpTo(0)}
                        }
                        .addOnFailureListener {
                            signinLoading = false
                            errorSnackBar(scope, it.message.toString())
                            Log.e("3636","Error in Get: ${it.message}")
                        }
                }
                .addOnFailureListener {
                    signinLoading = false
                    Log.e("3636", "Signup : ${it.message}")

                    if (it.message.equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred."))
                        errorSnackBar(scope, "Please check your network connection.")
                    else if (it.message?.contains("That’s all we know.") == true)
                        errorSnackBar(scope, "Sorry, This app isn't Available in your region.")
                    else if (it.message?.contains("The password is invalid or the user does not have a password.") == true)
                        errorSnackBar(scope, "username or password is incorrect.")
                    else if (it.message.equals("There is no user record corresponding to this identifier. The user may have been deleted."))
                        errorSnackBar(scope, "The email address doesn't exist.")
                    else
                        errorSnackBar(scope, "Please check tour network.")
                }
        } else {
            signinErrorPassword = true
        }
    } else {
        signinErrorEmail = true
    }
}


