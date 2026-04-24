package com.jworks.eigosage.ui.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.jworks.eigosage.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jworks.eigosage.data.ai.ChatPersona
import com.jworks.eigosage.ui.theme.GlassBorder
import com.jworks.eigosage.ui.theme.GlassGradient
import com.jworks.eigosage.ui.theme.glassCardColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatPanel(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    onSendMessage: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    suggestions: List<String> = emptyList(),
    extractedWords: List<String> = emptyList(),
    onBookmarkWords: () -> Unit = {},
    onExportText: () -> Unit = {},
    onExportPdf: () -> Unit = {},
    persona: ChatPersona = ChatPersona.DEFAULT,
    onPersonaChange: (ChatPersona) -> Unit = {}
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Visible messages: skip the seed (index 0) — user only sees model greeting + follow-ups
    val visibleMessages = remember(messages) {
        if (messages.size > 1) messages.drop(1) else emptyList()
    }

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(visibleMessages.size, isLoading) {
        if (visibleMessages.isNotEmpty()) {
            listState.animateScrollToItem(visibleMessages.lastIndex)
        }
    }

    Column(modifier = modifier.fillMaxSize().imePadding()) {
        // Header
        ChatHeader(
            onDismiss = onDismiss,
            onShareText = onExportText,
            onSharePdf = onExportPdf,
            hasMessages = visibleMessages.isNotEmpty(),
            persona = persona,
            onPersonaChange = onPersonaChange,
            canChangePersona = visibleMessages.isEmpty()
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // Message list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp)
        ) {
            items(visibleMessages, key = { it.id }) { message ->
                ChatBubble(message = message)
            }

            // Loading indicator
            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.chat_thinking),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Suggestion chips — show dynamic (AI-generated) or static fallback
        if (!isLoading) {
            if (suggestions.isNotEmpty()) {
                SuggestionChipsRow(
                    suggestions = suggestions,
                    onChipClick = { text -> onSendMessage(text) }
                )
            } else if (visibleMessages.size <= 2) {
                SuggestionChipsRow(onChipClick = { text -> onSendMessage(text) })
            }
        }

        // Save words chip — show when AI response contains bold key terms
        if (extractedWords.isNotEmpty() && !isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                AssistChip(
                    onClick = onBookmarkWords,
                    label = {
                        Text(
                            text = "Save ${extractedWords.size} word${if (extractedWords.size > 1) "s" else ""}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.BookmarkAdd,
                            contentDescription = "Save words",
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f),
                        labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        leadingIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // Input bar
        ChatInputBar(
            text = inputText,
            onTextChange = { inputText = it },
            onSend = {
                if (inputText.isNotBlank() && !isLoading) {
                    onSendMessage(inputText.trim())
                    inputText = ""
                }
            },
            enabled = !isLoading
        )
    }
}

@Composable
private fun ChatHeader(
    onDismiss: () -> Unit,
    onShareText: () -> Unit = {},
    onSharePdf: () -> Unit = {},
    hasMessages: Boolean = false,
    persona: ChatPersona = ChatPersona.DEFAULT,
    onPersonaChange: (ChatPersona) -> Unit = {},
    canChangePersona: Boolean = true
) {
    var showShareMenu by remember { mutableStateOf(false) }
    var showPersonaMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.chat_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        // Persona selector chip
        Box {
            AssistChip(
                onClick = { if (canChangePersona) showPersonaMenu = true },
                label = {
                    Text(
                        text = persona.displayName,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (canChangePersona)
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = if (canChangePersona)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            androidx.compose.material3.DropdownMenu(
                expanded = showPersonaMenu,
                onDismissRequest = { showPersonaMenu = false }
            ) {
                ChatPersona.entries.forEach { p ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = p.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (p == persona) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = p.shortDescription,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        onClick = {
                            showPersonaMenu = false
                            onPersonaChange(p)
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (hasMessages) {
            Box {
                IconButton(
                    onClick = { showShareMenu = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = "Share chat",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
                androidx.compose.material3.DropdownMenu(
                    expanded = showShareMenu,
                    onDismissRequest = { showShareMenu = false }
                ) {
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(stringResource(R.string.chat_share_as_text)) },
                        onClick = {
                            showShareMenu = false
                            onShareText()
                        }
                    )
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(stringResource(R.string.chat_export_as_pdf)) },
                        onClick = {
                            showShareMenu = false
                            onSharePdf()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val isUser = message.role == ChatRole.USER

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        if (isUser) {
            // User bubble
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        } else {
            // Model bubble — glass card style
            Card(
                modifier = Modifier.widthIn(max = 300.dp),
                colors = glassCardColors(),
                border = GlassBorder,
                shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(GlassGradient)
                        .padding(12.dp)
                ) {
                    StyledChatText(
                        text = message.content,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun StyledChatText(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier
) {
    val annotated = buildAnnotatedString {
        var remaining = text
        while (remaining.contains("**")) {
            val start = remaining.indexOf("**")
            val end = remaining.indexOf("**", start + 2)
            if (end == -1) {
                append(remaining)
                remaining = ""
                break
            }
            append(remaining.substring(0, start))
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(remaining.substring(start + 2, end))
            }
            remaining = remaining.substring(end + 2)
        }
        append(remaining)
    }

    Text(
        text = annotated,
        style = style,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

private val STATIC_SUGGESTIONS = listOf(
    "Explain simpler",
    "Give examples",
    "Translate to Japanese",
    "Key vocabulary",
    "Grammar check"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SuggestionChipsRow(
    onChipClick: (String) -> Unit,
    suggestions: List<String> = STATIC_SUGGESTIONS
) {

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        suggestions.forEach { text ->
            SuggestionChip(
                onClick = { onChipClick(text) },
                label = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                border = SuggestionChipDefaults.suggestionChipBorder(
                    enabled = true,
                    borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
private fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = stringResource(R.string.chat_input_placeholder),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            maxLines = 3,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            ),
            textStyle = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onSend,
            enabled = enabled && text.isNotBlank(),
            modifier = Modifier.size(44.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
