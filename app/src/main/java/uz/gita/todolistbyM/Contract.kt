package uz.gita.todolistbyM

import uz.gita.todolistbyM.database.MyEntity

interface Contract {
    interface Repository {

        fun add(data: MyEntity): Boolean

        fun getData(id: Int): MyEntity

        fun replace(data: MyEntity): Boolean

        fun removeAndSetDeleted(id: Int): Boolean

        fun remove(id: Int): Boolean

        fun getListTasks(): List<MyEntity>

        fun getListDeleted(): List<MyEntity>

        fun backDeleted(id: Int)

        fun search(query: String): List<MyEntity>

        fun searchDeleted(query: String): List<MyEntity>

        fun clearEndSetDeleted(): Boolean

        fun clear(): Boolean

        fun getMaxID(): Int
    }

    interface Presenter {

        fun onAdd(task: MyEntity)

        fun onRemoveItemAndSetDeleted(id: Int)

        fun onRemoveItem(id:Int)

        fun getData(id: Int): MyEntity

        fun update(id: Int, isChecked: Int)

        fun onClearAndSetDeleted()

        fun onClear()

        fun updateTasks()

        var isTask: Boolean

        fun getNearlyDeletedList()

        fun onBackReturn(id: Int)

        fun updateTasksNearly()

        fun search(text:String)

        fun searchDeleted(text:String)
    }

    interface View {

        fun clickClear(view: android.view.View)

        fun clickAdd(view: android.view.View)

        fun showList(list: List<MyEntity>)

        var isTask: Boolean
    }
}