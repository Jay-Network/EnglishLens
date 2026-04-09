package com.jworks.eigosage.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["session_id", "timestamp"])]
)
data class ChatMessageEntity(
    @PrimaryKey val id: String, // UUID
    @ColumnInfo(name = "session_id") val sessionId: String,
    val role: String, // "user", "model", "system"
    val content: String,
    val timestamp: Long
)
