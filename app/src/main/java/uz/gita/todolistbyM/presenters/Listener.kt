package uz.gita.todolistbyM.presenters

interface Listener {
    fun onDelete(id:Int)
    fun clickedOneItem(id:Int)
}