package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class LoginState(
    val login: String = "", val password: String = "", val rememberMe: Boolean = false , val loginError: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    fun onLoginChange(newLogin: String) {
        _loginState.value = _loginState.value.copy(login = newLogin)
    }

    fun onPasswordChange(newPassword: String) {
        _loginState.value = _loginState.value.copy(password = newPassword)
    }

    fun onRememberMeChange(isChecked: Boolean) {
        _loginState.value = _loginState.value.copy(rememberMe = isChecked)
    }

    fun onLoginClick(navigateToList: (String) -> Unit, onLoginError: () -> Unit) {
        val login = _loginState.value.login
        val password = _loginState.value.password

        // Removendo valores fixos
        if (!login.isEmpty() && !password.isEmpty()) {
            _loginState.value = _loginState.value.copy(loginError = false)
            navigateToList(login)
        } else {
            _loginState.value = _loginState.value.copy(loginError = true)
            onLoginError()
        }
    }
}