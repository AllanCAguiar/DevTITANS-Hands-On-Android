package com.example.plaintext.ui.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.editList.EditList
import com.example.plaintext.ui.screens.hello.Hello_screen
// import com.example.plaintext.ui.screens.list.PasswordListScreen // TODO: Descomentar quando Screen.PasswordList estiver definido e a tela for usada
import com.example.plaintext.ui.screens.login.Login_screen
import com.example.plaintext.ui.viewmodel.LoginViewModel
import com.example.plaintext.utils.parcelableType
import kotlin.reflect.typeOf

// COMENTÁRIO SOBRE A DEFINIÇÃO DE SCREEN (ISSO É SEGURO)
// TODO: Certifique-se de que o objeto/interface Screen está definido em algum lugar.
//       Quando for adicionar a tela de lista, você precisará adicionar uma entrada para ela.
// Exemplo de como seu objeto Screen pode ser (adicione PasswordList quando for implementar):
/*
@Serializable
sealed interface Screen {
    @Serializable
    data object Login : Screen
    @Serializable
    data class Hello(val name: String) : Screen
    @Serializable
    data class EditList(val passwordInfo: PasswordInfo?) : Screen
    // @Serializable // TODO: Descomentar e definir quando implementar a PasswordListScreen
    // data object PasswordList : Screen
}
*/

// DEFINIÇÃO DA FUNÇÃO COMPOSABLE PRINCIPAL
@Composable
fun PlainTextApp(
    appState: JetcasterAppState = rememberJetcasterAppState() // Verifique se JetcasterAppState e rememberJetcasterAppState estão definidos e importados
) {
    val loginViewModel: LoginViewModel = viewModel()
    val loginState by loginViewModel.loginState

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
                        // TODO: Implementar navegação para a tela principal após login.
                        //       Quando Screen.PasswordList e a rota estiverem prontas, descomente e ajuste a linha abaixo:
                        // appState.navController.navigate(Screen.PasswordList) {
                        //    popUpTo(Screen.Login) { inclusive = true }
                        // }

                        // Por agora, pode-se deixar sem navegação ou navegar para uma tela de placeholder se existir
                        println("Login clicado, navegação para a lista de senhas pendente.")
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
                    // Lógica para salvar/atualizar a senha
                    appState.navController.popBackStack()
                }
            )
        }

        // TODO: Implementar a rota para a tela da lista de senhas.
        //       Quando Screen.PasswordList estiver definido e a PasswordListScreen estiver pronta:
        /*
        composable<Screen.PasswordList> {
            PasswordListScreen(
                // Se PasswordListScreen precisar de callbacks de navegação, passe-os aqui
                // Exemplo:
                // navigateToEditPassword = { passwordId ->
                //    appState.navController.navigate(Screen.EditList(passwordId = passwordId))
                // },
                // navigateToAddPassword = {
                //    appState.navController.navigate(Screen.EditList(passwordInfo = null))
                // }
            )
        }
        */
    }
}

// Comentários sobre JetcasterAppState mantidos do código anterior
// (Se você não usa JetcasterAppState, pode remover este bloco de comentário ou implementar SimpleAppState)
/*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class SimpleAppState(
    val navController: NavHostController
)

@Composable
fun rememberSimpleAppState(
    navController: NavHostController = rememberNavController()
): SimpleAppState {
    return remember(navController) {
        SimpleAppState(navController)
    }
}
*/
