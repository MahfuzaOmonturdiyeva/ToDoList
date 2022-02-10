package uz.gita.todolistbyM.database

import androidx.room.*

@Dao
interface TaskDao {

    @Query("Select*from tasks where is_deleted=0 ")
    fun getAll(): List<TasksEntity>

    @Query("Select*from tasks where is_deleted=1 ")
    fun getAllDeleted(): List<TasksEntity>

    @Insert
    fun add(value: TasksEntity)

    @Query("update tasks set is_deleted=0 where id=:id")
    fun backDeleted(id: Int)

    @Query("update tasks set is_deleted=1 where id=:id")
    fun deleteIdAndSetDeleted(id: Int)

    @Query("select*from tasks where id=:id")
    fun get(id: Int):TasksEntity

    @Query("update tasks set is_deleted=1")
    fun deleteAllAndSetDeleted()

    @Query ("delete from tasks where id=:id")
    fun deleteId(id:Int)

    @Query("delete from  tasks where is_deleted=1")
    fun clear()

    @Update
    fun update(task: TasksEntity)

    @Query ("Select MAX(id) from tasks")
    fun getMaxID():Int

    @Query ("Select*from tasks where is_deleted=0 and task_description like:text")
    fun searchTasks(text:String):List<TasksEntity>

    @Query ("Select*from tasks where is_deleted=1 and task_description like :text")
    fun searchTasksDeleted(text:String):List<TasksEntity>
}