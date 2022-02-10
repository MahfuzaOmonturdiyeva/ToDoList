package uz.gita.todolistbyM.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TasksEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name = "task_description")
    var taskDescription:String?,
    var task:String,
    val date:String,
    @ColumnInfo(name = "is_deleted")
    var isDeleted:Int
):MyEntity
