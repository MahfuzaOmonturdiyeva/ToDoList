package uz.gita.todolistbyM.database

import android.content.Context

class LocalStorage(context: Context) {
    private val sharedPreferences=context.getSharedPreferences("LocalStorage",Context.MODE_PRIVATE)
    private val keyIsTask="keyTask"

    var isTask:Boolean
    get() = sharedPreferences.getBoolean(keyIsTask,false)
    set(value) = sharedPreferences.edit().putBoolean(keyIsTask,value).apply()

}