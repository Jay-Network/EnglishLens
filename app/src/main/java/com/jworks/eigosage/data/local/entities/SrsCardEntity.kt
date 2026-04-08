package com.jworks.eigosage.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Spaced repetition card using the SM-2 algorithm.
 *
 * SM-2 fields:
 * - easeFactor: starts at 2.5, adjusted per review (min 1.3)
 * - interval: days until next review
 * - repetition: consecutive correct answers (resets to 0 on incorrect)
 */
@Entity(
    tableName = "srs_cards",
    indices = [
        Index(value = ["word"], unique = true),
        Index(value = ["next_review_at"])
    ]
)
data class SrsCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
    val definition: String,
    val phonetic: String? = null,
    @ColumnInfo(name = "cefr_level") val cefrLevel: String? = null,
    @ColumnInfo(name = "context_snippet") val contextSnippet: String? = null,
    @ColumnInfo(name = "example_sentence") val exampleSentence: String? = null,

    // SM-2 scheduling fields
    @ColumnInfo(name = "ease_factor") val easeFactor: Double = 2.5,
    val interval: Int = 0,
    val repetition: Int = 0,
    @ColumnInfo(name = "next_review_at") val nextReviewAt: Long = 0L,
    @ColumnInfo(name = "last_reviewed_at") val lastReviewedAt: Long? = null,

    // Stats
    @ColumnInfo(name = "total_reviews") val totalReviews: Int = 0,
    @ColumnInfo(name = "correct_reviews") val correctReviews: Int = 0,

    @ColumnInfo(name = "created_at") val createdAt: Long,
    val source: String = "manual" // "manual", "bookmark", "difficult_words"
)
