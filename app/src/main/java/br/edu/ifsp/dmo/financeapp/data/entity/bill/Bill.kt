package br.edu.ifsp.dmo.financeapp.data.entity.bill

// Entidade que representa uma compra feita por um usuário.

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import br.edu.ifsp.dmo.financeapp.data.entity.user.User
import br.edu.ifsp.dmo.financeapp.util.Constants

@Entity(
    tableName = Constants.BILL_TB,
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = [Constants.USER_EMAIL],
            childColumns = [Constants.USER_EMAIL],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class Bill(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.BILL_ID)
    var id: Long = 0,

    @NonNull
    @ColumnInfo(name = Constants.BILL_NAME)
    var name: String,

    @NonNull
    @ColumnInfo(name = Constants.BILL_CATEGORY)
    var category: String,

    @NonNull
    @ColumnInfo(name = Constants.BILL_TYPE)
    var type: String,

    @NonNull
    @ColumnInfo(name = Constants.BILL_VALUE)
    var value: Double,

    @NonNull
    @ColumnInfo(name = Constants.USER_EMAIL)
    var email: String,

    @NonNull
    @ColumnInfo(name = Constants.BILL_DATE)
    var date: Long = System.currentTimeMillis()
) {
}

