package com.example.limitlife.ui.screen.mainScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.limitlife.network.ShortNote
import com.example.limitlife.repository.NetworkUserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class NotesListScreenViewModel @Inject constructor(
    private val userDataRepository: NetworkUserDataRepository
) : ViewModel() {
    var loadingScreenUiState : NotesListScreenUiState by  mutableStateOf(NotesListScreenUiState.Loading)
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
        } catch (e  : HttpException){
            NotesListScreenUiState.Error(e.message.toString())
        }
    }

    fun createNewNote (note: ShortNote) =  viewModelScope.launch{
        userDataRepository.createNewNote(note)
    }

//  //  val getUserToken : StateFlow<NotesListScreenUiState> = offlineUserTokenRepository.userToken
//        .filterNotNull()
//        .map {
//       // NotesListScreenUiState(userToken =  it)
//    }.stateIn(
//        viewModelScope , SharingStarted.WhileSubscribed(5_000L) ,  NotesListScreenUiState() ,
//    )
}

data class random (
    val userToken : String = ""
)

sealed interface NotesListScreenUiState{
    data class Success(val notes : List<ShortNote>) : NotesListScreenUiState
    data class Error(val error : String) : NotesListScreenUiState
    data object Loading : NotesListScreenUiState
}