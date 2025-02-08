package br.edu.ifsp.dmo.financeapp.data.repository.bill

import android.content.Context
import br.edu.ifsp.dmo.financeapp.data.database.FinanceDatabase
import br.edu.ifsp.dmo.financeapp.data.entity.bill.Bill

class BillRepository(context: Context) {

    private val database = FinanceDatabase.getInstance(context)
    private val dao = database.getBillDao()

    suspend fun create(bill: Bill): Boolean{
        return dao.create(bill) > 0
    }

    suspend fun updateValue(bill: Bill, value: Double): Boolean{
        return dao.updateValue(bill, value) > 0
    }

    suspend fun remove(bill: Bill): Boolean{
        return dao.delete(bill) > 0
    }

    suspend fun getAllByEmail(email: String): List<Bill> {
        return dao.getAllByEmail(email)
    }

    suspend fun getBillById(id: Long): Bill {
        return dao.getBillById(id)
    }

}