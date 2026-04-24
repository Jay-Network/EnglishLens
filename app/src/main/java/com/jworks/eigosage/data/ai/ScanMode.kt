package com.jworks.eigosage.data.ai

enum class ScanMode(
    val displayName: String,
    val shortLabel: String,
    val description: String
) {
    STANDARD(
        displayName = "Standard",
        shortLabel = "STD",
        description = "General English reading assistance"
    ),
    INTERPRETER(
        displayName = "Interpreter",
        shortLabel = "INT",
        description = "Translation aids and cultural context"
    ),
    MEDICAL(
        displayName = "Medical",
        shortLabel = "MED",
        description = "Clinical terms and medical vocabulary"
    ),
    LEGAL(
        displayName = "Legal",
        shortLabel = "LAW",
        description = "Legal terms, clauses, and obligations"
    );

    companion object {
        val DEFAULT = STANDARD
        val PROFESSIONAL = listOf(INTERPRETER, MEDICAL, LEGAL)
    }
}
