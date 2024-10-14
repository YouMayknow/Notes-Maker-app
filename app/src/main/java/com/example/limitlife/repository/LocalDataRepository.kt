package com.example.limitlife.repository

interface LocalDataRepository  {
    suspend fun  getAllData() : List<Note>
    suspend fun update(note : Note)
    suspend fun save(note : Note)
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

}

