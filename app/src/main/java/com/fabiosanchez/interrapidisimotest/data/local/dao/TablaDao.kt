package com.fabiosanchez.interrapidisimotest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fabiosanchez.interrapidisimotest.data.local.entity.TablaEntity

@Dao
interface TablaDao {
    // Inserta un campo de tabla en la base de datos y si ya existe lo reemplaza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTabla(tabla: TablaEntity)

    // Obtiene todos los datos guardados en la base de datos
    @Query("SELECT * FROM tabla")
    suspend fun getTables(): List<TablaEntity>

    // Elimina la tablas de la base de datos
    @Query("DELETE FROM tabla")
    suspend fun deleteTables()

}