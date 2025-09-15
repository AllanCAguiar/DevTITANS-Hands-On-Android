package com.example.plaintext.ui.screens.editList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
// import androidx.compose.material3.TextField // Não usado diretamente, pode ser removido se OutlinedTextField for suficiente
// import androidx.compose.material3.TextFieldDefaults // Não usado, pode ser removido
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Não usado diretamente, pode ser removido se não houver cores customizadas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Não usado diretamente, pode ser removido se não houver tamanhos de texto customizados aqui
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.Screen
// import com.example.plaintext.ui.screens.login.TopBarComponent // Não usado, pode ser removido

data class EditListState(
    val nomeState: MutableState<String>,
    val usuarioState: MutableState<String>,
    val senhaState: MutableState<String>,
    val notasState: MutableState<String>, // Se notas pode ser nulo, considere MutableState<String?>
)

fun isPasswordEmpty(password: PasswordInfo): Boolean {
    // Correção: Usa isNullOrEmpty() para o campo anulável 'notes'.
    // Os campos name, login, e password em PasswordInfo são definidos como String (não anuláveis),
    // então .isEmpty() é seguro para eles.
    return password.name.isEmpty() &&
            password.login.isEmpty() &&
            password.password.isEmpty() &&
            password.notes.isNullOrEmpty() // <<< CORREÇÃO APLICADA AQUI
}

@Composable
fun EditList(
    args: Screen.EditList,
    navigateBack: () -> Unit,
    savePassword: (password: PasswordInfo) -> Unit
) {
    // TODO: Implementar o layout da tela de edição aqui
    // Exemplo:
    // var name by rememberSaveable { mutableStateOf(args.passwordInfo?.name ?: "") }
    // var login by rememberSaveable { mutableStateOf(args.passwordInfo?.login ?: "") }
    // var passwordText by rememberSaveable { mutableStateOf(args.passwordInfo?.password ?: "") }
    // var notes by rememberSaveable { mutableStateOf(args.passwordInfo?.notes ?: "") }

    // Scaffold(
    //    topBar = { /* TopBarComponent(...) */ },
    //    floatingActionButton = {
    //        Button(onClick = {
    //            val updatedPasswordInfo = args.passwordInfo?.copy(
    //                name = name,
    //                login = login,
    //                password = passwordText,
    //                notes = notes.takeIf { it.isNotBlank() } // Salva null se notes estiver em branco
    //            ) ?: PasswordInfo(
    //                id = 0, // Ou um ID apropriado se for um novo item sem args
    //                name = name,
    //                login = login,
    //                password = passwordText,
    //                notes = notes.takeIf { it.isNotBlank() }
    //            )
    //            if (!isPasswordEmpty(updatedPasswordInfo)) { // Validação simples
    //                 savePassword(updatedPasswordInfo)
    //            }
    //        }) {
    //            Text("Salvar")
    //        }
    //    }
    // ) { paddingValues ->
    //    Column(
    //        modifier = Modifier
    //            .fillMaxSize()
    //            .padding(paddingValues)
    //            .padding(16.dp),
    //        horizontalAlignment = Alignment.CenterHorizontally
    //    ) {
    //        EditInput("Nome", rememberSaveable { mutableStateOf(name) }.also { name = it.value }) // Precisa de forma de atualizar 'name'
    //        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") })
    //        OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Login") })
    //        OutlinedTextField(value = passwordText, onValueChange = { passwordText = it }, label = { Text("Senha") })
    //        OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notas") })
    //    }
    // }
}


@Composable
fun EditInput(
    textInputLabel: String,
    textInputState: MutableState<String>, // Removido valor padrão para forçar passagem de estado
    textInputHeight: Int = 60
) {
    val paddingHorizontal: Int = 30 // Renomeado para clareza

    // Não use rememberSaveable aqui se o estado é elevado e passado como parâmetro.
    // O estado (textInputState) já é gerenciado pelo chamador (EditList).
    // var textState by rememberSaveable { textInputState } // REMOVA ESTA LINHA

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(textInputHeight.dp)
            .padding(horizontal = paddingHorizontal.dp), // Usando a variável renomeada
        horizontalArrangement = Arrangement.Center,
    ) {
        OutlinedTextField(
            value = textInputState.value, // Use textInputState.value
            onValueChange = { textInputState.value = it }, // Atualize textInputState.value
            label = { Text(textInputLabel) },
            modifier = Modifier
                // .height(textInputHeight.dp) // OutlinedTextField gerencia sua própria altura melhor
                .fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Preview(showBackground = true)
@Composable
fun EditListPreview() {
    // Para o preview, PasswordInfo.notes pode ser uma string vazia ou um valor.
    // Se a sua definição de Screen.EditList espera um PasswordInfo não nulo, está OK.
    // Se Screen.EditList pode ter passwordInfo nulo (para "adicionar novo"), ajuste o preview.
    EditList(
        args = Screen.EditList(PasswordInfo(1, "Nome Preview", "usuario@preview.com", "senha123", "Algumas notas aqui")),
        navigateBack = {},
        savePassword = {}
    )
}

@Preview(showBackground = true)
@Composable
fun EditInputPreview() {
    // Para o preview de EditInput, precisamos fornecer um MutableState real.
    val previewState = rememberSaveable { mutableStateOf("Texto de Preview") }
    EditInput(textInputLabel = "Campo de Teste", textInputState = previewState)
}

