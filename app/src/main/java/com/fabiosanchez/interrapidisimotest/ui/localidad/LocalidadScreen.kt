package com.fabiosanchez.interrapidisimotest.ui.localidad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fabiosanchez.interrapidisimotest.data.remote.LocalidadResponse

@Composable
fun LocalidadScreen(navController: NavController, viewModel: LocalidadViewModel = hiltViewModel()) {

    val localidadState = viewModel.localidadState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getLocalidades()
    }

    // Contenedor principal con condiciones según el estado
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp)
    ) {
        if (localidadState.value is LocalidadState.Loading) {
            CircularProgressIndicator()
        } else if (localidadState.value is LocalidadState.Success) {
            val localidades = (localidadState.value as LocalidadState.Success).localidades

            LazyColumn (
                modifier = Modifier.fillMaxSize(),
            ){
                item {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(text = "Listado de Localidades", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                items(localidades) { localidad ->
                    LocalidadItemCard(localidad)
                }
            }
        } else {
            val errorMessage = (localidadState.value as LocalidadState.Error).message
            Text(text = errorMessage)
            Button(onClick = {
                viewModel.setupLocalidades()
            }) {
                Text(text = "Reintentar")
            }
        }
    }
}

@Composable
fun LocalidadItemCard(localidad: LocalidadResponse) {

    Spacer(modifier = Modifier.height(8.dp))

    // Tarjeta que muestra los datos requeridos de la localidad (Nombre Completo y Abreviacion)
    Card (
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Column (
            modifier = Modifier.padding(16.dp).fillMaxSize()
        ){
            Text(text = "Nombre Completo: ", fontWeight = FontWeight.Bold)
            Text(text = localidad.NombreCompleto)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Abreviacion: ", fontWeight = FontWeight.Bold)
                Text(text = localidad.AbreviacionCiudad)
            }

        }
    }

    Spacer(modifier = Modifier.height(8.dp))

}