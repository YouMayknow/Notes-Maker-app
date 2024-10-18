package com.example.limitlife.ui.screen.mainScreen



import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.limitlife.network.DetailedNote
import com.example.limitlife.network.UpdatedShortNote
import com.example.limitlife.repository.NetworkUserDataRepository
import com.example.limitlife.repository.OfflineUserDataRepository
import com.example.limitlife.utils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class NotesListScreenViewModel @Inject constructor(
    private val offlineUserDataRepository: OfflineUserDataRepository ,
    private val userDataRepository: NetworkUserDataRepository ,
    @ApplicationContext val context: Context ,
) : ViewModel() {
    private  val _uiState  = MutableStateFlow(NotesListScreenUiState())
      val uiState  :  StateFlow<NotesListScreenUiState> = _uiState.asStateFlow()

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage : StateFlow<String?> =  _snackBarMessage

    init {
        checkForInternetAndFetchNotes()
        if (uiState.value.isInterNetAvailable){
           _snackBarMessage.value = "Connected to the server"
        } else {
            _snackBarMessage.value = "Performing in Offline Mode"

        }

    }
   private  suspend  fun checkForInternet() : Boolean {
       val isOnline =  isInternetAvailable(context)
       _uiState.update { it.copy(isInterNetAvailable =  isOnline) }
       return  isOnline
   }

    private fun checkForInternetAndFetchNotes()  = viewModelScope.launch{
         val isOnline = checkForInternet()
         if (isOnline) {
             try{
                 this@NotesListScreenViewModel.fetchOnlineNotes()
             } catch (e  : Exception){
                 Log.e("problem" , "$e")
                 catchingException(exception = e)
                 this@NotesListScreenViewModel.fetchOfflineNotes()
             }
         } else {
             this@NotesListScreenViewModel.fetchOfflineNotes()
         }
     }
    fun deleteNote(noteId : Int) = viewModelScope.launch {
        val isOnline = checkForInternet()
        if (isOnline){
            try {
              val response =   userDataRepository.deleteSelectedNote(noteId)
                if (response.isSuccessful){
                    _snackBarMessage.value = "Note deleted"
                } else {
                    _snackBarMessage.value = response.errorBody()?.string()
                }
                checkForInternetAndFetchNotes()
            }  catch (e  : Exception){
                Log.e("problem" , "$e")
                catchingException(e)
            }
        } else {
            _snackBarMessage.value = "Can't perform without internet"
        }
    }
    fun getDetailsOfNote(noteId: Int) = viewModelScope.launch {
       val isOnline = checkForInternet()
        if (isOnline){
            try {
                val response =  userDataRepository.getSelectedNote(noteId)
                _uiState.update {
                    it.copy(
                        isDetailedNoteVisible =  true ,
                        detailedNote = response.body() ,
                        detailedNoteMessage = response.errorBody()?.string() ?: ""
                    )
                }
            }catch (e : Exception ) {
                Log.e("problem" , "$e")
                catchingException(e)
            }
        }
    }
    fun closeDetailedNote(){
        _uiState.update {
            it.copy(isDetailedNoteVisible =  false)
        }
    }

    private  fun catchingException(exception: Exception) {
        val errorMessage = when (exception){
            is SocketTimeoutException -> "Check for internet : ${exception.message}"
            is IOException -> "Network error : ${exception.message}"
            is HttpException -> "Http error : ${exception.message}"
            else  -> "Unknown error : ${exception.message}"
        }
        _snackBarMessage.value = errorMessage
    }



    private  suspend fun fetchOfflineNotes() {
        val notes = offlineUserDataRepository.noteDao.getAllNotes().map {
            UpdatedShortNote(
                heading = it.heading ?: "" ,
                content = it.content  ?: "" ,
                id = it.noteId ?: 1
            )
        }
        _uiState.update {
            it.copy(
                notes = notes , isLoading = false , isInterNetAvailable = false
            )
        }
    }

    fun resetSnackBar() {
        _snackBarMessage.value = null
    }
    fun refreshNotes() {
        checkForInternetAndFetchNotes()
    }


    private suspend fun  fetchOnlineNotes() {
        val  response = userDataRepository.getAllUserNotes()
        _uiState.update {
            it.copy(notes = response.body() ?: emptyList() , isInterNetAvailable =  true , isLoading = false)
        }
    }

}




sealed interface NotesListProgressUiState{
    data class Success(val notes : List<UpdatedShortNote> , val isDetailedScreenVisible : Boolean = false , val isInterNetAvailable : Boolean = true) : NotesListProgressUiState
    data class Error(val error : String) : NotesListProgressUiState
    data object Loading : NotesListProgressUiState
}

data class  NotesListScreenUiState(
    val  detailedNote: DetailedNote? = null,
    val isDetailedNoteVisible : Boolean = false ,
    val detailedNoteMessage : String = "" ,
    val notes : List<UpdatedShortNote> = emptyList() ,
    val isInterNetAvailable: Boolean  = true ,
    val isLoading : Boolean = true ,
    val errorMessage : String = "" ,
)
