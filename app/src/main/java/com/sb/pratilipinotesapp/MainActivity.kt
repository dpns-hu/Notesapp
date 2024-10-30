package com.sb.pratilipinotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sb.pratilipinotesapp.presentation.AddEditNote.AddEditNoteScreen
import com.sb.pratilipinotesapp.presentation.NotesList.NoteListScreen
import com.sb.pratilipinotesapp.presentation.NotesList.NoteListViewModel
import com.sb.pratilipinotesapp.presentation.Screen
import com.sb.pratilipinotesapp.presentation.theme.PratilipiNotesAppTheme
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

