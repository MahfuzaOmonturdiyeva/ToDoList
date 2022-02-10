package uz.gita.todolistbyM.presenters

interface ListenerForChecklistRemoved :Listener{
    fun onChecked(id:Int, ischecked:Int)
    fun backReturn(id:Int)
}