package com.sb.deepnotesapp.domain.use_case

import com.sb.deepnotesapp.data.model.InvalidNoteException
import com.sb.deepnotesapp.data.model.Note
import com.sb.deepnotesapp.domain.repository.NoteRepository

class AddNote(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}