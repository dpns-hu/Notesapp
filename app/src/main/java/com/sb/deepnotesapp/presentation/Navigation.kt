package com.sb.deepnotesapp.presentation

sealed class Screen(val route: String) {
    object NotesListScreen : Screen("notes_list_screen")
    object AddEditNotesScreen : Screen("add_edit_screen")
}