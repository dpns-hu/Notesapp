package com.sb.pratilipinotesapp.presentation.NotesList.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.sb.pratilipinotesapp.data.model.Note
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun DragAndDropList(
    itemList: LazyPagingItems<Note>,
    onMove: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Note) -> Unit,
    onNoteClick:(Note) ->Unit
) {
    val scope = rememberCoroutineScope()
    var overScrollJob by remember { mutableStateOf<Job?>(null) }
    val dragDropListState = rememberDragDropListState(onMove = onMove)

    LazyColumn(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDrag = { change, offset ->
                        change.consumeAllChanges()
                         dragDropListState.onDrag(offset = offset)

                        if (overScrollJob?.isActive == true)
                            return@detectDragGesturesAfterLongPress

                        dragDropListState
                            .checkForOverScroll()
                            .takeIf { it != 0f }
                            ?.let {
                                overScrollJob = scope.launch {
                                    dragDropListState.lazyListState.scrollBy(it)
                                }
                            } ?: kotlin.run { overScrollJob?.cancel() }
                    },
                    onDragStart = { offset ->
                        dragDropListState.onDragStart(offset)
                    },
                    onDragEnd = {
                        dragDropListState.onDragInterrupted()
                     },
                    onDragCancel = {
                        dragDropListState.onDragInterrupted()
                     }
                )
            }
            .fillMaxSize()
             ,
        state = dragDropListState.lazyListState
    ) {
        items(itemList.itemSnapshotList.items) { item ->
            Log.d("ITEM","${item.position} title ${item.title}")
            Column(
                modifier = Modifier
                    .composed {
                        val offsetOrNull = dragDropListState.elementDisplacement.takeIf {
                            itemList.itemSnapshotList.indexOf(item) == dragDropListState.currentIndexOfDraggedItem
                        }
                        Modifier.graphicsLayer {
                            translationY = offsetOrNull ?: 0f
                        }
                    }
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .padding(12.dp)
                    .pointerInput(Unit) {
                    }
            ) {
//                NoteItem(note = item, onDeleteClick = { onDeleteClick(item) }, onNoteClick = {onNoteClick(item)})
            }
        }
        item {
            Spacer(modifier = Modifier.height(50.dp)) // Adjust height as needed
        }
    }
}
