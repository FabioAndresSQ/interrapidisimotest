package com.fabiosanchez.interrapidisimotest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fabiosanchez.interrapidisimotest.data.local.dao.UserDao
import com.fabiosanchez.interrapidisimotest.data.local.entity.UserEntity

// Base de datos principal de la aplicación que contiene las entidades y DAOs, de momento es solo el de User
@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // DAO para acceder a la tabla de usuarios
    abstract fun userDao(): UserDao
}