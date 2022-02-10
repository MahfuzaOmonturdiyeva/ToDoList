package uz.gita.todolistbyM.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolistbyM.R
import uz.gita.todolistbyM.TasksActivity
import uz.gita.todolistbyM.database.CheckListEntity
import uz.gita.todolistbyM.database.MyEntity
import uz.gita.todolistbyM.database.TasksEntity
import uz.gita.todolistbyM.presenters.*
import java.util.*
import kotlin.collections.ArrayList

class RemovedActivity : AppCompatActivity(), uz.gita.todolistbyM.Contract.View {
    private lateinit var imageClear: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterTaskListDeletedAdapter: TaskListDeletedAdapter
    private lateinit var adapterChekListDeletedAdapter: CheckListDeletedAdapter
    private lateinit var presenterImpl: uz.gita.todolistbyM.Contract.Presenter
    private lateinit var searchView: SearchView
    private lateinit var imageBack: ImageView
    private var isSearched = false
//    private var isOncreate=true

    override fun onBackPressed() {
        startActivity(Intent(this, TasksActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_removed)
        imageClear = findViewById(R.id.clear_removed)
        searchView = findViewById(R.id.search_removed)
        imageBack = findViewById(R.id.back_image_removed)

        recyclerView = findViewById(R.id.container_removed)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        presenterImpl = PresenterImpl(this)
        joinAdapters()

        presenterImpl.getNearlyDeletedList()

        imageBack.setOnClickListener {
            startActivity(Intent(this, TasksActivity::class.java))
            finish()
        }

        searchView.setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p1) {
                    findViewById<TextView>(R.id.text11).visibility = View.INVISIBLE
                    imageBack.visibility = View.INVISIBLE
                    imageClear.visibility = View.INVISIBLE
                    isSearched = true
                } else {
                    findViewById<TextView>(R.id.text11).visibility = View.VISIBLE
                    imageBack.visibility = View.VISIBLE
                    imageClear.visibility = View.VISIBLE
                    isSearched = false
                }
            }
        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                imageClear.visibility = View.GONE

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                presenterImpl.searchDeleted(p0 ?: "")
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
            adapterTaskListDeletedAdapter = TaskListDeletedAdapter(object : ListenerForTaskRemoved {
                override fun backReturn(id: Int) {
                    presenterImpl.onBackReturn(id)
//                    Toast.makeText(this@RemovedActivity, "Note restored", Toast.LENGTH_SHORT).show()
                }

                override fun onDelete(id: Int) {
                    oneDelete(id)
                }

                override fun clickedOneItem(id: Int) {
                    val intent = Intent(this@RemovedActivity, ItemTaskWithBackActivity::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                    finish()
                }
            })

            recyclerView.adapter = adapterTaskListDeletedAdapter
            findViewById<ConstraintLayout>(R.id.container_no_checklist_removed).visibility =
                View.GONE
            findViewById<ConstraintLayout>(R.id.container_no_tasks_removed).visibility =
                View.VISIBLE
        } else {
            adapterChekListDeletedAdapter =
                CheckListDeletedAdapter(object : ListenerForChecklistRemoved {
                    override fun onChecked(id: Int, ischecked: Int) {
                        presenterImpl.update(id, ischecked)
                        presenterImpl.updateTasksNearly()
                    }

                    override fun backReturn(id: Int) {
                        presenterImpl.onBackReturn(id)
//                                val snackbar=Snackbar.make(,"Note restored", Snackbar.LENGTH_SHORT);
//                        snackbar.show()
//                        Toast.makeText(this@RemovedActivity, "Note restored", Toast.LENGTH_SHORT)
//                            .show()
                    }

                    override fun clickedOneItem(id: Int) {
                        val intent =
                            Intent(this@RemovedActivity, ItemCheckListWithBackActivity::class.java)
                        intent.putExtra("id", id)
                        startActivity(intent)
                        finish()
                    }

                    override fun onDelete(id: Int) {
                        oneDelete(id)
                    }
                })
            recyclerView.adapter = adapterChekListDeletedAdapter
            findViewById<ConstraintLayout>(R.id.container_no_tasks_removed).visibility = View.GONE
            findViewById<ConstraintLayout>(R.id.container_no_checklist_removed).visibility =
                View.VISIBLE
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

        btnDelete.setOnClickListener {
            presenterImpl.onRemoveItem(id)
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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

        btnDelete.setOnClickListener {
            presenterImpl.onClear()
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun clickAdd(view: View) {
    }

    override fun showList(list: List<MyEntity>) {
        if (isTask) {
            if (!isSearched ) {
                if (list.isEmpty())
                    findViewById<ConstraintLayout>(R.id.container_no_tasks_removed).visibility =
                        View.VISIBLE
                else
                    findViewById<ConstraintLayout>(R.id.container_no_tasks_removed).visibility =
                        View.GONE
            }
            adapterTaskListDeletedAdapter.submitList((list as ArrayList<TasksEntity>))

        } else {
            if (!isSearched ) {
                if (list.isEmpty())
                    findViewById<ConstraintLayout>(R.id.container_no_checklist_removed).visibility =
                        View.VISIBLE
                else
                    findViewById<ConstraintLayout>(R.id.container_no_checklist_removed).visibility =
                        View.GONE
            }
            adapterChekListDeletedAdapter.submitList(list as ArrayList<CheckListEntity>)
        }
    }
}