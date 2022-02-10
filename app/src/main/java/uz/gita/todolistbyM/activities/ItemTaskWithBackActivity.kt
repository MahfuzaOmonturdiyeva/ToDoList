package uz.gita.todolistbyM.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import uz.gita.todolistbyM.R
import uz.gita.todolistbyM.app.App
import uz.gita.todolistbyM.database.DatabaseTask
import uz.gita.todolistbyM.database.TaskDao
import uz.gita.todolistbyM.database.TasksEntity
import java.util.*

class ItemTaskWithBackActivity() : AppCompatActivity() {
    private lateinit var database: DatabaseTask
    private lateinit var daoTask: TaskDao
    private var id = -1

    override fun onBackPressed() {
        startActivity(Intent(this, RemovedActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_task)

        id = intent.getIntExtra("id", -1)

        database = App.database
        daoTask = database.getTaskDao()
        val description = findViewById<EditText>(R.id.description_one_task)
        val task = findViewById<EditText>(R.id.task_one_task)
        val date = findViewById<TextView>(R.id.data_one_task)
        val enter = findViewById<ImageView>(R.id.saved_one_task)
        val back = findViewById<ImageView>(R.id.back_one_task)
        val delete = findViewById<ImageView>(R.id.delete_one_task)
        val backReturn = findViewById<ImageView>(R.id.back_return_one_task)

        backReturn.visibility = View.VISIBLE

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
                    daoTask.deleteId(id)
                    startActivity(Intent(this, RemovedActivity::class.java))
                    finish()
                }
                btnCancel.setOnClickListener() {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        if (id != -1) {
            backReturn.setOnClickListener {
                daoTask.backDeleted(id)
                Toast.makeText(this, "Note restored", Toast.LENGTH_SHORT).show()
//                Toast.makeText(this, "Note restored", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RemovedActivity::class.java))
                finish()
            }

            enter.isClickable = false
            enter.visibility = View.INVISIBLE
            val data = daoTask.get(id)
            description.setText(data.taskDescription)
            task.setText(data.task)
            date.text = data.date

            description.addTextChangedListener(object : TextWatcher {
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

            enter.setOnClickListener() {
                val view = currentFocus
                if (view != null) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                val dataTask = TasksEntity(
                    data.id,
                    description.text.toString(),
                    task.text.toString(),
                    date.text.toString(),
                    isDeleted = 1
                )
                daoTask.update(dataTask)
                task.setOnEditorActionListener { p0, p1, p2 -> true }
                enter.visibility = View.INVISIBLE

                startActivity(Intent(this, RemovedActivity::class.java))
                finish()
            }

        }
        back.setOnClickListener() {
            startActivity(Intent(this, RemovedActivity::class.java))
            finish()
        }
    }
}