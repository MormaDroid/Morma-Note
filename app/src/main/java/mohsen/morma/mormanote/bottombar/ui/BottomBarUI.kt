package mohsen.morma.mormanote.bottombar.ui

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.model.BottomBarModel
import mohsen.morma.mormanote.setting.appThemeSelected
import mohsen.morma.mormanote.util.RippleCustomTheme
import mohsen.morma.mormanote.util.times
import mohsen.morma.mormanote.util.transform
import kotlin.math.PI
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.S)
private fun getRenderEffect(): RenderEffect {
    val blurEffect = RenderEffect
        .createBlurEffect(80f, 80f, Shader.TileMode.MIRROR)

    val alphaMatrix = RenderEffect.createColorFilterEffect(
        ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, 50f, -5000f
                )
            )
        )
    )

    return RenderEffect
        .createChainEffect(alphaMatrix, blurEffect)
}

@Composable
fun BottomBar(navController: NavHostController) {

    val isMenuExtended = remember { mutableStateOf(false) }

    val fabAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = LinearEasing
        )
    )

    val clickAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 700,
            easing = LinearEasing
        )
    )

    val renderEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getRenderEffect().asComposeRenderEffect()
    } else {
        null
    }

    BottomBarUI(
        renderEffect = renderEffect,
        fabAnimationProgress = fabAnimationProgress,
        clickAnimationProgress = clickAnimationProgress,
        navController = navController
    ) {
        isMenuExtended.value = isMenuExtended.value.not()
    }
}


@Composable
fun BottomBarUI(
    renderEffect: androidx.compose.ui.graphics.RenderEffect?,
    fabAnimationProgress: Float = 0f,
    clickAnimationProgress: Float = 0f,
    navController: NavHostController,
    toggleAnimation: () -> Unit = { }
) {

    Box(
        Modifier
            .fillMaxSize()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            CustomBottomNavigation(navController)
            Circle(
                color = Color.Transparent,
                animationProgress = 0.5f
            )

            FabGroup(
                renderEffect = renderEffect,
                animationProgress = fabAnimationProgress,
                navController = navController
            )
            FabGroup(
                renderEffect = null,
                animationProgress = fabAnimationProgress,
                toggleAnimation = toggleAnimation,
                navController = navController
            )
            Circle(
                color = Color.Transparent,
                animationProgress = clickAnimationProgress
            )
        }
    }


}

@Composable
fun Circle(color: Color, animationProgress: Float) {
    val animationValue = sin(PI * animationProgress).toFloat()

    Box(
        modifier = Modifier
            .padding(44.dp)
            .size(56.dp)
            .scale(2 - animationValue)
            .border(
                width = 2.dp,
                color = color.copy(alpha = color.alpha * animationValue),
                shape = CircleShape
            )
    )
}

@Composable
fun CustomBottomNavigation(
    navController: NavHostController
) {

    val items = listOf(
        BottomBarModel(
            Screen.HomeScreen.route,
            "Home",
            R.drawable.home_selected,
            R.drawable.home_unselected
        ),
        BottomBarModel(
            Screen.SettingScreen.route,
            "Setting",
            R.drawable.setting_selected,
            R.drawable.setting_unselected
        )
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    val showBottomBar = backStackEntry.value?.destination?.route in items.map { it.route }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(80.dp)
            .paint(
                painter = painterResource(R.drawable.bottom_navigation),
                contentScale = ContentScale.FillHeight,
                colorFilter = ColorFilter.tint(appThemeSelected)
            )
    ) {
        if (showBottomBar) {
            items.forEach { item ->

                val selected = item.route == backStackEntry.value?.destination?.route

                BottomNavigationItem(
                    selected = selected,
                    onClick = { navController.navigate(item.route) },
                    icon = {
                        CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
                            IconButton(onClick = {
                                navController.navigate(item.route) {
                                    // popUpTo = 0 // DEPRECATED
                                    popUpTo(0)
                                }
                            }) {
                                Icon(
                                    painter = if (selected) painterResource(id = item.selectedIcon) else painterResource(
                                        id = item.unSelectedIcon
                                    ),
                                    contentDescription = null,
                                    tint = Color.White,  //Todo: Remember Theming
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                    }
                )
            }
        }
    }

}


@Composable
fun FabGroup(
    animationProgress: Float = 0f,
    renderEffect: androidx.compose.ui.graphics.RenderEffect? = null,
    navController: NavHostController,
    toggleAnimation: () -> Unit = { }
) {
    Box(
        Modifier
            .fillMaxSize()
            .graphicsLayer { this.renderEffect = renderEffect }
            .padding(bottom = 44.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        AnimatedFab(
            icon = R.drawable.background_note,
            modifier = Modifier
                .padding(
                    PaddingValues(
                        bottom = 72.dp,
                        end = 210.dp
                    ) * FastOutSlowInEasing.transform(0f, 0.8f, animationProgress)
                ),
            opacity = LinearEasing.transform(0.2f, 0.7f, animationProgress),
            circleSize = 38.dp
        ){ navController.navigate(Screen.NoteScreen.route + "?noteId=${-1}?bgImg=?link=") }

        AnimatedFab(
            icon = R.drawable.note,
            modifier = Modifier.padding(
                PaddingValues(
                    bottom = 88.dp,
                ) * FastOutSlowInEasing.transform(0.1f, 0.9f, animationProgress)
            ),
            opacity = LinearEasing.transform(0.3f, 0.8f, animationProgress),
            circleSize = 40.dp
        ) {
            navController.navigate(Screen.NoteScreen.route)
        }

        AnimatedFab(
            icon = R.drawable.web,
            modifier = Modifier.padding(
                PaddingValues(
                    bottom = 72.dp,
                    start = 210.dp
                ) * FastOutSlowInEasing.transform(0.2f, 1.0f, animationProgress)
            ),
            opacity = LinearEasing.transform(0.4f, 0.9f, animationProgress),
            circleSize = 38.dp
        ){
            navController.navigate(Screen.NoteScreen.route + "?noteId=${-1}?bgImg=?link=url")
        }

        AnimatedFab(
            modifier = Modifier.scale(1f - LinearEasing.transform(0.5f, 0.85f, animationProgress)),
            circleSize = 48.dp
        )

        AnimatedFab(
            icon = R.drawable.button,
            modifier = Modifier
                .rotate(
                    225 * FastOutSlowInEasing
                        .transform(0.35f, 0.65f, animationProgress)
                ),
            onClick = toggleAnimation,
            backgroundColor = appThemeSelected,
            circleSize = 48.dp
        )
    }
}

@Composable
fun AnimatedFab(
    modifier: Modifier,
    icon: Int? = null,
    opacity: Float = 1f,
    backgroundColor: Color = appThemeSelected,
    circleSize: Dp,
    onClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        FloatingActionButton(
            onClick = onClick,
            backgroundColor = backgroundColor,
            modifier = modifier.scale(1.25f)
        ) {
            icon?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = opacity),
                    modifier = Modifier.size(circleSize)
                )
            }
        }
    }
}

