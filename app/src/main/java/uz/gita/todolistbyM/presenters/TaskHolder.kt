package uz.gita.todolistbyM.presenters

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolistbyM.R
import uz.gita.todolistbyM.database.TasksEntity

class TaskHolder(private val view: View,private val listener:Listener):
    RecyclerView.ViewHolder(view){
    private val description=view.findViewById<TextView>(R.id.description_item_task_deleted)
    private val data=view.findViewById<TextView>(R.id.data_item_task_deleted)
    private val delete=view.findViewById<ImageView>(R.id.delete_item_task_deleted)

    fun bind(dataTask: TasksEntity){
        description.text=dataTask.taskDescription
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            description.text=Html.fromHtml(dataTask.taskDescription,Html.FROM_HTML_MODE_LEGACY)
//            task.text = Html.fromHtml(dataTask.task, Html.FROM_HTML_MODE_LEGACY)
        }
        data.text=dataTask.date
        delete.setOnClickListener(){
            listener.onDelete(dataTask.id)
        }
    }

}