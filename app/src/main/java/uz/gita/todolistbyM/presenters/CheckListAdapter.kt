package uz.gita.todolistbyM.presenters

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import uz.gita.todolistbyM.R
import uz.gita.todolistbyM.database.CheckListEntity
import uz.gita.todolistbyM.extention.inflate

class CheckListAdapter (val listener: ListenerForChecklist):
    androidx.recyclerview.widget.ListAdapter<CheckListEntity, ChecklistHolder>
        (object :DiffUtil.ItemCallback<CheckListEntity>(){
    override fun areItemsTheSame(oldItem: CheckListEntity, newItem: CheckListEntity): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: CheckListEntity, newItem: CheckListEntity): Boolean {
       return oldItem==newItem
    }}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistHolder {
        return ChecklistHolder(parent.inflate(R.layout.item_checklist), listener)
    }

    override fun onBindViewHolder(holder: ChecklistHolder, position: Int) {
        val data=getItem(position)
        holder.bind(getItem(position) as CheckListEntity)
        holder.itemView.findViewById<ConstraintLayout>(R.id.container_item_checklist).setOnClickListener(){
            listener.clickedOneItem(data.id)
        }
    }
}
