package uz.gita.todolistbyM.database

import androidx.room.*

@Dao
interface CheckListDao{

    @Query("Select*from checklist where is_deleted=0 ")
    fun getAll(): List<CheckListEntity>

    @Query("Select*from checklist where is_deleted=1 ")
    fun getAllDeleted(): List<CheckListEntity>

    @Insert
    fun add(value: CheckListEntity)

    @Query("update checklist set is_deleted=0 where id=:id")
    fun backDeleted(id: Int)

    @Query("update checklist set is_deleted=1 where id=:id")
    fun deleteIdAndSetDeleted(id: Int)

    @Query ("delete from checklist where id=:id")
    fun deleteId(id:Int)

    @Query("select*from checklist where id=:id")
    fun get(id: Int):CheckListEntity

    @Query("update checklist set is_deleted=1")
    fun deleteAllAndSetDeleted()

    @Query("delete from  checklist where is_deleted=1")
    fun clear()

    @Update
    fun update(task: CheckListEntity)

    @Query ("Select MAX(id) from checklist")
    fun getMaxID():Int

    @Query ("Select*from checklist where is_deleted=0 and task like :text")
    fun searchChecklist(text:String):List<CheckListEntity>

    @Query ("Select*from checklist where is_deleted=1 and task like :text")
    fun searchChecklistDeleted(text:String):List<CheckListEntity>
}
