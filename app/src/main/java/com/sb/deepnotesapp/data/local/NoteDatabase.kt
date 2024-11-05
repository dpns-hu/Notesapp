package com.sb.deepnotesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sb.deepnotesapp.data.model.Note

@Database(
    entities = [Note::class],
    version = 2
)
abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao

}