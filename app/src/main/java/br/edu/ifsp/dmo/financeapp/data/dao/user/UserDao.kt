package br.edu.ifsp.dmo.financeapp.data.dao.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.ifsp.dmo.financeapp.data.entity.user.User

@Dao
interface UserDao {

    @Insert
    suspend fun create(user: User): Long

    @Update
    suspend fun update(user: User): Int

    @Delete
    suspend fun delete (user: User): Int

    @Query("SELECT * FROM tb_users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User

}