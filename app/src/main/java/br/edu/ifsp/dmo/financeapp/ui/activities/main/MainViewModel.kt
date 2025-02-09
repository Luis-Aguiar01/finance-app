package br.edu.ifsp.dmo.financeapp.ui.activities.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.dmo.financeapp.data.datastore.DataStoreRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val dataStoreRepository: DataStoreRepository = DataStoreRepository(application)
    private val _isDisconnected = MutableLiveData<Boolean>()
    val isDisconnected : LiveData<Boolean> = _isDisconnected

    fun logout(){
        viewModelScope.launch{
            dataStoreRepository.savePreferencesLogout()
            dataStoreRepository.saveEmailStayLogged("")
            _isDisconnected.value = true
        }
    }
}