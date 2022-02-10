package uz.gita.todolistbyM.presenters

import uz.gita.todolistbyM.app.App
import uz.gita.todolistbyM.database.CheckListEntity
import uz.gita.todolistbyM.database.LocalStorage
import uz.gita.todolistbyM.database.MyEntity
import uz.gita.todolistbyM.database.TasksEntity
import uz.gita.todolistbyM.repositories.CheckListRepositoryImpl
import uz.gita.todolistbyM.repositories.TaskRepositoryImpl


class PresenterImpl(private val viewImpl: uz.gita.todolistbyM.Contract.View) : uz.gita.todolistbyM.Contract.Presenter {
    private lateinit var repositoryImpl: uz.gita.todolistbyM.Contract.Repository
    private lateinit var localStorage: LocalStorage

    init {
        localStorage = App.localStorage
        updateRepository()
    }

    private fun updateRepository() {
        repositoryImpl = if (isTask)
            TaskRepositoryImpl()
        else CheckListRepositoryImpl()
    }


    override fun onAdd(task: MyEntity) {
        repositoryImpl.add(task)
        updateTasks()
    }

    override fun onRemoveItemAndSetDeleted(id: Int) {
        repositoryImpl.removeAndSetDeleted(id)
        updateTasks()
    }

    override fun onRemoveItem(id: Int) {
        repositoryImpl.remove(id)
        updateTasksNearly()
    }

    override fun getData(id: Int): MyEntity {
        return repositoryImpl.getData(id)
    }

    override fun update(id: Int, isChecked: Int) {
        val data: CheckListEntity = getData(id) as CheckListEntity
        data.checkState = isChecked
        repositoryImpl.replace(data)
    }

    override fun getNearlyDeletedList() {
        updateTasksNearly()
    }

    override fun onClearAndSetDeleted() {
        repositoryImpl.clearEndSetDeleted()
        updateTasks()
    }

    override fun onClear() {
        repositoryImpl.clear()
        updateTasksNearly()
    }

    override fun updateTasks() {
        updateRepository()
        if (isTask)
            viewImpl.showList(repositoryImpl.getListTasks())
        else {
            val list = repositoryImpl.getListTasks() as ArrayList<CheckListEntity>

            if (list.isNotEmpty()) {
                val checkedList: ArrayList<CheckListEntity> = ArrayList()

                list.asSequence()
                    .map { data ->
                        if (data.checkState == 1)
                            checkedList.add(data)
                    }.toList()

                list.removeAll(checkedList)

                checkedList.addAll(list)
                viewImpl.showList(checkedList)
            } else viewImpl.showList(list)
        }
    }

    override fun updateTasksNearly() {
        updateRepository()
        if (isTask)
            viewImpl.showList(repositoryImpl.getListDeleted())
        else {
            val list = repositoryImpl.getListDeleted() as ArrayList<CheckListEntity>

            if (list.isNotEmpty()) {
                val checkedList: ArrayList<CheckListEntity> = ArrayList()

                list.asSequence()
                    .map { data ->
                        if (data.checkState == 1)
                            checkedList.add(data)
                    }.toList()

                list.removeAll(checkedList)

                checkedList.addAll(list)
                viewImpl.showList(checkedList)
            }
            else viewImpl.showList(list)
        }
    }

    override var isTask: Boolean
        get() = localStorage.isTask
        set(value) {
            localStorage.isTask = value
        }

    override fun onBackReturn(id: Int) {
        repositoryImpl.backDeleted(id)
        updateTasksNearly()
    }

    override fun search(text: String) {
        val query: String = text.trim()
        if (query != "") {
            val list = repositoryImpl.search(query) as ArrayList<MyEntity>

            list
                .asSequence()
                .map { w ->
                    if (w is TasksEntity) {
                        w.taskDescription = w.taskDescription!!.replace(
                            query,
                            "<span style=\"color:red\">$query</span>",
                            false
                        )
                    }
                    if (w is CheckListEntity) {
                        if (w.checkState == 0)
                            w.task = w.task.replace(
                                query,
                                "<span style=\"color:red\">$query</span>",
                                false
                            )
                    }
                }.toList()
            viewImpl.showList(list)
        } else updateTasks()
    }

    override fun searchDeleted(text: String) {
        val query: String = text.trim()
        if (query != "") {
            val list = repositoryImpl.searchDeleted(query) as ArrayList<MyEntity>

            list
                .asSequence()
                .map { w ->
                    if (w is TasksEntity) {
                        w.taskDescription = w.taskDescription!!.replace(
                            query,
                            "<span style=\"color:red\">$query</span>",
                            false
                        )
                    }
                    if (w is CheckListEntity) {
                        if (w.checkState == 0)
                            w.task = w.task.replace(
                                query,
                                "<span style=\"color:red\">$query</span>",
                                false
                            )
                    }
                }.toList()
            viewImpl.showList(list)
        } else updateTasksNearly()
    }
}
