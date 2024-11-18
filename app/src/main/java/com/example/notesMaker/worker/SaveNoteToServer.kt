package com.example.notesMaker.worker

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.notesMaker.network.ShortNote
import com.example.notesMaker.repository.NetworkUserDataRepository
import com.example.notesMaker.repository.OfflineUserDataRepository
import com.example.notesMaker.repository.UnSyncedUserNoteIdRepository
import com.example.notesMaker.utils.KEY_NOTE_CONTENT
import com.example.notesMaker.utils.KEY_NOTE_HEADING
import com.example.notesMaker.utils.KEY_NOTE_ID_STRING
import com.example.notesMaker.utils.KEY_WORKER_OUTPUT_DATA
import com.example.notesMaker.utils.basicNotificationFramework
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant.now


/**The below worker will save the note to the server , getting
 * noteId in response and then save the note id to the database
 * returning the success of the worker
 */


const val  LOGGING_OF_APP = "logging"

@HiltWorker
class SaveNoteToServer @AssistedInject constructor (
    @Assisted ctx : Context ,
  @Assisted   parameters: WorkerParameters ,
   private val localDataRepository: OfflineUserDataRepository  ,
    private val userDataRepository: NetworkUserDataRepository ,
    private val unSyncedUserNoteIdRepository: UnSyncedUserNoteIdRepository
    ) : CoroutineWorker(ctx ,parameters)
{
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun doWork(): Result {
         val heading = inputData.getString(KEY_NOTE_HEADING)
          val content = inputData.getString(KEY_NOTE_CONTENT)
         val shortNote = ShortNote(
            content = content ?: "" ,
        heading = heading ?: "" ,
        dateCreated = now().toString() ,
        )
        return withContext(Dispatchers.IO){
            val request = runCatching { userDataRepository.createNewNote(shortNote) }
            if (request.isSuccess) {
                localDataRepository.saveNoteId(heading = heading ?: "" , noteId = request.getOrNull()?.body()?.noteID ?:-1 ,)
                localDataRepository.addSyncedState(true , heading ?: "")
              return@withContext  Result.success(workDataOf(KEY_WORKER_OUTPUT_DATA to request.getOrNull()?.message()))
            } else {
                val localNoteId = localDataRepository.getNoteId(heading ?: "")
                Log.e(LOGGING_OF_APP , "worker is failed to get the value so living ahead  for notification for the note with the local noteID  : ${request.exceptionOrNull()} now attempting localid . ")
                basicNotificationFramework( applicationContext , "Sync failed","unable to sync : $heading" , localNoteId ?: -1)
                // here we will implement the logic to save the note id to the database so that we can save it later
              return@withContext Result.failure(workDataOf( KEY_WORKER_OUTPUT_DATA to request.exceptionOrNull()?.message, KEY_NOTE_ID_STRING to localNoteId))
            }
        }
    }
}

