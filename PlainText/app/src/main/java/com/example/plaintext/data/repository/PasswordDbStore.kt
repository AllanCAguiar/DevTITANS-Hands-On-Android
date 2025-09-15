package com.example.plaintext.data.repository

import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.model.Password
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordDbStore @Inject constructor(private val passwordDao: PasswordDao) {

    fun getAllPasswords(): Flow<List<Password>> = passwordDao.getAllPasswords()

    fun getPasswordById(id: Int): Flow<Password?> = passwordDao.getPasswordById(id)

    suspend fun insertPassword(password: Password) {
        passwordDao.insertPassword(password)
    }

    suspend fun updatePassword(password: Password) {
        passwordDao.updatePassword(password)
    }

    suspend fun deletePassword(password: Password) {
        passwordDao.deletePassword(password)
    }
}