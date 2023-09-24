package mohsen.morma.mormanote.home.top_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.note.SheetSpacer
import mohsen.morma.mormanote.profileImage
import mohsen.morma.mormanote.profileName
import mohsen.morma.mormanote.setting.appThemeSelected
import mohsen.morma.mormanote.ui.theme.dosis
import mohsen.morma.mormanote.util.Date

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageProfile(navController: NavHostController) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {

            GlideImage(
                model = profileImage,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Inside
            )

            SheetSpacer(14.dp)

            Text(buildAnnotatedString {
                withStyle(
                    ParagraphStyle(lineHeight = 28.sp),
                ) {
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Normal,
                            color = appThemeSelected.copy(alpha = 0.7f),
                            fontSize = 18.sp,
                            fontFamily = Font(dosis).toFontFamily()
                        )
                    ) {
                        append("Hi, ${checkHourForResponseToIt()}\n")
                    }

                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.ExtraBold,
                            color = appThemeSelected,
                            fontSize = 22.sp,
                            fontFamily = Font(dosis).toFontFamily()
                        )
                    ) {
                        append(profileName)
                    }
                }
            })

        }

        IconButton(onClick = { navController.navigate(Screen.SearchScreen.route) }) {
            Icon(painter = painterResource(id = R.drawable.search), contentDescription =null,Modifier.size(28.dp), tint = appThemeSelected )
        }

    }

    SheetSpacer()

}



fun checkHourForResponseToIt(): String = when (Date.calculateHour()) {
    in 6..10 -> "Good Morning"
    in 11..14 -> "Good Afternoon"
    in 15..20 -> "Good Evening"
    in 21..23 -> " GoodNight"
    else -> "Good Night"
}