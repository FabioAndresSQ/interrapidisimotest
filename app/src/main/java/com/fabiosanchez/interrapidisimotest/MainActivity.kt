package com.fabiosanchez.interrapidisimotest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.fabiosanchez.interrapidisimotest.navigation.AppNavigation
import com.fabiosanchez.interrapidisimotest.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

// MainActivity es la actividad principal de la aplicacion
// Aquí se configura Jetpack Compose como sistema de UI, y se inicia el sistema de navegacion

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicia Jetpack Compose con el tema personalizado de la app
        setContent {
            AppTheme {
                // Inicia la navegación con el controlador de navegación
                AppNavigation(
                    navController = rememberNavController(),
                )
            }
        }
    }
}
