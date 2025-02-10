package br.edu.ifsp.dmo.financeapp.ui.activities.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.dmo.financeapp.data.repository.user.UserRepository
import br.edu.ifsp.dmo.financeapp.data.entity.user.User
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application): AndroidViewModel(application) {

    private val userRepository = UserRepository(application)

    private val _insertedUser = MutableLiveData<Boolean>()
    val insertedUser: LiveData<Boolean> = _insertedUser

    fun insertUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.findByEmail(email)
            if (user == null) {
                userRepository.create(User(email, name, password))
                _insertedUser.value = true
            } else {
                _insertedUser.value = false
            }
        }
    }
}