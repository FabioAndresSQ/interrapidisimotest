package com.fabiosanchez.interrapidisimotest.ui.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiosanchez.interrapidisimotest.data.local.dao.UserDao
import com.fabiosanchez.interrapidisimotest.data.local.entity.UserEntity
import com.fabiosanchez.interrapidisimotest.data.remote.VersionApi
import com.fabiosanchez.interrapidisimotest.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val versionApi: VersionApi,
    private val loginUseCase: LoginUseCase,
    private val userDao: UserDao
) : ViewModel() {

    // Estado general del login (Idle, Loading, Success)
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    val loginState: StateFlow<LoginState> = _loginState

    // Campos del formulario de inicio de sesión
    private val _user = MutableStateFlow<String>("cGFtLm1lcmVkeTIx")
    val user: StateFlow<String> = _user

    private val _password = MutableStateFlow<String>("SW50ZXIyMDIx")
    val password: StateFlow<String> = _password

    // Mensaje de validación de versión
    private val _versionMessage = MutableStateFlow<String?>(null)
    val versionMessage: StateFlow<String?> = _versionMessage


    // Función para guardar cambios en el campo de usuario
    fun onUserChange(user: String) {
        _user.value = user
    }

    // Función para guardar cambios en el campo de contraseña
    fun onPasswordChange(password: String) {
        _password.value = password
    }


    // Mensaje Toast
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    // Emite un mensaje tipo Toast al UI
    fun showToast(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }

    // Función para obtener la versión local de la app
    fun getLocalAppVersion(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "0.0.0"
        } catch (e: Exception) {
            Log.e("Version", "Error al obtener la versión local", e)
            "0.0.0"
        }
    }

    // Función para verificar la versión de la app en el endpoint y actualiza el estado
    fun checkAppVersion(context: Context){
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val localVersion = getLocalAppVersion(context)
                val response = versionApi.getVersion()
                if (response.isSuccessful) {
                    val remoteVersion = response.body() ?: ""
                    if (remoteVersion > localVersion) {
                        _loginState.value = LoginState.Idle
                        _versionMessage.value = "Hay una nueva versión disponible: $remoteVersion"
                        Log.d("Version", "Hay una nueva versión disponible: $remoteVersion")
                    } else if (remoteVersion < localVersion) {
                        _loginState.value = LoginState.Idle
                        _versionMessage.value = "Estás usando una versión superior ($localVersion) a la oficial ($remoteVersion)"
                        Log.d("Version", "Estás usando una versión superior ($localVersion) a la oficial ($remoteVersion)")
                    } else {
                        _versionMessage.value = ""
                        _loginState.value = LoginState.Idle
                        Log.d("Version", "Estás usando la versión oficial ($localVersion)")
                    }
                } else {
                    _loginState.value = LoginState.Idle
                    _versionMessage.value = "Error obteniendo versión: código ${response.code()}"
                    Log.e("Version", "Error obteniendo versión: código ${response.code()}")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Idle
                _versionMessage.value = "Error de red: ${e.localizedMessage}"
                Log.e("Version", "Error de red: ${e.localizedMessage}")
            }
        }
    }

    // Actualiza el mensaje de versión cuando este ya se haya mostrado al usuario
    fun updateVersionMessage(message: String) {
        _versionMessage.value = message
    }

    // Ejecuta el login, guarda datos en Room si es exitoso y maneja errores HTTP
    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loginState.value = LoginState.LoginIn
            val user = _user.value
            val password = _password.value

            if (user.isEmpty() || password.isEmpty()) {
                showToast("Por favor, ingrese un usuario y contraseña válidos")
                delay(1000)
                _loginState.value = LoginState.Idle
                return@launch
            }

            try {
                val response = loginUseCase(user, password)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Log.d("Login", "Login exitoso: $loginResponse")

                        // Guardar usuario en base de datos local
                        val userEntity = UserEntity(
                            identification = loginResponse.Identificacion,
                            user = loginResponse.Usuario,
                            name = loginResponse.Nombre
                        )
                        userDao.insertUser(userEntity)
                        _loginState.value = LoginState.Idle

                        onSuccess()
                    } else {
                        showToast("Login fallido: ${response.message()}")
                        Log.e("Login", "Login fallido: ${response.message()}")
                        _loginState.value = LoginState.Idle
                    }
                } else {
                    // Si el servidor devuelve un error, mostrar el mensaje
                    val errorBody = response.errorBody()?.string()
                    Log.e("Login", "Error en la respuesta: $errorBody")

                    val errorMessage = try {
                        val json = JSONObject(errorBody ?: "")
                        json.optString("Message", "Error desconocido")
                    } catch (e: Exception) {
                        "Error desconocido - ${e.message}"
                    }

                    showToast("Error: ${response.code()} - ${errorMessage}")
                    Log.e("Login", "Error: ${response.code()} - ${errorMessage}")
                    _loginState.value = LoginState.Idle
                }
            } catch (e: Exception){
                showToast("Error: ${e.localizedMessage}")
                Log.e("Login", "Error: ${e.localizedMessage}")
                _loginState.value = LoginState.Idle
            }
        }
    }


    // Funcion que verifica si hay un usuario en base de datos local y que el mensaje de versión inválida sea nulo o vacio
    // (Esto quiere decir que el usuario ya dio cclick en actualizar o la versión es la correcta)
    fun getUser(userLogged: () -> Unit){
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val user = userDao.getUser()
            if (user.isNotEmpty() && _versionMessage.value.isNullOrEmpty()) {
                userLogged()
            } else {
                _loginState.value = LoginState.Idle
            }
        }
    }
}

// Clase con los estados de inicio de sesión
sealed class LoginState {
    object Loading : LoginState()
    object Idle : LoginState()
    object LoginIn : LoginState()
}