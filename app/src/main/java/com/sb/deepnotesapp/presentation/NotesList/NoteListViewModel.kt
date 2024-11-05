package com.sb.deepnotesapp.presentation.NotesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sb.deepnotesapp.data.model.Note
import com.sb.deepnotesapp.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(val noteUseCases: NoteUseCases) : ViewModel() {

    private val _notesListState =  MutableStateFlow<List<Note>>(emptyList())
    var noteListState = _notesListState.asStateFlow()

    private val _noteTitleState =  MutableStateFlow("")
    var noteTitleState = _noteTitleState.asStateFlow()

    private val _noteContentState =  MutableStateFlow<String>("")
    var noteContentState = _noteContentState.asStateFlow()

    private val _pagingData = MutableStateFlow<PagingData<Note>>(PagingData.empty())

     val pagingData: StateFlow<PagingData<Note>> = _pagingData.asStateFlow()


    init {
        getPagedNotes()
    }
      fun getNoteList(){
        viewModelScope.launch {
            noteUseCases.getNotes()
                .collect { notes ->
                    _notesListState.value = notes
                }
        }
    }

    fun addNote(noteData: Note){
        viewModelScope.launch {
            noteUseCases.addNote(noteData)
        }
    }
    fun deleteNote(note:Note){
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
        }
    }
    fun onTitleChanged(title:String){
        _noteTitleState.value = title
    }
    fun onContentChanged(title:String){
        _noteContentState.value = title
    }
      fun getPagedNotes() {
         viewModelScope.launch {
             noteUseCases.getNotes.getPagedNotes()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                     _pagingData.value = pagingData
                }
        }
    }

    fun reorderNote(note1:Note,note2:Note){
        viewModelScope.launch { noteUseCases.reorderNote(note1,note2) }
    }
}