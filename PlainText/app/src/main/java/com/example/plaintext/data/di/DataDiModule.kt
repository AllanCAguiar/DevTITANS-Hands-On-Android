package com.example.plaintext.data.di


import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.repository.LocalPasswordDBStore
import com.example.plaintext.data.repository.PasswordDBStore
import com.example.plaintext.ui.screens.hello.DbSimulator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataDiModule {
    @Provides
    @Singleton
    fun providePasswordDao(
        passwordDao: PasswordDao
    ): PasswordDBStore = LocalPasswordDBStore(passwordDao)

    @Provides
	@Singleton
	fun provideDBSimulator(): DbSimulator = DbSimulator()
}