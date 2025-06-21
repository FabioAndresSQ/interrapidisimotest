package com.fabiosanchez.interrapidisimotest.ui.table

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiosanchez.interrapidisimotest.data.local.dao.TablaDao
import com.fabiosanchez.interrapidisimotest.data.local.dao.UserDao
import com.fabiosanchez.interrapidisimotest.data.local.entity.TablaEntity
import com.fabiosanchez.interrapidisimotest.data.local.entity.UserEntity
import com.fabiosanchez.interrapidisimotest.data.repository.TablaRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TablaViewModel @Inject constructor(
    private val tablaRepository: TablaRepositoryImpl,
    private val tablaDao: TablaDao,
    private val userDao: UserDao
) : ViewModel() {

    // Estado de las tablas (Loading, Success, Error)
    private val _tableState = MutableStateFlow<TablaState>(TablaState.Loading)
    val tableState: StateFlow<TablaState> = _tableState

    // Usuario actual para obtener sus tablas
    private val _currentUser = MutableStateFlow<UserEntity?>(null)

    // Funcion para obtener el usuario actual y las tablas correspondientes
    fun getCurrentUserTables(){
        viewModelScope.launch {
            _tableState.value = TablaState.Loading
            val user = userDao.getUser()
            if (user.isNotEmpty()) {
                _currentUser.value = user[0]
                getTables(user[0]!!.user)
            } else {
                _tableState.value = TablaState.Error("No se detecto ningun usuario activo")
            }
        }
    }

    // Obtiene las tablas del usuario actual
    private fun getTables(user: String) {
        viewModelScope.launch {

            val localTables = getLocalTables() // Obtiene las tablas locales

            if (localTables.isNotEmpty()) {
                // Si hay tablas locales, las muestra y evita la llamada al servidor
                _tableState.value = TablaState.Success(localTables)
                return@launch

            } else {
                // Si no hay tablas locales, las obtiene del servidor y las guarda en la base de datos local
                try {
                    val response = tablaRepository.getTablas(user)
                    var tablesList = emptyList<TablaEntity>()
                    if (response.isSuccessful) {
                        val tables = response.body()
                        if (tables != null) {
                            // Guarda las tablas en la base de datos local
                            tables.forEach { table ->
                                val tablaEntity = TablaEntity(
                                    nombreTabla = table.nombreTabla,
                                    pk = table.pk,
                                    queryCreacion = table.queryCreacion,
                                    batchSize = table.batchSize,
                                    numeroCampos = table.numeroCampos,
                                    fechaActualizacionSincro = table.fechaActualizacionSincro,
                                    filtro = table.filtro,
                                    error = table.error,
                                    metodoApp = table.metodoApp
                                    )
                                tablesList = tablesList + tablaEntity
                                tablaDao.insertTabla(tablaEntity)
                            }
                            _tableState.value = TablaState.Success(tablesList)
                        } else {
                            _tableState.value = TablaState.Error("No se encontraron tablas")
                            }
                    } else {
                        _tableState.value = TablaState.Error("Error en la respuesta del servidor: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _tableState.value = TablaState.Error("Error al obtener las tablas: ${e.message}")
                    Log.d("Error", "Error al obtener las tablas: ${e.message}")
                }
            }

        }

    }

    // Funcion que obtiene las tablas locales de la base de datos
    private suspend fun getLocalTables(): List<TablaEntity?> {
        return try {
            tablaDao.getTables()
        } catch (e: Exception) {
            _tableState.value = TablaState.Error("Error al obtener las tablas locales: ${e.message}")
            Log.d("Error", "Error al obtener las tablas locales: ${e.message}")
            emptyList()
        }
    }

    // Funcion para reintentar obtener las tablas
    fun setupTables() {
        _currentUser.value?.let {
            // Limpia local si deseas
            viewModelScope.launch {
                tablaDao.deleteTables()
                getTables(it.user)
            }
        } ?: run {
            _tableState.value = TablaState.Error("No hay usuario activo para reintentar.")
        }
    }
}

// Clase con los estados de las tablas
sealed class TablaState{
    object Loading : TablaState()
    data class Success(val tables: List<TablaEntity?>) : TablaState()
    data class Error(val message: String) : TablaState()
}