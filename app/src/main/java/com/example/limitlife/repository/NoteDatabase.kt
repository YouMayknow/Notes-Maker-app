package com.example.limitlife.repository

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.room.Upsert
import org.w3c.dom.Text

/* here we are creating a class with unique noteid so that it will create conflict on
on editing the same note and this enable it to distinguish between edit and update on it's own.
 */

@Entity(
    indices = [Index(value = ["noteId"], unique = true)]
)
data class Note(
    @PrimaryKey(autoGenerate = true ) val id : Int = 0  ,
    @ColumnInfo val heading : String? ,
    @ColumnInfo val content : String? ,
    @ColumnInfo val noteId : Int?
)

@Dao
interface NoteDao{
    @Query("SELECT * FROM Note")
   suspend fun  getAllNotes() : List<Note>

   @Update
   suspend fun updateNote(note: Note)


   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun saveNote(note: Note)
}

@Database(entities = [Note::class], version = 3 )
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
