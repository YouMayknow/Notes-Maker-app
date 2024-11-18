package com.example.notesMaker.ui.screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesMaker.repository.NetworkUserDataRepository
import com.example.notesMaker.repository.OfflineUserTokenRepository
import com.example.notesMaker.utils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private  val userTokenRepository: OfflineUserTokenRepository ,
    private val userDataRepository: NetworkUserDataRepository ,
    @ApplicationContext val context: Context ,
) : ViewModel() {
    private  val _uiState = MutableStateFlow(HomeScreenUiState())
      val uiState : StateFlow<HomeScreenUiState> = _uiState.asStateFlow()



    fun checkLoginRequirement() = viewModelScope.launch {
        if (!isInternetAvailable(context = context)) {
            _uiState.update {
                it.copy(isNetworkAvailable =  false)
            }
        } else {
            _uiState.update {
                it.copy(isNetworkAvailable = true)
            }
            val token = userTokenRepository.userToken.first()
            _uiState.update {
                it.copy(isNewUser = token.isEmpty())
            }
         val response =  runCatching { userDataRepository.isTokenValid() }
                response.onSuccess {
                    _uiState.update {
                        it.copy(isTokenValid = response.getOrNull()?.isSuccessful == true)
                    }
                }
                .onFailure {
                    Log.e("problem", "$it")
                    _uiState.update {
                        it.copy(isTokenValid = false)
                    }
                }
        }
    }
}
data class HomeScreenUiState (
        val isNewUser : Boolean? = null ,
        val isNetworkAvailable : Boolean? = null ,
        val isTokenValid : Boolean? = null
        )