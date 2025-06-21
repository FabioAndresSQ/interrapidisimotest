package com.fabiosanchez.interrapidisimotest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad que representa un tabla obtenida del backend y almacenada en la base de datos local
@Entity(tableName = "tabla")
data class TablaEntity(
    val nombreTabla: String,
    @PrimaryKey val pk: String, // Clave primaria con el campo 'pk' ya que es un campo unico
    val queryCreacion: String,
    val filtro: String?,
    val error: String?,
    val metodoApp: String?,
    val batchSize: Int,
    val numeroCampos: Int,
    val fechaActualizacionSincro: String
)