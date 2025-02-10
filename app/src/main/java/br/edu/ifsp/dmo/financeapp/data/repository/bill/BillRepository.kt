package br.edu.ifsp.dmo.financeapp.data.repository.bill

import android.content.Context
import android.util.Log
import br.edu.ifsp.dmo.financeapp.data.database.FinanceDatabase
import br.edu.ifsp.dmo.financeapp.data.entity.bill.Bill
import java.util.Date

class BillRepository(context: Context) {

    private val database = FinanceDatabase.getInstance(context)
    private val dao = database.getBillDao()

    suspend fun create(bill: Bill): Boolean{
        return dao.create(bill) > 0
    }

    suspend fun update(bill: Bill): Boolean{
        return dao.update(bill) > 0
    }

    suspend fun updateValue(bill: Bill, value: Double): Boolean{
        return dao.updateValue(bill.id, value) > 0
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

    suspend fun getBillByDate(initialDate: Long, finalDate: Long, email: String): List<Bill>{

        var lista = dao.getBillByDate(initialDate, finalDate, email)

        return lista
    }

    suspend fun getTotalBillByCategory(email: String): Map<String, Double>{

        var map = dao.getSumByCategory(email)

        return map
    }
}