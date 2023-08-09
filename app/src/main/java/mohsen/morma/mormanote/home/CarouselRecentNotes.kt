package mohsen.morma.mormanote.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.model.NoteEntity
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.ysabeauBold
import mohsen.morma.mormanote.ui.theme.ysabeauMedium
import mohsen.morma.mormanote.util.RippleIcon
import kotlin.math.absoluteValue


@Composable
fun CarouselRecentNotes(noteVM: NoteVM = hiltViewModel(),navController: NavHostController) {

    val heightScreen = LocalConfiguration.current.screenHeightDp - 300

    noteVM.getAllNotes()
    val recentList: List<NoteEntity> =
        noteVM.notesList.collectAsState().value.sortedByDescending { it.id }.take(5)

    val pagerState = rememberPagerState(initialPage = 0)

    Column(Modifier.fillMaxSize()) {

        if (recentList.isNotEmpty()){
            Title(navController)

            Spacer(modifier = Modifier.size(8.dp))

            Pager(recentList = recentList, pagerState = pagerState)

            Spacer(modifier = Modifier.size(32.dp))

            Indicator(recentList = recentList, pagerState = pagerState)
        }
        else{
            Column(Modifier.fillMaxWidth().height(heightScreen.dp).padding(horizontal = 16.dp), Arrangement.Center, Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.nothing_note),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = Font(ysabeauBold).toFontFamily(),
                    color = DarkBlue,
                    textAlign = TextAlign.Center
                )
            }
        }



    }


}

@Composable
fun Indicator(recentList: List<NoteEntity>, pagerState: PagerState) {
    Column(Modifier.fillMaxWidth(),Arrangement.Center , Alignment.CenterHorizontally) {
        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = recentList.size,
            activeColor = DarkBlue,
            inactiveColor = Color.LightGray,
            indicatorHeight = 12.dp,
            indicatorWidth = 16.dp,
            spacing = 8.dp
        )
    }
}

@Composable
fun Title(navController: NavHostController) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Text(
            text = "Recent notes",
            fontSize = 16.sp,
            fontFamily = Font(ysabeauMedium).toFontFamily(),
            color = DarkBlue
        )

        RippleIcon(icon = R.drawable.forward, size = 24.dp){
            navController.navigate(Screen.AllNoteScreen.route)
        }

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Pager(recentList: List<NoteEntity>, pagerState: PagerState) {
    HorizontalPager(
        count = recentList.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp),
        contentPadding = PaddingValues(horizontal = 40.dp)
    ) { note ->


        Card(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(width = 400.dp, height = 450.dp)
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(note).absoluteValue
                    lerp(
                        start = 0.875f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                },
            backgroundColor = DarkBlue,
            elevation = 8.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = recentList[note].title,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = Font(ysabeauBold).toFontFamily(),
                        fontSize = 30.sp,
                        maxLines = 2,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = recentList[note].content,
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.Medium,
                        fontFamily = Font(ysabeauMedium).toFontFamily(),
                        fontSize = 22.sp,
                        maxLines = 10,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
        }

    }
}