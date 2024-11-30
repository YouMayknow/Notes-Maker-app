package com.example.notesMaker.ui.screen.notification

import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesMaker.network.UpdatedShortNote
import com.example.notesMaker.repository.OfflineUserDataRepository
import com.example.notesMaker.repository.WorkManagerRepository
import com.example.notesMaker.worker.LOGGING_OF_APP
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificationScreenViewModel  @Inject constructor(
    val workManagerRepository: WorkManagerRepository,
    val localDataRepository: OfflineUserDataRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState= MutableStateFlow(NotificationScreenUiState())
    val uiState = _uiState.asStateFlow()
    fun checkForUnSyncedNotes() = viewModelScope.launch {
        Log.e(LOGGING_OF_APP, "Checking for unsynced notes")
        _uiState.update { it.copy(isSyncing = true) }
        val unSyncedNotes =  localDataRepository.getUnSyncedNotes()
        if (unSyncedNotes.isEmpty()){
            Log.e(LOGGING_OF_APP, "no unsynced notes found")
            _uiState.update { it.copy(isSyncedFailed = false , isSyncing = false) }
        }
        else {
            _uiState.update { it.copy(isSyncedFailed = true)}
            val notification =   createRetryNotification()
            Log.e(LOGGING_OF_APP, "Notification created")
            _uiState.update { it.copy(notifications = listOf(notification) , isSyncing = false) }
        }
    }
    fun createRetryNotification() : Notification {
        Log.e(LOGGING_OF_APP, "Creating a notification along with retrying the notes")
        return  Notification(
            heading = "Sync failed" ,
            body = "Failed to sync notes with id " ,
            image = Icons.Default.Refresh ,
            time = "10 minutes before" ,
            action = {
                retryAction()
            }
        )
    }
    fun retryAction() = viewModelScope.launch(){
        val unSyncedNotes = localDataRepository.getUnSyncedNotes()
        Log.e(LOGGING_OF_APP, "worker came to retry function")
        for (unSyncedNote in unSyncedNotes) {
            Log.e(LOGGING_OF_APP, "Retrying to sync notes with id ${unSyncedNote.localNoteId} and heading : ${unSyncedNote.heading}")
            viewModelScope.launch{
                if (unSyncedNote.noteId == null || unSyncedNote.noteId == -1) {
                    Log.e(LOGGING_OF_APP, "Retrying to save notes with heading ${unSyncedNote.heading}")
                    workManagerRepository.saveNote(unSyncedNote.heading , unSyncedNote.content)
                } else {
                    Log.e(LOGGING_OF_APP, "Retrying to update notes with heading ${unSyncedNote.heading}")
                    workManagerRepository.updateNote(
                        UpdatedShortNote(
                            content = unSyncedNote.content  ,
                            heading = unSyncedNote.heading  ,
                            id = unSyncedNote.noteId  ,
                            lastUpdated = unSyncedNote.lastUpdated ?: "" ,
                            version = unSyncedNote.version
                        )
                    )
                }
            }
        }
    }
}

data class NotificationScreenUiState(
    val isSyncing : Boolean? = null ,
    val notifications : List<Notification> = emptyList() ,
    val isSyncedFailed : Boolean? = null ,
)