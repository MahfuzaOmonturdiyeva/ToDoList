package uz.gita.todolistbyM.repositories

import uz.gita.todolistbyM.app.App
import uz.gita.todolistbyM.database.CheckListDao
import uz.gita.todolistbyM.database.CheckListEntity
import uz.gita.todolistbyM.database.DatabaseTask
import uz.gita.todolistbyM.database.MyEntity


class CheckListRepositoryImpl : uz.gita.todolistbyM.Contract.Repository {
    private lateinit var database: DatabaseTask
    private lateinit var daoChecklist: CheckListDao


    init {
        database = App.DatabaseSetup.database
        daoChecklist = database.getCheckListDao()
    }

    override fun getListDeleted(): List<MyEntity> {
        return daoChecklist.getAllDeleted()
    }

    override fun backDeleted(id: Int) {
        daoChecklist.backDeleted(id)
    }

    override fun search(query: String): List<MyEntity> {
        return daoChecklist.searchChecklist("%$query%")
    }

    override fun searchDeleted(query: String): List<MyEntity> {
        return daoChecklist.searchChecklistDeleted("%$query%")
    }

    override fun add(data: MyEntity): Boolean {
        daoChecklist.add(data as CheckListEntity)
        return true
    }

    override fun getData(id: Int): MyEntity {
        return daoChecklist.get(id)
    }

    override fun replace(data: MyEntity): Boolean {
        daoChecklist.update(data as CheckListEntity)
        return true
    }

    override fun removeAndSetDeleted(id: Int): Boolean {
        daoChecklist.deleteIdAndSetDeleted(id)
        return true
    }

    override fun remove(id: Int): Boolean {
        daoChecklist.deleteId(id)
        return true
    }

    override fun getListTasks(): List<MyEntity> {
        return daoChecklist.getAll()
    }

    override fun clearEndSetDeleted(): Boolean {
        daoChecklist.deleteAllAndSetDeleted()
        return true
    }

    override fun clear(): Boolean {
        daoChecklist.clear()
        return true
    }

    override fun getMaxID(): Int {
        return daoChecklist.getMaxID()
    }

}