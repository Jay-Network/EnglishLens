package com.jworks.eigosage.data.repository

import com.jworks.eigosage.data.local.SrsDao
import com.jworks.eigosage.data.local.entities.SrsCardEntity
import com.jworks.eigosage.data.local.entities.StudySessionEntity
import com.jworks.eigosage.domain.srs.Sm2Algorithm
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SrsRepository @Inject constructor(
    private val srsDao: SrsDao
) {

    suspend fun addCard(
        word: String,
        definition: String,
        phonetic: String? = null,
        cefrLevel: String? = null,
        contextSnippet: String? = null,
        exampleSentence: String? = null,
        source: String = "manual"
    ): Long {
        val card = SrsCardEntity(
            word = word,
            definition = definition,
            phonetic = phonetic,
            cefrLevel = cefrLevel,
            contextSnippet = contextSnippet?.take(200),
            exampleSentence = exampleSentence,
            createdAt = System.currentTimeMillis(),
            nextReviewAt = System.currentTimeMillis(), // due immediately
            source = source
        )
        return srsDao.insertCard(card)
    }

    suspend fun reviewCard(card: SrsCardEntity, quality: Int): SrsCardEntity {
        val updated = Sm2Algorithm.review(card, quality)
        srsDao.updateCard(updated)
        return updated
    }

    suspend fun getDueCards(): List<SrsCardEntity> = srsDao.getDueCards()

    fun getDueCardCount(): Flow<Int> = srsDao.getDueCardCount()

    fun getTotalCardCount(): Flow<Int> = srsDao.getTotalCardCount()

    fun getAllCards(): Flow<List<SrsCardEntity>> = srsDao.getAllCards()

    fun isInDeck(word: String): Flow<Boolean> = srsDao.isInDeck(word)

    suspend fun removeCard(id: Long) = srsDao.deleteCard(id)

    suspend fun getCardByWord(word: String): SrsCardEntity? = srsDao.getCardByWord(word)

    // Sessions

    suspend fun startSession(): Long {
        return srsDao.insertSession(
            StudySessionEntity(startedAt = System.currentTimeMillis())
        )
    }

    suspend fun endSession(session: StudySessionEntity) {
        srsDao.updateSession(
            session.copy(endedAt = System.currentTimeMillis())
        )
    }

    fun getRecentSessions(limit: Int = 10): Flow<List<StudySessionEntity>> =
        srsDao.getRecentSessions(limit)

    fun getTotalWordsReviewed(): Flow<Int?> = srsDao.getTotalWordsReviewed()

    fun getBestStreak(): Flow<Int?> = srsDao.getBestStreak()
}
