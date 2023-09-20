package mohsen.morma.mormanote.home.top_bar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.razaghimahdi.library.core.CardDrawerState
import kotlinx.coroutines.CoroutineScope

@Composable
fun TopHomeBarScreen(
    navController: NavHostController,
    drawerState: CardDrawerState,
    scope : CoroutineScope
) {
    AppTitleMenuFavorite(scope,drawerState)
    ImageProfile(navController)
}