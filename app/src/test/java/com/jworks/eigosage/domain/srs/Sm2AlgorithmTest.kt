package com.jworks.eigosage.domain.srs

import com.jworks.eigosage.data.local.entities.SrsCardEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Sm2AlgorithmTest {

    private val now = 1_000_000_000L
    private val dayMs = 86_400_000L

    private fun newCard() = SrsCardEntity(
        id = 1,
        word = "test",
        definition = "a test word",
        createdAt = now
    )

    // -- First correct review --

    @Test
    fun `first correct review sets interval to 1 day`() {
        val card = newCard()
        val result = Sm2Algorithm.review(card, quality = 4, now = now)

        assertEquals(1, result.repetition)
        assertEquals(1, result.interval)
        assertEquals(now + dayMs, result.nextReviewAt)
    }

    @Test
    fun `second correct review sets interval to 6 days`() {
        val card = newCard().copy(repetition = 1, interval = 1)
        val result = Sm2Algorithm.review(card, quality = 4, now = now)

        assertEquals(2, result.repetition)
        assertEquals(6, result.interval)
        assertEquals(now + 6 * dayMs, result.nextReviewAt)
    }

    @Test
    fun `third correct review uses ease factor`() {
        val card = newCard().copy(repetition = 2, interval = 6, easeFactor = 2.5)
        val result = Sm2Algorithm.review(card, quality = 4, now = now)

        assertEquals(3, result.repetition)
        // interval = (6 * newEase).toInt() — newEase ≈ 2.5 + (0.1 - 1*0.1) = 2.5
        assertEquals(15, result.interval) // 6 * 2.5 = 15
        assertEquals(now + 15 * dayMs, result.nextReviewAt)
    }

    // -- Failed review --

    @Test
    fun `failed review resets repetition and interval`() {
        val card = newCard().copy(repetition = 3, interval = 15, easeFactor = 2.5)
        val result = Sm2Algorithm.review(card, quality = 2, now = now)

        assertEquals(0, result.repetition)
        assertEquals(0, result.interval)
        assertEquals(now, result.nextReviewAt) // due immediately
    }

    @Test
    fun `quality 0 resets card`() {
        val card = newCard().copy(repetition = 5, interval = 30, easeFactor = 2.5)
        val result = Sm2Algorithm.review(card, quality = 0, now = now)

        assertEquals(0, result.repetition)
        assertEquals(0, result.interval)
    }

    // -- Ease factor adjustments --

    @Test
    fun `perfect recall increases ease factor`() {
        val card = newCard().copy(easeFactor = 2.5)
        val result = Sm2Algorithm.review(card, quality = 5, now = now)

        assertTrue(result.easeFactor > 2.5)
    }

    @Test
    fun `hard correct recall decreases ease factor`() {
        val card = newCard().copy(easeFactor = 2.5)
        val result = Sm2Algorithm.review(card, quality = 3, now = now)

        assertTrue(result.easeFactor < 2.5)
    }

    @Test
    fun `ease factor never goes below minimum`() {
        val card = newCard().copy(easeFactor = 1.3)
        val result = Sm2Algorithm.review(card, quality = 0, now = now)

        assertTrue(result.easeFactor >= Sm2Algorithm.MIN_EASE_FACTOR)
    }

    @Test
    fun `repeated failures keep ease at minimum`() {
        var card = newCard().copy(easeFactor = 1.5)
        repeat(10) {
            card = Sm2Algorithm.review(card, quality = 0, now = now)
        }
        assertTrue(card.easeFactor >= Sm2Algorithm.MIN_EASE_FACTOR)
    }

    // -- Stats tracking --

    @Test
    fun `total reviews increments on every review`() {
        val card = newCard()
        val result = Sm2Algorithm.review(card, quality = 4, now = now)
        assertEquals(1, result.totalReviews)
    }

    @Test
    fun `correct reviews increments only on correct`() {
        val card = newCard()

        val correct = Sm2Algorithm.review(card, quality = 4, now = now)
        assertEquals(1, correct.correctReviews)

        val incorrect = Sm2Algorithm.review(card, quality = 2, now = now)
        assertEquals(0, incorrect.correctReviews)
    }

    @Test
    fun `last reviewed at is set to now`() {
        val card = newCard()
        val result = Sm2Algorithm.review(card, quality = 4, now = now)
        assertEquals(now, result.lastReviewedAt)
    }

    // -- Quality clamping --

    @Test
    fun `quality above 5 is clamped to 5`() {
        val card = newCard()
        val result = Sm2Algorithm.review(card, quality = 10, now = now)
        // Should behave like quality=5
        assertTrue(result.repetition == 1)
    }

    @Test
    fun `quality below 0 is clamped to 0`() {
        val card = newCard()
        val result = Sm2Algorithm.review(card, quality = -5, now = now)
        // Should behave like quality=0 (failure)
        assertEquals(0, result.repetition)
    }

    // -- Boundary: quality 3 is correct --

    @Test
    fun `quality 3 is treated as correct`() {
        val card = newCard()
        val result = Sm2Algorithm.review(card, quality = 3, now = now)
        assertEquals(1, result.repetition)
        assertEquals(1, result.correctReviews)
    }

    @Test
    fun `quality 2 is treated as incorrect`() {
        val card = newCard()
        val result = Sm2Algorithm.review(card, quality = 2, now = now)
        assertEquals(0, result.repetition)
        assertEquals(0, result.correctReviews)
    }

    // -- ReviewQuality enum --

    @Test
    fun `review quality values are correct`() {
        assertEquals(0, ReviewQuality.AGAIN.value)
        assertEquals(1, ReviewQuality.WRONG.value)
        assertEquals(3, ReviewQuality.HARD.value)
        assertEquals(4, ReviewQuality.GOOD.value)
        assertEquals(5, ReviewQuality.EASY.value)
    }
}
