package com.sb.deepnotesapp.presentation.NotesList.components

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.sb.deepnotesapp.presentation.util.getVisibleItemInfoFor
import com.sb.deepnotesapp.presentation.util.offsetEnd
import kotlinx.coroutines.Job

@Composable
fun rememberDragDropListState(
    lazyListState: LazyListState = rememberLazyListState(),
    onMove: (Int, Int) -> Unit
): DragDropListState {
    return remember { DragDropListState(lazyListState = lazyListState, onMove = onMove) }
}

class DragDropListState(
    val lazyListState: LazyListState,
    private val onMove: (Int, Int) -> Unit
) {
    var draggedDistance by mutableStateOf(0f)
    var initiallyDraggedElement by mutableStateOf<LazyListItemInfo?>(null)
    var currentIndexOfDraggedItem by mutableStateOf<Int?>(null)
    val initialOffsets: Pair<Int, Int>?
        get() = initiallyDraggedElement?.let {
            Pair(it.offset, it.offsetEnd)
        }
    val elementDisplacement: Float?
        get() = currentIndexOfDraggedItem
            ?.let {
                lazyListState.getVisibleItemInfoFor(absolute = it)
            }
            ?.let {
                    item -> (initiallyDraggedElement?.offset ?: 0f).toFloat() + draggedDistance - item.offset
            }

    val currentElement: LazyListItemInfo?
        get() = currentIndexOfDraggedItem?.let {
            lazyListState.getVisibleItemInfoFor(absolute = it)
        }

    var overScrollJob by mutableStateOf<Job?>(null)

    fun onDragStart(offset: Offset) {
        lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { item ->
                offset.y.toInt() in item.offset..(item.offset + item.size)
            }?.also {
                currentIndexOfDraggedItem = it.index
                initiallyDraggedElement = it
            }
    }

    fun onDragInterrupted() {
        draggedDistance = 0f
        currentIndexOfDraggedItem = null
        initiallyDraggedElement = null
        overScrollJob?.cancel()
    }

    fun onDrag(offset: Offset) {
        draggedDistance += offset.y

        initialOffsets?.let { (topOffset, bottomOffset) ->
            val startOffset = topOffset + draggedDistance
            val endOffset = bottomOffset + draggedDistance

            currentElement?.let { hovered ->
                lazyListState.layoutInfo.visibleItemsInfo
                    .filterNot { item ->
                        item.offsetEnd < startOffset || item.offset > endOffset || hovered.index == item.index
                    }
                    .firstOrNull { item ->
                        val delta = startOffset - hovered.offset
                        when {
                            delta > 0 -> (endOffset > item.offsetEnd) // Dragging down
                            else -> (startOffset < item.offset) // Dragging up
                        }
                    }?.also { item ->
                        currentIndexOfDraggedItem?.let { currentIndex ->
                            // Ensure both indices are within bounds before invoking onMove
                            if (currentIndex in 0 until lazyListState.layoutInfo.visibleItemsInfo.size &&
                                item.index in 0 until lazyListState.layoutInfo.visibleItemsInfo.size) {
                                onMove.invoke(currentIndex, item.index) // Safely invoke onMove
                                currentIndexOfDraggedItem = item.index // Update the index
                            }
                        }
                    }
            }
        }
    }



    fun checkForOverScroll(): Float {
        return initiallyDraggedElement?.let {
            val startOffset = it.offset + draggedDistance
            val endOffset = it.offsetEnd + draggedDistance

            return@let when {
                draggedDistance > 0 -> (endOffset - lazyListState.layoutInfo.viewportEndOffset).takeIf { diff ->
                    diff > 0
                }
                draggedDistance < 0 -> (startOffset - lazyListState.layoutInfo.viewportStartOffset).takeIf { diff ->
                    diff < 0
                }
                else -> null
            }
        } ?: 0f
    }

}