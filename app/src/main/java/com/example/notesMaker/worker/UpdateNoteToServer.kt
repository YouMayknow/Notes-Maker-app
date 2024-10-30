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
import com.example.notesMaker.utils.KEY_NOTE_ID
import com.example.notesMaker.utils.KEY_WORKER_OUTPUT_DATA
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltWorker
class  UpdateNoteToServer  @AssistedInject constructor(
  @Assisted  ctx : Context ,
  @Assisted  params : WorkerParameters ,
    val userDataRepository: NetworkUserDataRepository
) : CoroutineWorker(ctx , params) {
    val heading = inputData.getString(KEY_NOTE_HEADING)
    val content = inputData.getString(KEY_NOTE_CONTENT)
    val id = inputData.getString(KEY_NOTE_ID)
    val updatedShortNote = UpdatedShortNote(
        heading = heading ?: "" ,
        content = content ?: "" ,
        id = id?.toInt() ?: -1
    )
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO){
            val request = kotlin.runCatching {  userDataRepository.updateNote(updatedShortNote)}
            request.onSuccess {
                 Result.success(workDataOf(KEY_WORKER_OUTPUT_DATA to request.getOrNull()?.message()))
            }
             Result.failure(workDataOf(KEY_WORKER_OUTPUT_DATA to request.exceptionOrNull()?.message))
        }
    }
}