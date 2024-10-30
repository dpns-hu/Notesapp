package com.sb.pratilipinotesapp.domain.use_case

import androidx.paging.PagingData
import com.sb.pratilipinotesapp.data.model.Note
import com.sb.pratilipinotesapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotes(
    private val repository: NoteRepository
) {

    operator fun invoke(): Flow<List<Note>> {
        return repository.getNotes()
        }

      fun getPagedNotes():Flow<PagingData<Note>>{
        return repository.getPagedNotes()
    }
    }