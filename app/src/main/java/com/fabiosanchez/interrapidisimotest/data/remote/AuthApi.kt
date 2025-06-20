package com.fabiosanchez.interrapidisimotest.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Modelo de datos para la solicitud de login
data class LoginRequest(
    val Usuario: String,
    val Password: String,
    val Mac : String,
    val NomAplicacion : String,
    val Path : String)

// Modelo de datos para la respuesta del login (Solo los necesarios segun el ejercicio)
data class LoginResponse(
    val Usuario: String,
    val Identificacion: String,
    val Nombre: String
)

// API de autenticación utilizando Retrofit
interface AuthApi {
    // Endpoint para autenticar al usuario en la aplicación
    @POST("FtEntregaElectronica/MultiCanales/ApiSeguridadPruebas/api/Seguridad/AuthenticaUsuarioApp")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}