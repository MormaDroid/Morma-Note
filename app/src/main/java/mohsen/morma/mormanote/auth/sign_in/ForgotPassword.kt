package mohsen.morma.mormanote.auth.sign_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.auth.AuthTitle
import mohsen.morma.mormanote.auth.Loader
import mohsen.morma.mormanote.auth.errorSnackBar
import mohsen.morma.mormanote.auth.isValidEmail
import mohsen.morma.mormanote.auth.snackState
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.note.SheetSpacer
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.dosis
import mohsen.morma.mormanote.ui.theme.ysabeauBold


@Composable
fun ForgotPassword(navController: NavHostController) {

    var resetEmail by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var resetEmailError by remember {
        mutableStateOf(false)
    }

    var resetLoading by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    var isSuccessReset by remember {
        mutableStateOf(false)
    }

    Column {
        SheetSpacer()

        SnackbarHost(hostState = snackState) {
            Snackbar(
                snackbarData = it,
                shape = RoundedCornerShape(16.dp),
                containerColor = if(isSuccessReset) DarkBlue else MaterialTheme.colorScheme.error
            )
        }


    }

    if (resetLoading)
        Column(Modifier.fillMaxSize(),Arrangement.Center,Alignment.CenterHorizontally) {
            Loader()
        }
    else
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 78.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AuthTitle(
                title = "Reset Your Password",
                subTitle = "To reset the password type your email here."
            )

            SheetSpacer()

            OutlinedTextField(
                value = resetEmail,
                onValueChange = {
                    resetEmail = it
                    resetEmailError =
                        !(resetEmail.text.isNotEmpty() && isValidEmail(email = resetEmail.text))
                },
                label = {
                    Text(
                        text = "Reset Password ",
                        fontSize = 20.sp,
                        fontFamily = Font(dosis).toFontFamily()
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.reset),
                        contentDescription = "",
                        modifier = Modifier.size(28.dp)
                    )
                },
                isError = resetEmailError,
                textStyle = TextStyle(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = DarkBlue,
                    focusedLabelColor = DarkBlue,
                    unfocusedLabelColor = DarkBlue,
                    backgroundColor = Color.Transparent,
                    cursorColor = DarkBlue,
                    leadingIconColor = DarkBlue,
                    focusedBorderColor = DarkBlue,
                    unfocusedBorderColor = Color.LightGray,


                    ),
                modifier = Modifier.fillMaxWidth()
            )

            SheetSpacer()

            Row {
                Button(
                    onClick = {
                        if (resetEmail.text.isNotEmpty() && isValidEmail(email = resetEmail.text)){
                            resetLoading = true
                            Firebase.auth.sendPasswordResetEmail(resetEmail.text)
                                .addOnSuccessListener {
                                    resetLoading = false
                                    isSuccessReset = true
                                    errorSnackBar(scope, "Reset link sent.")
                                }
                                .addOnFailureListener {
                                    resetLoading = false
                                    if (it.message.equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred."))
                                        errorSnackBar(scope, "Please check your network connection.")
                                    else if (it.message?.contains("That’s all we know.") == true)
                                        errorSnackBar(scope, "Sorry, This app isn't Available in your region.")
                                    else if (it.message.equals("There is no user record corresponding to this identifier. The user may have been deleted."))
                                        errorSnackBar(scope, "The email address doesn't exist.")
                                    else
                                        errorSnackBar(scope, "Please check tour network.")
                                }
                        } else resetEmailError = true
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue)
                ) {
                    Text(
                        text = "Reset Password",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = Font(dosis).toFontFamily()
                    )
                }

                SheetSpacer()

                OutlinedButton(
                    onClick = { navController.navigate(Screen.SignInScreen.route) },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .fillMaxHeight(0.1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Return to login",
                        color = DarkBlue,
                        fontSize = 20.sp,
                        fontFamily = Font(dosis).toFontFamily()
                    )
                }

            }


        }

}