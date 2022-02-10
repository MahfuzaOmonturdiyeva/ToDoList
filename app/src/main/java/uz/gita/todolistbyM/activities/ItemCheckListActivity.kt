package uz.gita.todolistbyM.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.StrikethroughSpan
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import uz.gita.todolistbyM.R
import uz.gita.todolistbyM.TasksActivity
import uz.gita.todolistbyM.app.App
import uz.gita.todolistbyM.database.CheckListDao
import uz.gita.todolistbyM.database.CheckListEntity
import uz.gita.todolistbyM.database.DatabaseTask

class ItemCheckListActivity : AppCompatActivity() {
    private lateinit var database: DatabaseTask
    private lateinit var daoCheckList: CheckListDao
    private var id = -1

    override fun onBackPressed() {
        startActivity(Intent(this, TasksActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_cheklist)

        id = intent.getIntExtra("id", -1)

        database = App.database
        daoCheckList = database.getCheckListDao()
        val task = findViewById<EditText>(R.id.task_one_checklist)
        val date = findViewById<TextView>(R.id.data_one_checklist)
        val enter = findViewById<ImageView>(R.id.saved_one_checklist)
        val back = findViewById<ImageView>(R.id.back_one_checklist)
        val delete = findViewById<ImageView>(R.id.delete_one_checklist)


        val data = daoCheckList.get(id)
        if (data.checkState==1){
        val ss= SpannableString(data.task)
        ss.setSpan(StrikethroughSpan(), 0, data.task.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        task.setText(ss)}
        else task.setText(data.task)
        date.text = data.date

        delete.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.custom_dialog_remove)
            val window = dialog.window
            if (window != null) {
                window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                window.attributes.gravity = Gravity.BOTTOM
                val btnDelete = dialog.findViewById<Button>(R.id.delete_custom_dialog)
                val btnCancel = dialog.findViewById<Button>(R.id.cancel_custom_dialog)
                dialog.findViewById<TextView>(R.id.message_custom_dialog_remove).text =
                    getString(R.string.delete)

                btnDelete.setOnClickListener() {
                    daoCheckList.deleteIdAndSetDeleted(id)
                    startActivity(Intent(this, TasksActivity::class.java))
                    finish()
                }
                btnCancel.setOnClickListener() {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }

        enter.isClickable = false
        enter.visibility = View.INVISIBLE

        task.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                enter.isClickable = true
                enter.visibility = View.VISIBLE
            }

            override fun afterTextChanged(p0: Editable?) {
                enter.isClickable = true
            }

        })

        enter.setOnClickListener {
            val dataCheckList = CheckListEntity(
                data.id,
                data.checkState,
                task.text.toString(),
                date.text.toString(),
                isDeleted = 0
            )
            daoCheckList.update(dataCheckList)
            task.setOnEditorActionListener { p0, p1, p2 -> true }
            val view = currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            enter.visibility = View.INVISIBLE

            startActivity(Intent(this, TasksActivity::class.java))
            finish()
        }

        back.setOnClickListener {
            startActivity(Intent(this, TasksActivity::class.java))
            finish()
        }
    }
}