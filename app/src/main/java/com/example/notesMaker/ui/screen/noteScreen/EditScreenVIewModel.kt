package com.example.notesMaker.ui.screen.noteScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.impl.utils.taskexecutor.WorkManagerTaskExecutor
import com.example.notesMaker.network.ShortNote
import com.example.notesMaker.network.UpdatedShortNote
import com.example.notesMaker.repository.NetworkUserDataRepository
import com.example.notesMaker.repository.Note
import com.example.notesMaker.repository.NotesWorkManagerRepository
import com.example.notesMaker.repository.OfflineUserDataRepository
import com.example.notesMaker.repository.WorkManagerRepository
import com.example.notesMaker.utils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditScreenVIewModel @Inject constructor(
   private val offlineUserDataRepository : OfflineUserDataRepository ,
   private val workManagerRepository: WorkManagerRepository ,
   @ApplicationContext private  val  context : Context
) : ViewModel() {
    private  var  _uiState  =  MutableStateFlow(EditScreenUiState())
    val   uiState  : StateFlow<EditScreenUiState> = _uiState.asStateFlow()

    fun createNote(shortNote: ShortNote) = viewModelScope.launch{
        saveNotesLocally(shortNote)
       workManagerRepository.saveNote(shortNote.heading , shortNote.content)
    }

    fun updateNote(updatedShortNote: UpdatedShortNote) = viewModelScope.launch {
        updateNotesLocally(updatedShortNote)
        workManagerRepository.updateNote(updatedShortNote)
    }

    private suspend fun saveNotesLocally(shortNote: ShortNote) {
        val note  = Note(  heading =  shortNote.heading , content = shortNote.content ,)
       try {
       offlineUserDataRepository.save(note)
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
            noteId = updatedShortNote.id
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
)