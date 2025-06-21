package com.fabiosanchez.interrapidisimotest.di

import android.app.Application
import androidx.room.Room
import com.fabiosanchez.interrapidisimotest.data.local.AppDatabase
import com.fabiosanchez.interrapidisimotest.data.local.dao.TablaDao
import com.fabiosanchez.interrapidisimotest.data.local.dao.UserDao
import com.fabiosanchez.interrapidisimotest.data.remote.AuthApi
import com.fabiosanchez.interrapidisimotest.data.remote.TablasApi
import com.fabiosanchez.interrapidisimotest.data.remote.VersionApi
import com.fabiosanchez.interrapidisimotest.data.repository.AuthRepositoryImpl
import com.fabiosanchez.interrapidisimotest.data.repository.TablaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Provee una instancia de Retrofit para llamadas HTTP
    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://apitesting.interrapidisimo.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Provee la implementación de la API para obtener la versión
    @Provides
    @Singleton
    fun provideVersionApi(retrofit: Retrofit) : VersionApi {
        return retrofit.create(VersionApi::class.java)
    }

    // Provee la implementación de la API de autenticación
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit) : AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    // Provee el repositorio de autenticación
    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi) : AuthRepositoryImpl {
        return AuthRepositoryImpl(authApi)
    }

    // Provee una instancia de la base de datos local usando Room
    @Provides
    @Singleton
    fun provideAppDatabase(appContext: Application): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    // Provee el DAO para acceder a la tabla de usuarios
    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    // Provee la implementación de la API para obtener las tablas
    @Provides
    @Singleton
    fun provideTablasApi(retrofit: Retrofit) : TablasApi {
        return retrofit.create(TablasApi::class.java)
    }

    // Provee el repositorio de tablas
    @Provides
    @Singleton
    fun provideTablasRepository(tablasApi: TablasApi) : TablaRepositoryImpl {
        return TablaRepositoryImpl(tablasApi)
    }

    // Provee el DAO para acceder a la tabla de tablas
    @Provides
    @Singleton
    fun provideTablaDao(db: AppDatabase): TablaDao = db.tablaDao()


}