package uz.gita.todolistbyM.presenters

interface ListenerForChecklist :Listener{
    fun onChecked(id:Int, ischecked:Int)
}