package com.example.notesapp.repository

import androidx.lifecycle.LiveData
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteDao

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    fun getNoteById(id: Int): LiveData<Note?> {
        return noteDao.getNoteById(id)
    }

    suspend fun insert(note: Note): Long {
        return noteDao.insertNote(note)
    }

    suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun delete(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun deleteById(id: Int) {
        noteDao.deleteNoteById(id)
    }
}
