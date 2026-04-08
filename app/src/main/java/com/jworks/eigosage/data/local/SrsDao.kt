package com.jworks.eigosage.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jworks.eigosage.data.local.entities.SrsCardEntity
import com.jworks.eigosage.data.local.entities.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SrsDao {

    // --- Cards ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCard(card: SrsCardEntity): Long

    @Update
    suspend fun updateCard(card: SrsCardEntity)

    @Query("DELETE FROM srs_cards WHERE id = :id")
    suspend fun deleteCard(id: Long)

    @Query("SELECT * FROM srs_cards WHERE word = :word LIMIT 1")
    suspend fun getCardByWord(word: String): SrsCardEntity?

    @Query("SELECT * FROM srs_cards WHERE next_review_at <= :now ORDER BY next_review_at ASC")
    suspend fun getDueCards(now: Long = System.currentTimeMillis()): List<SrsCardEntity>

    @Query("SELECT COUNT(*) FROM srs_cards WHERE next_review_at <= :now")
    fun getDueCardCount(now: Long = System.currentTimeMillis()): Flow<Int>

    @Query("SELECT COUNT(*) FROM srs_cards")
    fun getTotalCardCount(): Flow<Int>

    @Query("SELECT * FROM srs_cards ORDER BY created_at DESC")
    fun getAllCards(): Flow<List<SrsCardEntity>>

    @Query("SELECT * FROM srs_cards ORDER BY next_review_at ASC LIMIT :limit")
    fun getUpcomingCards(limit: Int = 20): Flow<List<SrsCardEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM srs_cards WHERE word = :word LIMIT 1)")
    fun isInDeck(word: String): Flow<Boolean>

    // --- Sessions ---

    @Insert
    suspend fun insertSession(session: StudySessionEntity): Long

    @Update
    suspend fun updateSession(session: StudySessionEntity)

    @Query("SELECT * FROM study_sessions ORDER BY started_at DESC LIMIT :limit")
    fun getRecentSessions(limit: Int = 10): Flow<List<StudySessionEntity>>

    @Query("SELECT SUM(words_reviewed) FROM study_sessions")
    fun getTotalWordsReviewed(): Flow<Int?>

    @Query("SELECT MAX(streak) FROM study_sessions")
    fun getBestStreak(): Flow<Int?>
}
