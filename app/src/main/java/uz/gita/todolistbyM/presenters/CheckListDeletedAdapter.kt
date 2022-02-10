package uz.gita.todolistbyM.presenters

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import uz.gita.todolistbyM.R
import uz.gita.todolistbyM.database.CheckListEntity
import uz.gita.todolistbyM.extention.inflate

class CheckListDeletedAdapter (val listener: ListenerForChecklistRemoved):
    androidx.recyclerview.widget.ListAdapter<CheckListEntity, ChecklistRemovedHolder>
        (object :DiffUtil.ItemCallback<CheckListEntity>(){
    override fun areItemsTheSame(oldItem: CheckListEntity, newItem: CheckListEntity): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: CheckListEntity, newItem: CheckListEntity): Boolean {
       return oldItem==newItem
    }}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistRemovedHolder {
        return ChecklistRemovedHolder(parent.inflate(R.layout.item_checklist_deleted), listener)
    }

    override fun onBindViewHolder(holder: ChecklistRemovedHolder, position: Int) {
        val data=getItem(position)
        holder.bind(getItem(position) as CheckListEntity)
        holder.itemView.findViewById<ConstraintLayout>(R.id.container_item_checklist).setOnClickListener(){
            listener.clickedOneItem(data.id)
        }
    }
}
