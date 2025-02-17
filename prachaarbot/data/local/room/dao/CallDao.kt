package com.prachaarbot.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prachaarbot.data.local.room.entity.CallEntry
import java.time.LocalDate

@Dao
interface CallDao {
    @Query("SELECT * FROM calls WHERE number = :number")
    suspend fun getEntry(number: String): CallEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(callEntry: CallEntry)

    @Query("UPDATE calls SET messageCount = 0")
    suspend fun resetCounts()

    @Query("UPDATE calls SET date = :updatedDate")
    suspend fun updateDate(updatedDate: LocalDate)
}

