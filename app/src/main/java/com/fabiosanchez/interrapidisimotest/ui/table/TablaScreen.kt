package com.fabiosanchez.interrapidisimotest.ui.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fabiosanchez.interrapidisimotest.data.local.entity.TablaEntity

// Componente para mostrar la lista de tablas se pasa navController para navegar a otra pantalla de ser necesario
// En este caso no se utiliza
@Composable
fun TablaScreen(navController: NavHostController, viewModel: TablaViewModel = hiltViewModel()){
    // Estado observable del ViewModel con las tablas
    val tableState = viewModel.tableState.collectAsState()

    // Se inicia al cargar la pantalla para obtener las tablas del usuario actual
    LaunchedEffect(Unit) {
        viewModel.getCurrentUserTables()
    }

    // Contenedor principal con condiciones según el estado
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp)
    ) {
        if (tableState.value is TablaState.Loading) {
            CircularProgressIndicator()
        } else if (tableState.value is TablaState.Success) {
            val tables = (tableState.value as TablaState.Success).tables

            LazyColumn (
                modifier = Modifier.fillMaxSize(),
            ){
                item {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Listado de Tablas", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Toca una tabla para ver sus detalles")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                items(tables) { table ->
                    TableItemCard(table)
                }
            }
        } else {
            val errorMessage = (tableState.value as TablaState.Error).message
            Text(text = errorMessage)
            Button(onClick = {
                viewModel.setupTables()
            }) {
                Text(text = "Actualizar")
            }
        }
    }
}

// Componente para mostrar resumen de cada tabla
@Composable
fun TableItemCard(table: TablaEntity?) {

    // Estado para mostrar el dialogo con detalles
    var showDetails = remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(8.dp))

    // Tarjeta que muestra resumen de cada tabla
    Card (
        onClick = {
            showDetails.value = true
        },
    ) {
        Column (
            modifier = Modifier.padding(16.dp).fillMaxSize()
        ){
            InfoRowTabla("Nombre Tabla", table?.nombreTabla)
            InfoRowTabla("PK", table?.pk)
            InfoRowTabla("Campos", table?.numeroCampos.toString())
            InfoRowTabla("Batch", table?.batchSize.toString())
            InfoRowTabla("Actualizacion", table?.fechaActualizacionSincro)
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Mostrar popup con los detalles completos de la tabla
    if (showDetails.value) {
        TableDetailsDialog(table) { showDetails.value = false }
    }
}

// Dialogo popup con los detalles completos de la tabla
@Composable
fun TableDetailsDialog(table: TablaEntity?, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ){
        Card (
            modifier = Modifier.fillMaxHeight(0.9f)
        ){
            val scrollState = rememberScrollState()
            Column (
                modifier = Modifier.padding(8.dp).verticalScroll(scrollState)
            ){
                Spacer(modifier = Modifier.height(16.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "Detalles de la tabla", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    IconButton(
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                InfoRowTabla("Nombre Tabla", table?.nombreTabla)

                Spacer(modifier = Modifier.height(16.dp))

                InfoRowTabla("PK", table?.pk)

                Spacer(modifier = Modifier.height(16.dp))

                InfoRowTabla("Campos", table?.numeroCampos.toString())

                Spacer(modifier = Modifier.height(16.dp))

                InfoRowTabla("Batch", table?.batchSize.toString())

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Fecha de Actualizacion Sincronica: ", fontWeight = FontWeight.Bold)
                Text(text = table?.fechaActualizacionSincro ?: "Fecha no disponible")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Query de creación: ", fontWeight = FontWeight.Bold)
                Text(text = table?.queryCreacion ?: "Query de creación no disponible")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Filtro: ", fontWeight = FontWeight.Bold)
                Text(text = table?.filtro ?: "Filtro no disponible")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Error: ", fontWeight = FontWeight.Bold)
                Text(text = table?.error ?: "Error no disponible")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Método de aplicación: ", fontWeight = FontWeight.Bold)
                Text(text = table?.metodoApp ?: "Método de aplicación no disponible")

            }
        }
    }
}

// Componente para mostrar una fila de informacion y evitar repetitividad
@Composable
fun InfoRowTabla(title: String, value: String?){
    Row (
        modifier = Modifier.fillMaxWidth()
    ){
        Text(text = "$title: ", fontWeight = FontWeight.Bold)
        Text(text = value ?: "No disponible")
    }
}
