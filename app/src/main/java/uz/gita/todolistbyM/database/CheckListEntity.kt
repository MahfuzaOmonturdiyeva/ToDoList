package uz.gita.todolistbyM.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist")
data class CheckListEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name="checklist_state")
    var checkState:Int,
    var task:String,
    var date:String,
    @ColumnInfo(name = "is_deleted")
    var isDeleted:Int
):MyEntity
