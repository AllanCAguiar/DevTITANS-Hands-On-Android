package com.example.plaintext.ui.screens.preferences


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.plaintext.ui.screens.login.TopBarComponent
import com.example.plaintext.ui.screens.util.PreferenceInput
import com.example.plaintext.ui.screens.util.PreferenceItem
import com.example.plaintext.ui.viewmodel.PreferencesViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController?, viewModel: PreferencesViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopBarComponent()
        }) { padding ->
        SettingsContent(modifier = Modifier.padding(padding), viewModel)
    }
}

@Composable
fun SettingsContent(modifier: Modifier = Modifier, viewModel: PreferencesViewModel) {

    val state = viewModel.preferencesState

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        PreferenceInput(
            title = "Preencher Login",
            label = "Login",
            fieldValue = state.value.login,
            summary = "Preencher login na tela inicial"
        ) { novoLogin ->
            viewModel.updateLogin(novoLogin)
        }

        PreferenceInput(
            title = "Setar Senha",
            label = "Senha",
            fieldValue = state.value.password,
            summary = "Senha para entrar no sistema"
        ) { novaSenha ->
            viewModel.updatePassword(novaSenha)
        }

        PreferenceItem(
            title = "Preencher Login",
            summary = "Preencher login na tela inicial",
            onClick = {
                viewModel.updateRemember(!state.value.remember)
            },
            control = {
                Switch(
                    checked = state.value.remember, onCheckedChange = { novoValor ->
                        viewModel.updateRemember(novoValor)
                    })
            })
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(null)
}