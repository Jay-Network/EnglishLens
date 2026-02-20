package com.jworks.englishlens.ui.camera

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jworks.englishlens.domain.models.Definition
import com.jworks.englishlens.domain.models.Meaning
import com.jworks.englishlens.domain.models.PartOfSpeech
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DefinitionPanel(
    definition: Definition,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val slideOffset = remember { Animatable(40f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(definition.word) {
        launch {
            slideOffset.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(300)
            )
        }
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 72.dp)
                .alpha(alpha.value)
        ) {
            // Word header with gradient frequency badge
            WordHeader(
                word = definition.word,
                lemma = definition.lemma,
                frequency = definition.frequency,
                onDismiss = onDismiss
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Animated POS tag row
            POSTagRow(meanings = definition.meanings)

            Spacer(modifier = Modifier.height(12.dp))

            // Expandable definition cards
            DefinitionList(meanings = definition.meanings)
        }

        // TODO: Add-to-Vocabulary feature (requires persistence layer - deferred to v0.2.0)
    }
}

// -- Word Header --

@Composable
private fun WordHeader(
    word: String,
    lemma: String,
    frequency: Int?,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = word,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
            if (lemma != word) {
                Text(
                    text = "base: $lemma",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            frequency?.let { FrequencyBadge(it) }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun FrequencyBadge(frequency: Int) {
    val (label, gradient) = when {
        frequency <= 100 -> "Top 100" to listOf(Color(0xFF4CAF50), Color(0xFF81C784))
        frequency <= 1000 -> "Common" to listOf(Color(0xFF2196F3), Color(0xFF64B5F6))
        frequency <= 5000 -> "Moderate" to listOf(Color(0xFFFFA000), Color(0xFFFFCA28))
        else -> "Rare" to listOf(Color(0xFFFF5722), Color(0xFFFF8A65))
    }

    Box(
        modifier = Modifier
            .background(
                brush = Brush.horizontalGradient(gradient),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}

// -- Animated POS Tags --

@Composable
private fun POSTagRow(meanings: List<Meaning>) {
    val posSet = meanings.map { it.partOfSpeech }.distinct()

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(posSet) { index, pos ->
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(60L * index)
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(200)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )
            ) {
                POSTag(pos)
            }
        }
    }
}

@Composable
private fun POSTag(pos: PartOfSpeech) {
    val (color, label) = when (pos) {
        PartOfSpeech.NOUN -> Color(0xFF2196F3) to "noun"
        PartOfSpeech.VERB -> Color(0xFF4CAF50) to "verb"
        PartOfSpeech.ADJECTIVE -> Color(0xFFFFA000) to "adj"
        PartOfSpeech.ADVERB -> Color(0xFFFF5722) to "adv"
        PartOfSpeech.PREPOSITION -> Color(0xFF9C27B0) to "prep"
        PartOfSpeech.CONJUNCTION -> Color(0xFF795548) to "conj"
        PartOfSpeech.INTERJECTION -> Color(0xFFE91E63) to "intj"
        PartOfSpeech.UNKNOWN -> Color(0xFF9E9E9E) to "other"
    }

    Box(
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

// -- Expandable Definitions --

@Composable
private fun DefinitionList(meanings: List<Meaning>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        meanings.forEachIndexed { index, meaning ->
            var expanded by remember { mutableStateOf(index == 0) }

            DefinitionCard(
                meaning = meaning,
                index = index + 1,
                expanded = expanded,
                onToggle = { expanded = !expanded }
            )
        }
    }
}

@Composable
private fun DefinitionCard(
    meaning: Meaning,
    index: Int,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    val (posColor, _) = when (meaning.partOfSpeech) {
        PartOfSpeech.NOUN -> Color(0xFF2196F3) to "noun"
        PartOfSpeech.VERB -> Color(0xFF4CAF50) to "verb"
        PartOfSpeech.ADJECTIVE -> Color(0xFFFFA000) to "adj"
        PartOfSpeech.ADVERB -> Color(0xFFFF5722) to "adv"
        else -> Color(0xFF9E9E9E) to ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(spring(stiffness = Spring.StiffnessMediumLow))
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header row: index + POS + definition + expand icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "$index.",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = posColor,
                    modifier = Modifier.padding(end = 6.dp, top = 1.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = meaning.definition,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Expandable content
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(spring(stiffness = Spring.StiffnessMediumLow)) + fadeIn(),
                exit = shrinkVertically(spring(stiffness = Spring.StiffnessMediumLow)) + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 8.dp, start = 20.dp)) {
                    // Examples
                    if (meaning.examples.isNotEmpty()) {
                        meaning.examples.forEach { example ->
                            Row(modifier = Modifier.padding(top = 4.dp)) {
                                Text(
                                    text = "\u201C",
                                    color = posColor,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text(
                                    text = example,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Synonyms
                    if (meaning.synonyms.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text(
                                text = "syn ",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = meaning.synonyms.take(5).joinToString(", "),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4CAF50).copy(alpha = 0.8f)
                            )
                        }
                    }

                    // Antonyms
                    if (meaning.antonyms.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text(
                                text = "ant ",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFF44336),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = meaning.antonyms.take(5).joinToString(", "),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFF44336).copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// -- Loading Skeleton --

@Composable
fun DefinitionSkeleton(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Word placeholder
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = shimmerAlpha))
        )

        Spacer(modifier = Modifier.height(12.dp))

        // POS tags placeholder
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .width(56.dp)
                        .height(26.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray.copy(alpha = shimmerAlpha))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Definition card placeholders
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = shimmerAlpha))
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
