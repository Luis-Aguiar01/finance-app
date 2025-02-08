package br.edu.ifsp.dmo.financeapp.data.repository.user

import android.content.Context
import br.edu.ifsp.dmo.financeapp.data.database.FinanceDatabase
import br.edu.ifsp.dmo.financeapp.data.entity.user.User

class UserRepository(context: Context) {
    private val database = FinanceDatabase.getInstance(context)
    private val dao = database.getUserDao()

    suspend fun create(user: User): Boolean{
        return dao.create(user) > 0
    }
    suspend fun update(user: User): Boolean{
        return dao.update(user) > 0
    }
    suspend fun remove(user: User): Boolean{
        return dao.delete(user) > 0
    }
    suspend fun findByEmail(email: String): User{
        return dao.getUserByEmail(email)
    }
}