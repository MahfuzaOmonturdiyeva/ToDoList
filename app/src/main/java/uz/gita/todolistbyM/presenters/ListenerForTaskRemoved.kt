package uz.gita.todolistbyM.presenters


interface ListenerForTaskRemoved :Listener{
    fun backReturn(id:Int)
}