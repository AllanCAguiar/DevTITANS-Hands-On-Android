package com.example.plaintext.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.editList.EditList
import com.example.plaintext.ui.screens.hello.Hello_screen
import com.example.plaintext.ui.screens.list.AddButton
import com.example.plaintext.ui.screens.list.ListView
import com.example.plaintext.ui.screens.login.Login_screen
import com.example.plaintext.ui.viewmodel.LoginViewModel
import com.example.plaintext.utils.parcelableType
import kotlin.reflect.typeOf

@Composable
fun PlainTextApp(
    appState: JetcasterAppState = rememberJetcasterAppState()

) {
    val loginViewModel: LoginViewModel = viewModel()
    val loginState by loginViewModel.loginState
    NavHost(
        navController = appState.navController,
        startDestination = Screen.Login,
    )
    {
        composable<Screen.Hello>{
            var args = it.toRoute<Screen.Hello>()
            Hello_screen(args)
        }
        composable<Screen.Login> {
            Login_screen(
                loginState = loginState,
                onLoginChanged = loginViewModel::onLoginChange,
                onPasswordChanged = loginViewModel::onPasswordChange,
                onRememberMeChanged = loginViewModel::onRememberMeChange,
                onLoginClicked = { loginViewModel.onLoginClick {} },
                navigateToSettings = {appState.navigateToPreferences()},
            )
        }
        composable<Screen.EditList>(
            typeMap = mapOf(typeOf<PasswordInfo>() to parcelableType<PasswordInfo>())
        ) {
            val args = it.toRoute<Screen.EditList>()
            EditList(
                args,
                navigateBack = {},
                savePassword = { password -> Unit }
            )
        }
        composable<Screen.Preferences> {
            SettingsScreen(navController = appState.navController)
        }
    }
}