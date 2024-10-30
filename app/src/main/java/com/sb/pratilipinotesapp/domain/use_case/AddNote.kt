package com.sb.pratilipinotesapp.domain.use_case

import com.sb.pratilipinotesapp.data.model.InvalidNoteException
import com.sb.pratilipinotesapp.data.model.Note
import com.sb.pratilipinotesapp.domain.repository.NoteRepository

class AddNote(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}