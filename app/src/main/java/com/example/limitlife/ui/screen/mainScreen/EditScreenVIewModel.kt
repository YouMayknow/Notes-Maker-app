package com.example.limitlife.ui.screen.mainScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.limitlife.network.ShortNote
import com.example.limitlife.network.UpdatedShortNote
import com.example.limitlife.repository.NetworkUserDataRepository
import com.example.limitlife.repository.Note
import com.example.limitlife.repository.OfflineUserDataRepository
import com.example.limitlife.utils.isInternetAvailable
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
            }
        }
        else {
            saveNotesLocally(shortNote)
        }

    }
    fun updateNote(updatedShortNote: UpdatedShortNote) = viewModelScope.launch {
       val response =  userDataRepository.updateNote(updatedShortNote)
        _uiState.update {
            it.copy(
                isSaveSuccessful =   when {
                    response.isSuccessful -> true
                    else -> false
                } ,
                serverResponse = if (!response.isSuccessful)  response.message().toString() else ""
            )
        }
    }
    private suspend fun saveNotesLocally(shortNote: ShortNote) {
        val note  = Note(noteId = -1 , heading =  shortNote.heading , content = shortNote.content ,)
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
}

data class  EditScreenUiState(
    val serverResponse : String = "" ,
    val isSaveSuccessful : Boolean = false ,
    val isBuffering : Boolean = false,
)