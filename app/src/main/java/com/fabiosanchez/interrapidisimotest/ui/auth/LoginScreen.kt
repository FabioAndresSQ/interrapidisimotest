package com.fabiosanchez.interrapidisimotest.ui.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.fabiosanchez.interrapidisimotest.navigation.Home

@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {

    val context = LocalContext.current

    // Verificar la versión cuando la pantalla se carga
    LaunchedEffect(Unit) {
        viewModel.checkAppVersion(context)
    }

    // Escuchar y mostrar mensajes tipo Toast emitidos desde el ViewModel
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Mostrar un loading mientras se hace verifica la version
    val versionMessage by viewModel.versionMessage.collectAsState()
    val loginState by viewModel.loginState.collectAsState()

    if (loginState is LoginState.Loading) {
        Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
        }
    } else {
        if (versionMessage.isNullOrEmpty()) {
            LoginView(navController, viewModel)
        } else {
            WrongVersionView(navController, versionMessage!!, viewModel)
        }
    }




}

@Composable
fun LoginView(navController: NavHostController, viewModel: LoginViewModel){


    val user by viewModel.user.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginState by viewModel.loginState.collectAsState()

    // Si hay un login exitoso, redirigir automáticamente al Home
    LaunchedEffect(Unit) {
        if (loginState !is LoginState.Idle) {
            viewModel.getUser {
                navController.navigate(Home)
            }
        }

    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Iniciar Sesion", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = user,
            onValueChange = {
                viewModel.onUserChange(it)
            },
            modifier = Modifier.fillMaxWidth(0.8f),
            label = { Text(text = "Usuario") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            supportingText = {Text(text = "Test User: cGFtLm1lcmVkeTIx - Agrega \\n para error")}
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                viewModel.onPasswordChange(it)
            },
            modifier = Modifier.fillMaxWidth(0.8f),
            label = { Text(text = "Contraseña") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            supportingText = {Text(text = "Test Password: SW50ZXIyMDIx - Agrega \\n para error")}
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.login() {
                    navController.navigate(Home)
                }
            },
            enabled = loginState !is LoginState.LoginIn,
            modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
        ) {
            // Mostrar loading dentro del botón
            if (loginState is LoginState.LoginIn) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text(text = "Iniciar Sesión")
            }

        }
    }
}

@Composable
fun WrongVersionView(navController: NavHostController, versionMessage: String, viewModel: LoginViewModel) {

    Dialog(
        onDismissRequest = {} // En blanco apra que no se pueda cerrar tocando fuera
    ) {
        Card (
            modifier = Modifier.fillMaxWidth(0.9f)
        ){
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Atención", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar el mensaje de versión incorrecta
                Text(text = versionMessage, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de actualización (acción por definir)
                Button(
                    onClick = {
                        viewModel.showToast("Por definir Actualizacion de la app")
                        viewModel.updateVersionMessage("")
                        viewModel.getUser {
                            navController.navigate(Home)
                        }
                              },
                    modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
                ) {
                    Text(text = "Actualizar")
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}