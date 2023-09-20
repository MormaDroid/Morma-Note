package mohsen.morma.mormanote.home.top_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.razaghimahdi.library.core.CardDrawerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.setting.appFontSelected
import mohsen.morma.mormanote.setting.appThemeSelected
import mohsen.morma.mormanote.util.RippleIcon

@Composable
fun AppTitleMenuFavorite(
    scope: CoroutineScope,
    drawerState: CardDrawerState
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {

        Row(Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically) {

            RippleIcon(R.drawable.menu,tint= appThemeSelected) {
                scope.launch { if (drawerState.isOpen) drawerState.close() else drawerState.open() }
            }

            Text(buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = appThemeSelected.copy(alpha = 0.7f),
                        fontSize = 17.sp,
                        fontFamily = Font(appFontSelected).toFontFamily()
                    )
                ) {
                    append(stringResource(R.string.morma))
                }

                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.ExtraBold,
                        color = appThemeSelected,
                        fontSize = 20.sp,
                        fontFamily = Font(appFontSelected).toFontFamily()
                    )
                ) {
                    append(stringResource(R.string.note))
                }
            })

        }

        RippleIcon(R.drawable.fill_favorite, size = 24.dp,tint = appThemeSelected)

    }
}