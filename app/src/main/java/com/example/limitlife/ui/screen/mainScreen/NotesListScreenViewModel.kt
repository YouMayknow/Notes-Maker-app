package com.example.limitlife.ui.screen.mainScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.limitlife.network.DetailedNote
import com.example.limitlife.network.ShortNote
import com.example.limitlife.network.UpdatedShortNote
import com.example.limitlife.repository.FakeUserDataRepository
import com.example.limitlife.repository.NetworkUserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class NotesListScreenViewModel @Inject constructor(
    private val userDataRepository: NetworkUserDataRepository
) : ViewModel() {

    var loadingScreenUiState : NotesListScreenUiState by  mutableStateOf(NotesListScreenUiState.Loading)
         private set
    var detailedNoteUiState : DetailedScreenUiState by mutableStateOf(DetailedScreenUiState(visible = false, detailedNote = null , message = "" ))
    private set
    init {
        getNotes()
    }
     fun getNotes()  = viewModelScope.launch{
        loadingScreenUiState = try {
            val notes = userDataRepository.getAllUserNotes()
            NotesListScreenUiState.Success(notes.body() ?: emptyList())
        } catch (e  : Exception){
            NotesListScreenUiState.Error(e.message.toString())
        }
    }

    fun deleteNote(noteId : Int) = viewModelScope.launch {
        try {
       val action =  userDataRepository.deleteSelectedNote(noteId)
        } catch (e : IOException) {
            loadingScreenUiState = NotesListScreenUiState.Error(e.message.toString())
        } catch (e  : HttpException){
           loadingScreenUiState =  NotesListScreenUiState.Error(e.message.toString())
        }
    }

    fun getDetailsOfNote(noteId: Int) = viewModelScope.launch {
       try {
          val detailedNote =  userDataRepository.getSelectedNote(noteId)
           detailedNoteUiState = if (detailedNote.isSuccessful){
               DetailedScreenUiState( visible = true, detailedNote =detailedNote.body() , message = "")

           } else {
               DetailedScreenUiState( visible = true, detailedNote = null , message = detailedNote.errorBody()?.string() ?: "")

           }
        } catch (e  : IOException){
           detailedNoteUiState = DetailedScreenUiState( visible = true, detailedNote = null , message = e.message.toString())
        }
    }
    fun refreshNotes() {
        getNotes()
    }
}



sealed interface NotesListScreenUiState{
    data class Success(val notes : List<UpdatedShortNote> , val isDetailedScreenVisible : Boolean = false ) : NotesListScreenUiState
    data class Error(val error : String) : NotesListScreenUiState
    data object Loading : NotesListScreenUiState
}

data class  DetailedScreenUiState(
    val  detailedNote: DetailedNote? ,
    val visible : Boolean ,
    val message : String ,
)