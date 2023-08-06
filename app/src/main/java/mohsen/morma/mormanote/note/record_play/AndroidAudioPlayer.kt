package mohsen.morma.mormanote.note.record_play

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(private val context: Context): AudioPlayer {

    private var player : MediaPlayer? = null

    private var isPlaying :Boolean? = null

    override fun playAudio(file: File) {
        MediaPlayer.create(context,file.toUri()).apply {
            player = this
            start()
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }

    override fun onComplete():Boolean? {

        player?.setOnCompletionListener {
            isPlaying = false
        }
        return isPlaying
    }

    override fun player(): MediaPlayer? {
        return player
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun pause() {
        player?.pause()
    }



}