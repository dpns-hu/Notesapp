package com.sb.deepnotesapp.di

import android.app.Application
import androidx.room.Room
import com.sb.deepnotesapp.data.local.NoteDatabase
import com.sb.deepnotesapp.data.repository.NoteRepositoryImpl
import com.sb.deepnotesapp.domain.repository.NoteRepository
import com.sb.deepnotesapp.domain.use_case.AddNote
import com.sb.deepnotesapp.domain.use_case.DeleteNote
import com.sb.deepnotesapp.domain.use_case.GetNotes
import com.sb.deepnotesapp.domain.use_case.NoteUseCases
import com.sb.deepnotesapp.domain.use_case.ReorderNote
import com.sb.deepnotesapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            reorderNote = ReorderNote(repository)
        )
    }
}