package com.fabiosanchez.interrapidisimotest.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

// Modelo de datos para la solicitud de las tablas en caso de ser necesario
data class TablasRequest(
    val Usuario: String
)

// API de obtencion de tablas utilizando Retrofit
interface TablasApi {
    // Endpoint para obtener las tablas
    @GET("apicontrollerpruebas/api/SincronizadorDatos/ObtenerEsquema/true")
    suspend fun tablas(@Header("Usuario") usuario: String): Response<List<TablaResponse>>
}