package com.example.plaintext.data.di

import android.content.Context
import androidx.room.Room
import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.PlainTextDatabase
import com.example.plaintext.data.repository.LocalPasswordDBStore
import com.example.plaintext.data.repository.PasswordDBStore
import com.example.plaintext.ui.screens.hello.DbSimulator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataDiModule {

    // Provisão para PlainTextDatabase
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): PlainTextDatabase { // <<< TIPO DE RETORNO CORRIGIDO
        return Room.databaseBuilder(
            appContext,
            PlainTextDatabase::class.java,
            "password_database"
        )
            .build()
    }

    // Provisão para o PasswordDao
    @Provides
    @Singleton
    fun provideActualPasswordDao(plainTextDatabase: PlainTextDatabase): PasswordDao {
        return plainTextDatabase.passwordDao()
    }

    // Provisão para PasswordDBStore
    @Provides
    @Singleton
    fun providePasswordDBStore(passwordDao: PasswordDao): PasswordDBStore {
        return LocalPasswordDBStore(passwordDao)
    }
    @Provides
    @Singleton
    fun provideDBSimulator(): DbSimulator = DbSimulator()
}

