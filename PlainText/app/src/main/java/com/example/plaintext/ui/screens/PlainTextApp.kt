package com.example.plaintext.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // Mantido, pode ser usado por appState ou loginState
import androidx.lifecycle.viewmodel.compose.viewModel // Mantido
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// import androidx.navigation.compose.currentBackStackEntryAsState // Removido se não usado diretamente aqui
import androidx.navigation.toRoute // Mantido para navegação segura por tipo
import com.example.plaintext.data.model.PasswordInfo // Mantido para EditList
import com.example.plaintext.ui.screens.editList.EditList
import com.example.plaintext.ui.screens.hello.Hello_screen
// import com.example.plaintext.ui.screens.list.AddButton // Removido, AddButton está dentro de PasswordListScreen
// import com.example.plaintext.ui.screens.list.ListView    // <<< REMOVIDO o import de ListView
import com.example.plaintext.ui.screens.list.PasswordListScreen // <<< ADICIONADO import para PasswordListScreen
import com.example.plaintext.ui.screens.login.Login_screen
import com.example.plaintext.ui.viewmodel.LoginViewModel // Mantido
import com.example.plaintext.utils.parcelableType // Mantido para EditList
import kotlin.reflect.typeOf // Mantido para EditList


@Composable
fun PlainTextApp(
    appState: JetcasterAppState = rememberJetcasterAppState() // Verifique se JetcasterAppState e rememberJetcasterAppState estão definidos e importados
) {
    val loginViewModel: LoginViewModel = viewModel()
    val loginState by loginViewModel.loginState // Certifique-se que loginState é um State<>

    NavHost(
        navController = appState.navController,
        startDestination = Screen.Login, // Sua rota inicial
    ) {
        composable<Screen.Hello> {
            val args = it.toRoute<Screen.Hello>()
            Hello_screen(args)
        }

        composable<Screen.Login> {
            Login_screen(
                loginState = loginState,
                onLoginChanged = loginViewModel::onLoginChange,
                onPasswordChanged = loginViewModel::onPasswordChange,
                onRememberMeChanged = loginViewModel::onRememberMeChange,
                onLoginClicked = {
                    loginViewModel.onLoginClick {
                        // Navegue para a lista de senhas após o login bem-sucedido
                        appState.navController.navigate(Screen.PasswordList) {
                            popUpTo(Screen.Login) { inclusive = true } // Remove Login da backstack
                        }
                    }
                },
                navigateToSettings = {
                    // Implemente a navegação para configurações se necessário
                },
            )
        }

        composable<Screen.EditList>(
            typeMap = mapOf(typeOf<PasswordInfo>() to parcelableType<PasswordInfo>())
        ) {
            val args = it.toRoute<Screen.EditList>()
            EditList(
                args,
                navigateBack = { appState.navController.popBackStack() },
                savePassword = { passwordInfo ->
                    // Lógica para salvar/atualizar a senha (provavelmente no ViewModel da lista)
                    // Exemplo: listViewModel.savePassword(passwordInfo)
                    appState.navController.popBackStack()
                }
            )
        }

        // --- ADICIONE A ROTA PARA A TELA DA LISTA DE SENHAS ---
        composable<Screen.PasswordList> {
            PasswordListScreen(
                // Se PasswordListScreen precisar de callbacks de navegação, passe-os aqui
                // Exemplo:
                // navigateToEditPassword = { passwordId ->
                //    appState.navController.navigate(Screen.EditList(passwordId = passwordId)) // Ajuste conforme sua rota EditList
                // },
                // navigateToAddPassword = {
                //    appState.navController.navigate(Screen.EditList(passwordInfo = null)) // Ou uma rota específica para adicionar
                // }
            )
        }
    }
}
