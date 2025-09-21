package com.example.plaintext.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.model.toPassword
import com.example.plaintext.ui.screens.editList.EditList
import com.example.plaintext.ui.screens.hello.Hello_screen
import com.example.plaintext.ui.screens.list.ListScreen
import com.example.plaintext.ui.screens.login.Login_screen
import com.example.plaintext.ui.screens.preferences.SettingsScreen
import com.example.plaintext.ui.viewmodel.ListViewModel
import com.example.plaintext.ui.viewmodel.LoginViewModel
import com.example.plaintext.ui.viewmodel.PreferencesViewModel
import com.example.plaintext.utils.parcelableType
import kotlin.reflect.typeOf
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun PlainTextApp(
    appState: JetcasterAppState = rememberJetcasterAppState()

) {
    val loginViewModel: LoginViewModel = viewModel()
    val loginState by loginViewModel.loginState
    val listViewModel: ListViewModel = viewModel()
    val listState by listViewModel.listViewState
    val preferencesViewModel: PreferencesViewModel = viewModel()
    val preferencesState by preferencesViewModel.preferencesState
    val context = LocalContext.current

    NavHost(
        navController = appState.navController,
        startDestination = Screen.Login,
    ) {
        composable<Screen.Hello> {
            var args = it.toRoute<Screen.Hello>()
            Hello_screen(args)
        }
        composable<Screen.List> {Text("Login bem-sucedido! Você está na tela de Lista.")}
        composable<Screen.Login> {
            Login_screen(
                loginState = loginState,
                onLoginChanged = loginViewModel::onLoginChange,
                onPasswordChanged = loginViewModel::onPasswordChange,
                onRememberMeChanged = loginViewModel::onRememberMeChange,
                navigateToSettings = { appState.navigateToPreferences() },
                onLoginClicked = {
                    loginViewModel.onLoginClick(
                        navigateToList = {
                            appState.navController.navigate(Screen.List)
                        },
                        onLoginError = {
                            Toast.makeText(context, "Login/Senha inválidos!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },    
            )
        }
        composable<Screen.EditList>(
            typeMap = mapOf(typeOf<PasswordInfo>() to parcelableType<PasswordInfo>())
        ) {
            val args = it.toRoute<Screen.EditList>()
            EditList(args, navigateBack = { appState.navigateBack() }, savePassword = { password ->
                listViewModel.savePassword(password.toPassword())
            })
        }
        composable<Screen.Preferences> {
            SettingsScreen(navController = appState.navController, viewModel = preferencesViewModel)
        }
        composable<Screen.List> {
            ListScreen(
                onAddClick = { appState.navigateToAddPassword() },
                listState = listState,
                navigateToEdit = { password -> appState.navigateToEditPassword(password) })
        }
    }
}