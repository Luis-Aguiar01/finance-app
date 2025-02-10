package br.edu.ifsp.dmo.financeapp.ui.activities.chart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.dmo.financeapp.data.repository.bill.BillRepository
import kotlinx.coroutines.launch

class ChartViewModel(application: Application) : AndroidViewModel(application) {
    private val billRepository: BillRepository = BillRepository(application)

    private val _totalByCategory = MutableLiveData<Map<String, Double>>()
    val totalByCategory: LiveData<Map<String, Double>> = _totalByCategory

    private var emailUser = ""

    fun getTotalByDate(initialDate: Long, finalDate: Long) {
        viewModelScope.launch {
            _totalByCategory.value = billRepository.getTotalBillByCategoryAndDate(emailUser, initialDate, finalDate)
        }
    }

    fun setEmail(email: String){
        emailUser = email
        loadInitial()
    }

    private fun loadInitial() {
        viewModelScope.launch {
            _totalByCategory.value = billRepository.getTotalBillByCategory(emailUser)
        }
    }
}