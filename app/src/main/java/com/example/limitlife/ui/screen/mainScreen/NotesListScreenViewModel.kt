package com.example.limitlife.ui.screen.mainScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NotesListScreenViewModel @Inject constructor(
   // private val offlineUserTokenRepository: OfflineUserTokenRepository
) : ViewModel() {

//  //  val getUserToken : StateFlow<NotesListScreenUiState> = offlineUserTokenRepository.userToken
//        .filterNotNull()
//        .map {
//       // NotesListScreenUiState(userToken =  it)
//    }.stateIn(
//        viewModelScope , SharingStarted.WhileSubscribed(5_000L) ,  NotesListScreenUiState() ,
//    )
}

data class NotesListScreenUiState(
    val userToken : String = ""
)
