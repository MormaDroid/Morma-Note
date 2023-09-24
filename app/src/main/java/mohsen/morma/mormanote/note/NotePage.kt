package mohsen.morma.mormanote.note

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import mohsen.morma.mormanote.R
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.data.NoteVM
import mohsen.morma.mormanote.model.FontModel
import mohsen.morma.mormanote.model.NoteEntity
import mohsen.morma.mormanote.setting.appThemeSelected
import mohsen.morma.mormanote.ui.theme.BlueLink
import mohsen.morma.mormanote.ui.theme.Dark
import mohsen.morma.mormanote.ui.theme.DarkBlue
import mohsen.morma.mormanote.ui.theme.DarkRed
import mohsen.morma.mormanote.ui.theme.Gray
import mohsen.morma.mormanote.ui.theme.Green
import mohsen.morma.mormanote.ui.theme.LightYellow
import mohsen.morma.mormanote.ui.theme.Orange
import mohsen.morma.mormanote.ui.theme.Pink
import mohsen.morma.mormanote.ui.theme.SkyBlue
import mohsen.morma.mormanote.ui.theme.bKamran
import mohsen.morma.mormanote.ui.theme.dosis
import mohsen.morma.mormanote.ui.theme.iranNast
import mohsen.morma.mormanote.ui.theme.italianno
import mohsen.morma.mormanote.ui.theme.potra
import mohsen.morma.mormanote.ui.theme.ysabeauBold
import mohsen.morma.mormanote.ui.theme.ysabeauMedium
import mohsen.morma.mormanote.util.BlueRippleTheme
import mohsen.morma.mormanote.util.Date
import mohsen.morma.mormanote.util.RippleIcon
import mohsen.morma.mormanote.util.WhiteRippleTheme

/**
 * Calculate Date And Time
 */
@RequiresApi(Build.VERSION_CODES.O)
var dateAndTime = Date.calculateDateAndTime()

/**font*/
var fontSelected: MutableState<Int> = mutableIntStateOf(ysabeauMedium)
val fontList = listOf(
    FontModel("ysabeau", ysabeauMedium),
    FontModel("dosis", dosis),
    FontModel("potra", potra),
    FontModel("italianno", italianno),
    FontModel("کامران", bKamran),
    FontModel("ایران نستعلیق", iranNast)
)
var fontCounter by mutableIntStateOf(0)

/**text Color*/
val textColorList = listOf(
    appThemeSelected,
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
var bgIconTint = mutableStateOf(appThemeSelected)

/**Background Color*/
val bgColorList = listOf(
    White,
    Pink,
    Orange,
    SkyBlue,
    LightYellow,
    DarkBlue
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

/**fontSize*/
val fontSizeList = listOf(15, 18, 20, 22, 24, 28, 30)
var fontSizeSelected by mutableIntStateOf(fontSizeList[3])

/**text link*/
var textLink by mutableStateOf(TextFieldValue(""))
var isTextLinkDialogShow by mutableStateOf(false)

var isShowSelectBgImgDialog by mutableStateOf(false)



@OptIn(ExperimentalMaterial3Api::class
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotePage(
    navController: NavHostController,
    noteVM: NoteVM = hiltViewModel(),
    noteId: Int = -1,
    startWithBgImg: String =  "",
    link: String = ""
) {
    var id = -1
    if (noteId != -1) id = noteId
    val note = noteVM.getNotesById(id)

    /** open chrome */
    val uriHandler = LocalUriHandler.current

    /**
     * Text field(Title,Content,CharCount)
     */
    val titleTextFieldValue = remember {
        mutableStateOf(TextFieldValue(if (id != -1) note.title else ""))
    }
    val contentTextFieldValue = remember {
        mutableStateOf(TextFieldValue(if (id != -1) note.content else ""))
    }
    var charactersCount by remember {
        mutableIntStateOf(if (id != -1) note.charCount else contentTextFieldValue.value.text.length)
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

    val config = LocalConfiguration.current

    BottomSheetScaffold(
        sheetContent = {
            SheetContent()
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = sheetHeight,
        modifier = Modifier.fillMaxSize(),
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        sheetContainerColor = appThemeSelected,
        sheetDragHandle = { BottomSheetDefaults.DragHandle(color = if (appThemeSelected == Gray) Black else White) },
    ) {

        LaunchedEffect(key1 = true) {
            if (link != "") isTextLinkDialogShow = true

            if (startWithBgImg != "") isShowSelectBgImgDialog =true
        }

        LaunchedEffect(key1 = true) {
            if (id != -1) {
                fontSelected.value = note.font
                fontCounter = note.fontCounter
                textColorSelected = Color(note.color)
                bgIconTint.value = Color(note.iconTintSelected)
                bgPicSelected = note.backgroundPic
                bgColorSelected = Color(note.backgroundColor)
                iconAlignSelected = note.iconAlign
                textAlignSelected = textAlign(note)
                fontSizeSelected = note.fontSize
                textLink = TextFieldValue(note.link)
            } else {
                fontSelected.value = ysabeauMedium
                fontCounter = 0
                textColorSelected = DarkBlue
                bgIconTint.value = appThemeSelected
                bgPicSelected = null
                bgColorSelected = White
                iconAlignSelected = iconAlignList[0]
                textAlignSelected = TextAlign.Start
                fontSizeSelected = fontSizeList[3]
                textLink = TextFieldValue("")
            }
        }

        Box(
            Modifier
                .fillMaxSize()
                .background(bgColorSelected)
        ) {

            /**
             * Background Image
             */
            if (bgPicSelected != null && bgPicSelected != bgPicList[0] && bgPicSelected != 0) {
                Log.e("3636", "hello: $bgPicSelected")
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

                                var bgImage = 0
                                bgPicSelected?.let { bgImage = it }

                                if (id != -1) {
                                    Log.e("3636", "Update")
                                    noteVM.updateNote(
                                        NoteEntity(
                                            title = titleTextFieldValue.value.text,
                                            content = contentTextFieldValue.value.text,
                                            font = fontSelected.value,
                                            fontCounter = fontCounter,
                                            fontSize = fontSizeSelected,
                                            date = dateAndTime,
                                            charactersCount,
                                            textLink.text,
                                            textColorSelected.toArgb(),
                                            bgColorSelected.toArgb(),
                                            bgImage,
                                            bgIconTint.value.toArgb(),
                                            textAlignSelected.toString(),
                                            iconAlignSelected,
                                            note.id,
                                            Firebase.auth.uid.toString()
                                        )
                                    )
                                } else {
                                    Log.e("3636", "add")


                                    noteVM.insertNote(
                                        NoteEntity(
                                            title = titleTextFieldValue.value.text,
                                            content = contentTextFieldValue.value.text,
                                            font = fontSelected.value,
                                            fontCounter,
                                            fontSize = fontSizeSelected,
                                            date = dateAndTime,
                                            charactersCount,
                                            textLink.text,
                                            textColorSelected.toArgb(),
                                            bgColorSelected.toArgb(),
                                            bgImage,
                                            bgIconTint.value.toArgb(),
                                            textAlignSelected.toString(),
                                            iconAlignSelected,
                                            uid = Firebase.auth.uid.toString()
                                        )
                                    )
                                }
                                navController.navigate(Screen.HomeScreen.route) {
                                    popUpTo(0)
                                }
                            }
                        }

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
                    text = "$dateAndTime | $charactersCount characters",
                    color = Color.Gray,
                    fontFamily = Font(ysabeauMedium).toFontFamily(),
                    fontSize = 17.sp,
                    modifier = Modifier.padding(start = 18.dp)
                )
                /**Text Link UI*/
                if (textLink.text != "") {
                    SheetSpacer(10.dp)

                    Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (textLink.text.contains("https://"))
                                textLink.text
                            else if(textLink.text.contains("www.") && textLink.text.contains(".com") && !textLink.text.contains("https://") )
                                "https://${textLink.text}"
                            else
                                textLink.text,
                            fontFamily = Font(ysabeauBold).toFontFamily(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 20.sp,
                            color = BlueLink,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .widthIn(max = (config.screenWidthDp - 64).dp)
                                .padding(start = 18.dp)
                                .clickable {
                                    if (textLink.text.contains("https://"))
                                        uriHandler.openUri(textLink.text)
                                    else if (textLink.text.contains("www.") && textLink.text.contains(
                                            ".com"
                                        ) && !textLink.text.contains("https://")
                                    )
                                        uriHandler.openUri("https://${textLink.text}")
                                    else
                                        uriHandler.openUri("https://www.google.com/search?q=${textLink.text}")
                                }
                        )
                        IconButton(onClick = { isTextLinkDialogShow = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit),
                                contentDescription = null,
                                tint = BlueLink,
                                modifier = Modifier.size(24.dp)
                            )
                        }
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
                            fontSize = fontSizeSelected.sp,
                            fontFamily = Font(fontSelected.value).toFontFamily(),
                            textAlign = textAlignSelected
                        ),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                }

            }
        }

        if (isTextLinkDialogShow) EnterLinkDialog()
        if (isShowSelectBgImgDialog) BgImgDialog()

    }

}

@Composable
fun EnterLinkDialog() {
    Dialog(onDismissRequest = { isTextLinkDialogShow = false }) {
        LinkDialogContent()
    }
}

@Composable
fun BgImgDialog() {
    Dialog(onDismissRequest = { isShowSelectBgImgDialog = false }) {
        BgImgDialogContent()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BgImgDialogContent() {


    val bgPicListWithoutTransparent = listOf(
        R.drawable.pic2,
        R.drawable.pic5,
        R.drawable.pic6,
        R.drawable.pic7,
        R.drawable.pic8,
        R.drawable.pic9,
        R.drawable.pic10,
    )


    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.4f)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        FlowRow(
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.Center
        ) {

            for (bgImage in bgPicListWithoutTransparent) {
                Image(
                    painter = painterResource(id = bgImage),
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .padding(12.dp)
                        .clip(CircleShape)
                        .clickable {
                            bgPicSelected = bgImage

                            bgPicSelected = if (bgPicSelected == bgPicList[0]) null else bgImage

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

                            isShowSelectBgImgDialog = false
                        },
                    contentScale = ContentScale.FillBounds
                )

            }

        }
    }
}

@Composable
fun LinkDialogContent() {

    Column(
        Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.2f)
            .clip(RoundedCornerShape(16.dp))
            .background(White, RoundedCornerShape(16.dp)),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = textLink,
            onValueChange = { textLink = it },
            modifier = Modifier.fillMaxWidth(0.85f),
            singleLine = true,
            leadingIcon = { RippleIcon(icon = R.drawable.web,tint= DarkBlue,size=28.dp) },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = DarkBlue,
                backgroundColor = White,
                cursorColor = DarkBlue
            ),
            textStyle = TextStyle(
                color = DarkBlue,
                fontFamily = Font(fontSelected.value).toFontFamily(),
                fontSize = 18.sp
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Uri
            ),
            keyboardActions = KeyboardActions(onDone = { isTextLinkDialogShow = false })
        )

        SheetSpacer(10.dp)

        Button(
            onClick = { isTextLinkDialogShow = false },
            colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue)
        ) {
            Text(
                text = stringResource(R.string.done),
                fontWeight = FontWeight.SemiBold,
                fontFamily = Font(ysabeauMedium).toFontFamily(),
                color = White
            )
        }
    }
}


private fun textAlign(note: NoteEntity) =
    if (note.alignment == "Start") TextAlign.Start else if (note.alignment == "End") TextAlign.End else if (note.alignment == "Center") TextAlign.Center else TextAlign.Justify


@Composable
fun SheetContent() {

    fontSelected.value = fontList[fontCounter].font

    val rowAlpha = 0.1f

    Column(
        Modifier
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp / 1.7).dp)
            .padding(horizontal = 12.dp)
            .background(appThemeSelected)
    ) {

        /**
         * Font Row
         */
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(LightGray.copy(rowAlpha), RoundedCornerShape(16.dp)),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(
                text = "Typeface",
                fontSize = 18.sp,
                fontFamily = Font(dosis).toFontFamily(),
                modifier = Modifier.padding(start = 8.dp),
                color = if (appThemeSelected == Gray) Black else LightGray
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
                            tint = if (appThemeSelected == Gray) Black else White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Text(
                    text = fontList[fontCounter].name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Font(fontList[fontCounter].font).toFontFamily(),
                    color = if (appThemeSelected == Gray) Black else White
                )

                CompositionLocalProvider(LocalRippleTheme provides WhiteRippleTheme) {
                    IconButton(onClick = {
                        if (fontCounter < (fontList.size - 1)) fontCounter++
                    }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.left_arrow),
                            contentDescription = null,
                            tint = if (appThemeSelected == Gray) Black else White,
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
         * Font Size
         */
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(LightGray.copy(rowAlpha), RoundedCornerShape(16.dp)),
            Arrangement.SpaceEvenly, Alignment.CenterVertically
        ) {
            fontSizeList.forEach {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .selectable(
                            selected = (fontSizeSelected == it),
                            onClick = { fontSizeSelected = it },
                            role = Role.RadioButton
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it.toString(),
                        fontSize = it.sp,
                        color = if (appThemeSelected == Gray) Black else White,
                        textDecoration = if (fontSizeSelected == it) TextDecoration.Underline else TextDecoration.None,
                        fontFamily = Font(dosis).toFontFamily()
                    )
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
                .background(LightGray.copy(rowAlpha), RoundedCornerShape(16.dp)),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(
                text = "Text Color",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Font(dosis).toFontFamily(),
                modifier = Modifier.padding(start = 8.dp),
                color = if (appThemeSelected == Gray) Black else LightGray
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
                                tint = if (appThemeSelected == Gray) Black else White
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
                .background(LightGray.copy(rowAlpha), RoundedCornerShape(16.dp)),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(
                text = "Background Color",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Font(dosis).toFontFamily(),
                modifier = Modifier.padding(start = 8.dp),
                color = if (appThemeSelected == Gray) Black else LightGray
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
                                onClick = {
                                    bgColorSelected = it
                                    if (bgColorSelected == DarkBlue) bgIconTint.value =
                                        White else bgIconTint.value = DarkBlue
                                },
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
                .background(LightGray.copy(rowAlpha), RoundedCornerShape(16.dp)),
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
                .background(LightGray.copy(rowAlpha), RoundedCornerShape(16.dp)),
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

    }

}

@Composable
fun SheetSpacer(size: Dp = 20.dp) {
    Spacer(modifier = Modifier.size(size))
}

