package com.jworks.eigosage.domain.srs

import com.jworks.eigosage.data.local.entities.SrsCardEntity
import kotlin.math.max

/**
 * SM-2 (SuperMemo 2) spaced repetition algorithm.
 *
 * Quality ratings:
 *   0 = complete blackout
 *   1 = incorrect, but recognized on reveal
 *   2 = incorrect, but easy recall after reveal
 *   3 = correct with serious difficulty
 *   4 = correct with hesitation
 *   5 = perfect recall
 *
 * For our flashcard UI, we simplify to:
 *   Again (0) → Wrong (1) → Hard (3) → Good (4) → Easy (5)
 */
object Sm2Algorithm {

    const val MIN_EASE_FACTOR = 1.3

    /**
     * Compute the next review state for a card given a quality rating (0-5).
     * Returns a copy of the card with updated SM-2 fields.
     */
    fun review(card: SrsCardEntity, quality: Int, now: Long = System.currentTimeMillis()): SrsCardEntity {
        val q = quality.coerceIn(0, 5)

        val newEase = max(
            MIN_EASE_FACTOR,
            card.easeFactor + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
        )

        val isCorrect = q >= 3

        val (newRepetition, newInterval) = if (isCorrect) {
            val rep = card.repetition + 1
            val interval = when (rep) {
                1 -> 1
                2 -> 6
                else -> (card.interval * newEase).toInt().coerceAtLeast(1)
            }
            rep to interval
        } else {
            // Reset on failure
            0 to 0
        }

        val nextReviewMs = if (newInterval == 0) {
            // Due immediately (failed card — review again this session)
            now
        } else {
            now + newInterval * DAY_MS
        }

        return card.copy(
            easeFactor = newEase,
            interval = newInterval,
            repetition = newRepetition,
            nextReviewAt = nextReviewMs,
            lastReviewedAt = now,
            totalReviews = card.totalReviews + 1,
            correctReviews = if (isCorrect) card.correctReviews + 1 else card.correctReviews
        )
    }

    private const val DAY_MS = 86_400_000L
}

enum class ReviewQuality(val value: Int, val label: String) {
    AGAIN(0, "Again"),
    WRONG(1, "Wrong"),
    HARD(3, "Hard"),
    GOOD(4, "Good"),
    EASY(5, "Easy");
}
