package com.jmbedu.school.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DemoStudent::class],
    version = 1,
    exportSchema = false
)
abstract class JmbEduDatabase : RoomDatabase() {
    abstract fun demoStudentDao(): DemoStudentDao

    companion object {
        @Volatile
        private var instance: JmbEduDatabase? = null

        fun getInstance(context: Context): JmbEduDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    JmbEduDatabase::class.java,
                    "jmb_edu_demo.db"
                ).build().also { instance = it }
            }
        }
    }
}