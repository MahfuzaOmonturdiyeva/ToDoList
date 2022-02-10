package uz.gita.todolistbyM.repositories

import uz.gita.todolistbyM.app.App
import uz.gita.todolistbyM.database.DatabaseTask
import uz.gita.todolistbyM.database.MyEntity
import uz.gita.todolistbyM.database.TaskDao
import uz.gita.todolistbyM.database.TasksEntity


class TaskRepositoryImpl : uz.gita.todolistbyM.Contract.Repository {
    private lateinit var database: DatabaseTask
    private lateinit var daoTask: TaskDao


    init {
        database = App.DatabaseSetup.database
        daoTask = database.getTaskDao()
    }

    override fun add(data: MyEntity): Boolean {
        daoTask.add(data as TasksEntity)
        return true
    }

    override fun getListDeleted(): List<MyEntity> {
        return daoTask.getAllDeleted()
    }

    override fun backDeleted(id: Int) {
        daoTask.backDeleted(id)
    }

    override fun search(query: String): List<MyEntity> {
        return daoTask.searchTasks("%$query%")
    }

    override fun searchDeleted(query: String): List<MyEntity> {
        return daoTask.searchTasksDeleted("%$query%")
    }

    override fun getData(id: Int): MyEntity {
        return daoTask.get(id)
    }

    override fun replace(data: MyEntity): Boolean {
        daoTask.update(data as TasksEntity)
        return true
    }

    override fun remove(id: Int): Boolean {
        daoTask.deleteId(id)
        return true
    }

    override fun removeAndSetDeleted(id: Int): Boolean {
        daoTask.deleteIdAndSetDeleted(id)
        return true
    }

    override fun getListTasks(): List<MyEntity> {
        return daoTask.getAll()
    }

    override fun clearEndSetDeleted(): Boolean {
        daoTask.deleteAllAndSetDeleted()
        return true
    }

    override fun clear(): Boolean {
        daoTask.clear()
        return true
    }

    override fun getMaxID(): Int {
        return daoTask.getMaxID()
    }

}