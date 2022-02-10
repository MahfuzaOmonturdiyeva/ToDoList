package uz.gita.todolistbyM.app

import android.app.Application
import androidx.room.Room
import uz.gita.todolistbyM.database.DatabaseTask
import uz.gita.todolistbyM.database.LocalStorage

class App:Application() {

    companion object DatabaseSetup {
        lateinit var database: DatabaseTask
        lateinit var localStorage: LocalStorage
    }

    override fun onCreate() {
        super.onCreate()
        App.database =  Room.databaseBuilder(this, DatabaseTask::class.java, "ToDo1").allowMainThreadQueries().build()
        App.localStorage=LocalStorage(this)
    }
}
