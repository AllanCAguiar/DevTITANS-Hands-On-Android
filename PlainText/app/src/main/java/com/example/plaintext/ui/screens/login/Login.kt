package com.example.plaintext.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plaintext.R
import com.example.plaintext.ui.theme.PlainTextTheme
import com.example.plaintext.ui.viewmodel.LoginState

@Composable
fun Login_screen(
    loginState: LoginState,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRememberMeChanged: (Boolean) -> Unit,
    onLoginClicked: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBarComponent(
                navigateToSettings = navigateToSettings, navigateToAbout = {})
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
                    .fillMaxWidth()
                    .background(Color(120, 160, 20)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo"
                )

                Text(
                    text = stringResource(R.string.textLogotype),
                    fontSize = 14.sp,
                    color = Color.White,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(end = 8.dp)
                )
            }

            Text(
                text = stringResource(R.string.textInfo),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 40.dp, bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.labelLogin) + ":",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(70.dp)
                        .padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = loginState.login,
                    onValueChange = onLoginChanged,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.labelPassword) + ":",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(70.dp)
                        .padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = loginState.password,
                    onValueChange = onPasswordChanged,
                    modifier = Modifier.padding(end = 16.dp),
                    visualTransformation = if (loginState.password.isEmpty()) VisualTransformation.None else PasswordVisualTransformation(),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp)
            ) {
                Checkbox(
                    checked = loginState.rememberMe,
                    onCheckedChange = onRememberMeChanged,
                )
                Text(
                    text = stringResource(R.string.checkboxSave),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Button(
                onClick = {
                    onLoginClicked()
                },
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(2.dp, Color.Blue),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White,
                ),
                elevation = ButtonDefaults.buttonElevation(8.dp),
                enabled = true
            ) {
                Text(stringResource(R.string.buttonLogin))
            }
        }
    }
}

@Composable
fun MyAlertDialog(shouldShowDialog: MutableState<Boolean>) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
            shouldShowDialog.value = false
        },

            title = { Text(text = stringResource(R.string.textAbout)) },
            text = { Text(text = stringResource(R.string.textVersion)) },
            confirmButton = {
                Button(
                    onClick = { shouldShowDialog.value = false }) {
                    Text(text = "Ok")
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(
    navigateToSettings: (() -> Unit?)? = null,
    navigateToAbout: (() -> Unit?)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val shouldShowDialog = remember { mutableStateOf(false) }

    if (shouldShowDialog.value) {
        MyAlertDialog(shouldShowDialog = shouldShowDialog)
    }

    TopAppBar(title = { Text(stringResource(R.string.textTitle)) }, actions = {
        if (navigateToSettings != null && navigateToAbout != null) {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.textSettings)) }, onClick = {
                    navigateToSettings()
                    expanded = false
                }, modifier = Modifier.padding(8.dp)
                )
                DropdownMenuItem(
                    text = {
                    Text(stringResource(R.string.textAbout))
                }, onClick = {
                    shouldShowDialog.value = true
                    expanded = false
                }, modifier = Modifier.padding(8.dp)
                )
            }
        }
    })
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PlainTextTheme {
        Login_screen(
            loginState = LoginState(),
            onLoginChanged = {},
            onPasswordChanged = {},
            onRememberMeChanged = {},
            onLoginClicked = {},
            navigateToSettings = {})
    }
}