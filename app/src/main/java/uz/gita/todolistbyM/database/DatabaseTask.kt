package uz.gita.todolistbyM.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TasksEntity::class,CheckListEntity::class],
    version = 1
)
abstract class DatabaseTask : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
    abstract fun getCheckListDao():CheckListDao

//    companion object {
//        @Volatile
//        private var INSTANCE: DatabaseTask? = null
//
//        fun getDatabase(context: Context): DatabaseTask {
//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    DatabaseTask::class.java,
//                    "app_database"
//                )
//                    .build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
}