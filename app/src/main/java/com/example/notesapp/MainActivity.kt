package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notesapp.ui.screens.AddEditNoteScreen
import com.example.notesapp.ui.screens.NotesListScreen
import com.example.notesapp.ui.screens.ViewNoteScreen
import com.example.notesapp.ui.theme.NotesAppTheme
import com.example.notesapp.ui.viewmodel.NoteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                val navController = rememberNavController()
                val viewModel: NoteViewModel = viewModel()
                NavHost(navController = navController, startDestination = "notes_list") {
                    composable("notes_list") {
                        NotesListScreen(navController = navController, viewModel = viewModel)
                    }
                    composable("add_note") {
                        AddEditNoteScreen(navController = navController, viewModel = viewModel, noteId = -1)
                    }
                    composable(
                        route = "view_note/{noteId}",
                        arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1
                        ViewNoteScreen(navController = navController, viewModel = viewModel, noteId = noteId)
                    }
                    composable(
                        route = "edit_note/{noteId}",
                        arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1
                        AddEditNoteScreen(navController = navController, viewModel = viewModel, noteId = noteId)
                    }
                }
            }
        }
    }
}
