package com.sb.deepnotesapp.domain.use_case

import com.sb.deepnotesapp.data.model.Note
import com.sb.deepnotesapp.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}