package com.jworks.eigosage.domain.export

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

data class ChatExportData(
    val sessionTitle: String,
    val cefrLevel: String?,
    val messages: List<ChatExportMessage>,
    val createdAt: Long
)

data class ChatExportMessage(
    val role: String, // "user", "model", "system"
    val content: String,
    val timestamp: Long
)

@Singleton
class ChatExporter @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PAGE_WIDTH = 595
        private const val PAGE_HEIGHT = 842
        private const val MARGIN = 50f
        private const val LINE_SPACING = 1.4f
    }

    private val titlePaint = Paint().apply {
        color = Color.BLACK
        textSize = 24f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
    }

    private val subtitlePaint = Paint().apply {
        color = Color.DKGRAY
        textSize = 12f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        isAntiAlias = true
    }

    private val userLabelPaint = Paint().apply {
        color = Color.rgb(33, 150, 243) // Blue
        textSize = 11f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
    }

    private val modelLabelPaint = Paint().apply {
        color = Color.rgb(76, 175, 80) // Green
        textSize = 11f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
    }

    private val bodyPaint = Paint().apply {
        color = Color.DKGRAY
        textSize = 11f
        typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
        isAntiAlias = true
    }

    private val footerPaint = Paint().apply {
        color = Color.GRAY
        textSize = 9f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        isAntiAlias = true
    }

    /**
     * Format chat as plain text for sharing.
     */
    fun formatAsText(data: ChatExportData): String {
        val sb = StringBuilder()
        sb.appendLine("EigoSage Chat Export")
        sb.appendLine("====================")
        sb.appendLine()
        sb.appendLine("Topic: ${data.sessionTitle}")
        if (data.cefrLevel != null) {
            sb.appendLine("CEFR Level: ${data.cefrLevel}")
        }
        val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date(data.createdAt))
        sb.appendLine("Date: $dateStr")
        sb.appendLine()
        sb.appendLine("--------------------")
        sb.appendLine()

        for (msg in data.messages) {
            if (msg.role == "system") continue // skip seed messages
            val label = if (msg.role == "user") "You" else "EigoSage"
            val content = msg.content.replace("**", "") // strip markdown bold
            sb.appendLine("[$label]")
            sb.appendLine(content)
            sb.appendLine()
        }

        sb.appendLine("--------------------")
        sb.appendLine("Exported from EigoSage")
        return sb.toString()
    }

    /**
     * Export chat as PDF file.
     */
    fun exportAsPdf(data: ChatExportData): Result<File> {
        return try {
            val outputDir = File(
                context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS),
                "EigoSage"
            )
            outputDir.mkdirs()
            val fileName = "chat_${System.currentTimeMillis()}.pdf"
            val outputFile = File(outputDir, fileName)

            val pdf = PdfDocument()
            renderChatPages(pdf, data)
            FileOutputStream(outputFile).use { pdf.writeTo(it) }
            pdf.close()

            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun renderChatPages(pdf: PdfDocument, data: ChatExportData) {
        var pageNum = 1
        var y = MARGIN

        var currentPage: PdfDocument.Page? = null
        var canvas: Canvas? = null

        fun startNewPage(): Canvas {
            currentPage?.let {
                drawFooter(it.canvas, pageNum - 1)
                pdf.finishPage(it)
            }
            val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNum++).create()
            val p = pdf.startPage(pageInfo)
            currentPage = p
            y = MARGIN
            return p.canvas
        }

        fun ensureSpace(needed: Float): Canvas {
            if (canvas == null || y + needed > PAGE_HEIGHT - MARGIN - 20f) {
                val newCanvas = startNewPage()
                canvas = newCanvas
                return newCanvas
            }
            return canvas ?: startNewPage().also { canvas = it }
        }

        // Header
        var c = ensureSpace(80f)
        c.drawText("EigoSage Chat", MARGIN, y + titlePaint.textSize, titlePaint)
        y += titlePaint.textSize + 8f

        val dateStr = SimpleDateFormat("MMMM d, yyyy 'at' h:mm a", Locale.US).format(Date(data.createdAt))
        c.drawText(dateStr, MARGIN, y + subtitlePaint.textSize, subtitlePaint)
        y += subtitlePaint.textSize + 4f

        val topicText = "Topic: ${data.sessionTitle.take(80)}"
        c.drawText(topicText, MARGIN, y + subtitlePaint.textSize, subtitlePaint)
        y += subtitlePaint.textSize + 4f

        if (data.cefrLevel != null) {
            c.drawText("CEFR Level: ${data.cefrLevel}", MARGIN, y + subtitlePaint.textSize, subtitlePaint)
            y += subtitlePaint.textSize + 4f
        }

        y += 16f

        // Divider line
        val dividerPaint = Paint().apply { color = Color.LTGRAY; strokeWidth = 1f }
        c.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, dividerPaint)
        y += 12f

        // Messages
        val maxWidth = PAGE_WIDTH - 2 * MARGIN
        for (msg in data.messages) {
            if (msg.role == "system") continue

            val labelPaint = if (msg.role == "user") userLabelPaint else modelLabelPaint
            val label = if (msg.role == "user") "You" else "EigoSage"

            // Label line
            c = ensureSpace(30f)
            c.drawText(label, MARGIN, y + labelPaint.textSize, labelPaint)
            y += labelPaint.textSize + 4f

            // Message body — strip markdown bold markers
            val content = msg.content.replace("**", "")
            val lines = wrapText(content, bodyPaint, maxWidth)
            val lineHeight = bodyPaint.textSize * LINE_SPACING

            for (line in lines) {
                c = ensureSpace(lineHeight + 2f)
                c.drawText(line, MARGIN + 8f, y + bodyPaint.textSize, bodyPaint)
                y += lineHeight
            }

            y += 10f // gap between messages
        }

        // Finish last page
        currentPage?.let {
            drawFooter(it.canvas, pageNum - 1)
            pdf.finishPage(it)
        }
    }

    private fun drawFooter(canvas: Canvas, pageNum: Int) {
        val footerY = PAGE_HEIGHT - 20f
        canvas.drawText("Exported from EigoSage", MARGIN, footerY, footerPaint)
        val pageText = "Page $pageNum"
        val pageWidth = footerPaint.measureText(pageText)
        canvas.drawText(pageText, PAGE_WIDTH - MARGIN - pageWidth, footerY, footerPaint)
    }

    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val lines = mutableListOf<String>()
        val paragraphs = text.split("\n")
        for (paragraph in paragraphs) {
            if (paragraph.isBlank()) {
                lines.add("")
                continue
            }
            val words = paragraph.split(Regex("\\s+"))
            val currentLine = StringBuilder()
            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
                if (paint.measureText(testLine) <= maxWidth) {
                    currentLine.clear().append(testLine)
                } else {
                    if (currentLine.isNotEmpty()) lines.add(currentLine.toString())
                    currentLine.clear().append(word)
                }
            }
            if (currentLine.isNotEmpty()) lines.add(currentLine.toString())
        }
        return lines
    }
}
