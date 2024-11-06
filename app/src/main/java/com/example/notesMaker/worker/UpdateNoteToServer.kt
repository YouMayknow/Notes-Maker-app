package com.example.notesMaker.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.notesMaker.network.UpdatedShortNote
import com.example.notesMaker.repository.NetworkUserDataRepository
import com.example.notesMaker.utils.KEY_NOTE_CONTENT
import com.example.notesMaker.utils.KEY_NOTE_HEADING
import com.example.notesMaker.utils.KEY_WORKER_OUTPUT_DATA
import com.example.notesMaker.utils.basicNotificationFramework
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@HiltWorker
class  UpdateNoteToServer  @AssistedInject constructor(
  @Assisted  ctx : Context ,
  @Assisted  params : WorkerParameters ,
    val userDataRepository: NetworkUserDataRepository
) : CoroutineWorker(ctx , params) {
    val heading = inputData.getString(KEY_NOTE_HEADING)
    val content = inputData.getString(KEY_NOTE_CONTENT)
    val id = inputData.getInt("KEY_NOTE_ID",0)
    val updatedShortNote = UpdatedShortNote(
        heading = heading ?: "" ,
        id = id ,
        content = content ?: "" ,
    )
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO){
            val request = kotlin.runCatching {  userDataRepository.updateNote(updatedShortNote)}
            request.onSuccess {
                return@withContext Result.success(workDataOf(KEY_WORKER_OUTPUT_DATA to request.getOrNull()?.message()))
            }
            Log.e(LOGGING_OF_APP , "worker is failed to get the value so living ahead  for notification for the note with the local noteID  : $id due to : ${request.exceptionOrNull()}now attempting localid . ")
            basicNotificationFramework( applicationContext , "Sync failed","unable to update : $heading", id)
                return@withContext Result.failure(workDataOf(KEY_WORKER_OUTPUT_DATA to request.exceptionOrNull()?.message))
        }
    }
}