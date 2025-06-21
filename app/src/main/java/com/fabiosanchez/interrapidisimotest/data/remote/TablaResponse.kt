package com.fabiosanchez.interrapidisimotest.data.remote

import com.google.gson.annotations.SerializedName

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