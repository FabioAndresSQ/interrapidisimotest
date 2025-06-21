package com.fabiosanchez.interrapidisimotest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiosanchez.interrapidisimotest.data.local.dao.TablaDao
import com.fabiosanchez.interrapidisimotest.data.local.dao.UserDao
import com.fabiosanchez.interrapidisimotest.data.local.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDao: UserDao,
    private val tablaDao: TablaDao
) : ViewModel() {

    // Estado de la pantalla de inicio (Loading / Idle)
    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState

    // Usuario actual en sesion
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    // Carga el usuario desde la base de datos local solo cargamos el primero ya que solo puede haber un usuario
    fun getUser() {
        viewModelScope.launch {
            _user.value = userDao.getUser()[0]
            if (_user.value != null) {
                _homeState.value = HomeState.Idle
            } else {
                _homeState.value = HomeState.Idle
            }
        }
    }

    // Cierra sesión: elimina usuario y tablas locales
    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            val user = _user.value
            if (user != null) {
                userDao.deleteUser(user)
                tablaDao.deleteTables()
                onLogout()
            }
        }
    }
}

// Clase con los estados posibles de la vista Home
sealed class HomeState {
    object Loading : HomeState()
    object Idle : HomeState()
}