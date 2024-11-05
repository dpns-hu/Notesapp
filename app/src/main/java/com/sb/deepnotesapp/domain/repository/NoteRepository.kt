package com.sb.deepnotesapp.domain.repository

import androidx.paging.PagingData
import com.sb.deepnotesapp.data.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    fun getPagedNotes(): Flow<PagingData<Note>>

    suspend fun reorderNote(note1: Note, note2: Note)
}