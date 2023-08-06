package mohsen.morma.mormanote.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.razaghimahdi.library.core.CardDrawerState
import kotlinx.coroutines.launch
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.ysabeauBold
import mohsen.morma.mormanote.ui.theme.ysabeauMedium
import mohsen.morma.mormanote.util.Date
import mohsen.morma.mormanote.util.RippleIcon

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(
    navController: NavHostController,
    drawerState: CardDrawerState
) {

    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {

            Row(Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically) {

                RippleIcon(R.drawable.menu) {
                    scope.launch { if (drawerState.isOpen) drawerState.close() else drawerState.open() }
                }

                Text(buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = DarkBlue.copy(alpha = 0.7f),
                            fontSize = 17.sp,
                            fontFamily = ysabeauMedium
                        )
                    ) {
                        append(" Morma ")
                    }

                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkBlue,
                            fontSize = 20.sp,
                            fontFamily = ysabeauBold
                        )
                    ) {
                        append("Note")
                    }
                })

            }

            RippleIcon(R.drawable.fill_favorite, size = 24.dp)

        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 32.dp,
                            bottomStart = 32.dp,
                            bottomEnd = 32.dp,
                            topEnd = 12.dp
                        )
                    )
                    .background(DarkBlue),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_placeholder),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Inside
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Text(buildAnnotatedString {
                withStyle(
                    ParagraphStyle(lineHeight = 28.sp),
                ) {
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Normal,
                            color = DarkBlue.copy(alpha = 0.7f),
                            fontSize = 18.sp,
                            fontFamily = ysabeauMedium
                        )
                    ) {
                        append("Hi, ${checkHourForResponseToIt()}\n")
                    }

                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkBlue,
                            fontSize = 22.sp,
                            fontFamily = ysabeauBold
                        )
                    ) {
                        append("Mohsen")
                    }
                }
            })

        }

        Spacer(modifier = Modifier.size(height = 16.dp, width = 24.dp))

        CarouselRecentNotes(navController = navController)


    }
}


fun checkHourForResponseToIt(): String = when (Date.calculateHour()) {
    in 6..10 -> "Good Morning"
    in 11..14 -> "Good Afternoon"
    in 15..20 -> "Good Evening"
    in 21..23 -> " GoodNight"
    else -> "Good Night"
}


