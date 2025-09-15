package com.example.plaintext.ui.screens.hello


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.ui.screens.Screen // Verifique se este import é necessário e correto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject // Importe @Inject

// DbSimulator agora pode ser injetado pelo Hilt
class DbSimulator @Inject constructor() { // <--- @Inject constructor() ADICIONADO AQUI
    private val datalist = mutableListOf<String>()

    init {
        for (i in 1..100) {
            datalist.add("devtitans #$i")
        }
    }

    fun getData(): Flow<List<String>> = flow {
        delay(5000) // Simula um atraso de rede/DB
        emit(datalist)
    }
}

// Estado para a UI da tela Hello
data class listViewState( // Considere renomear para HelloViewState ou algo mais específico para esta tela
    var listState: List<String> = emptyList(),
    var size: Int = 0
)

// ViewModel para a tela Hello
@HiltViewModel
class ListViewModel @Inject constructor(
    private val dbSimulator: DbSimulator // Hilt agora sabe como fornecer DbSimulator
) : ViewModel() {
    var listState by mutableStateOf(listViewState())
        private set

    init {
        // Inicia a coleta de dados quando o ViewModel é criado
        collectData()
    }

    fun collectData() {
        viewModelScope.launch {
            dbSimulator.getData().collect { dataFromSimulator ->
                listState = listState.copy(listState = dataFromSimulator, size = dataFromSimulator.size)
            }
        }
    }
}

// Composable principal para a tela Hello
@Composable
fun Hello_screen(modifier: Modifier = Modifier, viewModel: ListViewModel = hiltViewModel()) {
    val currentUiState: listViewState = viewModel.listState // Renomeado para evitar conflito com o tipo

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (currentUiState.listState.isEmpty()) { // Verificando se a lista está vazia
            Text("Carregando dados da tela Hello...", fontSize = 20.sp)
        } else {
            Column { // Removido parênteses desnecessários
                Text(
                    text = "Total de itens (Hello Screen): ${currentUiState.size}",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red) // Apenas para destaque
                        .padding(16.dp)
                )
                LazyColumn(modifier = Modifier.weight(1f)) { // Adicionado weight para preencher o espaço restante
                    items(currentUiState.listState.size) { index ->
                        Text(
                            text = currentUiState.listState[index],
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// Sobrecarga do Composable para integração com navegação (se Screen.Hello for um argumento de navegação)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Hello_screen(args: Screen.Hello) { // Verifique se Screen.Hello é realmente usado/necessário
    Scaffold { innerPadding ->
        Hello_screen(modifier = Modifier.padding(innerPadding))
    }
}
