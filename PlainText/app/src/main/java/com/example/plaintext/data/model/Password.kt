package com.example.plaintext.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty

@Entity(tableName = "passwords")
data class Password(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,

    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "login") val login: String,

    @ColumnInfo(name = "password_text") val passwordText: String,

    @ColumnInfo(name = "notes") val notes: String? = null
)

@Serializable
@Parcelize
data class PasswordInfo(
    val id: Int,
    val name: String,
    val login: String,
    val password: String,
    val notes: String?,
) : Parcelable {

    operator fun getValue(nothing: Nothing?, property: KProperty<*>): Password = Password(
        id = id,
        name = name,
        login = login,
        passwordText = password,
        notes = notes,
    )
}

fun PasswordInfo.toPassword(): Password {
    return Password(
        id = this.id,
        name = this.name,
        login = this.login,
        passwordText = this.password, // Supondo que o campo se chame 'passwordText' na classe Password
        notes = this.notes
    )
}