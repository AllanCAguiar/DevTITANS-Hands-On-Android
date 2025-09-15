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

    private val _listViewState = MutableStateFlow(ListViewState())
    val listViewState: StateFlow<ListViewState> = _listViewState.asStateFlow()

    init {
        observePasswordList()
    }

    /**
     * Inicia a observação do Flow de senhas fornecido pelo passwordDBStore.
     * Atualiza o _listViewState conforme novas listas são emitidas ou erros ocorrem.
     */
    private fun observePasswordList() {
        _listViewState.update { currentState ->
            currentState.copy(isLoading = true, error = null)
        }

        passwordDBStore.getList() // <<< CORRIGIDO DE getAllPasswords() PARA getList()
            .onEach { passwordsFromDb ->
                _listViewState.update { currentState ->
                    currentState.copy(
                        passwords = passwordsFromDb,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { exception ->
                _listViewState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = exception.localizedMessage ?: "Ocorreu um erro desconhecido ao buscar as senhas."
                    )
                }
            }
            .launchIn(viewModelScope)
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
        viewModelScope.launch {
            try {
                val newPassword = Password(
                    // O 'id' é gerado automaticamente pelo Room, então não precisamos definir aqui.
                    name = name,
                    login = login,
                    password = pass, // Lembre-se que no Password.kt corrigido, o campo 'password' da entidade pode ter sido renomeado (ex: password_text)
                    notes = notes
                )
                passwordDBStore.add(newPassword) // <<< CORRIGIDO DE insertPassword() PARA add()
                // Após a inserção, o Flow de getList() deverá emitir
                // automaticamente a lista atualizada se a implementação do repositório
                // e o DAO estiverem configurados para isso (o Room faz isso automaticamente).
            } catch (e: Exception) {
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
                // TODO: A interface PasswordDBStore não possui um método deletePassword(Password) ou delete(Password).
                //       Você precisará:
                //       1. Adicionar um método `delete(password: Password)` (ou similar com o ID) à interface PasswordDBStore.
                //       2. Implementar este método na classe que implementa PasswordDBStore (ex: LocalPasswordDBStore),
                //          fazendo a chamada ao DAO correspondente (ex: passwordDao.deletePassword(password)).
                //       3. Descomentar e usar a chamada abaixo:
                // passwordDBStore.delete(password) // Exemplo de chamada após adicionar o método à interface

                // Se você já tem um método de exclusão no DAO e quer usá-lo através de uma implementação
                // concreta que não seja a interface PasswordDBStore diretamente, você precisaria rever
                // a estrutura de injeção ou a própria interface.

                // Por enquanto, esta operação não pode ser completada com a interface PasswordDBStore atual.
                _listViewState.update { currentState ->
                    currentState.copy(error = "Função de exclusão não implementada na interface do repositório.")
                }
            } catch (e: Exception) {
                _listViewState.update { currentState ->
                    currentState.copy(error = e.localizedMessage ?: "Falha ao excluir a senha.")
                }
            }
        }
    }
}
