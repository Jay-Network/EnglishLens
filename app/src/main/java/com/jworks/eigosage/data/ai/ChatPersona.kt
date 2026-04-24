package com.jworks.eigosage.data.ai

/**
 * Chat personas for GeminiChatClient. Each persona has a distinct teaching focus
 * and system prompt style that adapts to the user's CEFR level.
 */
enum class ChatPersona(
    val displayName: String,
    val shortDescription: String
) {
    SAGE(
        displayName = "Sage",
        shortDescription = "Comprehension & context"
    ),
    LEXICON(
        displayName = "Lexicon",
        shortDescription = "Vocabulary & definitions"
    ),
    TUTOR(
        displayName = "Tutor",
        shortDescription = "Grammar & practice"
    );

    companion object {
        val DEFAULT = SAGE
    }
}
