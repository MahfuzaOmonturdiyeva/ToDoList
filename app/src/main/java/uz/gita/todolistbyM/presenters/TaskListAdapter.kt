package uz.gita.todolistbyM.presenters

import android.util.Log
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import uz.gita.todolistbyM.R
import uz.gita.todolistbyM.database.TasksEntity
import uz.gita.todolistbyM.extention.inflate

class TaskListAdapter(private val listener: Listener) :
    androidx.recyclerview.widget.ListAdapter<TasksEntity, TaskHolder>
        (object : DiffUtil.ItemCallback<TasksEntity>() {
        override fun areItemsTheSame(oldItem: TasksEntity, newItem: TasksEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TasksEntity, newItem: TasksEntity): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder(parent.inflate(R.layout.item_tasks), listener)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
        holder.itemView.findViewById<ConstraintLayout>(R.id.container_item)
            .setOnClickListener {
                listener.clickedOneItem(data.id)
                Log.d("item", data.id.toString() + "adapter")
            }
    }

}