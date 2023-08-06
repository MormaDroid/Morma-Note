package mohsen.morma.mormanote.data

import mohsen.morma.mormanote.model.NoteEntity
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val dao: NoteDao
) {

    suspend fun getNotes() = dao.getNotes()

    suspend fun searchByTitle(title:String) = dao.searchByTitle(title)

    fun getNotesById(id:Int) = dao.getNotesById(id)

    suspend fun insertNote(note:NoteEntity) = dao.insertNote(note)

    suspend fun deleteNote(note:NoteEntity) = dao.deleteNote(note)

    suspend fun updateNote(note:NoteEntity) = dao.updateNote(note)



}