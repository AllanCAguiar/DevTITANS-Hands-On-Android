package com.example.plaintext.ui.screens.editList

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plaintext.R
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.Screen
import com.example.plaintext.ui.screens.login.TopBarComponent

fun isPasswordEmpty(password: PasswordInfo): Boolean {
    return password.name.isEmpty() && password.login.isEmpty() && password.password.isEmpty()
}

@Composable
fun EditList(
    args: Screen.EditList, navigateBack: () -> Unit, savePassword: (password: PasswordInfo) -> Unit
) {

    var isEditMode = false
    if (args.password.name != "" && args.password.login != "" && args.password.password != "") {
        isEditMode = true
    }

    val nameState = remember {
        mutableStateOf(args.password.name)
    }
    val loginState = remember {
        mutableStateOf(args.password.login)
    }
    val passwordState = remember {
        mutableStateOf(args.password.password)
    }
    val notesState = remember {
        mutableStateOf(args.password.notes ?: "")
    }

    Scaffold(
        topBar = {
            TopBarComponent()
        }) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .background(Color(120, 160, 20)),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val message =
                    if (isEditMode) stringResource(R.string.textEditPassword) else stringResource(R.string.textCreatePassword)
                Text(
                    text = message,
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(top = 12.dp, bottom = 12.dp, start = 30.dp)
                )
            }

            EditInput(
                textInputLabel = stringResource(R.string.textName), textInputState = nameState
            )
            EditInput(
                textInputLabel = stringResource(R.string.textUser), textInputState = loginState
            )
            EditInput(
                textInputLabel = stringResource(R.string.textPassword),
                textInputState = passwordState
            )
            EditInput(
                textInputLabel = stringResource(R.string.textNotes),
                textInputState = notesState,
                textInputHeight = 160
            )
            Button(
                onClick = {
                    val updatedPassword = args.password?.copy(
                        name = nameState.value,
                        login = loginState.value,
                        password = passwordState.value,
                        notes = notesState.value
                    ) ?: PasswordInfo(
                        name = nameState.value,
                        login = loginState.value,
                        password = passwordState.value,
                        notes = notesState.value,
                        id = args.password.id
                    )

                    savePassword(updatedPassword)
                    navigateBack()
                },
                modifier = Modifier.padding(24.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(2.dp, Color.Blue),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White,
                ),
                elevation = ButtonDefaults.buttonElevation(8.dp),
                enabled = true
            ) {
                Text(stringResource(R.string.buttonSave))
            }
        }
    }
}


@Composable
fun EditInput(
    textInputLabel: String,
    textInputState: MutableState<String> = mutableStateOf(""),
    textInputHeight: Int = 60
) {
    val padding: Int = 30

    var textState by rememberSaveable { textInputState }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(textInputHeight.dp)
            .padding(horizontal = padding.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            label = { Text(textInputLabel) },
            modifier = Modifier
                .height(textInputHeight.dp)
                .fillMaxWidth()
        )

    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Preview(showBackground = true)
@Composable
fun EditListPreview() {
    EditList(
        Screen.EditList(PasswordInfo(1, "Nome", "Usuário", "Senha", "Notas")),
        navigateBack = {},
        savePassword = {})
}

@Preview(showBackground = true)
@Composable
fun EditInputPreview() {
    EditInput("Nome")
}