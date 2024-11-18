package com.example.notesMaker.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class OfflineUserTokenRepository(
    private  val dataStore : DataStore<Preferences>

) {
   private companion object {
        val TOKEN = stringPreferencesKey("user_token")
    }
    suspend fun saveUserToken (userToken : String) {
        dataStore.edit { token ->
            token[TOKEN] =  userToken
        }
    }
    val userToken : kotlinx.coroutines.flow.Flow<String> = dataStore.data
        .catch {
            if ( it is IOException){
                emit(emptyPreferences())
            }
            else {
                throw it
            }
        }
        .map { token ->
        token[TOKEN] ?: ""
    }
}


class UnSyncedUserNoteIdRepository(
    private  val dataStore : DataStore<Preferences>
) {
    private companion object {
        val UNSYNCED_NOTES = stringPreferencesKey("unSynced_notes")
    }
    suspend fun saveUnSyncedNoteId(noteId: String) {
        dataStore.edit { preferences ->
            val currentNotes = preferences[UNSYNCED_NOTES]?.split(",")?.toMutableSet() ?: mutableSetOf()
            currentNotes.add(noteId)
            preferences[UNSYNCED_NOTES] = currentNotes.joinToString(",")
        }
    }
    val unSyncedNotes: kotlinx.coroutines.flow.Flow<Set<String>> = dataStore.data
        .catch {
            if ( it is IOException){
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[UNSYNCED_NOTES]?.split(",")?.filter { it.isNotEmpty() }?.toSet() ?: emptySet()
        }
}