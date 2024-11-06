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
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now

interface LocalDataRepository  {
    suspend fun  getAllData() : List<Note>
    suspend fun update(note : Note)
    suspend fun save(note : Note)
    suspend fun saveNoteId(heading : String ,noteId : Int)
    suspend fun getNoteId(heading : String): Int?

}


class OfflineUserDataRepository(val noteDao: NoteDao ) : LocalDataRepository {
    override suspend fun getAllData() : List<Note> {
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
}


/* here we are creating a class with unique noteid so that it will create conflict on
on editing the same note and this enable it to distinguish between edit and update on it's own.
 */

@Entity()
data class Note(
    @PrimaryKey(autoGenerate = true ) val id : Int = 0  ,
    @ColumnInfo val heading : String? ,
    @ColumnInfo val content : String? ,
    @ColumnInfo val noteId : Int? = null ,
    @ColumnInfo val lastEdited : String = now().toString()
)

@Dao
interface NoteDao{
    @Query("SELECT * FROM Note")
    suspend fun  getAllNotes() : List<Note>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Insert()
    suspend fun saveNote(note: Note)

    @Query("UPDATE note SET noteId = :noteId WHERE heading = :heading ")
    suspend fun saveNoteId(heading: String? , noteId: Int)

    @Query("SELECT id FROM note WHERE heading = :heading")
    suspend fun  getNoteID(heading: String) : Int?
}


@Database(entities = [Note::class], version = 6  , exportSchema = false  )
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

