package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class PreferencesState(
    val login: String = "",
    val password: String = "",
    val preencher: Boolean = false
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val handle: SavedStateHandle,
) : ViewModel() {

    companion object {
        private const val keyLogin = "login"
        private const val keyPassword = "password"
        private const val keyPreencher = "preencher"
    }

    var preferencesState by mutableStateOf(
        PreferencesState(
            login = handle[keyLogin] ?: "devtitans",
            password = handle[keyPassword] ?: "123",
            preencher = handle[keyPreencher] ?: true
        )
    )
        private set

    fun updateLogin(login: String) {
        preferencesState = preferencesState.copy(login = login)
        handle[keyLogin] = login
    }

    fun updatePassword(password: String) {
        preferencesState = preferencesState.copy(password = password)
        handle[keyPassword] = password
    }

    fun updatePreencher(preencher: Boolean) {
        preferencesState = preferencesState.copy(preencher = preencher)
        handle[keyPreencher] = preencher
    }

    fun checkCredentials(login: String, password: String): Boolean {
        return login == preferencesState.login && password == preferencesState.password
    }
}



