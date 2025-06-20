package com.fabiosanchez.interrapidisimotest.data.remote

import retrofit2.Response
import retrofit2.http.GET

// Interfaz que define el endpoint para obtener la versión remota de la aplicacion
// Este metodo hace una solicitud GET al backend para consultar la version
interface VersionApi {
    @GET("apicontrollerpruebas/api/ParametrosFramework/ConsultarParametrosFramework/VPStoreAppControl")
    suspend fun getVersion(): Response<String>
}