package com.jworks.eigosage.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "started_at") val startedAt: Long,
    @ColumnInfo(name = "ended_at") val endedAt: Long? = null,
    @ColumnInfo(name = "words_reviewed") val wordsReviewed: Int = 0,
    @ColumnInfo(name = "words_correct") val wordsCorrect: Int = 0,
    val streak: Int = 0
)
