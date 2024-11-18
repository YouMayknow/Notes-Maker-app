package com.example.notesMaker.broadcaster

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notesMaker.repository.OfflineUserDataRepository
import com.example.notesMaker.repository.WorkManagerRepository
import com.example.notesMaker.utils.RetryBroadcastReceiver
import com.example.notesMaker.worker.LOGGING_OF_APP
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Inject


class RetryReceiver @Inject  constructor(
    val workManagerRepository: WorkManagerRepository,
    val dataRepository: OfflineUserDataRepository ,
) : RetryBroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(LOGGING_OF_APP, "onReceive: flow had reached to the retry receiver")
        intent?.getIntArrayExtra("failedNoteIds").let {
            Log.e(LOGGING_OF_APP, "onReceive: noteId is ${it?.toList()}")
        }
        retryNotification(intent?.getIntArrayExtra("failedNoteIds")?.toList() ?: emptyList())
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun retryNotification(failedNoteIds : List<Int>) {
        for (failedNoteId in failedNoteIds) {
        }
    }
}