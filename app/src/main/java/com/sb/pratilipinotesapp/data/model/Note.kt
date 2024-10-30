package com.sb.pratilipinotesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val content: String,
    var position: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0)

class InvalidNoteException(message: String): Exception(message)