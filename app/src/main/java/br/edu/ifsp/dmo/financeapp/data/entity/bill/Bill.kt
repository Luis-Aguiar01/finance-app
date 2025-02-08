package br.edu.ifsp.dmo.financeapp.data.entity.bill

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import br.edu.ifsp.dmo.financeapp.data.entity.user.User
import br.edu.ifsp.dmo.financeapp.data.enums.BillCategory
import br.edu.ifsp.dmo.financeapp.util.Constants
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(
    tableName = Constants.BILL_TB,
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = [Constants.USER_EMAIL],
            childColumns = [Constants.USER_EMAIL],
            onDelete = ForeignKey.CASCADE
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
    var category: BillCategory,

    @NonNull
    @ColumnInfo(name = Constants.BILL_VALUE)
    var value: Double,

    @NonNull
    @ColumnInfo(name = Constants.USER_EMAIL)
    var email: String,

    date: LocalDate
){
    @ColumnInfo(name = Constants.BILL_DATE)
    var date: String = ""

    @Ignore
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    init {
        this.date = formatter.format(date)
    }
}

