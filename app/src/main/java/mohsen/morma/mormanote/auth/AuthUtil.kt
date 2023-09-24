package mohsen.morma.mormanote.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.note.SheetSpacer
import mohsen.morma.mormanote.ui.theme.BlueLink
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.dosis
import java.util.regex.Pattern

val snackState = SnackbarHostState()

@Composable
fun AuthTitle(title: String, subTitle: String) {
    Column {
        Text(
            text = title,
            color = DarkBlue,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Font(dosis).toFontFamily()
        )

        Text(
            text = subTitle,
            color = BlueLink,
            fontSize = 20.sp,
            fontFamily = Font(dosis).toFontFamily()
        )
    }
}

@Composable
fun TitleInBox(text: String) {

    Text(
        text = text,
        color = Color.White,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Font(dosis).toFontFamily()
    )

}

@Composable
fun ButtonInBox(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(1f),
        colors = ButtonDefaults.buttonColors(Color.White),
        shape = RoundedCornerShape(36.dp)
    ) {
        Text(
            text = text,
            color = DarkBlue,
            fontSize = 26.sp,
            fontFamily = Font(dosis).toFontFamily()
        )
    }
}

fun isValidEmail(email: String): Boolean {

    val emailPatterns = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

    return Pattern.compile(emailPatterns).matcher(email).matches()

}

@Composable
fun ErrorText(text: String, boolean: Boolean) {
    if (boolean)
        Text(
            text = text,
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp
        )
}

fun errorSnackBar(scope: CoroutineScope, text: String) {

    scope.launch {
        snackState.showSnackbar(
            text,
            duration = SnackbarDuration.Short
        )
    }
}

@Composable
fun OptionalText(title: Int, clickableText: Int, onClick: () -> Unit) {
    Text(
        text = stringResource(id = title),
        color = DarkBlue,
        fontSize = 20.sp,
        fontFamily = Font(dosis).toFontFamily(),
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
    )

    SheetSpacer(16.dp)

    Text(
        text = stringResource(id = clickableText),
        color = BlueLink,
        fontSize = 20.sp,
        fontFamily = Font(dosis).toFontFamily(),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    )
}

@Composable
fun Loader(size : Int = 128,loadingFile:Int = R.raw.darkblue_loading) {

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(loadingFile))

    LottieAnimation(
        composition = composition,
        modifier = Modifier
            .size(size.dp),
        iterations = LottieConstants.IterateForever,
        clipSpec = LottieClipSpec.Progress(0.0f, 1f)
    )

}

