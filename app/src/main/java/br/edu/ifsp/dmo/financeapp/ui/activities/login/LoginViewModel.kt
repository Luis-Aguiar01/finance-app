package br.edu.ifsp.dmo.financeapp.ui.activities.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.dmo.financeapp.data.datastore.DataStoreRepository
import br.edu.ifsp.dmo.financeapp.data.repository.user.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepository(application)
    private val dataStoreRepository: DataStoreRepository = DataStoreRepository(application)

    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged : LiveData<Boolean> = _isLogged

    val loginPreferences:  LiveData<Pair<Boolean, Boolean>> = dataStoreRepository.loginPreferences.asLiveData()
    val dataPreferences: LiveData<Pair<String, String>> = dataStoreRepository.dataPreferences.asLiveData()

    fun login(email: String, password: String, stayLogged: Boolean, saveData: Boolean) {
        viewModelScope.launch {
            val user =  userRepository.findByEmail(email)
            if (user != null && user.authenticate(email, password)) {
                if (saveData) {
                    savePreferences(email, password, saveData, stayLogged)
                } else {
                    savePreferences("", "", saveData, stayLogged)
                }
                _isLogged.value = true
            } else {
                _isLogged.value = false
            }
        }
    }

    private fun savePreferences(email: String, password: String, saveLogin: Boolean, stayLoggedIn: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.savePreferences(email, password, saveLogin, stayLoggedIn)
        }
    }

}