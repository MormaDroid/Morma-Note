package mohsen.morma.mormanote.note.record

import java.io.File

interface AudioRecorder {

    fun start(outputFile: File)
    fun stop()
    fun pause()
    fun resume()

}