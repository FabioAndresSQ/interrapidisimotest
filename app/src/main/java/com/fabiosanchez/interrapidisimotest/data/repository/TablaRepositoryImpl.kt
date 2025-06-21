package com.fabiosanchez.interrapidisimotest.data.repository

import android.util.Log
import com.fabiosanchez.interrapidisimotest.data.remote.TablaResponse
import com.fabiosanchez.interrapidisimotest.data.remote.TablasApi
import retrofit2.Response
import javax.inject.Inject

// Repositorio que contiene la logica para obtener las tablas desde el backend
class TablaRepositoryImpl @Inject constructor(
    private val tablasApi: TablasApi
){
    // Llama al endpoint y retorna la respuesta con la lista de tablas
    suspend fun getTablas(usuario: String): Response<List<TablaResponse>> {
        Log.d("Tablas", "Tablas request: $usuario")
        return tablasApi.tablas(usuario)
    }
}