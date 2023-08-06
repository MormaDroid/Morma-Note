package mohsen.morma.mormanote.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mohsen.morma.mormanote.R

val ysabeauBold = FontFamily(Font(R.font.ysabeau_bold_italic))
val ysabeauMedium = FontFamily(Font(R.font.ysabeau_medium_italic))

val nexa = FontFamily(Font(R.font.nexa))
val dragonWing = FontFamily(Font(R.font.dragon_wing))
val jmh = FontFamily(Font(R.font.jmh))
val potra = FontFamily(Font(R.font.potra))
val unicorns = FontFamily(Font(R.font.unicorns))
val zero = FontFamily(Font(R.font.zero))
val bKamran = FontFamily(Font(R.font.b_kamran))
val iranNast = FontFamily(Font(R.font.iran_nastaliq))

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)