package br.edu.ifsp.dmo.financeapp.data.dao.bill

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.ifsp.dmo.financeapp.data.entity.bill.Bill

@Dao
interface BillDao {

    @Insert
    suspend fun create(bill: Bill): Long

    /*@Query("SELECT * FROM tb_goals ORDER BY data ASC")
    suspend fun getAll(): List<Bill>*/

    @Query("SELECT * FROM tb_goals WHERE goal_id = :id")
    suspend fun getGoal(id: Long): Bill

    @Update
    suspend fun update(bill: Bill, value: Double): Int

    @Delete
    suspend fun delete(bill: Bill): Int

}