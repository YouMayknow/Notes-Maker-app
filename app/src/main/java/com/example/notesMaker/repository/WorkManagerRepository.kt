package com.example.notesMaker.repository

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.notesMaker.network.UpdatedShortNote
import com.example.notesMaker.utils.KEY_NOTE_CONTENT
import com.example.notesMaker.utils.KEY_NOTE_CREATED_AT
import com.example.notesMaker.utils.KEY_NOTE_HEADING
import com.example.notesMaker.utils.KEY_NOTE_ID
import com.example.notesMaker.worker.LOGGING_OF_APP
import com.example.notesMaker.worker.SaveNoteToServer
import com.example.notesMaker.worker.UpdateNoteToServer
import java.time.Instant.now


interface  WorkManagerRepository {
    fun saveNote (heading : String , content : String)
    fun updateNote(updatedShortNote: UpdatedShortNote)
}

class NotesWorkManagerRepository(context: Context) : WorkManagerRepository  {
   private val workManager = WorkManager.getInstance(context)
    override fun saveNote(heading : String , content : String ) {
        val saveNoteToServerBuilder = OneTimeWorkRequestBuilder<SaveNoteToServer>()
        val inputData = workDataOf(
            KEY_NOTE_CONTENT to content ,
            KEY_NOTE_HEADING to heading
        )
        saveNoteToServerBuilder.setInputData(inputData)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
        saveNoteToServerBuilder.setConstraints(constraints.build())
        workManager.enqueue(saveNoteToServerBuilder.build())
    }

    override fun updateNote(updatedShortNote: UpdatedShortNote) {
        val updateNoteBuilder = OneTimeWorkRequestBuilder<UpdateNoteToServer>()
        val inputData = workDataOf(
            KEY_NOTE_CONTENT to updatedShortNote.content  ,
            KEY_NOTE_HEADING to updatedShortNote.heading ,
            "KEY_NOTE_ID" to updatedShortNote.id
        )
        Log.e(LOGGING_OF_APP , "Sending the note to the server with the heading : ${updatedShortNote.heading} and content : ${updatedShortNote.content}")
        Log.e(LOGGING_OF_APP , "Sending note toe the worrker where heading : $KEY_NOTE_HEADING and content : $KEY_NOTE_CONTENT")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        updateNoteBuilder.setInputData(inputData)
            .setConstraints(constraints)
        workManager.enqueueUniqueWork(KEY_NOTE_ID.toString(), ExistingWorkPolicy.REPLACE , updateNoteBuilder.build() )
    }
}