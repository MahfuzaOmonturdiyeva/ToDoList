package uz.gita.todolistbyM

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolistbyM.presenters.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uz.gita.todolistbyM.activities.ItemCheckListActivity
import uz.gita.todolistbyM.activities.ItemTaskActivity
import uz.gita.todolistbyM.activities.RemovedActivity
import uz.gita.todolistbyM.database.CheckListEntity
import uz.gita.todolistbyM.database.MyEntity
import uz.gita.todolistbyM.database.TasksEntity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TasksActivity() : AppCompatActivity(), Contract.View {
    private lateinit var buttonTask: Button
    private lateinit var buttonCheckList: Button
    private lateinit var buttonAdd: FloatingActionButton
    private lateinit var imageClear: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterTaskListAdapter: TaskListAdapter
    private lateinit var adapterChekListAdapter: CheckListAdapter
    private lateinit var presenterImpl: Contract.Presenter
    private lateinit var searchView: SearchView
    private lateinit var imageBasket: ImageView
    private var isSearched: Boolean = false
    private var isOncreate=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)
        buttonTask = findViewById(R.id.btn_tasks_tasks)
        buttonCheckList = findViewById(R.id.btn_checklist_tasks)
        buttonAdd = findViewById(R.id.add_tasks)
        imageClear = findViewById(R.id.clear_tasks)
        searchView = findViewById(R.id.search_tasks)
        imageBasket = findViewById(R.id.basket_image_tasks)

        recyclerView = findViewById(R.id.container)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        presenterImpl = PresenterImpl(this)
        isTask = presenterImpl.isTask

        loadTaskOrChecklist()

        imageBasket.setOnClickListener {
            startActivity(Intent(this, RemovedActivity::class.java))
            finish()
        }
        presenterImpl.updateTasks()

        isOncreate=false
        buttonTask.setOnClickListener {
            clickTask()
        }
        buttonCheckList.setOnClickListener {
            clickCheckList()
        }

        searchView.setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p1){
                    findViewById<TextView>(R.id.text11).visibility=View.INVISIBLE
                    imageBasket.visibility=View.INVISIBLE
                    imageClear.visibility = View.INVISIBLE
                    buttonAdd.visibility = View.INVISIBLE
                    isSearched = true
                }
                else{
                    findViewById<TextView>(R.id.text11).visibility=View.VISIBLE
                    imageBasket.visibility=View.VISIBLE
                    imageClear.visibility = View.VISIBLE
                    buttonAdd.visibility = View.VISIBLE
                    isSearched = false
                }
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                presenterImpl.search(p0 ?: "")
                return true
            }
        })
    }

    override var isTask: Boolean
        get() = presenterImpl.isTask
        set(value) {
            presenterImpl.isTask = value
        }

    private fun joinAdapters() {

        if (isTask) {
            adapterTaskListAdapter = TaskListAdapter(object : Listener {
                override fun onDelete(id: Int) {
                    oneDelete(id)
                }

                override fun clickedOneItem(id: Int) {
                    val intent = Intent(this@TasksActivity, ItemTaskActivity::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                    finish()
                }
            })
            recyclerView.adapter = adapterTaskListAdapter
            findViewById<ConstraintLayout>(R.id.container_no_checklist).visibility = View.GONE
            findViewById<ConstraintLayout>(R.id.container_no_tasks).visibility = View.VISIBLE
        } else {
            adapterChekListAdapter = CheckListAdapter(object : ListenerForChecklist {
                override fun onChecked(id: Int, ischecked: Int) {
                    presenterImpl.update(id, ischecked)
                    presenterImpl.updateTasks()

                }

                override fun clickedOneItem(id: Int) {
                    val intent = Intent(this@TasksActivity, ItemCheckListActivity::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                    finish()
                }

                override fun onDelete(id: Int) {
                    oneDelete(id)
                }
            })
            recyclerView.adapter = adapterChekListAdapter
            findViewById<ConstraintLayout>(R.id.container_no_tasks).visibility = View.GONE
            findViewById<ConstraintLayout>(R.id.container_no_checklist).visibility = View.VISIBLE
        }
    }

    private fun oneDelete(id: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_remove)
        val window = dialog.window
        if (window == null)
            return

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
            presenterImpl.onRemoveItemAndSetDeleted(id)
            dialog.dismiss()
        }
        btnCancel.setOnClickListener() {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun clickTask() {
        buttonTask.isClickable = false
        buttonCheckList.isClickable = true
        isOncreate=true
        isTask = true
        joinAdapters()
        presenterImpl.updateTasks()
        buttonTask.backgroundTintList =
            ContextCompat.getColorStateList(this,
                R.color.orange_600
            )
        buttonTask.setTextColor(ContextCompat.getColorStateList(this,
            R.color.white
        ))
        buttonCheckList.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.gray_200
            )
        buttonCheckList.setTextColor(ContextCompat.getColorStateList(this,
            R.color.gray_700
        ))
    }

    private fun clickCheckList() {
        buttonTask.isClickable = true
        buttonCheckList.isClickable = false
        isOncreate=true
        isTask = false
        joinAdapters()
        presenterImpl.updateTasks()
        buttonCheckList.backgroundTintList =
            ContextCompat.getColorStateList(this,
                R.color.orange_600
            )
        buttonCheckList.setTextColor(ContextCompat.getColorStateList(this,
            R.color.white
        ))
        buttonTask.backgroundTintList = ContextCompat.getColorStateList(this,
            R.color.gray_200
        )
        buttonTask.setTextColor(ContextCompat.getColorStateList(this,
            R.color.gray_700
        ))
    }

    private fun loadTaskOrChecklist() {
        if (!isTask) {
            joinAdapters()
            buttonTask.backgroundTintList = ContextCompat.getColorStateList(this,
                R.color.gray_200
            )
            buttonTask.setTextColor(ContextCompat.getColorStateList(this,
                R.color.gray_700
            ))
            buttonTask.isClickable = true
            buttonCheckList.isClickable = false
            buttonCheckList.backgroundTintList =
                ContextCompat.getColorStateList(this,
                    R.color.orange_600
                )
            buttonCheckList.setTextColor(ContextCompat.getColorStateList(this,
                R.color.white
            ))

        } else {
            joinAdapters()
            buttonTask.backgroundTintList =
                ContextCompat.getColorStateList(this,
                    R.color.orange_600
                )
            buttonTask.setTextColor(ContextCompat.getColorStateList(this,
                R.color.white
            ))
            buttonCheckList.isClickable = true
            buttonTask.isClickable = false
            buttonCheckList.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.gray_200
                )
            buttonCheckList.setTextColor(ContextCompat.getColorStateList(this, R.color.gray_700
            ))
        }
    }

    override fun clickClear(view: View) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_remove)
        val window = dialog.window
        if (window == null)
            return

        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.attributes.gravity = Gravity.BOTTOM
        val btnDelete = dialog.findViewById<Button>(R.id.delete_custom_dialog)
        val btnCancel = dialog.findViewById<Button>(R.id.cancel_custom_dialog)
        dialog.findViewById<TextView>(R.id.message_custom_dialog_remove).text =
            getString(R.string.delete_all_notes)

        btnDelete.setOnClickListener() {
            presenterImpl.onClearAndSetDeleted()
            dialog.dismiss()
        }
        btnCancel.setOnClickListener() {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun clickAdd(view: View) {
        if (isTask) {
            startActivity(Intent(this, ItemTaskActivity::class.java))
            finish()
        } else {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.custom_dialog_add_checktask)

            val window = dialog.window
            if (window == null)
                return

            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.attributes.gravity = Gravity.BOTTOM
            val addBtn = dialog.findViewById<Button>(R.id.add_custom_dialog)
            val task = dialog.findViewById<EditText>(R.id.task_custom_dialog)

            addBtn.isEnabled = false

            val view = dialog.currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 1)
            }
            task.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    addBtn.isEnabled = true
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.isEmpty() == true) {
                        addBtn.isEnabled = false
                    }
                }
            })

            addBtn.setOnClickListener() {
                val dateFormat: String =
                    DateFormat.getDateInstance(DateFormat.MONTH_FIELD)
                        .format(Calendar.getInstance().time)

                val clock = SimpleDateFormat("HH:mm").format(Calendar.getInstance().time)
                val data = CheckListEntity(
                    0,
                    0,
                    task.text.toString(),
                    (dateFormat.split(".")[0] + " " + clock),
                    0
                )
                presenterImpl.onAdd(data)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun showList(list: List<MyEntity>) {
        if (isTask) {
            if (!isSearched || isOncreate) {
                if (list.isEmpty())
                    findViewById<ConstraintLayout>(R.id.container_no_tasks).visibility =
                        View.VISIBLE
                else
                    findViewById<ConstraintLayout>(R.id.container_no_tasks).visibility = View.GONE
            }
            isOncreate=false
            adapterTaskListAdapter.submitList((list as List<TasksEntity>))
        } else {
            if (!isSearched || isOncreate) {
                if (list.isEmpty())
                    findViewById<ConstraintLayout>(R.id.container_no_checklist).visibility =
                        View.VISIBLE
                else
                    findViewById<ConstraintLayout>(R.id.container_no_checklist).visibility =
                        View.GONE
            }
            isOncreate=false
            adapterChekListAdapter.submitList(list as List<CheckListEntity>)
        }
    }
}
