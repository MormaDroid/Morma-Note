package mohsen.morma.mormanote.ui.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.razaghimahdi.library.core.CardDrawerState
import mohsen.morma.mormanote.note.SheetSpacer
import mohsen.morma.mormanote.profileEmail
import mohsen.morma.mormanote.profileName
import mohsen.morma.mormanote.ui.theme.ysabeauBold

@Composable
fun DrawerContent(drawerState: CardDrawerState) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        Arrangement.Top
    ) {

        ProfileContent()

    }

}



@Composable
fun ProfileContent() {

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.28f)
        ,
        Arrangement.Center, Alignment.CenterHorizontally
    ) {

        if (Firebase.auth.currentUser?.photoUrl != null)
            Image(
                painter = painterResource(id = Firebase.auth.currentUser?.photoUrl.toString().toInt()),
                contentDescription = null,
                modifier = Modifier
                    .size(84.dp),
            )

        SheetSpacer(10.dp)

        ProfileText(profileEmail, 14, Color.LightGray)

        SheetSpacer(5.dp)

        ProfileText(profileName, 30, Color.White)

    }

}

@Composable
fun ProfileText(text: String, size: Int, color: Color) {

    Text(
        text = text,
        fontSize = size.sp,
        color = color,
        fontFamily = Font(ysabeauBold).toFontFamily()
    )

}
