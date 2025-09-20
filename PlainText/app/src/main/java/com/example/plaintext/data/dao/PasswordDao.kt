package com.example.plaintext.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.plaintext.data.model.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: Password)

    @Update
    suspend fun updatePassword(password: Password)

    @Delete
    suspend fun deletePassword(password: Password)

    @Query("SELECT * FROM passwords WHERE id = :id")
    fun getPasswordById(id: Int): Flow<Password?>

    @Query("SELECT * FROM passwords ORDER BY name ASC")
    fun getAllPasswords(): Flow<List<Password>>

}