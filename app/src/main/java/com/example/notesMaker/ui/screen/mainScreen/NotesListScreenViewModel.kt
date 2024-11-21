package com.example.notesMaker.ui.screen.mainScreen



import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesMaker.network.DetailedNote
import com.example.notesMaker.network.UpdatedShortNote
import com.example.notesMaker.repository.NetworkUserDataRepository
import com.example.notesMaker.repository.Note
import com.example.notesMaker.repository.OfflineUserDataRepository
import com.example.notesMaker.utils.isInternetAvailable
import com.example.notesMaker.worker.LOGGING_OF_APP
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class NotesListScreenViewModel @Inject constructor(
    private val offlineUserDataRepository: OfflineUserDataRepository,
    private val onlineUserDataRepository: NetworkUserDataRepository,
    @ApplicationContext val context: Context,
) : ViewModel() {
    private  val _uiState  = MutableStateFlow(NotesListScreenUiState())
      val uiState  :  StateFlow<NotesListScreenUiState> = _uiState.asStateFlow()

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage : StateFlow<String?> =  _snackBarMessage

    private val _searchUiState = MutableStateFlow(NotesSearchUiState())
    val searchUiState : StateFlow<NotesSearchUiState> = _searchUiState.asStateFlow()
    private val _query = MutableStateFlow("")
    val query : StateFlow<String> = _query.asStateFlow()
    init {
        if (uiState.value.isInterNetAvailable){
            _snackBarMessage.value = "Connected to the server"
        } else {
            _snackBarMessage.value = "Performing in Offline Mode"
        }

    }

    var notes : Flow<List<UpdatedShortNote>> = offlineUserDataRepository.noteDao.getAllNotes()
        .filterNotNull()
        .map {
            it.map {
                UpdatedShortNote(
                    content = it.content ,
                    heading = it.heading,
                    id = it.noteId ?: -1,
                    localNoteId = it.id,
                    version = it.version
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    private  set

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults : Flow<List<Note>> =
        query
            .debounce(500)
            .flatMapLatest { query ->
                if (query.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    Log.e(LOGGING_OF_APP , "searching in database function called ${query}")
               val notes =  offlineUserDataRepository.noteDao.getSuggestedNotes("%$query%")
                    Log.e(LOGGING_OF_APP , "searching in database function called ${notes.map { it.map { it.heading } }}")
                    return@flatMapLatest notes
                }
            }
    fun updateSearchWord(word : String){
        _searchUiState
            .update {
            it.copy(searchWord = word)
        }
        _query.value = word
        Log.e(LOGGING_OF_APP , "search word updated to : $word")
    }
    fun clearSearchWord(){
        _query.value = ""
    }

    private fun syncWithDatabase()  = viewModelScope.launch{
        val isOnline = checkForInternet()
        if (isOnline){
            try {
                fetchOnlineNotes()
            }catch (e : Exception){
                Log.e("problem" , "$e")
                catchingException(e)
            }
        } else {
            _snackBarMessage.value = "Can't perform without internet"
        }
     }

    fun deleteNote(noteId : Int) = viewModelScope.launch {
        val isOnline = checkForInternet()
        if (isOnline){
            runCatching { onlineUserDataRepository.deleteSelectedNote(noteId) }
                .onFailure {
                    Log.e("problem" , "$it")
                    catchingException(it)
                }
                .onSuccess {
                  _snackBarMessage.value = if (it.isSuccessful)  "Note deleted"  else it.errorBody()?.string()
                }
        } else {
            _snackBarMessage.value = "Can't perform without internet"
        }
    }


    fun getDetailsOfNote(noteId: Int) = viewModelScope.launch {
       val isOnline = checkForInternet()
        if (isOnline){
            try {
                val response =  onlineUserDataRepository.getSelectedNote(noteId)
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



    private suspend fun  fetchOnlineNotes() {
        val  response = onlineUserDataRepository.getAllUserNotes()
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
            syncWithDatabase()
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
    val isLoading : Boolean = false ,
    val errorMessage : String = "" ,
)
data class NotesSearchUiState(
    val isSearching : Boolean = false ,
    val searchWord : String = "" ,
    val suggestionsOfNotes : List<UpdatedShortNote> = emptyList()
)
