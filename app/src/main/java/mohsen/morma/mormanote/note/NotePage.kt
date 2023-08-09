package mohsen.morma.mormanote.note

import android.Manifest.permission.RECORD_AUDIO
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.model.FontModel
import mohsen.morma.mormanote.model.NoteEntity
import mohsen.morma.mormanote.note.record.AndroidAudioRecorder
import mohsen.morma.mormanote.note.record_play.AndroidAudioPlayer
import mohsen.morma.mormanote.ui.theme.Dark
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.DarkRed
import mohsen.morma.mormanote.ui.theme.Green
import mohsen.morma.mormanote.ui.theme.LightYellow
import mohsen.morma.mormanote.ui.theme.Orange
import mohsen.morma.mormanote.ui.theme.Pink
import mohsen.morma.mormanote.ui.theme.SkyBlue
import mohsen.morma.mormanote.ui.theme.bKamran
import mohsen.morma.mormanote.ui.theme.dragonWing
import mohsen.morma.mormanote.ui.theme.iranNast
import mohsen.morma.mormanote.ui.theme.jmh
import mohsen.morma.mormanote.ui.theme.nexa
import mohsen.morma.mormanote.ui.theme.potra
import mohsen.morma.mormanote.ui.theme.unicorns
import mohsen.morma.mormanote.ui.theme.ysabeauBold
import mohsen.morma.mormanote.ui.theme.ysabeauMedium
import mohsen.morma.mormanote.ui.theme.zero
import mohsen.morma.mormanote.util.BlueRippleTheme
import mohsen.morma.mormanote.util.Date
import mohsen.morma.mormanote.util.RippleIcon
import mohsen.morma.mormanote.util.WhiteRippleTheme
import java.io.File

/**font*/
var fontSelected: MutableState<Int> = mutableIntStateOf(ysabeauMedium)
val fontList = listOf(
    FontModel("ysabeau", ysabeauMedium),
    FontModel("nexa", nexa),
    FontModel("dragonWing", dragonWing),
    FontModel("jmh", jmh),
    FontModel("potra", potra),
    FontModel("unicorns", unicorns),
    FontModel("zero", zero),
    FontModel("کامران", bKamran),
    FontModel("ایران نستعلیق", iranNast)
)
var fontCounter by mutableIntStateOf(0)

/**text Color*/
val textColorList = listOf(
    DarkBlue,
    DarkRed,
    Orange,
    LightYellow,
    Green,
    White,
    Dark
)
var textColorSelected by mutableStateOf(textColorList[0])

/**Background Image*/
val bgPicList = listOf(
    R.drawable.transparent,
    R.drawable.pic2,
    R.drawable.pic5,
    R.drawable.pic6,
    R.drawable.pic7,
    R.drawable.pic8,
    R.drawable.pic9,
    R.drawable.pic10,
)
var bgPicSelected by mutableStateOf<Int?>(null)
var bgIconTint = mutableStateOf(DarkBlue)

/**Background Color*/
const val bgAlpha = 0.6f
val bgColorList = listOf(
    White,
    Pink,
    Orange,
    SkyBlue,
    LightYellow,
    Green.copy(bgAlpha)
)
var bgColorSelected by mutableStateOf(bgColorList[0])

/**Text Align*/
val iconAlignList = listOf(
    R.drawable.left_alighnment,
    R.drawable.center_align,
    R.drawable.right_alignment,
    R.drawable.justify,
)
var iconAlignSelected by mutableIntStateOf(iconAlignList[0])
var textAlignSelected by mutableStateOf(TextAlign.Start)

/**Voice File*/
var audioFile: File? = null

/**Dialog*/
var isShowDialog by mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotePage(
    navController: NavHostController,
    noteVM: NoteVM = hiltViewModel(),
    activity: Activity,
    cacheDir: File?
) {

    /**
     * Record and Play Record File
     * */
    val recorder by lazy {
        AndroidAudioRecorder(activity.applicationContext)
    }
    val player by lazy {
        AndroidAudioPlayer(activity)
    }

    /**
     * Image From Gallery
     */
    var selectImages by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectImages = uri
        }

    /**
     * Calculate Date And Time
     */
    val dateAndTime = Date.calculateDateAndTime()

    /**
     * Text field(Title,Content,CharCount)
     */
    val titleTextFieldValue = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val contentTextFieldValue = remember {
        mutableStateOf(TextFieldValue(""))
    }
    var charactersCount by remember {
        mutableIntStateOf(contentTextFieldValue.value.text.length)
    }

    /**
     * Bottom Sheet Scaffold Variables
     */
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = SheetState(false))
    val scope = rememberCoroutineScope()
    val sheetHeight = (LocalConfiguration.current.screenHeightDp / 1.7).dp

    /**
     * interactionButtonSource just for Invisible Button Click Ripple
     */
    val interactionButtonSource = remember { mutableStateOf(MutableInteractionSource()) }

    /**
     * another interaction just for Invisible Text field ripple after click then hide bottom sheet scaffold
     */
    val focus = LocalFocusManager.current
    val interaction = remember { MutableInteractionSource() }
    val isPressed: Boolean by interaction.collectIsPressedAsState()
    LaunchedEffect(isPressed) {
        if (isPressed) {
            bottomSheetScaffoldState.bottomSheetState.hide()
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            SheetContent(
                galleryLauncher,
                bottomSheetScaffoldState,
                recorder,
                player,
                activity
            )
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = sheetHeight,
        modifier = Modifier.fillMaxSize(),
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        sheetContainerColor = DarkBlue,
        sheetDragHandle = { BottomSheetDefaults.DragHandle(color = White) },
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(bgColorSelected)
        ) {

            /**
             * Background Image
             */
            if (bgPicSelected != null && bgPicSelected != bgPicList[0]) {
                Image(
                    painter = painterResource(bgPicSelected!!),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }


            Column(
                Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = { scope.launch { bottomSheetScaffoldState.bottomSheetState.hide() } },
                        indication = null,
                        interactionSource = interactionButtonSource.value
                    )
                    .padding(vertical = 48.dp, horizontal = 16.dp)
            ) {

                /**
                 * Top bar
                 */
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {

                    RippleIcon(icon = R.drawable.back, size = 32.dp, tint = bgIconTint.value) {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(0)
                        }
                    }

                    Row(Modifier.wrapContentSize()) {
                        RippleIcon(icon = R.drawable.check, size = 32.dp, tint = bgIconTint.value) {
                            if (titleTextFieldValue.value.text.isNotBlank() || contentTextFieldValue.value.text.isNotBlank()) {

                                    noteVM.insertNote(
                                        NoteEntity(
                                            title = titleTextFieldValue.value.text,
                                            content = contentTextFieldValue.value.text,
                                            font = fontSelected.value,
                                            date = dateAndTime,
                                            textColorSelected.toArgb(),
                                            bgColorSelected.toArgb(),
                                            2,
                                            bgIconTint.value.toArgb(),
                                            textAlignSelected.toString(),
                                            selectImages.toString()
                                        )
                                    )

                                navController.navigate(Screen.HomeScreen.route) {
                                    popUpTo(0)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.size(0.dp))

                        RippleIcon(icon = R.drawable.more, size = 27.dp, tint = bgIconTint.value) {
                            focus.clearFocus()
                            scope.launch {
                                if (bottomSheetScaffoldState.bottomSheetState.isVisible)
                                    bottomSheetScaffoldState.bottomSheetState.hide()
                                else
                                    bottomSheetScaffoldState.bottomSheetState.show()
                            }
                        }
                    }

                }

                /**
                 * Title text field
                 */
                CompositionLocalProvider(LocalTextSelectionColors provides BlueRippleTheme.customTextSelectionColors) {
                    TextField(
                        value = titleTextFieldValue.value,
                        onValueChange = { titleTextFieldValue.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        interactionSource = interaction,
                        placeholder = {
                            Text(
                                text = "Title",
                                fontFamily = Font(ysabeauBold).toFontFamily(),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 30.sp
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Transparent,
                            focusedIndicatorColor = Transparent,
                            unfocusedIndicatorColor = Transparent
                        ),
                        textStyle = TextStyle(
                            color = textColorSelected,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = Font(fontSelected.value).toFontFamily()
                        ),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                }

                /**
                 * date text
                 */
                Text(
                    text = "${Date.calculateDateAndTime()} | $charactersCount characters",
                    color = Color.Gray,
                    fontFamily = Font(ysabeauMedium).toFontFamily(),
                    fontSize = 17.sp,
                    modifier = Modifier.padding(start = 18.dp)
                )

                /**
                 * Image From Gallery
                 */
                if (selectImages != null) {

                    SheetSpacer(20.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .fillMaxHeight(0.5f)
                            .clip(RoundedCornerShape(16.dp)), contentAlignment = Alignment.TopEnd
                    ) {

                        AsyncImage(
                            model = selectImages,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )

                        RippleIcon(icon = R.drawable.delete, size = 28.dp) {
                            selectImages = null
                        }


                    }
                }

                /**
                 * audio section
                 */
                if (audioFile != null) {
                    SheetSpacer(10.dp)
                    var isPlaying by remember {
                        mutableStateOf(false)
                    }

                    val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(if (bgIconTint.value == White) R.raw.audio_wave_white else R.raw.audio_wave_darkblue)
                    )

                    Row(
                        modifier = Modifier
                            .size(width = 100.dp, height = 24.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        Arrangement.End,
                        Alignment.CenterVertically
                    ) {

                        player.player()?.setOnCompletionListener { isPlaying = false }

                        SheetSpacer(16.dp)

                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            isPlaying = isPlaying,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .size(width = 128.dp, height = 48.dp)
                                .clickable(
                                    onClick = {
                                        isPlaying = !isPlaying
                                        if (isPlaying) {
                                            File(cacheDir, "audio.mp3").also {
                                                player.playAudio(it)
                                                audioFile = it
                                            }
                                        } else {
                                            player.pause()
                                        }
                                    },
                                    indication = null,
                                    interactionSource = interactionButtonSource.value
                                )
                        )
                    }
                }

                /**
                 * content text field
                 */
                CompositionLocalProvider(LocalTextSelectionColors provides BlueRippleTheme.customTextSelectionColors) {

                    TextField(
                        value = contentTextFieldValue.value,
                        onValueChange = {
                            charactersCount = it.text.length
                            contentTextFieldValue.value = it
                        },
                        modifier = Modifier.fillMaxSize(),
                        interactionSource = interaction,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Transparent,
                            focusedIndicatorColor = Transparent,
                            unfocusedIndicatorColor = Transparent
                        ),
                        textStyle = TextStyle(
                            color = textColorSelected,
                            fontSize = 22.sp,
                            fontFamily = Font(fontSelected.value).toFontFamily(),
                            textAlign = textAlignSelected
                        ),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                }

            }
        }
    }

    /**
     * Record Audio Dialog
     */
    if (isShowDialog) ShowDialog(recorder, cacheDir)

}

@Composable
fun ShowDialog(recorder: AndroidAudioRecorder, cacheDir: File?) {

    var isPlay by remember {
        mutableStateOf(true)
    }

    val infinite = rememberInfiniteTransition()

    val rotate by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2000, 0), RepeatMode.Reverse)
    )

    File(cacheDir, "audio.mp3").also {
        recorder.start(it)
        audioFile = it
        isPlay = true
    }


    Dialog(
        onDismissRequest = { isShowDialog = false },
        properties = DialogProperties()
    ) {
        Row(
            Modifier
                .fillMaxWidth(0.9f)
                .height(128.dp)
                .background(White, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp)), Arrangement.Center, Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .border(1.dp, DarkBlue, CircleShape)
                    .rotate(if (isPlay) rotate else 0f)
                    .background(Transparent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(DarkBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    RippleIcon(
                        icon = if (isPlay) R.drawable.pause else R.drawable.play,
                        tint = White,
                        36.dp,
                        WhiteRippleTheme
                    ) {
                        isPlay = if (isPlay) {
                            recorder.pause()
                            false
                        } else {
                            recorder.resume()
                            true
                        }
                    }
                }
            }


            SheetSpacer(56.dp)

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(DarkBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                RippleIcon(icon = R.drawable.stop, tint = White, 36.dp, WhiteRippleTheme) {
                    recorder.stop()
                    isPlay = false
                    isShowDialog = false
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetContent(
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    sheetState: BottomSheetScaffoldState,
    recorder: AndroidAudioRecorder,
    player: AndroidAudioPlayer,
    activity: Activity,
) {

    fontSelected.value = fontList[fontCounter].font
    val scope = rememberCoroutineScope()

    val rowAlpha = 0.07f

    Column(
        Modifier
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp / 1.7).dp)
            .padding(horizontal = 12.dp)
            .background(DarkBlue)
    ) {

        /**
         * Font Row
         */
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(SkyBlue.copy(rowAlpha), RoundedCornerShape(16.dp)),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(
                text = "Typeface",
                fontSize = 18.sp,
                fontFamily = Font(ysabeauBold).toFontFamily(),
                modifier = Modifier.padding(start = 8.dp),
                color = LightGray
            )

            Row(
                modifier = Modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CompositionLocalProvider(LocalRippleTheme provides WhiteRippleTheme) {
                    IconButton(onClick = {
                        if (fontCounter > 0) fontCounter--
                    }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.left_arrow),
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier
                                .size(24.dp)

                        )
                    }
                }

                Text(
                    text = fontList[fontCounter].name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Font(fontList[fontCounter].font).toFontFamily(),
                    color = White
                )

                CompositionLocalProvider(LocalRippleTheme provides WhiteRippleTheme) {
                    IconButton(onClick = {
                        if (fontCounter < (fontList.size - 1)) fontCounter++
                    }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.left_arrow),
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier
                                .size(24.dp)
                                .rotate(180f)

                        )
                    }
                }

            }
        }

        SheetSpacer()

        /**
         * text Color
         */
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(SkyBlue.copy(rowAlpha), RoundedCornerShape(16.dp)),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(
                text = "Text Color",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Font(ysabeauBold).toFontFamily(),
                modifier = Modifier.padding(start = 8.dp),
                color = LightGray
            )

            Row(
                modifier = Modifier.padding(end = 8.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                textColorList.forEach {
                    SheetSpacer(size = 4.dp)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color = it, CircleShape)
                            .padding(0.dp)
                            .border(2.dp, White, CircleShape)
                            .selectable(
                                selected = (textColorSelected == it),
                                onClick = { textColorSelected = it },
                                role = Role.RadioButton
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (textColorSelected == it)
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = White
                            )
                        else
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(0.dp)
                            )

                    }
                }
            }

        }

        SheetSpacer()

        /**
         * bg Color
         */
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(SkyBlue.copy(rowAlpha), RoundedCornerShape(16.dp)),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(
                text = "Background Color",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Font(ysabeauBold).toFontFamily(),
                modifier = Modifier.padding(start = 8.dp),
                color = LightGray
            )

            Row(
                modifier = Modifier.padding(end = 8.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                bgColorList.forEach {
                    SheetSpacer(size = 4.dp)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color = it, CircleShape)
                            .padding(0.dp)
                            .border(2.dp, White, CircleShape)
                            .selectable(
                                selected = (bgColorSelected == it),
                                onClick = { bgColorSelected = it },
                                role = Role.RadioButton
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (bgColorSelected == it)
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        else
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(0.dp)
                            )

                    }
                }
            }

        }

        SheetSpacer()

        /**
         * bg Image
         */
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(SkyBlue.copy(rowAlpha), RoundedCornerShape(16.dp)),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            LazyRow {
                items(bgPicList) {
                    SheetSpacer(6.dp)
                    Box(modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(
                            1.dp,
                            if (bgPicSelected == it) White else Transparent,
                            CircleShape
                        )
                        .selectable(
                            selected = (bgPicSelected == it),
                            onClick = {
                                bgPicSelected = if (bgPicSelected == bgPicList[0]) null else it

                                if (
                                    bgPicSelected == bgPicList[0] ||
                                    bgPicSelected == bgPicList[6]
                                )
                                    bgIconTint.value = DarkBlue
                                else if (
                                    bgPicSelected == bgPicList[1] ||
                                    bgPicSelected == bgPicList[2] ||
                                    bgPicSelected == bgPicList[3] ||
                                    bgPicSelected == bgPicList[4] ||
                                    bgPicSelected == bgPicList[5] ||
                                    bgPicSelected == bgPicList[7]
                                )
                                    bgIconTint.value = White


                            }
                        ))
                    {
                        AsyncImage(
                            model = it, contentDescription = null, modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape), contentScale = ContentScale.Crop
                        )
                    }


                }
            }
        }

        SheetSpacer()

        /**
         * Alignment Row
         */
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(SkyBlue.copy(rowAlpha), RoundedCornerShape(16.dp)),
            Arrangement.SpaceAround, Alignment.CenterVertically
        ) {

            iconAlignList.forEach {

                Box(modifier = Modifier
                    .size(40.dp)
                    .selectable(
                        selected = (iconAlignSelected == it),
                        onClick = { iconAlignSelected = it }
                    )
                    .background(
                        if (iconAlignSelected == it) LightGray.copy(0.5f) else Transparent,
                        CircleShape
                    )
                    .border(
                        1.dp,
                        if (iconAlignSelected == it) LightGray else Transparent,
                        CircleShape
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    RippleIcon(it, White, 32.dp, WhiteRippleTheme) {
                        iconAlignSelected = it
                        when (iconAlignSelected) {
                            iconAlignList[0] -> textAlignSelected = TextAlign.Start
                            iconAlignList[1] -> textAlignSelected = TextAlign.Center
                            iconAlignList[2] -> textAlignSelected = TextAlign.End
                            iconAlignList[3] -> textAlignSelected = TextAlign.Justify
                        }
                    }
                }
            }

        }

        SheetSpacer()

        /**
         * Audio Gallery Row
         */
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clickable {
                    recorder.stop()
                    player.playAudio(audioFile ?: return@clickable)
                }
                .background(SkyBlue.copy(rowAlpha), RoundedCornerShape(16.dp)),
            Arrangement.SpaceEvenly, Alignment.CenterVertically
        ) {
            RippleIcon(icon = R.drawable.gallery, tint = White, rippleTheme = WhiteRippleTheme) {
                galleryLauncher.launch("image/*")
                scope.launch {
                    sheetState.bottomSheetState.hide()
                }
            }

            RippleIcon(icon = R.drawable.microphone, tint = White, rippleTheme = WhiteRippleTheme) {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(activity, RECORD_AUDIO) -> {
                        isShowDialog = true
                        scope.launch {
                            sheetState.bottomSheetState.hide()
                        }
                    }

                    else -> {
                        ActivityCompat.requestPermissions(activity, arrayOf(RECORD_AUDIO), 0)
                    }
                }
            }

        }

    }

}

@Composable
fun SheetSpacer(size: Dp = 20.dp) {
    Spacer(modifier = Modifier.size(size))
}
