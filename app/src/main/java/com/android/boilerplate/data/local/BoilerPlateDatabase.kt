package com.android.boilerplate.data.local

import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase



@Database(entities = [UserLocalData::class], version = 1, exportSchema = true)
abstract class BoilerPlateDatabase : RoomDatabase(){
    abstract val userDao : UserDao

}