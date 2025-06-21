package com.fabiosanchez.interrapidisimotest.ui.localidad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiosanchez.interrapidisimotest.data.remote.LocalidadResponse
import com.fabiosanchez.interrapidisimotest.data.repository.LocalidadRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalidadViewModel @Inject constructor(
    private val localidadRepository: LocalidadRepositoryImpl
): ViewModel(){
    // Estado de las localidades (Loading, Success, Error)
    private val _localidadState = MutableStateFlow<LocalidadState>(LocalidadState.Loading)
    val localidadState: StateFlow<LocalidadState> = _localidadState

    // Funcion para obtener las localidades
    fun getLocalidades(){
        viewModelScope.launch {
            _localidadState.value = LocalidadState.Loading
            try {
                val response = localidadRepository.getLocalidades()
                if (response.isSuccessful) {
                    val localidades = response.body()
                    if (localidades != null) {
                        _localidadState.value = LocalidadState.Success(localidades)
                        } else {
                        _localidadState.value = LocalidadState.Error("No se encontraron localidades")
                    }
                } else {
                    _localidadState.value =
                        LocalidadState.Error("Error en la respuesta del servidor: ${response.code()}")
                }
                } catch (e: Exception) {
                _localidadState.value = LocalidadState.Error("Error al obtener las localidades: ${e.message}")
            }
        }
    }

    // Funcion para reintentar obtener las localidades
    fun setupLocalidades(){
        getLocalidades()
    }
}

// Clase con los estados de las localidades
sealed class LocalidadState{
    object Loading : LocalidadState()
    data class Success(val localidades: List<LocalidadResponse>) : LocalidadState()
    data class Error(val message: String) : LocalidadState()
}