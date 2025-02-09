package br.edu.ifsp.dmo.financeapp.ui.activities.profile

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.dmo.financeapp.data.entity.user.User
import br.edu.ifsp.dmo.financeapp.data.repository.user.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepository(application)

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _salary = MutableLiveData<Double>()
    val salary: LiveData<Double> = _salary

    private var oldEmail = ""

    fun updateData(email: String) {
        viewModelScope.launch {
            if(email.isNotBlank()) {
                val user: User? = userRepository.findByEmail(email)
                if(user != null) {
                    _name.value = user.name
                    _email.value = user.email
                    _password.value = user.password
                    _salary.value = user.salary
                    oldEmail = user.email
                }
            }
        }
    }

    fun edit() {
        viewModelScope.launch {
            if(!_email.value.isNullOrBlank()) {
                //verifica se o email foi alterado e caso ele ja esteja em uso, retorna
                if(_email.value != oldEmail && userRepository.findByEmail(_email.value!!) != null) {
                    return@launch
                }
            }
            val user = User(_email.value!!, _name.value!!, _password.value!!, _salary.value!!)
            userRepository.update(user)
            updateData(_email.value!!)
        }
    }

    fun setName(name: String) {
        _name.value = name
    }
    fun setEmail(email: String) {
        _email.value = email
    }
    fun setPassword(password: String) {
        _password.value = password
    }
    fun setSalary(salary: Double) {
        _salary.value = salary
    }
}