package com.fabiosanchez.interrapidisimotest.data.repository

import com.fabiosanchez.interrapidisimotest.data.remote.LocalidadApi
import com.fabiosanchez.interrapidisimotest.data.remote.LocalidadResponse
import retrofit2.Response
import javax.inject.Inject

// Repositorio que contiene la logica para obtener las localidades desde el backend
class LocalidadRepositoryImpl @Inject constructor(
    private val localidadApi: LocalidadApi
) {
    // Llama al endpoint y retorna la respuesta con la lista de localidades
    suspend fun getLocalidades(): Response<List<LocalidadResponse>> {
        return localidadApi.getLocalidades()
    }
}