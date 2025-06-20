package com.fabiosanchez.interrapidisimotest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad que representa la tabla de usuarios en la base de datos local
@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val identification: String, // Identificacion es unica para cada usuario por ende esta es la clave primaria
    val user: String,
    val name: String
)