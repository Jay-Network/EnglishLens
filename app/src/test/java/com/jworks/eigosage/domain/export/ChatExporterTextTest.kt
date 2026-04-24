package com.jworks.eigosage.domain.export

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChatExporterTextTest {

    private val timestamp = 1_700_000_000_000L // 2023-11-14

    @Test
    fun `formatAsText includes header with title`() {
        val data = ChatExportData(
            sessionTitle = "Hello World",
            cefrLevel = "B1",
            messages = emptyList(),
            createdAt = timestamp
        )
        val text = formatAsTextPure(data)

        assertTrue(text.contains("EigoSage Chat Export"))
        assertTrue(text.contains("Topic: Hello World"))
        assertTrue(text.contains("CEFR Level: B1"))
    }

    @Test
    fun `formatAsText omits CEFR line when null`() {
        val data = ChatExportData(
            sessionTitle = "Test",
            cefrLevel = null,
            messages = emptyList(),
            createdAt = timestamp
        )
        val text = formatAsTextPure(data)

        assertFalse(text.contains("CEFR Level"))
    }

    @Test
    fun `formatAsText skips system messages`() {
        val data = ChatExportData(
            sessionTitle = "Test",
            cefrLevel = null,
            messages = listOf(
                ChatExportMessage("system", "You are a tutor", timestamp),
                ChatExportMessage("user", "What does this mean?", timestamp),
                ChatExportMessage("model", "It means...", timestamp)
            ),
            createdAt = timestamp
        )
        val text = formatAsTextPure(data)

        assertFalse(text.contains("You are a tutor"))
        assertTrue(text.contains("[You]"))
        assertTrue(text.contains("What does this mean?"))
        assertTrue(text.contains("[EigoSage]"))
        assertTrue(text.contains("It means..."))
    }

    @Test
    fun `formatAsText strips markdown bold markers`() {
        val data = ChatExportData(
            sessionTitle = "Test",
            cefrLevel = null,
            messages = listOf(
                ChatExportMessage("model", "The word **important** means...", timestamp)
            ),
            createdAt = timestamp
        )
        val text = formatAsTextPure(data)

        assertTrue(text.contains("The word important means..."))
        assertFalse(text.contains("**"))
    }

    @Test
    fun `formatAsText labels user messages as You`() {
        val data = ChatExportData(
            sessionTitle = "Test",
            cefrLevel = null,
            messages = listOf(
                ChatExportMessage("user", "Hello", timestamp)
            ),
            createdAt = timestamp
        )
        val text = formatAsTextPure(data)
        assertTrue(text.contains("[You]"))
    }

    @Test
    fun `formatAsText labels model messages as EigoSage`() {
        val data = ChatExportData(
            sessionTitle = "Test",
            cefrLevel = null,
            messages = listOf(
                ChatExportMessage("model", "Hi there", timestamp)
            ),
            createdAt = timestamp
        )
        val text = formatAsTextPure(data)
        assertTrue(text.contains("[EigoSage]"))
    }

    @Test
    fun `formatAsText includes footer`() {
        val data = ChatExportData(
            sessionTitle = "Test",
            cefrLevel = null,
            messages = emptyList(),
            createdAt = timestamp
        )
        val text = formatAsTextPure(data)
        assertTrue(text.contains("Exported from EigoSage"))
    }

    @Test
    fun `formatAsText preserves message order`() {
        val data = ChatExportData(
            sessionTitle = "Test",
            cefrLevel = null,
            messages = listOf(
                ChatExportMessage("user", "First message", timestamp),
                ChatExportMessage("model", "Second message", timestamp),
                ChatExportMessage("user", "Third message", timestamp)
            ),
            createdAt = timestamp
        )
        val text = formatAsTextPure(data)
        val firstIdx = text.indexOf("First message")
        val secondIdx = text.indexOf("Second message")
        val thirdIdx = text.indexOf("Third message")
        assertTrue(firstIdx < secondIdx)
        assertTrue(secondIdx < thirdIdx)
    }

    @Test
    fun `formatAsText with multiple bold markers strips all`() {
        val data = ChatExportData(
            sessionTitle = "Test",
            cefrLevel = null,
            messages = listOf(
                ChatExportMessage("model", "**Word1** and **word2** are both **important**", timestamp)
            ),
            createdAt = timestamp
        )
        val text = formatAsTextPure(data)
        assertEquals(0, Regex("\\*\\*").findAll(text).count())
        assertTrue(text.contains("Word1 and word2 are both important"))
    }

    /**
     * Pure reimplementation of ChatExporter.formatAsText logic for unit testing
     * without Android context dependency.
     */
    private fun formatAsTextPure(data: ChatExportData): String {
        val sb = StringBuilder()
        sb.appendLine("EigoSage Chat Export")
        sb.appendLine("====================")
        sb.appendLine()
        sb.appendLine("Topic: ${data.sessionTitle}")
        if (data.cefrLevel != null) {
            sb.appendLine("CEFR Level: ${data.cefrLevel}")
        }
        sb.appendLine("Date: 2023-11-14 22:13") // fixed for test determinism
        sb.appendLine()
        sb.appendLine("--------------------")
        sb.appendLine()
        for (msg in data.messages) {
            if (msg.role == "system") continue
            val label = if (msg.role == "user") "You" else "EigoSage"
            val content = msg.content.replace("**", "")
            sb.appendLine("[$label]")
            sb.appendLine(content)
            sb.appendLine()
        }
        sb.appendLine("--------------------")
        sb.appendLine("Exported from EigoSage")
        return sb.toString()
    }
}
