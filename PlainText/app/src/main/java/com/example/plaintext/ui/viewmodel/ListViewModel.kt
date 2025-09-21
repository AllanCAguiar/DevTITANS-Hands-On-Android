package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.model.Password
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.repository.PasswordDBStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListViewState(
    var passwordList: List<PasswordInfo>, var isCollected: Boolean = false
)

@HiltViewModel
open class ListViewModel @Inject constructor(
    private val passwordDBStore: PasswordDBStore
) : ViewModel() {
    private val _listViewState = mutableStateOf(ListViewState(passwordList = emptyList()))

    val listViewState: State<ListViewState> = _listViewState

    init {
        viewModelScope.launch {
            passwordDBStore.getList().collect { passwordsFromDb ->

                val passwordInfoList = passwordsFromDb.map { password ->
                    PasswordInfo(
                        id = password.id,
                        name = password.name,
                        login = password.login,
                        password = password.passwordText,
                        notes = password.notes,
                    )
                }

                _listViewState.value = _listViewState.value.copy(
                    passwordList = passwordInfoList, isCollected = true
                )
            }
        }
    }


    fun savePassword(password: Password) {
        viewModelScope.launch {
            passwordDBStore.add(password)
        }
    }
}
