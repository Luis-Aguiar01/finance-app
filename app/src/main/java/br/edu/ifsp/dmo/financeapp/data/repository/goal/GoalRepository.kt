package br.edu.ifsp.dmo.financeapp.data.repository.goal

import android.content.Context
import br.edu.ifsp.dmo.financeapp.data.database.FinanceDatabase
import br.edu.ifsp.dmo.financeapp.data.entity.goal.Goal

class GoalRepository(context: Context) {
    private val database = FinanceDatabase.getInstance(context)
    private val dao = database.getGoalDao()

    suspend fun create(goal: Goal): Boolean {
        return dao.create(goal) > 0
    }

    suspend fun updateAccumulated(goal: Goal, value: Double): Boolean {
        return dao.updateAccumulated(goal.id, value) > 0
    }

    suspend fun updateTarget(goal: Goal, value: Double): Boolean {
        return dao.updateTarget(goal.id, value) > 0
    }

    suspend fun delete(goal: Goal): Boolean {
        return dao.delete(goal) > 0
    }

    suspend fun getAllByEmail(email: String): List<Goal> {
        return dao.getAllByEmail(email)
    }

    suspend fun getGoalById(id: Long): Goal? {
        return dao.getGoalById(id)
    }
}