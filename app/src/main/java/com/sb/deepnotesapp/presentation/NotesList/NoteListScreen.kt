package com.sb.deepnotesapp.presentation.NotesList

import android.annotation.SuppressLint
import android.app.Activity
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.sb.deepnotesapp.R
import com.sb.deepnotesapp.presentation.NotesList.components.NoteItem
import com.sb.deepnotesapp.presentation.Screen
import com.sb.deepnotesapp.presentation.theme.green
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteListScreen(
    navController: NavController,
    noteViewModel: NoteListViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    val pagedNotes = noteViewModel.pagingData.collectAsLazyPagingItems()


    val view = LocalView.current
    val lazyListState = rememberLazyListState()
    val activity = LocalView.current.context as Activity
    val backgroundArgb = green.toArgb()
    activity.window.statusBarColor = backgroundArgb
    val reorderableLazyListState =
        rememberReorderableLazyListState(lazyListState) { from, to ->
            val fromIndex = lazyListState.layoutInfo.visibleItemsInfo.indexOf(from)
            val toIndex = lazyListState.layoutInfo.visibleItemsInfo.indexOf(to)

            if (fromIndex >= 0 && toIndex >= 0) {
                val note1 =
                    pagedNotes.itemSnapshotList[fromIndex]
                val note2 =
                    pagedNotes.itemSnapshotList[toIndex]

                if (note1 != null && note2 != null) {
                    noteViewModel.reorderNote(
                        note1,
                        note2
                    )
                }
            }
            noteViewModel.getPagedNotes()
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
        }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Notes",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                backgroundColor = green,
                modifier = Modifier.statusBarsPadding()

            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditNotesScreen.route + "?noteTitle=&noteContent=&noteId=&notePosition=")
                },
                backgroundColor = green,
                modifier = Modifier.padding(bottom = 35.dp),


                ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.White,
                    contentDescription = "Add note"
                )
            }
        },
        scaffoldState = scaffoldState,
    ) {
        if (pagedNotes.itemSnapshotList.items.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.empty_notes_text),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        } else {


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

//            DragAndDropList(itemList = pagedNotes,
//                onMove = { fromIndex, toIndex ->
//                    Log.d("ItemsOnMove","$fromIndex to $toIndex")
//                    val note1 = pagedNotes[fromIndex]
//                    val note2 = pagedNotes[toIndex]
//                     if (note1 != null) {
//                        if (note2 != null) {
//                            noteViewModel.reorderNote(note1, note2)
//                        }
//                    }
//                    noteViewModel.getPagedNotes()
//                }, onDeleteClick = { note -> noteViewModel.deleteNote(note) },
//                onNoteClick = { note ->
//                    navController.navigate(Screen.AddEditNotesScreen.route +
//                            "?noteTitle=${note.title}&noteContent=${note.content}&noteId=${note.id}&notePosition=${note.position}")
//
//                }
//            )


                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(pagedNotes.itemSnapshotList.items.size, key = {
                            pagedNotes[it]?.id
                                ?: 0
                        }) { index ->
                            val note = pagedNotes[index]
                            note?.let {
                                ReorderableItem(
                                    reorderableLazyListState,
                                    key = it.id
                                ) { isDragging ->
                                    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)

                                    Surface(shadowElevation = elevation) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            NoteItem(note = note,
                                                onDeleteClick = { noteViewModel.deleteNote(note) },
                                                onNoteClick = {
                                                    navController.navigate(
                                                        Screen.AddEditNotesScreen.route +
                                                                "?noteTitle=${note.title}&noteContent=${note.content}&noteId=${note.id}&notePosition=${note.position}"
                                                    )
                                                }, scope = this@ReorderableItem)

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}












