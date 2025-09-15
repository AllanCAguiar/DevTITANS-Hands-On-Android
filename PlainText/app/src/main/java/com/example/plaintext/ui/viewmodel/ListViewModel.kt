package com.example.plaintext.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.model.Password // entidade do banco de dados
import com.example.plaintext.data.repository.PasswordDBStore // A interface para repositório
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update // Função de extensão útil para atualizar StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Representa o estado da tela de listagem de senhas.
 * É uma data class para facilitar a criação de novas instâncias de estado de forma imutável.
 *
 * @property passwords A lista atual de senhas a ser exibida.
 * @property isLoading Indica se os dados estão sendo carregados.
 * @property error Mensagem de erro, caso ocorra algum problema.
 */
data class ListViewState(
    val passwords: List<Password> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel para a tela que exibe a lista de senhas.
 *
 * Utiliza Hilt para injeção de dependência do [PasswordDBStore].
 * Observa as mudanças na lista de senhas do banco de dados e atualiza o [ListViewState].
 */
@HiltViewModel
class ListViewModel @Inject constructor(
    private val passwordDBStore: PasswordDBStore // Injetamos a interface, não a implementação
) : ViewModel() {

    // _listViewState é um MutableStateFlow privado que só o ViewModel pode modificar.
    // Ele guarda o estado atual da tela.
    private val _listViewState = MutableStateFlow(ListViewState())

    // listViewState é um StateFlow público e imutável que a UI pode observar.
    // A UI coleta deste Flow para receber atualizações de estado.
    val listViewState: StateFlow<ListViewState> = _listViewState.asStateFlow()

    init {
        // Ao iniciar o ViewModel, começamos a observar a lista de senhas.
        observePasswordList()
    }

    /**
     * Inicia a observação do Flow de senhas fornecido pelo passwordDBStore.
     * Atualiza o _listViewState conforme novas listas são emitidas ou erros ocorrem.
     */
    private fun observePasswordList() {
        // Marcarcarregamento começou.
        _listViewState.update { currentState ->
            currentState.copy(isLoading = true, error = null)
        }

        // passwordDBStore.getAllPasswords() deve retornar um Flow<List<Password>>.
        // Este Flow emitirá uma nova lista sempre que os dados no banco mudarem.
        passwordDBStore.getAllPasswords()
            .onEach { passwordsFromDb ->
                // Quando uma nova lista é emitida pelo Flow,
                // atualizamos nosso estado com os novos dados.
                _listViewState.update { currentState ->
                    currentState.copy(
                        passwords = passwordsFromDb,
                        isLoading = false, // O carregamento terminou
                        error = null       // Limpamos qualquer erro anterior
                    )
                }
            }
            .catch { exception ->
                // Se ocorrer um erro durante a coleta do Flow (ex: problema no banco),
                // capturamos a exceção e atualizamos o estado com a mensagem de erro.
                _listViewState.update { currentState ->
                    currentState.copy(
                        isLoading = false, // O carregamento falhou
                        error = exception.localizedMessage ?: "Ocorreu um erro desconhecido ao buscar as senhas."
                    )
                }
            }
            .launchIn(viewModelScope) // Inicia a coleta do Flow no escopo do ViewModel.
        // A coleta será cancelada automaticamente quando o ViewModel for destruído.
    }

    /**
     * Adiciona uma nova senha ao banco de dados.
     *
     * @param name O nome ou título da senha.
     * @param login O login/usuário associado.
     * @param pass A senha em si.
     * @param notes Anotações opcionais.
     */
    fun addPassword(name: String, login: String, pass: String, notes: String?) {
        // As operações de banco de dados devem ser feitas em uma coroutine.
        viewModelScope.launch {
            try {
                val newPassword = Password(
                    // O 'id' é gerado automaticamente pelo Room, então não precisamos definir aqui.
                    name = name,
                    login = login,
                    password = pass,
                    notes = notes
                )
                passwordDBStore.insertPassword(newPassword)
                // Após a inserção, o Flow de getAllPasswords() deverá emitir
                // automaticamente a lista atualizada, e o observePasswordList()
                // atualizará o _listViewState. Não precisamos fazer nada aqui para forçar a atualização da UI.
            } catch (e: Exception) {
                // Se a inserção falhar, atualizamos o estado com uma mensagem de erro.
                _listViewState.update { currentState ->
                    currentState.copy(error = e.localizedMessage ?: "Falha ao adicionar a senha.")
                }
            }
        }
    }

    /**
     * Exclui uma senha do banco de dados.
     *
     * @param password A entidade Password a ser excluída.
     */
    fun deletePassword(password: Password) {
        viewModelScope.launch {
            try {
                passwordDBStore.deletePassword(password)
                // Similar à adição, o Flow de getAllPasswords() cuidará de atualizar a UI.
            } catch (e: Exception) {
                _listViewState.update { currentState ->
                    currentState.copy(error = e.localizedMessage ?: "Falha ao excluir a senha.")
                }
            }
        }
    }


}
