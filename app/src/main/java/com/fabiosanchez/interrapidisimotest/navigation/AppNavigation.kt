package com.fabiosanchez.interrapidisimotest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fabiosanchez.interrapidisimotest.ui.auth.LoginScreen
import com.fabiosanchez.interrapidisimotest.ui.home.HomeScreen
import kotlinx.serialization.Serializable

// Composable que define la navegación principal de la aplicación
// navController es el Controlador de navegación que permite movernos entre pantallas

@Composable
fun AppNavigation(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Login,
    ){
        composable<Login> {
            LoginScreen(navController)
        }
        composable<Home> {
            HomeScreen(navController)
        }
        composable<Tablas> {
            //TablasScreen(navController)
        }
        composable<Localidad> {
            //LocalidadScreen(navController)
        }


    }
}

//Objetos que representan las rutas de navegación de cada pantalla
@Serializable
object Login

@Serializable
object Home

@Serializable
object Tablas

@Serializable
object Localidad