package com.prachaarbot.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "calls")
data class CallEntry(
    @PrimaryKey val number: String,
    var messageCount: Int = 0,
    var date: LocalDate = LocalDate.now()
)