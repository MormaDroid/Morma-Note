package mohsen.morma.mormanote.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import mohsen.morma.mormanote.model.NoteEntity
import javax.inject.Inject

@HiltViewModel
class NoteVM @Inject constructor(
    private val repository: NoteRepository
) : ViewModel(){

    var notesList = MutableStateFlow<List<NoteEntity>>(emptyList())
    var searchList = MutableStateFlow<List<NoteEntity>>(emptyList())

    fun getAllNotes(){
        viewModelScope.launch(Dispatchers.IO) {
            notesList.value = repository.getNotes().sortedByDescending { it.id }
        }
    }
    fun searchByTitle(title:String){
        viewModelScope.launch(Dispatchers.IO) {
            searchList.value = repository.searchByTitle(title)
        }
    }
    fun getNotesById(id:Int):NoteEntity = repository.getNotesById(id)

    fun insertNote(note: NoteEntity){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertNote(note)
        }
    }
    fun deleteNote(note: NoteEntity){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteNote(note)
        }
    }
    fun updateNote(note: NoteEntity){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateNote(note)
        }
    }


}