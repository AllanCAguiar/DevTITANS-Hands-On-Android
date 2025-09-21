package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class PreferencesState(
    val login: String = "devtitans", val password: String = "123", val remember: Boolean = false
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val handle: SavedStateHandle,
) : ViewModel() {

    companion object {
        private const val KEY_LOGIN = "login"
        private const val KEY_PASSWORD = "password"
        private const val KEY_REMEMBER = "remember"
    }

    private val _preferencesState = mutableStateOf(
        PreferencesState(
            login = handle.get<String>(KEY_LOGIN) ?: "devtitans",
            password = handle.get<String>(KEY_PASSWORD) ?: "123",
            remember = handle.get<Boolean>(KEY_REMEMBER) ?: false
        )
    )
    val preferencesState: State<PreferencesState> = _preferencesState

    fun updateLogin(login: String) {
        _preferencesState.value = _preferencesState.value.copy(login = login)
        handle[KEY_LOGIN] = login
    }

    fun updatePassword(password: String) {
        _preferencesState.value = _preferencesState.value.copy(password = password)
        handle[KEY_PASSWORD] = password
    }

    fun updateRemember(remember: Boolean) {
        _preferencesState.value = _preferencesState.value.copy(remember = remember)
        handle[KEY_REMEMBER] = remember
    }

    fun checkCredentials(login: String, password: String): Boolean {
        return login == _preferencesState.value.login && password == _preferencesState.value.password
    }
}



