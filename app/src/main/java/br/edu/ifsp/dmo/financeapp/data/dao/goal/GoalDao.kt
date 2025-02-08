package br.edu.ifsp.dmo.financeapp.data.dao.goal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.ifsp.dmo.financeapp.data.entity.goal.Goal

@Dao
interface GoalDao {

    @Insert
    suspend fun create(goal: Goal): Long

    @Query("SELECT *, (target_value - accumulated_value) as difference FROM tb_goals WHERE email = :email ORDER BY difference ASC")
    suspend fun getAllByEmail(email: String): List<Goal>

    @Query("SELECT * FROM tb_goals WHERE goal_id = :id")
    suspend fun getGoalById(id: Long): Goal

    @Update
    suspend fun updateAccumulated(goal: Goal, value: Double): Int

    @Update
    suspend fun updateTarget(goal: Goal, value: Double): Int

    @Delete
    suspend fun delete(goal: Goal): Int

}