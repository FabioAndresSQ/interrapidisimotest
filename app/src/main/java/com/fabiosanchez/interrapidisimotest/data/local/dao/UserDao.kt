package com.fabiosanchez.interrapidisimotest.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fabiosanchez.interrapidisimotest.data.local.entity.UserEntity

@Dao
interface UserDao {
    // Inserta un usuario en la base de datos y si ya existe lo reemplaza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // Obtiene todos los usuarios guardados que en este caso, debería devolver solo uno solo ya que
    // solo trabajamos con un usuario a la vez
    @Query("SELECT * FROM user")
    suspend fun getUser(): List<UserEntity?>

    // Elimina un usuario específico de la base de datos
    @Delete
    suspend fun deleteUser(user: UserEntity)

}