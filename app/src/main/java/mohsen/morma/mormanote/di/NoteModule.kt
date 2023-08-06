package mohsen.morma.mormanote.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mohsen.morma.mormanote.data.NoteDao
import mohsen.morma.mormanote.data.NoteDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app : Application) : NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            "note_db"
        ).build()
    }

    @Provides @Singleton fun provideDao(noteDatabase: NoteDatabase) : NoteDao = noteDatabase.dao


}