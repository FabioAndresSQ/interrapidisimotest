package com.fabiosanchez.interrapidisimotest.usecases

import com.fabiosanchez.interrapidisimotest.data.repository.AuthRepositoryImpl
import javax.inject.Inject

// Caso de uso para realizar el inicio de sesión
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepositoryImpl
){
    // Invoca el metodo de login del repositorio
    suspend operator fun invoke(username: String, password: String) = authRepository.login(username, password)
}