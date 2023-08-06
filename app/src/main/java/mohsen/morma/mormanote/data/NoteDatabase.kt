package mohsen.morma.mormanote.data

import androidx.room.Database
import androidx.room.RoomDatabase
import mohsen.morma.mormanote.model.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract val dao : NoteDao

}