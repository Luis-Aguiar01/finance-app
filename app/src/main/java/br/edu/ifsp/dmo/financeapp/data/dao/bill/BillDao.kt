package br.edu.ifsp.dmo.financeapp.data.dao.bill

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.Query
import androidx.room.Update
import br.edu.ifsp.dmo.financeapp.data.entity.bill.Bill
import br.edu.ifsp.dmo.financeapp.util.Constants

@Dao
interface BillDao {

    @Insert
    suspend fun create(bill: Bill): Long

    @Update
    suspend fun update(bill: Bill): Int

    @Query("SELECT * FROM tb_bills WHERE email = :email ORDER BY bill_date DESC")
    suspend fun getAllByEmail(email: String): List<Bill>

    @Query("SELECT * FROM tb_bills WHERE bill_id = :id")
    suspend fun getBillById(id: Long): Bill?

    @Query("UPDATE tb_bills SET value = :value WHERE bill_id = :id")
    suspend fun updateValue(id: Long, value: Double): Int

    @Delete
    suspend fun delete(bill: Bill): Int

    @Query("SELECT * FROM tb_bills WHERE email = :email AND bill_date BETWEEN :initialDate AND :finalDate")
    suspend fun getBillByDate(initialDate: Long, finalDate: Long, email: String): List<Bill>

    @MapInfo(keyColumn = Constants.BILL_CATEGORY, valueColumn = Constants.BILL_TOTAL_BY_CATEGORY)
    @Query("SELECT category, SUM(value) as total FROM tb_bills WHERE email = :email GROUP BY category ORDER BY total DESC LIMIT 5")
    suspend fun getSumByCategory(email: String): Map<String, Double>

    @MapInfo(keyColumn = Constants.BILL_CATEGORY, valueColumn = Constants.BILL_TOTAL_BY_CATEGORY)
    @Query("SELECT category, SUM(value) as total FROM tb_bills WHERE email = :email AND bill_date BETWEEN :initialDate AND :finalDate GROUP BY category ORDER BY total DESC LIMIT 5")
    suspend fun getSumByCategoryAndDate(
        email: String,
        initialDate: Long,
        finalDate: Long
    ): Map<String, Double>

}