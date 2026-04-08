package com.jworks.eigosage.ui.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jworks.eigosage.data.local.entities.SrsCardEntity
import com.jworks.eigosage.data.local.entities.StudySessionEntity
import com.jworks.eigosage.data.repository.SrsRepository
import com.jworks.eigosage.domain.srs.ReviewQuality
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudyUiState(
    val isStudying: Boolean = false,
    val isRevealed: Boolean = false,
    val currentCard: SrsCardEntity? = null,
    val remainingCards: List<SrsCardEntity> = emptyList(),
    val sessionWordsReviewed: Int = 0,
    val sessionWordsCorrect: Int = 0,
    val sessionStreak: Int = 0,
    val sessionBestStreak: Int = 0,
    val isSessionComplete: Boolean = false,
    val currentSession: StudySessionEntity? = null
)

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val srsRepository: SrsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudyUiState())
    val uiState: StateFlow<StudyUiState> = _uiState.asStateFlow()

    val dueCardCount: StateFlow<Int> = srsRepository.getDueCardCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCardCount: StateFlow<Int> = srsRepository.getTotalCardCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allCards: StateFlow<List<SrsCardEntity>> = srsRepository.getAllCards()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalWordsReviewed: StateFlow<Int?> = srsRepository.getTotalWordsReviewed()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val bestStreak: StateFlow<Int?> = srsRepository.getBestStreak()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun startStudySession() {
        viewModelScope.launch {
            val dueCards = srsRepository.getDueCards()
            val firstCard = dueCards.firstOrNull() ?: return@launch

            val sessionId = srsRepository.startSession()
            _uiState.update {
                StudyUiState(
                    isStudying = true,
                    currentCard = firstCard,
                    remainingCards = dueCards.drop(1),
                    currentSession = StudySessionEntity(
                        id = sessionId,
                        startedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun revealCard() {
        _uiState.update { it.copy(isRevealed = true) }
    }

    fun answerCard(quality: ReviewQuality) {
        val state = _uiState.value
        val card = state.currentCard ?: return

        viewModelScope.launch {
            val updated = srsRepository.reviewCard(card, quality.value)
            val isCorrect = quality.value >= 3
            val newStreak = if (isCorrect) state.sessionStreak + 1 else 0
            val newBestStreak = maxOf(state.sessionBestStreak, newStreak)
            val reviewed = state.sessionWordsReviewed + 1
            val correct = state.sessionWordsCorrect + if (isCorrect) 1 else 0

            // If failed, re-queue at end
            val remaining = if (quality.value < 3) {
                state.remainingCards + updated
            } else {
                state.remainingCards
            }

            if (remaining.isEmpty()) {
                // Session complete
                state.currentSession?.let { session ->
                    srsRepository.endSession(
                        session.copy(
                            wordsReviewed = reviewed,
                            wordsCorrect = correct,
                            streak = newBestStreak
                        )
                    )
                }
                _uiState.update {
                    it.copy(
                        isStudying = false,
                        isRevealed = false,
                        currentCard = null,
                        remainingCards = emptyList(),
                        sessionWordsReviewed = reviewed,
                        sessionWordsCorrect = correct,
                        sessionStreak = newStreak,
                        sessionBestStreak = newBestStreak,
                        isSessionComplete = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isRevealed = false,
                        currentCard = remaining.firstOrNull(),
                        remainingCards = remaining.drop(1),
                        sessionWordsReviewed = reviewed,
                        sessionWordsCorrect = correct,
                        sessionStreak = newStreak,
                        sessionBestStreak = newBestStreak
                    )
                }
            }
        }
    }

    fun dismissSessionComplete() {
        _uiState.update { StudyUiState() }
    }

    fun removeCard(cardId: Long) {
        viewModelScope.launch { srsRepository.removeCard(cardId) }
    }
}
