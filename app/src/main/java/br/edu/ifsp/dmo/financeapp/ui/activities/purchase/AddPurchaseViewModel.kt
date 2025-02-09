package br.edu.ifsp.dmo.financeapp.ui.activities.purchase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.dmo.financeapp.data.entity.bill.Bill
import br.edu.ifsp.dmo.financeapp.data.enums.BillCategory
import br.edu.ifsp.dmo.financeapp.data.repository.bill.BillRepository
import kotlinx.coroutines.launch

class AddPurchaseViewModel(application: Application): AndroidViewModel(application) {
    private val billRepository: BillRepository = BillRepository(application)

    private val _bills = MutableLiveData<List<Bill>>()
    val bills: LiveData<List<Bill>> = _bills

    private val _selectedBill = MutableLiveData<Bill?>()
    val selectedBill: LiveData<Bill?> = _selectedBill

    private val _inserted = MutableLiveData<Boolean>()
    val inserted: LiveData<Boolean> = _inserted

    private val _deleted = MutableLiveData<Boolean>()
    val deleted: LiveData<Boolean> = _deleted

    private val _updated = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> = _updated

    private var emailUser = ""

    fun insertPurchase(name: String, value: Double, category: String) {
        viewModelScope.launch {
            billRepository.create(Bill(name = name, value = value, category = category , type = BillCategory.VARIABLE.toString(), email = emailUser, date = System.currentTimeMillis()))
            _inserted.value = true
            load()
        }
    }

    fun deletePurchase(id: Long) {
        viewModelScope.launch {
            val bill = billRepository.getBillById(id)

            if(bill != null) {
                billRepository.remove(bill)
                _deleted.value = true
                load()
            }
        }
    }

    fun updatePurchase(id: Long, name: String, category: String, value: Double){
        viewModelScope.launch {
            val bill = billRepository.getBillById(id)

            if(bill != null){
                bill.value = value
                bill.name = name
                bill.category = category
                bill.date = System.currentTimeMillis()

                billRepository.update(bill)
                _updated.value = true
                load()
            }
        }
    }

    fun getBillById(id: Long) {
        viewModelScope.launch {
            _selectedBill.value = billRepository.getBillById(id)
        }
    }

    fun setEmail(email: String){
        emailUser = email
    }

    private fun load() {
        viewModelScope.launch {
            _bills.value = billRepository.getAllByEmail(emailUser)
        }
    }
}