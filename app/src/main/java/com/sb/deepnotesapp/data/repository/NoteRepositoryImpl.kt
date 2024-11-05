package com.sb.deepnotesapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sb.deepnotesapp.data.local.NoteDao
import com.sb.deepnotesapp.data.model.Note
import com.sb.deepnotesapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun insertNote(note: Note) {
        if(note.position==-1){
            val maxPosition = dao.getMaxPosition() ?: 0
            note.position = maxPosition + 1
        }
         dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }

    override fun getPagedNotes(): Flow<PagingData<Note>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, // Number of items per page
                enablePlaceholders = true
            ),
            pagingSourceFactory = { dao.getPagedNotes() }
        ).flow
    }

    override suspend fun reorderNote(note1: Note, note2: Note) {
         val tempPosition = note1.position
            note1.position = note2.position
            note2.position = tempPosition
             dao.insertNote(note1)
             dao.insertNote(note2)
         }



}