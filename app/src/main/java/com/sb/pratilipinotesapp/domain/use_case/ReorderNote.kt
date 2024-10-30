package com.sb.pratilipinotesapp.domain.use_case

import com.sb.pratilipinotesapp.data.model.InvalidNoteException
import com.sb.pratilipinotesapp.data.model.Note
import com.sb.pratilipinotesapp.domain.repository.NoteRepository

class ReorderNote(
    private val repository: NoteRepository
) {

     suspend operator fun invoke(note1: Note,note2:Note) {
        repository.reorderNote(note1,note2)
    }
}