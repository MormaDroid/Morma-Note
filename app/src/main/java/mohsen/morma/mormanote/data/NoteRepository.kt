package mohsen.morma.mormanote.data

import mohsen.morma.mormanote.model.NoteEntity
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val dao: NoteDao
) {

    fun getNotes() = dao.getNotes()

    fun searchByTitle(title:String) = dao.searchByTitle(title)

    fun getNotesById(id:Int) = dao.getNotesById(id)

    suspend fun insertNote(note:NoteEntity) = dao.insertNote(note)

    fun deleteNote(note:NoteEntity) = dao.deleteNote(note)

    suspend fun updateNote(note:NoteEntity) = dao.updateNote(note)



}