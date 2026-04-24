package com.jworks.eigosage.ui.study

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jworks.eigosage.R
import com.jworks.eigosage.data.local.entities.SrsCardEntity
import com.jworks.eigosage.domain.models.CefrLevel
import com.jworks.eigosage.domain.models.color
import com.jworks.eigosage.domain.srs.ReviewQuality
import com.jworks.eigosage.ui.theme.GlassBorder
import com.jworks.eigosage.ui.theme.GlassGradient
import com.jworks.eigosage.ui.theme.glassCardColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StudyScreen(
    onBackClick: () -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dueCount by viewModel.dueCardCount.collectAsState()
    val totalCount by viewModel.totalCardCount.collectAsState()
    val allCards by viewModel.allCards.collectAsState()
    val totalReviewed by viewModel.totalWordsReviewed.collectAsState()
    val bestStreak by viewModel.bestStreak.collectAsState()

    if (uiState.isStudying) {
        FlashcardSession(
            uiState = uiState,
            onReveal = { viewModel.revealCard() },
            onAnswer = { viewModel.answerCard(it) },
            onQuit = { viewModel.dismissSessionComplete() }
        )
    } else if (uiState.isSessionComplete) {
        SessionCompleteScreen(
            wordsReviewed = uiState.sessionWordsReviewed,
            wordsCorrect = uiState.sessionWordsCorrect,
            bestStreak = uiState.sessionBestStreak,
            onDone = { viewModel.dismissSessionComplete() }
        )
    } else {
        StudyDashboard(
            dueCount = dueCount,
            totalCount = totalCount,
            allCards = allCards,
            totalReviewed = totalReviewed ?: 0,
            bestStreak = bestStreak ?: 0,
            onStartStudy = { viewModel.startStudySession() },
            onRemoveCard = { viewModel.removeCard(it) },
            onBackClick = onBackClick
        )
    }
}

@Composable
private fun StudyDashboard(
    dueCount: Int,
    totalCount: Int,
    allCards: List<SrsCardEntity>,
    totalReviewed: Int,
    bestStreak: Int,
    onStartStudy: () -> Unit,
    onRemoveCard: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = stringResource(R.string.study_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(label = "Due", value = "$dueCount", highlight = dueCount > 0)
            StatCard(label = "Total", value = "$totalCount")
            StatCard(label = "Reviewed", value = "$totalReviewed")
            StatCard(label = "Best Streak", value = "$bestStreak")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Start study button
        if (dueCount > 0) {
            Button(
                onClick = onStartStudy,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Start study session")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Study $dueCount Words",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = glassCardColors(),
                border = GlassBorder,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(GlassGradient)
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "All caught up",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        if (totalCount == 0) "No cards yet" else "All caught up!",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (totalCount == 0) "Add words from captures or bookmarks"
                        else "Come back later for more reviews",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs: Deck / History
        PrimaryTabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(stringResource(R.string.study_deck_tab, totalCount)) }
            )
        }

        // Card list
        if (allCards.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.School,
                        contentDescription = "Empty study deck",
                        tint = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Your study deck is empty",
                        color = Color.White.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allCards, key = { it.id }) { card ->
                    DeckCardRow(
                        card = card,
                        onRemove = { onRemoveCard(card.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, highlight: Boolean = false) {
    Card(
        colors = glassCardColors(),
        border = GlassBorder,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .background(GlassGradient)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (highlight) MaterialTheme.colorScheme.primary else Color.White
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun DeckCardRow(
    card: SrsCardEntity,
    onRemove: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM d", Locale.US) }
    val cefrLevel = CefrLevel.fromString(card.cefrLevel)

    Card(
        colors = glassCardColors(),
        border = GlassBorder,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .background(GlassGradient)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // CEFR badge
            cefrLevel?.let { cefr ->
                val badgeColor = cefr.color().takeIf { it != Color.Transparent }
                    ?: MaterialTheme.colorScheme.outline
                Box(
                    modifier = Modifier
                        .background(badgeColor.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = cefr.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = badgeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = card.word,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                card.phonetic?.let { ipa ->
                    Text(
                        text = ipa,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 11.sp
                    )
                }
                Text(
                    text = card.definition,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f),
                    maxLines = 1
                )
            }

            // Next review date
            Column(horizontalAlignment = Alignment.End) {
                val isDue = card.nextReviewAt <= System.currentTimeMillis()
                Text(
                    text = if (isDue) "Due" else dateFormat.format(Date(card.nextReviewAt)),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDue) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f),
                    fontWeight = if (isDue) FontWeight.Bold else FontWeight.Normal
                )
                if (card.totalReviews > 0) {
                    Text(
                        text = "${card.correctReviews}/${card.totalReviews}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 10.sp
                    )
                }
            }

            IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun FlashcardSession(
    uiState: StudyUiState,
    onReveal: () -> Unit,
    onAnswer: (ReviewQuality) -> Unit,
    onQuit: () -> Unit
) {
    val card = uiState.currentCard ?: return
    val remaining = uiState.remainingCards.size + 1
    val cefrLevel = CefrLevel.fromString(card.cefrLevel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${uiState.sessionWordsReviewed} reviewed",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                "$remaining remaining",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
            OutlinedButton(onClick = onQuit, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
                Text(stringResource(R.string.study_quit), style = MaterialTheme.typography.labelMedium)
            }
        }

        // Streak
        if (uiState.sessionStreak > 1) {
            Text(
                "Streak: ${uiState.sessionStreak}",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFFFFC107),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Flashcard
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !uiState.isRevealed) { onReveal() },
            colors = glassCardColors(),
            border = GlassBorder,
            shape = RoundedCornerShape(20.dp)
        ) {
            AnimatedContent(
                targetState = uiState.isRevealed,
                transitionSpec = {
                    (slideInVertically { it / 4 } + fadeIn()) togetherWith
                            (slideOutVertically { -it / 4 } + fadeOut())
                },
                label = "flashcard_flip"
            ) { revealed ->
                Column(
                    modifier = Modifier
                        .background(GlassGradient)
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // CEFR badge
                    cefrLevel?.let { cefr ->
                        val badgeColor = cefr.color().takeIf { it != Color.Transparent }
                            ?: MaterialTheme.colorScheme.outline
                        Box(
                            modifier = Modifier
                                .background(badgeColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = cefr.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = badgeColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Word
                    Text(
                        text = card.word,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    // IPA
                    card.phonetic?.let { ipa ->
                        Text(
                            text = ipa,
                            style = MaterialTheme.typography.bodyLarge,
                            fontStyle = FontStyle.Italic,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }

                    if (revealed) {
                        Spacer(modifier = Modifier.height(20.dp))

                        // Definition
                        Text(
                            text = card.definition,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        // Example sentence
                        card.exampleSentence?.let { example ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "\"$example\"",
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic,
                                color = Color.White.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                        }

                        // Context
                        card.contextSnippet?.let { ctx ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Context: $ctx",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            "Tap to reveal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Answer buttons
        AnimatedVisibility(visible = uiState.isRevealed) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AnswerButton("Again", Color(0xFFF44336), ReviewQuality.AGAIN, onAnswer)
                AnswerButton("Hard", Color(0xFFFF9800), ReviewQuality.HARD, onAnswer)
                AnswerButton("Good", Color(0xFF4CAF50), ReviewQuality.GOOD, onAnswer)
                AnswerButton("Easy", Color(0xFF2196F3), ReviewQuality.EASY, onAnswer)
            }
        }

        if (!uiState.isRevealed) {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun AnswerButton(
    label: String,
    color: Color,
    quality: ReviewQuality,
    onAnswer: (ReviewQuality) -> Unit
) {
    FilledTonalButton(
        onClick = { onAnswer(quality) },
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = color.copy(alpha = 0.2f),
            contentColor = color
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(label, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SessionCompleteScreen(
    wordsReviewed: Int,
    wordsCorrect: Int,
    bestStreak: Int,
    onDone: () -> Unit
) {
    val accuracy = if (wordsReviewed > 0) (wordsCorrect * 100) / wordsReviewed else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = "Session complete",
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Session Complete!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(label = "Reviewed", value = "$wordsReviewed")
            StatCard(label = "Accuracy", value = "$accuracy%")
            StatCard(label = "Best Streak", value = "$bestStreak")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(stringResource(R.string.study_done), style = MaterialTheme.typography.titleMedium)
        }
    }
}
