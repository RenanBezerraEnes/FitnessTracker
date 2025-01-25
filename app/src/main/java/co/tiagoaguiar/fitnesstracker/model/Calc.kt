package co.tiagoaguiar.fitnesstracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

//adding entity means it is a class of a database
@Entity
data class Calc(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "res") val res: Double,
    @ColumnInfo(name = "created_date") val createdDate: Date = Date()
)
