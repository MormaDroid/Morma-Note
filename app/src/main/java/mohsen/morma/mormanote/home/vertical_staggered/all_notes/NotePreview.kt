package mohsen.morma.mormanote.home.vertical_staggered.all_notes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import mohsen.morma.mormanote.bottombar.setup.Screen
import mohsen.morma.mormanote.model.NoteEntity
import mohsen.morma.mormanote.ui.theme.BlueLink
import mohsen.morma.mormanote.ui.theme.ysabeauMedium



@Composable
@OptIn(ExperimentalMaterialApi::class)
fun NotePreview(
    navController: NavHostController,
    note: NoteEntity
) {
    Card(
        onClick = { goToFullScreenNote(navController, note) },
        modifier = Modifier.wrapContentHeight(),
        backgroundColor = Color(note.backgroundColor),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        CardContent(note)
    }
}


private fun goToFullScreenNote(navController: NavHostController, note: NoteEntity) {
    navController.navigate(Screen.NoteScreen.route + "?noteId=${note.id}?gallery=?link=")
}


@Composable
private fun CardContent(note: NoteEntity) {

    /**size of content text for fixing background image size*/
    var contentSize by remember{mutableStateOf(4.dp)}

    /**size of title text for fixing background image size*/
    var titleSize by remember{mutableStateOf(4.dp)}

    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.Transparent)
    ) {

        Log.d("3636", "Gallery: ${note.gallery}")

        BackgroundImage(note, contentSize, titleSize)

        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {

            GalleryImage(note)

            Text(
                text = note.title,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = Font(note.font).toFontFamily(),
                fontSize = 30.sp,
                maxLines = 2,
                textAlign = updateAlignment(note),
                color = Color(note.color),
                overflow = TextOverflow.Ellipsis,
                onTextLayout = {
                    titleSize = if(it.didOverflowHeight) (it.multiParagraph.height /1.7 ).dp  else (it.multiParagraph.height).dp
                }
            )


            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = note.content,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                fontFamily = Font(note.font).toFontFamily(),
                fontSize = note.fontSize.sp,
                maxLines = 15,
                textAlign = updateAlignment(note),
                color = Color(note.color),
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { contentSize = (it.multiParagraph.height / 2.75).dp }
            )

            Spacer(modifier = Modifier.size(8.dp))

            NoteLink(note)

            Spacer(modifier = Modifier.size(8.dp))

            NoteInfo(note)

        }

    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun BackgroundImage(
    note: NoteEntity,
    contentSize: Dp,
    titleSize: Dp
) {
    val notePreviewHeight =
        if (note.gallery != "") (contentSize + titleSize + 240.dp) else (contentSize + titleSize + 40.dp)

    Log.e("3636", "Height ${note.id} : $notePreviewHeight")
    if (note.backgroundPic != 0 && note.backgroundPic != 2131165332) {
        GlideImage(
            model = note.backgroundPic,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.height(notePreviewHeight)
        )
    }
}

@Composable
private fun NoteInfo(note: NoteEntity) {
    Text(
        text = "${note.date} | ${note.charCount} characters",
        color = Color.LightGray,
        fontFamily = Font(ysabeauMedium).toFontFamily(),
        fontSize = 11.sp
    )
}

@Composable
private fun NoteLink(note: NoteEntity) {
    if (note.link != "")
        Text(
            text = note.link,
            color = BlueLink,
            textDecoration = TextDecoration.Underline,
            fontFamily = Font(ysabeauMedium).toFontFamily(),
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun GalleryImage(note: NoteEntity) {
    Log.d("3636", "GalleryImage: ${note.gallery}")
    if (note.gallery != "") {
        GlideImage(
            model = note.gallery,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(256.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.size(8.dp))
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun BackgroundImage(note: NoteEntity) {

}

@Composable
private fun updateAlignment(note: NoteEntity) =
    if (note.alignment == "Start") TextAlign.Start else if (note.alignment == "End") TextAlign.End else if (note.alignment == "Center") TextAlign.Center else TextAlign.Justify