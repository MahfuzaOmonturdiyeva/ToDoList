package uz.gita.todolistbyM.presenters

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolistbyM.R
import uz.gita.todolistbyM.database.CheckListEntity

class ChecklistHolder(private val view: View, private val listener:ListenerForChecklist):
    RecyclerView.ViewHolder(view) {
    private val imageView=view.findViewById<ImageView>(R.id.checkbox_item_checklist_deleted)
    private val task=view.findViewById<TextView>(R.id.task_item_checklist_deleted)
    private val data=view.findViewById<TextView>(R.id.data_item_checklist_deleted)
    private val delete=view.findViewById<ImageView>(R.id.delete_item_checklist_deleted)

    fun bind(dataTask: CheckListEntity){

        setText(dataTask)
        data.text=dataTask.date
        imageView.setOnClickListener(){
            if (dataTask.checkState==0) {
                imageView.setImageResource(R.drawable.ic_check_circle)
                dataTask.checkState=1
                setText(dataTask)
            }
            else {
                imageView.setImageResource(R.drawable.ic_unchecked)
                dataTask.checkState=0
                setText(dataTask)
            }
            listener.onChecked(dataTask.id, dataTask.checkState)
        }
        delete.setOnClickListener(){
            listener.onDelete(dataTask.id)
        }
    }

    private fun setText(dataTask: CheckListEntity){
        if (dataTask.checkState==0) {
            imageView.setImageResource(R.drawable.ic_unchecked)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                task.text = Html.fromHtml(dataTask.task, Html.FROM_HTML_MODE_LEGACY)
            }
        }
        else {
            imageView.setImageResource(R.drawable.ic_check_circle)
            val ss=SpannableString(dataTask.task)
            ss.setSpan(StrikethroughSpan(), 0, dataTask.task.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            task.text=ss
        }
    }
}