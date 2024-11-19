package com.example.notesMaker.ui.screen.mainScreen



import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesMaker.network.DetailedNote
import com.example.notesMaker.network.UpdatedShortNote
import com.example.notesMaker.repository.NetworkUserDataRepository
import com.example.notesMaker.repository.OfflineUserDataRepository
import com.example.notesMaker.utils.isInternetAvailable
import com.example.notesMaker.worker.LOGGING_OF_APP
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

    fun searchForWords(query : String) = viewModelScope.launch {
//        offlineUserDataRepository.noteDao.searchForWords(query)
//           // .onEach {  _uiState.update { it.copy(isDetailedNoteVisible =  true) } }
//            .filterNotNull()
//            .map{
//                UpdatedShortNote(
//                    content = it.content  ?: "",
//                    heading = it.heading ?: "",
//                    id = it.noteId ?: -1,
//                    localNoteId = it.id,
//                    version = it.version
//                )
//            }
//            .stateIn()
//        _uiState.update { it.copy(suggestionsOfNotes = notes , isLoading = false) }
    }

    private fun checkForInternetAndFetchNotes()  = viewModelScope.launch{
         val isOnline = checkForInternet()
         if (isOnline) {
          runCatching { this@NotesListScreenViewModel.fetchOnlineNotes() }
              .onFailure {
                  Log.e("problem" , "$it")
                  catchingException(it)
                  this@NotesListScreenViewModel.fetchOfflineNotes()
              }
         } else {
             this@NotesListScreenViewModel.fetchOfflineNotes()
         }
     }

    fun deleteNote(noteId : Int) = viewModelScope.launch {
        val isOnline = checkForInternet()
        if (isOnline){
            runCatching { userDataRepository.deleteSelectedNote(noteId) }
                .onFailure {
                    Log.e("problem" , "$it")
                    catchingException(it)
                }
                .onSuccess {
                  _snackBarMessage.value = if (it.isSuccessful)  "Note deleted"  else it.errorBody()?.string()
                    checkForInternetAndFetchNotes()
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

    private  fun catchingException(exception: Throwable?) {
        val errorMessage = when (exception){
            is SocketTimeoutException -> "Check for internet : ${exception.message}"
            is IOException -> "Network error : ${exception.message}"
            is HttpException -> "Http error : ${exception.message}"
            else  -> "Unknown error : ${exception?.message}"
        }
        _snackBarMessage.value = errorMessage
    }
    private  suspend fun fetchOfflineNotes() {
        val notes = offlineUserDataRepository.noteDao.getAllNotes().map {
            UpdatedShortNote(
                content = it.content  ?: "",
                heading = it.heading ?: "",
                id = it.noteId ?: -1,
                localNoteId = it.id,
                version = it.version
            )
        }
        _uiState.update {
            it.copy(
                notes = notes , isLoading = false , isInterNetAvailable = false
            )
        }
    }


    private suspend fun  fetchOnlineNotes() {
        val  response = userDataRepository.getAllUserNotes()
        _uiState.update {
            it.copy(notes = response.body() ?: emptyList() , isInterNetAvailable =  true , isLoading = false)
        }
    }

    fun resetSnackBar() {
        _snackBarMessage.value = null
    }


    fun refreshNotes() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        Log.e(LOGGING_OF_APP ,"Refreshing notes")
        viewModelScope.launch{
            checkForInternetAndFetchNotes()
        }
        Log.e(LOGGING_OF_APP ,"refrshign just finsihed")
    }


    // helper function to check for internet
    private fun checkForInternet() : Boolean {
        val isOnline =  isInternetAvailable(context)
        _uiState.update { it.copy(isInterNetAvailable =  isOnline) }
        return  isOnline
    }
}


data class  NotesListScreenUiState(
    val  detailedNote: DetailedNote? = null,
    val isDetailedNoteVisible : Boolean = false ,
    val detailedNoteMessage : String = "" ,
    val notes : List<UpdatedShortNote> = emptyList() ,
    val isInterNetAvailable: Boolean  = true ,
    val isLoading : Boolean = true ,
    val errorMessage : String = "" ,
    val suggestionsOfNotes : List<UpdatedShortNote> = emptyList() ,
    val searchWord : String  = ""
)
