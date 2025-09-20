package com.example.plaintext.data.repository

import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.model.Password
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PasswordDBStore {
    fun getList(): Flow<List<Password>>
    suspend fun add(password: Password)
    suspend fun update(password: Password)
    fun get(id: Int): Flow<Password?>
    suspend fun delete(password: Password)
    fun isEmpty(): Flow<Boolean>
}

class LocalPasswordDBStore(
    private val passwordDao: PasswordDao
    // Sem @Inject, via Módulo Hilt.
) : PasswordDBStore {

    override fun getList(): Flow<List<Password>> {
        // Pega todas as senhas do DAO.
        return passwordDao.getAllPasswords()
    }

    override suspend fun add(password: Password) {
        // DAO já é suspend e OnConflictStrategy.REPLACE está no @Insert.
        passwordDao.insertPassword(password)
    }

    override suspend fun update(password: Password) {
        // Direto para o DAO.
        passwordDao.updatePassword(password)
    }

    override fun get(id: Int): Flow<Password?> {
        // Pega uma senha específica pelo ID, também como um Flow.
        return passwordDao.getPasswordById(id)
    }

    override suspend fun delete(password: Password) {
        // Deleta a senha usando o objeto Password.
        passwordDao.deletePassword(password)
    }

    override fun isEmpty(): Flow<Boolean> {
        return passwordDao.getAllPasswords().map { it.isEmpty() }
    }
}
