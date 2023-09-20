package mohsen.morma.mormanote.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.data.dtatstore.DatastoreVM
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.DarkRed
import mohsen.morma.mormanote.ui.theme.Gray
import mohsen.morma.mormanote.ui.theme.dosis
import mohsen.morma.mormanote.ui.theme.potra
import mohsen.morma.mormanote.ui.theme.ysabeauBold
import mohsen.morma.mormanote.util.RippleIcon

val colorList = listOf(
    AppThemeModel("Blue", DarkBlue),
    AppThemeModel("Red", DarkRed),
    AppThemeModel("purple", Gray)
)
var appThemeSelected by mutableStateOf(colorList[0].color)

val fontList = listOf(
    AppFontModel("Ysabeau", ysabeauBold),
    AppFontModel("Dosis", dosis),
    AppFontModel("Potra", potra)
)
var appFontSelected by mutableIntStateOf(fontList[0].font)


@Composable
fun SettingPage() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appThemeSelected)
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White), Alignment.Center
        ) {
            RippleIcon(icon = R.drawable.back, appThemeSelected)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.White)
            , Alignment.BottomCenter
        ){

        }

    }

}


@Composable
fun EditAppTheme(datastoreVM: DatastoreVM = hiltViewModel()) {
    for (color in colorList) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(color.color)
                .selectable(
                    selected = (appThemeSelected == color.color),
                    onClick = {
                        appThemeSelected = color.color
                        datastoreVM.putThemeColor(color.color.toArgb())
                    }
                ),
            Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .padding(4.dp)
                    .border(
                        1.dp,
                        if (appThemeSelected == color.color) Color.White else color.color,
                        CircleShape
                    )
            )

            Text(
                text = color.text,
                color = Color.White,
                fontFamily = Font(appFontSelected).toFontFamily(),
                fontSize = 22.sp,
            )

        }
    }
}

@Composable
private fun SectionTitle(text: String) {

    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        color = appThemeSelected,
        fontFamily = Font(appFontSelected).toFontFamily(),
        fontSize = 22.sp,
        textAlign = TextAlign.Start
    )
}

@Composable
fun EditAccountInfo(text: String, size: Int, isName: Boolean) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            color = appThemeSelected,
            fontFamily = Font(appFontSelected).toFontFamily(),
            fontSize = size.sp
        )

        if (isName) RippleIcon(icon = R.drawable.name_edit, appThemeSelected, 28.dp) {}

    }


}

@Composable
fun EditAppFont(datastoreVM: DatastoreVM = hiltViewModel()) {

    for (font in fontList) {

        Text(
            text = font.text,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .selectable(selected = (appFontSelected == font.font), onClick = {
                    appFontSelected = font.font
                    datastoreVM.putFont(appFontSelected)
                }),
            color = appThemeSelected,
            fontFamily = Font(font.font).toFontFamily(),
            fontSize = 22.sp,
            textDecoration = if (appFontSelected == font.font) TextDecoration.Underline else TextDecoration.None,

            )

    }

}


data class AppFontModel(
    val text: String,
    val font: Int
)

data class AppThemeModel(
    val text: String,
    val color: Color
)