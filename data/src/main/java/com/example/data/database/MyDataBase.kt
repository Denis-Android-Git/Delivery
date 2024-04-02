package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.domain.model.ProductFromDb
import com.example.domain.repository.DbRepository

@Database(
    entities = [
        ProductFromDb::class
    ],
    version = 1
)
abstract class MyDataBase : RoomDatabase() {

    abstract fun itemDao(): DbRepository

    companion object {
        @Volatile
        private var INSTANCE: MyDataBase? = null

        fun getDatabase(
            context: Context
        ): MyDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDataBase::class.java,
                    "DataBase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}