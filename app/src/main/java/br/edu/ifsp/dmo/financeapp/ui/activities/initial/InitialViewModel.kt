package br.edu.ifsp.dmo.financeapp.ui.activities.initial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import br.edu.ifsp.dmo.financeapp.data.datastore.DataStoreRepository

class InitialViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStoreRepository: DataStoreRepository = DataStoreRepository(application)
    val loginPreferences: LiveData<Pair<Boolean, Boolean>> = dataStoreRepository.loginPreferences.asLiveData()


}