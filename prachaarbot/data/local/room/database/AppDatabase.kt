package com.prachaarbot.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prachaarbot.data.local.room.dao.CallDao
import com.prachaarbot.data.local.room.entity.CallEntry
import com.prachaarbot.data.local.room.typeconverters.DateTypeConverter

@Database(entities = [CallEntry::class], version = 1)
@TypeConverters(DateTypeConverter::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun callDao(): CallDao


}