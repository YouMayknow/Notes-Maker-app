package com.example.notesMaker.ui.screen.noteScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesMaker.network.ShortNote
import com.example.notesMaker.network.UpdatedShortNote
import com.example.notesMaker.repository.NetworkUserDataRepository
import com.example.notesMaker.repository.Note
import com.example.notesMaker.repository.OfflineUserDataRepository
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
   private val userDataRepository: NetworkUserDataRepository ,
    private val offlineUserDataRepository : OfflineUserDataRepository ,
    @ApplicationContext private  val  context : Context
) : ViewModel() {
    private  var  _uiState  =  MutableStateFlow(EditScreenUiState())
    val   uiState  : StateFlow<EditScreenUiState> = _uiState.asStateFlow()

    private suspend fun checkForInternet() : Boolean {
        return  isInternetAvailable(context)
    }

    fun createNote(shortNote: ShortNote) = viewModelScope.launch{
        if (checkForInternet()){
            try {
                val response   =  userDataRepository.createNewNote(shortNote)
                    _uiState.update {
                        it.copy(
                            isSaveSuccessful =   when {
                              response.isSuccessful -> true
                              else -> false
                            } ,
                            serverResponse = if (!response.isSuccessful)  response.message().toString() else ""
                        )
                }
            } catch ( e : Exception ){
                _uiState.update {
                    it.copy(
                        serverResponse =  e.message ?: "Something went wrong"
                    )
                }
                saveNotesLocally(shortNote)
                _uiState.update {
                    it.copy(
                        isSaveSuccessful =  true
                    )
                }
            }
        }
        else {
            saveNotesLocally(shortNote)
        }
    }


    /** See it have to handle a lot of fundamentals of updating the notes  on different basis so here ite begins
     *
     */
    fun updateNote(updatedShortNote: UpdatedShortNote) = viewModelScope.launch {
        if (!checkForInternet()){
          updateNotesLocally(updatedShortNote)
        }
        else {
            try {
                val response =  userDataRepository.updateNote(updatedShortNote)
                if (updatedShortNote.localNoteId != -1 ){
                    updateNotesLocally(updatedShortNote)
                }
                _uiState.update {
                    it.copy(
                        isSaveSuccessful =   when {
                            response.isSuccessful -> true
                            else -> false
                        } ,
                        serverResponse = if (!response.isSuccessful)  response.message().toString() else ""
                    )
                }
            } catch ( e : Exception){
                updateNotesLocally(updatedShortNote)
                Log.e("Problem", "$e")
            }
        }

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