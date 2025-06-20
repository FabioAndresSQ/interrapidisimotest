package com.fabiosanchez.interrapidisimotest.data.repository

import android.util.Log
import com.fabiosanchez.interrapidisimotest.data.remote.AuthApi
import com.fabiosanchez.interrapidisimotest.data.remote.LoginRequest
import com.fabiosanchez.interrapidisimotest.data.remote.LoginResponse
import retrofit2.Response
import javax.inject.Inject

// Implementación del repositorio de autenticación
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
){
    // Realiza la llamada de inicio de sesión al backend
    suspend fun login(username: String, password: String): Response<LoginResponse> {
        val mac = "" // Valor de MAC (es vacío segun el ejercicio pero se puede implementar lógica más adelante)
        val nomAplicacion = "Controller APP" // Nombre de la aplicación
        val path = "" // Ruta (es vacío segun el ejercicio pero se puede implementar lógica más adelante)

        Log.d("Login", "Login request: $username, $password, $mac, $nomAplicacion, $path")

        // Realiza la solicitud al endpoint
        return authApi.login(LoginRequest(username, password, mac, nomAplicacion, path))
    }
}