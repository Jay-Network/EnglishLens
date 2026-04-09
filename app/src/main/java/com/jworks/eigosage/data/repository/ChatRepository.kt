package com.jworks.eigosage.data.repository

import com.jworks.eigosage.data.local.ChatDao
import com.jworks.eigosage.data.local.entities.ChatMessageEntity
import com.jworks.eigosage.data.local.entities.ChatSessionEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatDao: ChatDao
) {
    fun getAllSessions(): Flow<List<ChatSessionEntity>> = chatDao.getAllSessions()

    fun getSessionCount(): Flow<Int> = chatDao.getSessionCount()

    suspend fun getSession(sessionId: String): ChatSessionEntity? =
        chatDao.getSession(sessionId)

    suspend fun getMessages(sessionId: String): List<ChatMessageEntity> =
        chatDao.getMessages(sessionId)

    fun getMessagesFlow(sessionId: String): Flow<List<ChatMessageEntity>> =
        chatDao.getMessagesFlow(sessionId)

    /**
     * Save a chat session with all its messages.
     * Creates or updates the session, and replaces all messages.
     */
    suspend fun saveSession(
        sessionId: String,
        ocrTextPreview: String,
        cefrLevel: String?,
        systemPrompt: String?,
        messages: List<ChatMessageData>
    ) {
        val now = System.currentTimeMillis()
        val existing = chatDao.getSession(sessionId)

        val session = ChatSessionEntity(
            id = sessionId,
            ocrTextPreview = ocrTextPreview.take(200),
            cefrLevel = cefrLevel,
            systemPrompt = systemPrompt,
            messageCount = messages.size,
            createdAt = existing?.createdAt ?: now,
            updatedAt = now
        )
        chatDao.insertSession(session)

        val entities = messages.map { msg ->
            ChatMessageEntity(
                id = msg.id,
                sessionId = sessionId,
                role = msg.role,
                content = msg.content,
                timestamp = msg.timestamp
            )
        }
        chatDao.deleteMessages(sessionId)
        chatDao.insertMessages(entities)
    }

    suspend fun deleteSession(sessionId: String) {
        chatDao.deleteSession(sessionId)
    }

    fun newSessionId(): String = UUID.randomUUID().toString()
}

/**
 * Simple data holder for passing messages to saveSession without
 * coupling to the UI ChatMessage class.
 */
data class ChatMessageData(
    val id: String,
    val role: String, // "user", "model", "system"
    val content: String,
    val timestamp: Long
)
