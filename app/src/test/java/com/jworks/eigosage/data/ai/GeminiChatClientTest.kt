package com.jworks.eigosage.data.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GeminiChatClientTest {

    // -- parseSuggestions --

    @Test
    fun `parseSuggestions extracts three suggestions`() {
        val input = "Here is the answer.\n\n[SUGGESTIONS: \"What does X mean\" | \"Give examples\" | \"Translate it\"]"
        val (content, suggestions) = GeminiChatClient.parseSuggestions(input)
        assertEquals("Here is the answer.", content)
        assertEquals(3, suggestions.size)
        assertEquals("What does X mean", suggestions[0])
        assertEquals("Give examples", suggestions[1])
        assertEquals("Translate it", suggestions[2])
    }

    @Test
    fun `parseSuggestions returns empty list when no tag present`() {
        val input = "Just a plain response with no suggestions."
        val (content, suggestions) = GeminiChatClient.parseSuggestions(input)
        assertEquals("Just a plain response with no suggestions.", content)
        assertTrue(suggestions.isEmpty())
    }

    @Test
    fun `parseSuggestions trims whitespace around content`() {
        val input = "Answer text   \n\n  [SUGGESTIONS: \"a\" | \"b\" | \"c\"]"
        val (content, _) = GeminiChatClient.parseSuggestions(input)
        assertEquals("Answer text", content)
    }

    @Test
    fun `parseSuggestions handles suggestions with special characters`() {
        val input = "OK\n[SUGGESTIONS: \"What's this?\" | \"How about that\" | \"Tell me more\"]"
        val (_, suggestions) = GeminiChatClient.parseSuggestions(input)
        assertEquals("What's this?", suggestions[0])
    }

    @Test
    fun `parseSuggestions handles empty string`() {
        val (content, suggestions) = GeminiChatClient.parseSuggestions("")
        assertEquals("", content)
        assertTrue(suggestions.isEmpty())
    }

    // -- buildScanModeAnalysisPrompt --

    @Test
    fun `buildScanModeAnalysisPrompt returns null for STANDARD`() {
        assertNull(GeminiChatClient.buildScanModeAnalysisPrompt(ScanMode.STANDARD))
    }

    @Test
    fun `buildScanModeAnalysisPrompt returns interpreter prompt`() {
        val prompt = GeminiChatClient.buildScanModeAnalysisPrompt(ScanMode.INTERPRETER)!!
        assertTrue(prompt.contains("interpreter"))
        assertTrue(prompt.contains("false friends"))
        assertTrue(prompt.contains("idiomatic"))
    }

    @Test
    fun `buildScanModeAnalysisPrompt returns medical prompt`() {
        val prompt = GeminiChatClient.buildScanModeAnalysisPrompt(ScanMode.MEDICAL)!!
        assertTrue(prompt.contains("medical"))
        assertTrue(prompt.contains("drug names"))
        assertTrue(prompt.contains("⚠️"))
    }

    @Test
    fun `buildScanModeAnalysisPrompt returns legal prompt`() {
        val prompt = GeminiChatClient.buildScanModeAnalysisPrompt(ScanMode.LEGAL)!!
        assertTrue(prompt.contains("legal"))
        assertTrue(prompt.contains("obligations"))
        assertTrue(prompt.contains("shall"))
    }

    // -- buildCefrSystemPrompt --

    @Test
    fun `buildCefrSystemPrompt includes persona name`() {
        val prompt = GeminiChatClient.buildCefrSystemPrompt("B2", ChatPersona.SAGE)
        assertTrue(prompt.contains("**Sage**"))
    }

    @Test
    fun `buildCefrSystemPrompt includes CEFR level`() {
        val prompt = GeminiChatClient.buildCefrSystemPrompt("A1", ChatPersona.TUTOR)
        assertTrue(prompt.contains("CEFR A1"))
        assertTrue(prompt.contains("basic, everyday words"))
    }

    @Test
    fun `buildCefrSystemPrompt includes suggestion instruction`() {
        val prompt = GeminiChatClient.buildCefrSystemPrompt("B2")
        assertTrue(prompt.contains("[SUGGESTIONS:"))
    }

    @Test
    fun `buildCefrSystemPrompt with standard mode has no mode overlay`() {
        val prompt = GeminiChatClient.buildCefrSystemPrompt("B2", scanMode = ScanMode.STANDARD)
        assertTrue(!prompt.contains("Interpreter mode"))
        assertTrue(!prompt.contains("Medical mode"))
        assertTrue(!prompt.contains("Legal mode"))
    }

    @Test
    fun `buildCefrSystemPrompt with interpreter mode includes overlay`() {
        val prompt = GeminiChatClient.buildCefrSystemPrompt("B2", scanMode = ScanMode.INTERPRETER)
        assertTrue(prompt.contains("**Interpreter mode**"))
        assertTrue(prompt.contains("translation aids"))
    }

    @Test
    fun `buildCefrSystemPrompt with medical mode includes overlay`() {
        val prompt = GeminiChatClient.buildCefrSystemPrompt("B2", scanMode = ScanMode.MEDICAL)
        assertTrue(prompt.contains("**Medical mode**"))
        assertTrue(prompt.contains("clinical vocabulary"))
    }

    @Test
    fun `buildCefrSystemPrompt with legal mode includes overlay`() {
        val prompt = GeminiChatClient.buildCefrSystemPrompt("B2", scanMode = ScanMode.LEGAL)
        assertTrue(prompt.contains("**Legal mode**"))
        assertTrue(prompt.contains("legal terms of art"))
    }

    @Test
    fun `buildCefrSystemPrompt unknown CEFR level uses default guidance`() {
        val prompt = GeminiChatClient.buildCefrSystemPrompt("X9")
        assertTrue(prompt.contains("intermediate English learner"))
    }

    @Test
    fun `buildCefrSystemPrompt each persona produces distinct prompt`() {
        val sage = GeminiChatClient.buildCefrSystemPrompt("B2", ChatPersona.SAGE)
        val lexicon = GeminiChatClient.buildCefrSystemPrompt("B2", ChatPersona.LEXICON)
        val tutor = GeminiChatClient.buildCefrSystemPrompt("B2", ChatPersona.TUTOR)
        assertTrue(sage != lexicon)
        assertTrue(lexicon != tutor)
        assertTrue(sage.contains("reading comprehension"))
        assertTrue(lexicon.contains("vocabulary specialist"))
        assertTrue(tutor.contains("practice coach"))
    }

    @Test
    fun `buildCefrSystemPrompt C2 level uses native guidance`() {
        val prompt = GeminiChatClient.buildCefrSystemPrompt("C2")
        assertTrue(prompt.contains("native-level English"))
        assertTrue(prompt.contains("literary devices"))
    }
}
