package com.jworks.eigosage.data.ai

import android.util.Log
import com.jworks.eigosage.domain.ai.AiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

class GeminiChatClient(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val model: String = DEFAULT_MODEL
) {
    companion object {
        private const val TAG = "GeminiChatClient"
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"
        private const val DEFAULT_MODEL = "gemini-2.5-flash"

        private const val SUGGESTION_INSTRUCTION = " After your response, add exactly 3 short follow-up suggestions the user might want to ask next. Format them on a single line at the very end: [SUGGESTIONS: \"suggestion 1\" | \"suggestion 2\" | \"suggestion 3\"]. Keep each suggestion under 6 words. Match suggestions to the user's level."

        private val DEFAULT_SYSTEM_PROMPT = "You are an English language tutor helping a user understand text they captured with EigoSage (an English reading assistant app). Be concise, helpful, and friendly. Use simple English when possible. If asked to translate, provide the translation along with brief notes on nuance. Format responses with markdown bold for key terms and bullet points for lists." + SUGGESTION_INSTRUCTION

        fun buildCefrSystemPrompt(
            cefrLevel: String,
            persona: ChatPersona = ChatPersona.DEFAULT,
            scanMode: ScanMode = ScanMode.DEFAULT
        ): String {
            val levelGuidance = when (cefrLevel) {
                "A1" -> "Use only basic, everyday words (under 500 most common). Keep sentences very short (5-8 words). Avoid idioms, phrasal verbs, and complex grammar. Define any word above elementary level."
                "A2" -> "Use simple vocabulary and short sentences. Explain idioms and phrasal verbs when they appear. Avoid complex clause structures. Keep explanations under 3 sentences each."
                "B1" -> "Use clear, straightforward language. You may use common idioms but explain less common ones. Keep grammar explanations practical with examples. Moderate detail in responses."
                "B2" -> "Use natural English at an upper-intermediate level. Explain nuanced vocabulary and advanced grammar points. Include collocations and register notes where relevant."
                "C1" -> "Use sophisticated, natural English. Discuss subtle distinctions in meaning, register, and style. Include advanced vocabulary notes, etymology when interesting, and academic/professional usage."
                "C2" -> "Use full native-level English. Discuss fine nuances, literary devices, rhetorical effects, and stylistic choices. Assume near-native comprehension."
                else -> "Adapt your language to an intermediate English learner."
            }

            val personaRole = when (persona) {
                ChatPersona.SAGE -> "You are **Sage**, a reading comprehension guide. Focus on main ideas, context clues, text structure, and reading strategies. Help the user understand what the text means as a whole — summarize, explain relationships between ideas, and clarify implied meaning. When the user asks about specific words, relate them back to the broader text."
                ChatPersona.LEXICON -> "You are **Lexicon**, a vocabulary specialist. Focus on word meanings, synonyms, antonyms, collocations, etymology, and usage examples. When explaining a word, provide: (1) a clear definition, (2) example sentences, (3) related words or word family members. Bold all key vocabulary terms. Help the user build a rich mental word map."
                ChatPersona.TUTOR -> "You are **Tutor**, an English practice coach. Focus on grammar rules, sentence structure, translation exercises, and active practice. After explaining something, give the user a quick exercise or question to check understanding. Correct errors gently with the right form and a brief rule. Encourage the user to try rephrasing or translating."
            }

            val modeOverlay = buildScanModeOverlay(scanMode)

            return """$personaRole The user captured text with EigoSage (an English reading assistant app). The user's English level is CEFR $cefrLevel. $levelGuidance$modeOverlay Be concise and friendly. If asked to translate, provide the translation along with brief notes on nuance. Format responses with markdown bold for key terms and bullet points for lists.$SUGGESTION_INSTRUCTION"""
        }

        fun buildScanModeAnalysisPrompt(scanMode: ScanMode): String? {
            if (scanMode == ScanMode.STANDARD) return null
            return when (scanMode) {
                ScanMode.INTERPRETER -> "You are an expert interpreter and cross-cultural communication specialist analyzing English text. For this text:\n1. Identify key terms that may be difficult to interpret or translate — highlight nuances, false friends, and culturally loaded words\n2. Flag idiomatic expressions, phrasal verbs, and figurative language with literal vs. intended meaning\n3. Note cultural references or context that a non-native speaker might miss\n4. Suggest natural translations or paraphrases for ambiguous passages\nFormat: Bold key terms. Use bullet points. Keep explanations practical for real-time interpretation."
                ScanMode.MEDICAL -> "You are a medical English specialist analyzing clinical or health-related text. For this text:\n1. Identify medical terminology — drug names, anatomical terms, diagnostic/procedural terms, abbreviations\n2. Provide clear plain-English definitions for each medical term\n3. Flag safety-critical terms (dosage instructions, contraindications, warnings) with ⚠️\n4. Note Latin/Greek roots where they help understanding\nFormat: Bold all medical terms. Group by category (diagnosis, treatment, anatomy). Be precise — medical context demands accuracy."
                ScanMode.LEGAL -> "You are a legal English expert analyzing legal or regulatory text. For this text:\n1. Identify legal terms of art and provide precise definitions\n2. Extract key clauses — obligations (\"shall\", \"must\"), permissions (\"may\"), conditions (\"provided that\", \"subject to\")\n3. Flag rights, liabilities, and deadlines\n4. Simplify complex sentence structures while preserving legal meaning\nFormat: Bold all legal terms. Use bullet points for obligations vs. rights. Note any ambiguous language that could be interpreted multiple ways."
                ScanMode.STANDARD -> null
            }
        }

        private fun buildScanModeOverlay(scanMode: ScanMode): String {
            if (scanMode == ScanMode.STANDARD) return ""
            return when (scanMode) {
                ScanMode.INTERPRETER -> " The user is in **Interpreter mode** — prioritize translation aids, cultural context, idiomatic expressions, and cross-cultural communication. When explaining vocabulary, include interpretation-relevant nuances and potential translation pitfalls."
                ScanMode.MEDICAL -> " The user is in **Medical mode** — prioritize medical terminology, clinical vocabulary, drug names, and anatomical terms. Flag safety-critical language with ⚠️. Be precise and accurate with medical definitions."
                ScanMode.LEGAL -> " The user is in **Legal mode** — prioritize legal terms of art, clause analysis, obligations vs. permissions, and contractual language. Clarify ambiguous legal phrasing and explain rights/liabilities."
                ScanMode.STANDARD -> ""
            }
        }

        private val SUGGESTION_REGEX = Regex("""\[SUGGESTIONS:\s*"([^"]+)"\s*\|\s*"([^"]+)"\s*\|\s*"([^"]+)"\s*]""")

        /**
         * Parses [SUGGESTIONS: "a" | "b" | "c"] from the end of a response.
         * Returns (cleaned content, suggestions list).
         */
        fun parseSuggestions(content: String): Pair<String, List<String>> {
            val match = SUGGESTION_REGEX.find(content)
                ?: return content.trimEnd() to emptyList()
            val suggestions = listOf(
                match.groupValues[1].trim(),
                match.groupValues[2].trim(),
                match.groupValues[3].trim()
            ).filter { it.isNotBlank() }
            val cleanedContent = content.substring(0, match.range.first).trimEnd()
            return cleanedContent to suggestions
        }
    }

    val isAvailable: Boolean get() = apiKey.isNotBlank()

    suspend fun send(
        messages: List<Pair<String, String>>, // (role, content) — "user" or "model"
        systemPrompt: String = DEFAULT_SYSTEM_PROMPT
    ): Result<AiResponse> {
        if (!isAvailable) return Result.failure(IllegalStateException("Gemini API key not configured"))

        val startTime = System.currentTimeMillis()
        val url = "$BASE_URL/$model:generateContent?key=$apiKey"

        val requestBody = buildJsonObject {
            putJsonObject("systemInstruction") {
                putJsonArray("parts") {
                    add(buildJsonObject { put("text", systemPrompt) })
                }
            }
            putJsonArray("contents") {
                for ((role, content) in messages) {
                    add(buildJsonObject {
                        put("role", role)
                        putJsonArray("parts") {
                            add(buildJsonObject { put("text", content) })
                        }
                    })
                }
            }
            putJsonObject("generationConfig") {
                put("maxOutputTokens", 1024)
                put("temperature", 0.5)
            }
        }.toString()

        return try {
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            val responseText = response.bodyAsText()
            val elapsed = System.currentTimeMillis() - startTime

            if (response.status.value != 200) {
                Log.e(TAG, "API error ${response.status.value}: $responseText")
                return Result.failure(RuntimeException("Gemini API error: ${response.status.value}"))
            }

            val json = Json.parseToJsonElement(responseText).jsonObject
            val content = json["candidates"]?.jsonArray?.firstOrNull()
                ?.jsonObject?.get("content")?.jsonObject
                ?.get("parts")?.jsonArray?.firstOrNull()
                ?.jsonObject?.get("text")?.jsonPrimitive?.content
                ?: return Result.failure(RuntimeException("Empty response from Gemini"))

            val usageMetadata = json["usageMetadata"]?.jsonObject
            val inputTokens = usageMetadata?.get("promptTokenCount")?.jsonPrimitive?.content?.toIntOrNull()
            val outputTokens = usageMetadata?.get("candidatesTokenCount")?.jsonPrimitive?.content?.toIntOrNull()
            val tokensUsed = usageMetadata?.get("totalTokenCount")?.jsonPrimitive?.content?.toIntOrNull()

            Log.d(TAG, "Chat response in ${elapsed}ms, tokens: $tokensUsed (in=$inputTokens, out=$outputTokens)")

            Result.success(
                AiResponse(
                    content = content,
                    provider = "Gemini",
                    model = model,
                    tokensUsed = tokensUsed,
                    inputTokens = inputTokens,
                    outputTokens = outputTokens,
                    processingTimeMs = elapsed
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Chat request failed", e)
            Result.failure(e)
        }
    }
}
