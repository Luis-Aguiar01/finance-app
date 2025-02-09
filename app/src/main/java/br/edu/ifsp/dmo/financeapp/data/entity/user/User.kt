package br.edu.ifsp.dmo.financeapp.data.entity.user

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.edu.ifsp.dmo.financeapp.util.Constants

@Entity(tableName = Constants.USER_TB)
class User(
    @PrimaryKey
    @ColumnInfo(name = Constants.USER_EMAIL)
    var email: String,

    @NonNull
    @ColumnInfo(name = Constants.USER_NAME)
    var name: String,

    @NonNull
    @ColumnInfo(name = Constants.USER_PASSWORD)
    var password: String,

    @NonNull
    @ColumnInfo(name = Constants.USER_SALARY)
    var salary: Double

) {
    fun authenticate(email: String, password: String): Boolean{
        return this.email == email && this.password == password
    }
}