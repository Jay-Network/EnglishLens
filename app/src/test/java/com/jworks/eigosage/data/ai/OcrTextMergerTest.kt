package com.jworks.eigosage.data.ai

import android.graphics.Rect
import android.util.Size
import com.jworks.eigosage.domain.models.DetectedText
import com.jworks.eigosage.domain.models.OCRResult
import com.jworks.eigosage.domain.models.TextElement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OcrTextMergerTest {

    private val defaultSize = Size(1080, 1920)

    private fun makeElement(text: String, left: Int = 0, top: Int = 0, right: Int = 100, bottom: Int = 50): TextElement {
        return TextElement(text = text, bounds = Rect(left, top, right, bottom))
    }

    private fun makeLine(text: String, elements: List<TextElement>, bounds: Rect? = Rect(0, 0, 500, 50)): DetectedText {
        return DetectedText(text = text, bounds = bounds, confidence = 0.95f, elements = elements)
    }

    private fun makeResult(lines: List<DetectedText>): OCRResult {
        return OCRResult(texts = lines, timestamp = System.currentTimeMillis(), imageSize = defaultSize)
    }

    // -- Empty input handling --

    @Test
    fun `returns mlKit result when mlKit texts are empty`() {
        val mlKit = makeResult(emptyList())
        val result = OcrTextMerger.merge(mlKit, listOf("Hello world"))
        assertEquals(0, result.texts.size)
    }

    @Test
    fun `returns mlKit result when gemini lines are empty`() {
        val line = makeLine("Helo", listOf(makeElement("Helo")))
        val mlKit = makeResult(listOf(line))
        val result = OcrTextMerger.merge(mlKit, emptyList())
        assertEquals(1, result.texts.size)
        assertEquals("Helo", result.texts[0].text)
    }

    @Test
    fun `returns mlKit result when both are empty`() {
        val mlKit = makeResult(emptyList())
        val result = OcrTextMerger.merge(mlKit, emptyList())
        assertEquals(0, result.texts.size)
    }

    // -- Line count mismatch --

    @Test
    fun `returns mlKit result when line counts differ - more gemini lines`() {
        val line = makeLine("Hello world", listOf(makeElement("Hello"), makeElement("world")))
        val mlKit = makeResult(listOf(line))
        val geminiLines = listOf("Hello world", "Extra line")

        val result = OcrTextMerger.merge(mlKit, geminiLines)
        assertEquals(1, result.texts.size)
        assertEquals("Hello world", result.texts[0].text)
    }

    @Test
    fun `returns mlKit result when line counts differ - more mlKit lines`() {
        val line1 = makeLine("Line one", listOf(makeElement("Line"), makeElement("one")))
        val line2 = makeLine("Line two", listOf(makeElement("Line"), makeElement("two")))
        val mlKit = makeResult(listOf(line1, line2))
        val geminiLines = listOf("Line one")

        val result = OcrTextMerger.merge(mlKit, geminiLines)
        assertEquals(2, result.texts.size)
        assertEquals("Line one", result.texts[0].text)
        assertEquals("Line two", result.texts[1].text)
    }

    // -- Matching lines, matching word counts (happy path) --

    @Test
    fun `merges single line with matching word count`() {
        val elements = listOf(makeElement("Helo", 0, 0, 50, 30), makeElement("wrld", 60, 0, 120, 30))
        val line = makeLine("Helo wrld", elements)
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("Hello world"))

        assertEquals(1, result.texts.size)
        assertEquals("Hello world", result.texts[0].text)
        assertEquals(2, result.texts[0].elements.size)
        assertEquals("Hello", result.texts[0].elements[0].text)
        assertEquals("world", result.texts[0].elements[1].text)
    }

    @Test
    fun `preserves bounding boxes from mlKit after merge`() {
        val bounds1 = Rect(10, 20, 80, 50)
        val bounds2 = Rect(90, 20, 160, 50)
        val elements = listOf(
            TextElement("Helo", bounds1),
            TextElement("wrld", bounds2)
        )
        val line = makeLine("Helo wrld", elements)
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("Hello world"))

        assertEquals(bounds1, result.texts[0].elements[0].bounds)
        assertEquals(bounds2, result.texts[0].elements[1].bounds)
    }

    @Test
    fun `merges multiple lines with matching word counts`() {
        val line1 = makeLine("Helo wrld", listOf(makeElement("Helo"), makeElement("wrld")))
        val line2 = makeLine("Gd morning", listOf(makeElement("Gd"), makeElement("morning")))
        val mlKit = makeResult(listOf(line1, line2))

        val result = OcrTextMerger.merge(mlKit, listOf("Hello world", "Good morning"))

        assertEquals(2, result.texts.size)
        assertEquals("Hello world", result.texts[0].text)
        assertEquals("Hello", result.texts[0].elements[0].text)
        assertEquals("world", result.texts[0].elements[1].text)
        assertEquals("Good morning", result.texts[1].text)
        assertEquals("Good", result.texts[1].elements[0].text)
        assertEquals("morning", result.texts[1].elements[1].text)
    }

    @Test
    fun `merges single-word lines`() {
        val line = makeLine("Helo", listOf(makeElement("Helo")))
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("Hello"))

        assertEquals("Hello", result.texts[0].text)
        assertEquals("Hello", result.texts[0].elements[0].text)
    }

    // -- Matching lines, mismatched word counts --

    @Test
    fun `keeps mlKit words when gemini has more words per line`() {
        val elements = listOf(makeElement("Hello"), makeElement("world"))
        val line = makeLine("Hello world", elements)
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("Hello beautiful world"))

        assertEquals("Hello beautiful world", result.texts[0].text)
        assertEquals(2, result.texts[0].elements.size)
        assertEquals("Hello", result.texts[0].elements[0].text)
        assertEquals("world", result.texts[0].elements[1].text)
    }

    @Test
    fun `keeps mlKit words when gemini has fewer words per line`() {
        val elements = listOf(makeElement("Hello"), makeElement("beautiful"), makeElement("world"))
        val line = makeLine("Hello beautiful world", elements)
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("Hello world"))

        assertEquals("Hello world", result.texts[0].text)
        assertEquals(3, result.texts[0].elements.size)
        assertEquals("Hello", result.texts[0].elements[0].text)
        assertEquals("beautiful", result.texts[0].elements[1].text)
        assertEquals("world", result.texts[0].elements[2].text)
    }

    // -- isWord flag --

    @Test
    fun `sets isWord true for alphabetic gemini words`() {
        val elements = listOf(makeElement("wrds"))
        val line = makeLine("wrds", elements)
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("words"))
        assertTrue(result.texts[0].elements[0].isWord)
    }

    @Test
    fun `sets isWord false for pure punctuation`() {
        val elements = listOf(makeElement("Hello"), makeElement("..."))
        val line = makeLine("Hello ...", elements)
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("Hello ..."))

        assertTrue(result.texts[0].elements[0].isWord)
        assertEquals(false, result.texts[0].elements[1].isWord)
    }

    @Test
    fun `sets isWord true for words with embedded punctuation`() {
        val elements = listOf(makeElement("dont"))
        val line = makeLine("dont", elements)
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("don't"))
        assertTrue(result.texts[0].elements[0].isWord)
    }

    // -- Preserves OCRResult metadata --

    @Test
    fun `preserves timestamp and imageSize after merge`() {
        val line = makeLine("Helo", listOf(makeElement("Helo")))
        val mlKit = OCRResult(
            texts = listOf(line),
            timestamp = 1234567890L,
            imageSize = Size(640, 480),
            processingTimeMs = 42L
        )

        val result = OcrTextMerger.merge(mlKit, listOf("Hello"))

        assertEquals(1234567890L, result.timestamp)
        assertEquals(Size(640, 480), result.imageSize)
        assertEquals(42L, result.processingTimeMs)
    }

    @Test
    fun `preserves line-level bounds and confidence after merge`() {
        val lineBounds = Rect(5, 10, 500, 60)
        val elements = listOf(makeElement("Helo"))
        val line = DetectedText(text = "Helo", bounds = lineBounds, confidence = 0.87f, elements = elements)
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("Hello"))

        assertEquals(lineBounds, result.texts[0].bounds)
        assertEquals(0.87f, result.texts[0].confidence, 0.001f)
    }

    // -- Mixed scenarios --

    @Test
    fun `handles line where some match and some do not in word count`() {
        val line1 = makeLine("Helo wrld", listOf(makeElement("Helo"), makeElement("wrld")))
        val line2 = makeLine("Foo bar baz", listOf(makeElement("Foo"), makeElement("bar"), makeElement("baz")))
        val mlKit = makeResult(listOf(line1, line2))

        val result = OcrTextMerger.merge(mlKit, listOf("Hello world", "Foobar baz"))

        // Line 1: 2 words match → corrected
        assertEquals("Hello world", result.texts[0].text)
        assertEquals("Hello", result.texts[0].elements[0].text)
        assertEquals("world", result.texts[0].elements[1].text)

        // Line 2: 3 vs 2 words mismatch → keeps mlKit element text
        assertEquals("Foobar baz", result.texts[1].text)
        assertEquals("Foo", result.texts[1].elements[0].text)
        assertEquals("bar", result.texts[1].elements[1].text)
        assertEquals("baz", result.texts[1].elements[2].text)
    }

    @Test
    fun `handles gemini line with extra whitespace`() {
        val elements = listOf(makeElement("Hello"), makeElement("world"))
        val line = makeLine("Hello world", elements)
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("Hello   world"))

        assertEquals("Hello   world", result.texts[0].text)
        assertEquals("Hello", result.texts[0].elements[0].text)
        assertEquals("world", result.texts[0].elements[1].text)
    }

    @Test
    fun `handles line with empty mlKit elements list`() {
        val line = makeLine("", emptyList())
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("Hello"))

        assertEquals("Hello", result.texts[0].text)
        assertEquals(0, result.texts[0].elements.size)
    }

    @Test
    fun `handles gemini line that is all whitespace`() {
        val elements = listOf(makeElement("Hello"))
        val line = makeLine("Hello", elements)
        val mlKit = makeResult(listOf(line))

        val result = OcrTextMerger.merge(mlKit, listOf("   "))

        assertEquals("   ", result.texts[0].text)
        assertEquals("Hello", result.texts[0].elements[0].text)
    }

    // -- Large input --

    @Test
    fun `handles many lines with matching structure`() {
        val lines = (1..10).map { i ->
            makeLine("word$i", listOf(makeElement("word$i")))
        }
        val mlKit = makeResult(lines)
        val geminiLines = (1..10).map { "corrected$it" }

        val result = OcrTextMerger.merge(mlKit, geminiLines)

        assertEquals(10, result.texts.size)
        result.texts.forEachIndexed { i, text ->
            assertEquals("corrected${i + 1}", text.text)
            assertEquals("corrected${i + 1}", text.elements[0].text)
        }
    }
}
