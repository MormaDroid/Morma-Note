package mohsen.morma.mormanote.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import mohsen.morma.mormanote.model.NoteEntity

@Dao
interface NoteDao {

    @Query("SELECT * FROM NoteEntity")
    fun getNotes() : List<NoteEntity>

    @Query("SELECT * FROM NoteEntity WHERE title LIKE   '%' || :title || '%' ")
     fun searchByTitle(title : String) : List<NoteEntity>

    @Query("SELECT * FROM NoteEntity WHERE id = :id")
    fun getNotesById(id : Int) : NoteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertNote(note: NoteEntity)

    @Delete
     fun deleteNote(note: NoteEntity)

    @Update()
     fun updateNote(note: NoteEntity)

}