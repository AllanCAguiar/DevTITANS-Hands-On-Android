package com.example.plaintext.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

// Password deve ser uma entidade do Room
@Entity(tableName = "passwords") // Correção: Anotação @Entity aplicada corretamente
data class Password(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") // Boa prática especificar o nome da coluna, mesmo que seja igual ao da propriedade
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "password_text") // Renomeado para evitar confusão com o nome da classe/tipo
    val password: String,                 // O comentário /* String */ foi removido

    @ColumnInfo(name = "notes")
    val notes: String? = null
)
// A segunda definição de 'data class Password' foi completamente REMOVIDA daqui.

// PasswordInfo deve ser uma classe de dados serializável e parcelável
@Serializable // Para kotlinx.serialization
@Parcelize    // Para Android Parcelable
data class PasswordInfo(
    val id: Int,
    val name: String,
    val login: String,
    val password: String, // Mantendo como 'password' aqui, pois é um DTO
    val notes: String?    // Tornando notes anulável para corresponder à entidade, se necessário
) : Parcelable {
}

fun Password.toPasswordInfo(): PasswordInfo {
    return PasswordInfo(
        id = this.id,
        name = this.name,
        login = this.login,
        password = this.password, // Mapeando de password_text (entidade) para password (DTO)
        notes = this.notes
    )
}

