package com.example.notesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notesapp.ui.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    navController: NavController,
    viewModel: NoteViewModel,
    noteId: Int
) {
    val isEditing = noteId != -1
    val existingNote by if (isEditing) {
        viewModel.getNoteById(noteId).observeAsState(initial = null)
    } else {
        remember { mutableStateOf(null) }
    }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(existingNote) {
        if (existingNote != null && !initialized) {
            title = existingNote?.title ?: ""
            content = existingNote?.content ?: ""
            initialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Note" else "Add Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isEditing) {
                        IconButton(onClick = {
                            viewModel.deleteNoteById(noteId) {
                                navController.popBackStack()
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Note")
                        }
                    }
                    IconButton(onClick = {
                        if (isEditing) {
                            viewModel.updateNote(noteId, title, content) {
                                navController.popBackStack()
                            }
                        } else {
                            viewModel.insertNote(title, content) {
                                navController.popBackStack()
                            }
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save Note")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
