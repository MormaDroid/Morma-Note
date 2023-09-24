package mohsen.morma.mormanote.ui.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.razaghimahdi.library.core.CardDrawerState
import kotlinx.coroutines.launch
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.note.SheetSpacer
import mohsen.morma.mormanote.profileEmail
import mohsen.morma.mormanote.profileImage
import mohsen.morma.mormanote.profileName
import mohsen.morma.mormanote.ui.theme.dosis
import kotlin.system.exitProcess

@Composable
fun DrawerContent(drawerState: CardDrawerState,navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        Arrangement.Top
    ) { ProfileContent(drawerState,navController) }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileContent(drawerState:CardDrawerState,navController: NavHostController) {



    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 24.dp, end = 24.dp),
        Arrangement.Top, Alignment.CenterHorizontally
    ) {


        GlideImage(
            model = profileImage,
            contentDescription = null,
            modifier = Modifier
                .size(84.dp),
        )

        SheetSpacer(10.dp)

        ProfileText(profileEmail, 14, Color.LightGray)

        SheetSpacer(5.dp)

        ProfileText(profileName, 30, White)

        DrawerItem(icon = R.drawable.home_unselected, drawerState = drawerState,title = "Home") {
            navController.navigate(Screen.HomeScreen.route){popUpTo(0)}
        }

        DrawerItem(icon = R.drawable.setting_unselected, title = "Setting", drawerState = drawerState) {
            navController.navigate(Screen.SettingScreen.route){popUpTo(0)}
        }

        DrawerItem(icon = R.drawable.search, title = "Search", drawerState = drawerState) {
            navController.navigate(Screen.SearchScreen.route){}
        }

        DrawerItem(icon = R.drawable.exit, title = "Exit", drawerState = drawerState) {
            exitProcess(0)
        }


    }

}

@Composable
fun ProfileText(text: String, size: Int, color: Color) {

    Text(
        text = text,
        fontSize = size.sp,
        color = color,
        fontFamily = Font(dosis).toFontFamily()
    )

}

@Composable
fun DrawerItem(icon: Int, title: String,drawerState: CardDrawerState, ocClick: () -> Unit) {

    val scope = rememberCoroutineScope()

    SheetSpacer(48.dp)

    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                scope.launch {
                    drawerState.close()
                }
                ocClick()
            }, Arrangement.Start, Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            Modifier.size(36.dp),
            colorFilter = ColorFilter.tint(White)
        )
        SheetSpacer(24.dp)
        Text(
            text = title,
            fontSize = 24.sp,
            color = White,
            fontFamily = Font(dosis).toFontFamily(),
            fontWeight = FontWeight.Bold
        )

    }

}
