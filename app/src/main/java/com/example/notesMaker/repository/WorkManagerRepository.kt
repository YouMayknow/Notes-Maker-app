package com.example.notesMaker.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.notesMaker.network.UpdatedShortNote
import com.example.notesMaker.utils.KEY_NOTE_CONTENT
import com.example.notesMaker.utils.KEY_NOTE_HEADING
import com.example.notesMaker.utils.KEY_NOTE_ID
import com.example.notesMaker.worker.SaveNoteToServer
import com.example.notesMaker.worker.UpdateNoteToServer


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
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        updateNoteBuilder.setInputData(inputData)
            .setConstraints(constraints)
        workManager.enqueueUniqueWork(KEY_NOTE_ID.toString(), ExistingWorkPolicy.REPLACE , updateNoteBuilder.build() )
    }
}