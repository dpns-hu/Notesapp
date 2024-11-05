package com.sb.deepnotesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sb.deepnotesapp.presentation.AddEditNote.AddEditNoteScreen
import com.sb.deepnotesapp.presentation.NotesList.NoteListScreen
import com.sb.deepnotesapp.presentation.theme.PratilipiNotesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PratilipiNotesAppTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesListScreen.route
                    ) {
                        composable(route = Screen.NotesListScreen.route) {
                            NoteListScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditNotesScreen.route +
                                    "?noteTitle={noteTitle}&noteContent={noteContent}&noteId={noteId}&notePosition={notePosition}",
                                    arguments = listOf(
                                navArgument("noteTitle") { type = NavType.StringType },
                                navArgument("noteContent") { type = NavType.StringType },
                                navArgument("noteId") { type = NavType.StringType },
                                navArgument("notePosition") { type = NavType.StringType },
                            )
                        ) { backStackEntry ->
                            val noteTitle = backStackEntry.arguments?.getString("noteTitle") ?: ""
                            val noteContent =
                                backStackEntry.arguments?.getString("noteContent") ?: ""
                                val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                                val notePosition = backStackEntry.arguments?.getString("notePosition") ?: ""
                            AddEditNoteScreen(
                                navController = navController,
                                noteTitle = noteTitle,
                                noteContent = noteContent,
                                noteId = noteId,
                                notePosition = notePosition
                            )
                        }
                    }
                }
            }
        }
    }
}

