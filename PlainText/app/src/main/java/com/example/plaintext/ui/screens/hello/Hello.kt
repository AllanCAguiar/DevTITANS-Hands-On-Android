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
import com.example.plaintext.ui.screens.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DbSimulator() {
    private val datalist = mutableListOf<String>();

    init {
        for (i in 1..100) {
            datalist.add("devtitans #$i");
        }

    }

    fun getData(): Flow<List<String>> = flow {
        delay(5000)
        emit(datalist)
    }
}

data class listViewState(
    var listState: List<String> = emptyList(),
    var size: Int = 0
)

@HiltViewModel
class ListViewModel @Inject constructor(
    private val dbSimulator: DbSimulator
) : ViewModel() {
    var listState by mutableStateOf(listViewState())
        private set

    init {
        viewModelScope.launch {
            collectData()
        }
    }

    fun collectData() {
        viewModelScope.launch {
            dbSimulator.getData().collect {
                listState = listState.copy(listState = it, size = it.size)
            }
        }
    }
}

@Composable
fun Hello_screen(modifier: Modifier, viewModel: ListViewModel = hiltViewModel()) {
    val listViewState: listViewState = viewModel.listState

    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        if (listViewState.listState.size == 0) {
            Text("Carregando...", fontSize = 20.sp)
        } else {
            Column() {
                Text(
                    text = "Total de itens: ${listViewState.size}",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                        .padding(16.dp)
                )
                LazyColumn {
                    items(listViewState.listState.size) { index ->
                        Text(
                            text = listViewState.listState[index],
                            fontSize = 20.sp,
                            modifier = Modifier.padding(16.dp).fillMaxWidth()
                        )
                    }
                }

            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Hello_screen(args: Screen.Hello) {
    Scaffold { padding ->
        Hello_screen(Modifier.padding(padding))
    }
}
