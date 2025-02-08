package br.edu.ifsp.dmo.financeapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.ifsp.dmo.financeapp.data.dao.bill.BillDao
import br.edu.ifsp.dmo.financeapp.data.dao.goal.GoalDao
import br.edu.ifsp.dmo.financeapp.data.dao.user.UserDao
import br.edu.ifsp.dmo.financeapp.data.entity.bill.Bill
import br.edu.ifsp.dmo.financeapp.data.entity.goal.Goal
import br.edu.ifsp.dmo.financeapp.data.entity.user.User
import br.edu.ifsp.dmo.financeapp.util.Constants

@Database(entities = [User::class, Bill::class, Goal::class], version = 1)
abstract class FinanceDatabase: RoomDatabase() {

    companion object{
        private lateinit var instance: FinanceDatabase

        fun getInstance(context: Context): FinanceDatabase {
            if (!::instance.isInitialized) {
                synchronized(FinanceDatabase::class) {
                    instance = Room
                        .databaseBuilder(
                            context,
                            FinanceDatabase::class.java,
                            Constants.DATABASE_NAME
                        )
                        .build()
                }
            }
            return instance
        }

    }

    abstract fun getBillDao(): BillDao
    abstract fun getGoalDao(): GoalDao
    abstract fun getUserDao(): UserDao
}