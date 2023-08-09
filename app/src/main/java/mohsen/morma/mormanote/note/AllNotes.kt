package mohsen.morma.mormanote.note

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.ysabeauBold
import mohsen.morma.mormanote.ui.theme.ysabeauMedium
import mohsen.morma.mormanote.util.RippleIcon

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AllNotes(
    navController: NavHostController,
    noteVM: NoteVM = hiltViewModel()
) {

    Scaffold(floatingActionButton = { Fab(navController = navController)}) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {

                RippleIcon(R.drawable.back, size = 32.dp) {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(0)
                    }
                }

                Text(buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = DarkBlue.copy(alpha = 0.7f),
                            fontSize = 17.sp,
                            fontFamily = Font(ysabeauMedium).toFontFamily()
                        )
                    ) {
                        append(" My ")
                    }

                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkBlue,
                            fontSize = 20.sp,
                            fontFamily = Font(ysabeauBold).toFontFamily()
                        )
                    ) {
                        append("Notes")
                    }
                })

                RippleIcon(R.drawable.search, size = 32.dp)

            }

            Spacer(modifier = Modifier.size(12.dp))

            VerticalStaggered()
        }
    }




}

@Composable
private fun Fab(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 48.dp, end = 32.dp), contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(Screen.NoteScreen.route) },
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 12.dp,
                pressedElevation = 20.dp
            ),
            containerColor = DarkBlue,
            modifier = Modifier.border(2.dp, Color.White, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = Color.White
            )
        }
    }
}