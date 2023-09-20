package mohsen.morma.mormanote.home.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.home.vertical_staggered.VerticalStaggered
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.ysabeauMedium
import mohsen.morma.mormanote.util.RippleIcon

@Composable
fun SearchPage(
    navController: NavHostController,
    noteVM: NoteVM = hiltViewModel()
) {

    val searchTextField by remember {
        mutableStateOf("")
    }

    noteVM.searchByTitle(searchTextField)
    val searchList = noteVM.searchList.collectAsState().value.sortedByDescending { it.id }

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 48.dp), Arrangement.Top, Alignment.CenterHorizontally
    ) {

        SearchTextField(searchTextField, navController)

        Spacer(modifier = Modifier.size(12.dp))

        VerticalStaggered(navController = navController, allNotes = searchList)

    }

}

@Composable
private fun SearchTextField(
    searchTextField: String,
    navController: NavHostController
) {
    var searchTextField1 = searchTextField
    OutlinedTextField(
        value = searchTextField1,
        onValueChange = { searchTextField1 = it },
        label = {
            Text(
                text = "Search notes",
                fontFamily = Font(ysabeauMedium).toFontFamily()
            )
        },
        leadingIcon = {
            RippleIcon(
                icon = R.drawable.back,
                size = 30.dp
            ) { navController.navigate(Screen.HomeScreen.route) { popUpTo(0) } }
        },
        modifier = Modifier.fillMaxWidth(0.94f),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = DarkBlue,
            cursorColor = DarkBlue,
            focusedBorderColor = DarkBlue,
            focusedLabelColor = DarkBlue
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        shape = RoundedCornerShape(16.dp)
    )
}