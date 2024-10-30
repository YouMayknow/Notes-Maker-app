package com.example.notesMaker.repository

import javax.inject.Inject


interface LocalDataRepository  {
    suspend fun  getAllData() : List<Note>
    suspend fun update(note : Note)
    suspend fun save(note : Note)
    suspend fun saveNoteId(heading : String ,noteId : Int)

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
}

