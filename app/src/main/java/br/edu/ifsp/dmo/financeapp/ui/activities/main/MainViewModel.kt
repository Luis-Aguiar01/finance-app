package br.edu.ifsp.dmo.financeapp.ui.activities.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.dmo.financeapp.data.datastore.DataStoreRepository
import br.edu.ifsp.dmo.financeapp.data.entity.user.User
import br.edu.ifsp.dmo.financeapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)
    private val dataStoreRepository: DataStoreRepository = DataStoreRepository(application)

    private val _isDisconnected = MutableLiveData<Boolean>()
    val isDisconnected: LiveData<Boolean> = _isDisconnected

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user;

    private var emailUser: String = ""

    fun logout() {
        viewModelScope.launch {
            dataStoreRepository.savePreferencesLogout()
            dataStoreRepository.saveEmailStayLogged("")
            _isDisconnected.value = true
        }
    }

    fun saveEmailStayLogged(email: String) {
        viewModelScope.launch {
            val (saveLogin, stayLoggedIn) = dataStoreRepository.loginPreferences.first()

            if (stayLoggedIn) dataStoreRepository.saveEmailStayLogged(email)

            val user = userRepository.findByEmail(emailUser)

            if (saveLogin && user != null) {
                dataStoreRepository.savePreferences(
                    email = user.email,
                    password = user.password,
                    saveLogin = saveLogin,
                    stayLoggedIn = stayLoggedIn
                )
            }
        }
    }

    fun setEmail(email: String) {
        emailUser = email
        viewModelScope.launch {
            _user.value = userRepository.findByEmail(emailUser)
        }
    }

    fun getEmail(): String {
        return emailUser
    }
}