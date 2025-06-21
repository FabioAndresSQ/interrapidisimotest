package com.fabiosanchez.interrapidisimotest.data.remote

import com.google.gson.annotations.SerializedName

// Modelo de datos para la respuesta del endpoint de las localidades
data class LocalidadResponse (
    @SerializedName("AbreviacionCiudad") val AbreviacionCiudad: String,
    @SerializedName("NombreCompleto") val NombreCompleto: String
)