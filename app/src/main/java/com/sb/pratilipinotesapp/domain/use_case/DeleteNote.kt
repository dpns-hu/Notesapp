package com.sb.pratilipinotesapp.domain.use_case

import com.sb.pratilipinotesapp.data.model.Note
import com.sb.pratilipinotesapp.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}