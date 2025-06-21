package com.fabiosanchez.interrapidisimotest.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fabiosanchez.interrapidisimotest.navigation.Localidad
import com.fabiosanchez.interrapidisimotest.navigation.Login
import com.fabiosanchez.interrapidisimotest.navigation.Tablas

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {

    val homeState by viewModel.homeState.collectAsState()
    val user by viewModel.user.collectAsState()

    // Cargar usuario al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.getUser()
    }

    // Mostrar indicador de carga si esta cargando
    if (homeState is HomeState.Loading) {
        Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
        }
    } else {
        // Contenido principal si hay usuario activo
        if (user != null) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Home Screen", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(32.dp))

                InfoRowHome("Nombre", user?.name)

                Spacer(modifier = Modifier.height(16.dp))

                InfoRowHome("Identificacion", user?.identification)

                Spacer(modifier = Modifier.height(16.dp))

                InfoRowHome("Usuario", user?.user)

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        navController.navigate(Tablas)
                    },
                    modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
                ) {
                    Text(text = "Tablas")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate(Localidad)
                    },
                    modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
                ) {
                    Text(text = "Localidades")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = {
                    viewModel.logout() {
                        navController.navigate(Login) { popUpTo(0) }
                    } },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text(text = "Cerrar Sesion")
                }
            }
        } else {
            // Vista alternativa si no hay usuario (No deberia pasar pero igual cubrimos el caso)
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Home Screen", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Usuario no encontrado")
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    viewModel.logout() {
                        navController.navigate(Login) { popUpTo(0) }
                    }
                }) {
                    Text(text = "Cerrar Sesion")
                }
            }
        }
    }
}

// Componente auxiliar para mostrar filas de texto y evitar repetitividad
@Composable
fun InfoRowHome(label: String, value: String?) {
    Row {
        Text("$label: ", fontWeight = FontWeight.Bold)
        Text(value ?: "No disponible")
    }
}