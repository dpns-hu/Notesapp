package com.sb.pratilipinotesapp.presentation.NotesList

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.sb.pratilipinotesapp.presentation.NotesList.components.DragAndDropList
import com.sb.pratilipinotesapp.presentation.Screen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteListScreen(
    navController: NavController,
    noteViewModel: NoteListViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
     val pagedNotes = noteViewModel.pagingData.collectAsLazyPagingItems()



    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditNotesScreen.route + "?noteTitle=&noteContent=&noteId=&notePosition=")
                },
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.padding(bottom = 35.dp)

            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My notes",
                    style = MaterialTheme.typography.h4,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(top = 40.dp)
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
            DragAndDropList(itemList = pagedNotes,
                onMove = { fromIndex, toIndex ->
                    Log.d("ItemsOnMove","$fromIndex to $toIndex")
                    val note1 = pagedNotes[fromIndex]
                    val note2 = pagedNotes[toIndex]
                     if (note1 != null) {
                        if (note2 != null) {
                            noteViewModel.reorderNote(note1, note2)
                        }
                    }
                    noteViewModel.getPagedNotes()
                }, onDeleteClick = { note -> noteViewModel.deleteNote(note) },
                onNoteClick = { note ->
                    navController.navigate(Screen.AddEditNotesScreen.route +
                            "?noteTitle=${note.title}&noteContent=${note.content}&noteId=${note.id}&notePosition=${note.position}")

                }
            )


        }
    }
}

