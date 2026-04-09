package com.jworks.eigosage.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_sessions",
    indices = [Index(value = ["created_at"])]
)
data class ChatSessionEntity(
    @PrimaryKey val id: String, // UUID
    @ColumnInfo(name = "ocr_text_preview") val ocrTextPreview: String, // first ~200 chars of captured text
    @ColumnInfo(name = "cefr_level") val cefrLevel: String? = null,
    @ColumnInfo(name = "system_prompt") val systemPrompt: String? = null,
    @ColumnInfo(name = "message_count") val messageCount: Int = 0,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
