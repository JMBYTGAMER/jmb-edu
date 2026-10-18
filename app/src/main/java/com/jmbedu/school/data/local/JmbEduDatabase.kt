package com.jmbedu.school.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [DemoStudent::class, AttendanceRecord::class],
    version = 2,
    exportSchema = false
)
abstract class JmbEduDatabase : RoomDatabase() {
    abstract fun demoStudentDao(): DemoStudentDao
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile
        private var instance: JmbEduDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `attendance_records` (" +
                        "`id` TEXT NOT NULL, `studentId` TEXT NOT NULL, " +
                        "`studentName` TEXT NOT NULL, `date` TEXT NOT NULL, " +
                        "`status` TEXT NOT NULL, PRIMARY KEY(`id`))"
                )
            }
        }

        fun getInstance(context: Context): JmbEduDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    JmbEduDatabase::class.java,
                    "jmb_edu_demo.db"
                ).addMigrations(MIGRATION_1_2)
                    .build()
                    .also { instance = it }
            }
        }
    }
}