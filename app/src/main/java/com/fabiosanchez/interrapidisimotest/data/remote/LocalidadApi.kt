package com.fabiosanchez.interrapidisimotest.data.remote

import retrofit2.Response
import retrofit2.http.GET

// API de obtencion de localidades utilizando Retrofit
interface LocalidadApi {
    // Endpoint para obtener las localidades
    @GET("apicontrollerpruebas/api/ParametrosFramework/ObtenerLocalidadesRecogidas")
    suspend fun getLocalidades(): Response<List<LocalidadResponse>>
}