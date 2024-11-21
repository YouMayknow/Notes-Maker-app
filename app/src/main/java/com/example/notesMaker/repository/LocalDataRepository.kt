package com.example.notesMaker.repository

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface LocalDataRepository  {
     fun  getAllData() : Flow<List<Note>>
    suspend fun update(note : Note)
    suspend fun save(note: Note)
    suspend fun saveNoteId(heading : String ,noteId : Int)
    suspend fun getNoteId(heading : String): Int?
    suspend fun getNoteWithNoteId(noteId: Int) : Note
    suspend fun createNoteAndGetId(note: Note) : Int
    suspend fun addSyncedState(isSynced: Boolean , heading: String)
    suspend fun getUnSyncedNotes() : List<Note>
     fun getSearchedWord(word: String) :Flow<List<Note>>
}


class OfflineUserDataRepository(val noteDao: NoteDao ) : LocalDataRepository {
     override fun getAllData() :  Flow<List<Note>>{
      return  noteDao.getAllNotes()
    }
    override suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun save(note: Note) {
        noteDao.saveNote(note)
    }

    override suspend fun saveNoteId(heading: String, noteId: Int) {
        noteDao.saveNoteId(heading ,noteId)
    }

    override suspend fun getNoteId(heading: String) : Int? {
      return  noteDao.getNoteID(heading )
    }

    override suspend fun getNoteWithNoteId(noteId: Int): Note {
        return noteDao.getNoteWithNoteId(noteId)
    }

    override suspend fun createNoteAndGetId(note: Note): Int {
        return noteDao.createNoteAndGetId(note)
    }

    override suspend fun addSyncedState(isSynced: Boolean, heading: String) {
        return noteDao.addSyncedState(isSynced ,heading )
    }

    override suspend fun getUnSyncedNotes(): List<Note> {
        return noteDao.getUnSyncedNotes()
    }

      override fun getSearchedWord(word: String): Flow<List<Note>>{
        return noteDao.getSuggestedNotes(word)
    }
}


/* here we are creating a class with unique noteid so that it will create conflict on
on editing the same note and this enable it to distinguish between edit and update on it's own.
 */

@Entity()
data class Note(
    @PrimaryKey(autoGenerate = true ) val id: Int = 0,
    @ColumnInfo val heading: String,
    @ColumnInfo val content: String,
    @ColumnInfo val noteId: Int? = null,
    @ColumnInfo val createdAt: String = "",
    @ColumnInfo val lastUpdated: String? = null,
    @ColumnInfo val version: Int = 1,
    @ColumnInfo val isSynced: Boolean = false,
)

@Dao
interface NoteDao{
    @Query("SELECT * FROM Note")
     fun  getAllNotes() : Flow<List<Note>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Insert()
    suspend fun saveNote(note: Note)

    @Query("UPDATE note SET noteId = :noteId WHERE heading = :heading ")
    suspend fun saveNoteId(heading: String? , noteId: Int)

    @Query("SELECT id FROM note WHERE heading = :heading")
    suspend fun  getNoteID(heading: String) : Int?

    @Query("SELECT * FROM note WHERE noteId = :noteId")
    suspend fun  getNoteWithNoteId(noteId: Int) : Note
    @Query("UPDATE note SET isSynced = :isSynced WHERE heading = :heading")
    suspend  fun addSyncedState(isSynced: Boolean , heading: String)

    @Query("SELECT * FROM note WHERE isSynced = 0") // here 0 means false whereas 1 means true
    suspend fun getUnSyncedNotes() : List<Note>

    suspend fun  createNoteAndGetId(note: Note) : Int {
        saveNote(note)
        return getNoteID(note.heading) ?: -1
    }
    @Query("SELECT * FROM note WHERE heading LIKE :word OR content LIKE :word")
     fun getSuggestedNotes(word: String) : Flow<List<Note>>
}
@Database(entities = [Note::class], version = 12 , exportSchema = false  )
abstract class NoteDatabase : RoomDatabase() {
    abstract  fun noteDao() : NoteDao
    companion object {
        @Volatile
        private  var Instance : NoteDatabase? = null
        fun getDatabase(context : Context ) : NoteDatabase{
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context , NoteDatabase::class.java , "item_database"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

