package com.example.plaintext.data.di

import android.content.Context
import androidx.room.Room
import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.db.AppDatabase
import com.example.plaintext.data.repository.LocalPasswordDBStore
import com.example.plaintext.data.repository.PasswordDBStore
// import com.example.plaintext.ui.screens.hello.DbSimulator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataDIModule {

    //Prover a instância do AppDatabase
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "password_database"
        )
            // .fallbackToDestructiveMigration()
            .build()
    }

    // Prover a instância do PasswordDao a partir do AppDatabase
    @Provides
    @Singleton
    fun providePasswordDao(appDatabase: AppDatabase): PasswordDao {
        return appDatabase.passwordDao() // Obtém o DAO da instância do banco de dados
    }

    // Passo 3: Prover a instância do PasswordDBStore
    @Provides
    @Singleton
    fun providePasswordDBStore(passwordDao: PasswordDao): PasswordDBStore {
        return LocalPasswordDBStore(passwordDao)
    }

    /*
    @Provides
    @Singleton
    fun provideDBSimulator(): DbSimulator = DbSimulator()
    */
}
