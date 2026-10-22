package com.example.notesapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteDatabase
import com.example.notesapp.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes
    }

    fun getNoteById(id: Int): LiveData<Note?> {
        return repository.getNoteById(id)
    }

    fun insertNote(title: String, content: String, onInserted: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = Note(title = title, content = content, timestamp = System.currentTimeMillis())
            repository.insert(newNote)
            viewModelScope.launch(Dispatchers.Main) {
                onInserted()
            }
        }
    }

    fun updateNote(id: Int, title: String, content: String, onUpdated: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedNote = Note(id = id, title = title, content = content, timestamp = System.currentTimeMillis())
            repository.update(updatedNote)
            viewModelScope.launch(Dispatchers.Main) {
                onUpdated()
            }
        }
    }

    fun deleteNote(note: Note, onDeleted: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(note)
            viewModelScope.launch(Dispatchers.Main) {
                onDeleted()
            }
        }
    }

    fun deleteNoteById(id: Int, onDeleted: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteById(id)
            viewModelScope.launch(Dispatchers.Main) {
                onDeleted()
            }
        }
    }
}
