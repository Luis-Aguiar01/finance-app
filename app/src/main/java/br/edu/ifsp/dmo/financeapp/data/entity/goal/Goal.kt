package br.edu.ifsp.dmo.financeapp.data.entity.goal

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import br.edu.ifsp.dmo.financeapp.data.entity.user.User
import br.edu.ifsp.dmo.financeapp.util.Constants

@Entity(
    tableName = Constants.GOAL_TB,
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
class Goal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.GOAL_ID)
    var id: Long = 0,

    @NonNull
    @ColumnInfo(name = Constants.GOAL_NAME)
    var name: String,

    @NonNull
    @ColumnInfo(name = Constants.GOAL_ACCUMULATED_VALUE)
    var accumulated_value: Double = 0.0,

    @NonNull
    @ColumnInfo(name = Constants.GOAL_TARGET_VALUE)
    var target_value: Double,

    @NonNull
    @ColumnInfo(name = Constants.USER_EMAIL)
    var email: String
) {
}