package com.example.notesMaker.ui.screen.entryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesMaker.repository.NetworkUserDataRepository
import com.example.notesMaker.repository.OfflineUserTokenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EntryScreenViewModel   @Inject constructor(
    private  val userDataRepository: NetworkUserDataRepository,
    private val userTokenRepository: OfflineUserTokenRepository ,
) : ViewModel() {
    private var _uiState = MutableStateFlow(EntryScreenUiState())
    fun registerUser(email: String , password: String , fullName : String) = viewModelScope.launch{
        userDataRepository.registerUser(
            username = email ,
            password = password ,
        )
    }
}

private data class EntryScreenUiState(
    val email : String = "" ,
    val password : String = "" ,

)
