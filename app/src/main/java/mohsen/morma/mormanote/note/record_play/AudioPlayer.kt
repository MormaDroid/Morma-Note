package mohsen.morma.mormanote.note.record_play

import android.media.MediaPlayer
import java.io.File

interface AudioPlayer {

    fun playAudio(file : File)

    fun pause()

    fun stop()

    fun onComplete():Boolean?

    fun player():MediaPlayer?

}