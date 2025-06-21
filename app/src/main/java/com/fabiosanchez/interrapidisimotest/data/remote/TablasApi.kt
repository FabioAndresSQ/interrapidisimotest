package com.fabiosanchez.interrapidisimotest.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

// Modelo de datos para la solicitud de las tablas
data class TablasRequest(
    val Usuario: String
)

// Modelo de datos para la respuesta del las tablas
data class TablaResponse(
    @SerializedName("NombreTabla") val nombreTabla: String,
    @SerializedName("Pk") val pk: String,
    @SerializedName("QueryCreacion") val queryCreacion: String,
    @SerializedName("BatchSize") val batchSize: Int,
    @SerializedName("Filtro") val filtro: String?,
    @SerializedName("Error") val error: String?,
    @SerializedName("NumeroCampos") val numeroCampos: Int,
    @SerializedName("MetodoApp") val metodoApp: String?,
    @SerializedName("FechaActualizacionSincro") val fechaActualizacionSincro: String
)

// API de obtencion de tablas utilizando Retrofit
interface TablasApi {
    // Endpoint para obtener las tablas
    @GET("apicontrollerpruebas/api/SincronizadorDatos/ObtenerEsquema/true")
    suspend fun tablas(@Header("Usuario") usuario: String): Response<List<TablaResponse>>
}