package com.example.plaintext.ui.screens.list

// Imports existentes que são úteis
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer // Adicionado
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Adicionado para LazyColumn com lista
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete // Adicionado
import androidx.compose.material3.Button // Adicionado para diálogo
import androidx.compose.material3.Card // Adicionado para melhor visualização do item
import androidx.compose.material3.CircularProgressIndicator // Adicionado
import androidx.compose.material3.ExperimentalMaterial3Api // Adicionado
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // Mantido e usado
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField // Adicionado para diálogo
import androidx.compose.material3.TopAppBar // Adicionado
import androidx.compose.material3.TopAppBarDefaults // Adicionado
import androidx.compose.material3.AlertDialog // Adicionado
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // Adicionado
import androidx.compose.runtime.getValue // Mantido
import androidx.compose.runtime.mutableStateOf // Adicionado
import androidx.compose.runtime.remember // Adicionado
import androidx.compose.runtime.setValue // Adicionado
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Mantido, mas pode não ser necessário se usar cores do tema
import androidx.compose.ui.platform.LocalContext // Adicionado
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview // Mantido
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel // Importante
import com.example.plaintext.R
import com.example.plaintext.data.model.Password // Importe a ENTIDADE Password
// import com.example.plaintext.data.model.PasswordInfo // Veja nota abaixo
import com.example.plaintext.ui.viewmodel.ListViewModel // Importe seu ViewModel
import com.example.plaintext.ui.viewmodel.ListViewState // Importe seu Estado


// O Composable principal da tela
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordListScreen( // Renomeado de ListView para seguir convenção de telas
    listViewModel: ListViewModel = hiltViewModel(),
    // Se precisar navegar para edição:
    // navigateToEdit: (passwordId: Int) -> Unit
) {
    val uiState by listViewModel.listViewState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Suas Senhas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            AddButton(onClick = { showAddDialog = true })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Usando seu ListItemContent adaptado
            ListItemContent(
                modifier = Modifier.weight(1f), // Para ocupar o espaço disponível
                listState = uiState,
                onDeletePassword = { password ->
                    listViewModel.deletePassword(password)
                }
                // navigateToEdit = { password ->
                //    // Aqui você pegaria o ID e chamaria a navegação
                //    // navigateToEdit(password.id)
                // }
            )
        }
    }

    if (showAddDialog) {
        AddPasswordDialog(
            onDismiss = { showAddDialog = false },
            onSavePassword = { name, login, pass, notes ->
                listViewModel.addPassword(name, login, pass, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddButton(onClick: () -> Unit) { // Seu AddButton, sem alterações
    FloatingActionButton(
        onClick = onClick, // Simplificado
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(Icons.Filled.Add, "Adicionar Nova Senha") // Descrição mais clara
    }
}

// Adaptado para usar o ListViewState e lidar com os diferentes estados
@Composable
fun ListItemContent(
    modifier: Modifier = Modifier, // Adicionado valor padrão
    listState: ListViewState,
    onDeletePassword: (Password) -> Unit // Mudei para receber Password e ação de delete
    // navigateToEdit: (password: Password) -> Unit // Se precisar, use Password
) {
    when {
        listState.isLoading -> {
            LoadingScreen() // Sua tela de loading
        }
        listState.error != null -> {
            ErrorScreen(message = listState.error)
        }
        listState.passwords.isEmpty() -> {
            EmptyScreen()
        }
        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                // Usando items com a lista diretamente
                items(listState.passwords, key = { it.id }) { password ->
                    ListItem(
                        password = password, // Agora é a entidade Password
                        onDelete = { onDeletePassword(password) }
                        // onClick = { navigateToEdit(password) }
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() { // Sua tela de Loading, sem alterações
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator() // Usando o indicador padrão do Material
        Spacer(Modifier.height(8.dp))
        Text("Carregando senhas...")
    }
}

@Composable
fun ErrorScreen(message: String) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Erro: $message", color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun EmptyScreen() {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Nenhuma senha encontrada. Adicione uma nova!")
    }
}


// ListItem adaptado para usar a entidade Password e incluir botão de delete
@Composable
fun ListItem(
    password: Password, // Alterado de PasswordInfo para Password (entidade Room)
    onDelete: () -> Unit
    // onClick: () -> Unit // Se quiser que o item inteiro seja clicável para edição
) {
    // Usando Card para um visual melhor
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
        // .clickable(onClick = onClick) // Adicione se o card for clicável
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image( // Sua imagem, mantida
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Verifique se este drawable existe
                contentDescription = "Logo",
                modifier = Modifier
                    .height(50.dp) // Ajuste o tamanho conforme necessário
                    .padding(end = 16.dp)
            )
            Column(
                modifier = Modifier.weight(1f) // Ocupa o espaço restante antes do botão
            ) {
                Text(password.name, style = MaterialTheme.typography.titleMedium)
                Text(password.login, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Excluir Senha",
                    tint = MaterialTheme.colorScheme.error // Dando uma cor de "perigo"
                )
            }
            // Seu ícone de seta, se ainda for necessário (talvez para navegação para edição)
            /*
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Editar", // Ou "Detalhes"
                modifier = Modifier.clickable ( onClick = onClick )
            )
            */
        }
    }
}


@Composable
fun AddPasswordDialog( // Copiado da minha sugestão anterior, parece bom
    onDismiss: () -> Unit,
    onSavePassword: (name: String, login: String, pass: String, notes: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") } // Renomeado para não conflitar com o tipo Password
    var notes by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Nova Senha") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Nome do Serviço") })
                Spacer(Modifier.height(8.dp))
                TextField(value = login, onValueChange = { login = it }, label = { Text("Login/Email") })
                Spacer(Modifier.height(8.dp))
                TextField(value = passwordValue, onValueChange = { passwordValue = it }, label = { Text("Senha") })
                Spacer(Modifier.height(8.dp))
                TextField(value = notes, onValueChange = { notes = it }, label = { Text("Notas (Opcional)") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && login.isNotBlank() && passwordValue.isNotBlank()) {
                        onSavePassword(name, login, passwordValue, notes.ifBlank { null })
                    } else {
                        android.widget.Toast.makeText(context, "Nome, Login e Senha são obrigatórios.", android.widget.Toast.LENGTH_LONG).show()
                    }
                }
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


// Preview (opcional, mas útil)
@Preview(showBackground = true)
@Composable
fun PasswordListScreenPreview() {
    // Para o preview funcionar bem, você precisaria de um ListViewModel mockado ou
    // um estado de preview. Por simplicidade, vou mostrar um estado fixo.
    val previewState = ListViewState(
        passwords = listOf(
            Password(id = 1, name = "Site Legal", login = "usuario@email.com", password = "123", notes = "Lembrar de trocar"),
            Password(id = 2, name = "Outro App", login = "meu_user", password = "asd", notes = null)
        ),
        isLoading = false,
        error = null
    )
    MaterialTheme { // Envolva com seu tema para o preview
        PasswordListScreenContentForPreview(previewState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordListScreenContentForPreview(uiState: ListViewState) {
    // Uma versão simplificada do Scaffold para o preview
    Scaffold(
        topBar = { TopAppBar(title = { Text("Minhas Senhas (Preview)") }) },
        floatingActionButton = { AddButton(onClick = { }) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            ListItemContent(
                listState = uiState,
                onDeletePassword = {}
            )
        }
    }
}

