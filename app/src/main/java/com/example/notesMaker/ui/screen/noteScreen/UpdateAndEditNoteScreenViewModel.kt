package com.example.notesMaker.ui.screen.noteScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesMaker.network.UpdatedShortNote
import com.example.notesMaker.repository.Note
import com.example.notesMaker.repository.OfflineUserDataRepository
import com.example.notesMaker.repository.WorkManagerRepository
import com.example.notesMaker.worker.LOGGING_OF_APP
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

// here in the function it will store the noteID too so that  if the work manager fails to update
// a lot of notes we can pass just noteID there and update the notes later
@HiltViewModel
class UpdateAndEditNoteScreenViewModel @Inject constructor(
   private val offlineUserDataRepository : OfflineUserDataRepository ,
   private val workManagerRepository: WorkManagerRepository ,
   @ApplicationContext private  val  context : Context
) : ViewModel() {
    private  var  _uiState  =  MutableStateFlow(EditScreenUiState())
    val   uiState  : StateFlow<EditScreenUiState> = _uiState.asStateFlow()

    fun createNote(note : Note) = viewModelScope.launch{
      val localNoteId =   saveNotesLocally(note)
      workManagerRepository.saveNote(note.heading, note.content )
    }

    fun updateNote(updatedShortNote: UpdatedShortNote) = viewModelScope.launch {
        updateNotesLocally(updatedShortNote)
        workManagerRepository.updateNote(updatedShortNote)
    }

    fun getNote(noteID : Int) = viewModelScope.launch{
        Log.e(LOGGING_OF_APP , "got the ntoe id here that is $noteID")
       val note =  offlineUserDataRepository.getNoteWithLocalNoteId(noteID)
        _uiState.update { it.copy(note = note) }
    }

    private suspend fun saveNotesLocally(note: Note) : Int {
       try {
           val localNoteId = offlineUserDataRepository.createNoteAndGetId(note)
           return localNoteId
           _uiState.update {
               it.copy(
               isSaveSuccessful =  true
                )
           }
       } catch (e : Exception){
           _uiState.update { it.copy(
               isSaveSuccessful =  false ,
               serverResponse = e.message ?: "Something went wrong"
                )
           }
           return -1 // returning -1 if the note is not saved
       }
    }
    /**
     * Here we are first checking it the notes is set to backup or not
     * if the notes is set to backup we have to save it offline and then sachedule for backup
     * if not , then we have to directly schedule for backup
     */
    private suspend fun updateNotesLocally (updatedShortNote : UpdatedShortNote) {
        val note = Note(
            heading = updatedShortNote.heading,
            content = updatedShortNote.content ,
            id = updatedShortNote.localNoteId ?: -1 ,
            noteId = updatedShortNote.id ,
            createdAt = LocalDateTime.now().toString() ,
            version = updatedShortNote.version ,
            lastUpdated = updatedShortNote.lastUpdated
        )
        try {
            offlineUserDataRepository.update(note)
            _uiState.update {
                it.copy(
                    isSaveSuccessful =  true
                )
            }
        } catch (e : Exception){
            _uiState.update { it.copy(
                isSaveSuccessful =  false ,
                serverResponse = e.message ?: "Something went wrong"
            )
            }
        }
    }
}
/**
 * were after updating the notes offline it will now schedule for
 * online via work manager ,
 * 1. it will create a note if the note  id is null or it will update the note according to the noteId.
 */

data class  EditScreenUiState(
    val serverResponse : String = "" ,
    val isSaveSuccessful : Boolean = false ,
    val isBuffering : Boolean = false,
    val note : Note = Note(heading = "" , content = "")
)