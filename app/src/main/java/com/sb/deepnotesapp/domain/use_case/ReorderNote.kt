package com.sb.deepnotesapp.domain.use_case

import com.sb.deepnotesapp.data.model.Note
import com.sb.deepnotesapp.domain.repository.NoteRepository

class ReorderNote(
    private val repository: NoteRepository
) {

     suspend operator fun invoke(note1: Note,note2:Note) {
        repository.reorderNote(note1,note2)
    }
}