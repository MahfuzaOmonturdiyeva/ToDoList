package uz.gita.todolistbyM.presenters

import android.util.Log
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import uz.gita.todolistbyM.R
import uz.gita.todolistbyM.database.TasksEntity
import uz.gita.todolistbyM.extention.inflate

class TaskListDeletedAdapter(private val listener: ListenerForTaskRemoved) :
    androidx.recyclerview.widget.ListAdapter<TasksEntity, TaskDeletedHolder>
        (object : DiffUtil.ItemCallback<TasksEntity>() {
        override fun areItemsTheSame(oldItem: TasksEntity, newItem: TasksEntity): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: TasksEntity, newItem: TasksEntity): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskDeletedHolder {
        return TaskDeletedHolder(parent.inflate(R.layout.item_tasks_deleted), listener)
    }

    override fun onBindViewHolder(holder: TaskDeletedHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
        holder.itemView.findViewById<ConstraintLayout>(R.id.container_item_task_deleted)
            .setOnClickListener {
                listener.clickedOneItem(data.id)
                Log.d("item", data.id.toString() + "adapter")
            }
    }
}