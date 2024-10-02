package com.example.limitlife.ui.screen.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.limitlife.repository.OfflineUserTokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
