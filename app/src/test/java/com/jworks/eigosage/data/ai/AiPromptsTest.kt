package com.jworks.eigosage.data.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiPromptsTest {

    @Test
    fun `systemPromptForMode STANDARD returns default system prompt`() {
        val prompt = AiPrompts.systemPromptForMode(ScanMode.STANDARD)
        assertEquals(AiPrompts.SYSTEM_PROMPT, prompt)
    }

    @Test
    fun `systemPromptForMode INTERPRETER returns interpreter prompt`() {
        val prompt = AiPrompts.systemPromptForMode(ScanMode.INTERPRETER)
        assertTrue(prompt.contains("interpreter"))
        assertTrue(prompt.contains("cross-cultural"))
        assertTrue(prompt.contains("Format responses with markdown"))
        assertTrue(prompt != AiPrompts.SYSTEM_PROMPT)
    }

    @Test
    fun `systemPromptForMode MEDICAL returns medical prompt`() {
        val prompt = AiPrompts.systemPromptForMode(ScanMode.MEDICAL)
        assertTrue(prompt.contains("medical"))
        assertTrue(prompt.contains("drug names"))
        assertTrue(prompt.contains("Format responses with markdown"))
    }

    @Test
    fun `systemPromptForMode LEGAL returns legal prompt`() {
        val prompt = AiPrompts.systemPromptForMode(ScanMode.LEGAL)
        assertTrue(prompt.contains("legal"))
        assertTrue(prompt.contains("obligations"))
        assertTrue(prompt.contains("Format responses with markdown"))
    }

    @Test
    fun `systemPromptForMode all modes include markdown instruction`() {
        ScanMode.entries.forEach { mode ->
            val prompt = AiPrompts.systemPromptForMode(mode)
            assertTrue("Mode $mode should include markdown instruction", prompt.contains("markdown"))
        }
    }

    @Test
    fun `systemPromptForMode professional modes are distinct from each other`() {
        val interpreter = AiPrompts.systemPromptForMode(ScanMode.INTERPRETER)
        val medical = AiPrompts.systemPromptForMode(ScanMode.MEDICAL)
        val legal = AiPrompts.systemPromptForMode(ScanMode.LEGAL)
        assertTrue(interpreter != medical)
        assertTrue(medical != legal)
        assertTrue(interpreter != legal)
    }
}
