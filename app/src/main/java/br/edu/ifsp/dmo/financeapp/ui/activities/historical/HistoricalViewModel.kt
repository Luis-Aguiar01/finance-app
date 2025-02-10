package br.edu.ifsp.dmo.financeapp.ui.activities.historical

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.dmo.financeapp.data.entity.bill.Bill
import br.edu.ifsp.dmo.financeapp.data.repository.bill.BillRepository
import kotlinx.coroutines.launch
import java.sql.Date

class HistoricalViewModel(application: Application): AndroidViewModel(application) {

    private val billRepository = BillRepository(application)

    private val _bills = MutableLiveData<List<Bill>>()
    val bills: LiveData<List<Bill>> = _bills

    private var emailUser: String = ""

    fun getBillByDate( initialDate: Long, finalDate: Long){
        viewModelScope.launch {
           _bills.value =  billRepository.getBillByDate(initialDate, finalDate, emailUser)
        }
    }

    fun setEmail(email: String){
        emailUser = email
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _bills.value = billRepository.getAllByEmail(emailUser)
        }
    }
}