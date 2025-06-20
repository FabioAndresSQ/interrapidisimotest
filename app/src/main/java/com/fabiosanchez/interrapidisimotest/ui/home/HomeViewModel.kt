package com.fabiosanchez.interrapidisimotest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabiosanchez.interrapidisimotest.data.local.dao.UserDao
import com.fabiosanchez.interrapidisimotest.data.local.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

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

    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            val user = _user.value
            if (user != null) {
                userDao.deleteUser(user)
                onLogout()
            }
        }
    }
}

sealed class HomeState {
    object Loading : HomeState()
    object Idle : HomeState()
}