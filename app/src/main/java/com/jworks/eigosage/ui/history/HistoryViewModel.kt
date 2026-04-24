package com.jworks.eigosage.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jworks.eigosage.data.local.entities.BookmarkedWordEntity
import com.jworks.eigosage.data.local.entities.ChatSessionEntity
import com.jworks.eigosage.data.local.entities.LookupHistoryEntity
import com.jworks.eigosage.data.repository.ChatRepository
import com.jworks.eigosage.data.repository.HistoryRepository
import com.jworks.eigosage.data.repository.SrsRepository
import com.jworks.eigosage.domain.export.ChatExportData
import com.jworks.eigosage.domain.export.ChatExportMessage
import com.jworks.eigosage.domain.export.ChatExporter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

sealed class ChatExportEvent {
    data class TextReady(val text: String) : ChatExportEvent()
    data class PdfReady(val file: File) : ChatExportEvent()
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val srsRepository: SrsRepository,
    private val chatRepository: ChatRepository,
    private val chatExporter: ChatExporter
) : ViewModel() {

    val recentHistory: StateFlow<List<LookupHistoryEntity>> = historyRepository.getRecentHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkedWordEntity>> = historyRepository.getAllBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val historyCount: StateFlow<Int> = historyRepository.getHistoryCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val bookmarkCount: StateFlow<Int> = historyRepository.getBookmarkCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val chatSessions: StateFlow<List<ChatSessionEntity>> = chatRepository.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatSessionCount: StateFlow<Int> = chatRepository.getSessionCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _wordsInDeck = MutableStateFlow<Set<String>>(emptySet())
    val wordsInDeck: StateFlow<Set<String>> = _wordsInDeck.asStateFlow()

    init {
        refreshDeckStatus()
    }

    private fun refreshDeckStatus() {
        viewModelScope.launch {
            val bookmarkList = historyRepository.getAllBookmarksOnce()
            val inDeck = mutableSetOf<String>()
            for (bookmark in bookmarkList) {
                if (srsRepository.getCardByWord(bookmark.word) != null) {
                    inDeck.add(bookmark.word)
                }
            }
            _wordsInDeck.value = inDeck
        }
    }

    fun addToStudyDeck(bookmark: BookmarkedWordEntity) {
        viewModelScope.launch {
            srsRepository.addCard(
                word = bookmark.word,
                definition = bookmark.definition,
                contextSnippet = bookmark.contextSnippet,
                source = "bookmark"
            )
            _wordsInDeck.value = _wordsInDeck.value + bookmark.word
        }
    }

    fun addAllToStudyDeck() {
        viewModelScope.launch {
            val allBookmarks = historyRepository.getAllBookmarksOnce()
            for (bookmark in allBookmarks) {
                if (bookmark.word !in _wordsInDeck.value) {
                    srsRepository.addCard(
                        word = bookmark.word,
                        definition = bookmark.definition,
                        contextSnippet = bookmark.contextSnippet,
                        source = "bookmark"
                    )
                }
            }
            _wordsInDeck.value = _wordsInDeck.value + allBookmarks.map { it.word }
        }
    }

    fun clearHistory() {
        viewModelScope.launch { historyRepository.clearHistory() }
    }

    fun removeBookmark(word: String) {
        viewModelScope.launch { historyRepository.removeBookmark(word) }
    }

    fun deleteChatSession(sessionId: String) {
        viewModelScope.launch { chatRepository.deleteSession(sessionId) }
    }

    private val _chatExportEvent = MutableStateFlow<ChatExportEvent?>(null)
    val chatExportEvent: StateFlow<ChatExportEvent?> = _chatExportEvent.asStateFlow()

    fun exportChatSession(sessionId: String, asPdf: Boolean) {
        viewModelScope.launch {
            val session = chatRepository.getSession(sessionId) ?: return@launch
            val messages = chatRepository.getMessages(sessionId)
            if (messages.isEmpty()) return@launch

            val exportData = ChatExportData(
                sessionTitle = session.ocrTextPreview.ifBlank { "Chat session" },
                cefrLevel = session.cefrLevel,
                messages = messages.map { msg ->
                    ChatExportMessage(
                        role = msg.role,
                        content = msg.content,
                        timestamp = msg.timestamp
                    )
                },
                createdAt = session.createdAt
            )

            if (asPdf) {
                chatExporter.exportAsPdf(exportData).onSuccess { file ->
                    _chatExportEvent.value = ChatExportEvent.PdfReady(file)
                }
            } else {
                val text = chatExporter.formatAsText(exportData)
                _chatExportEvent.value = ChatExportEvent.TextReady(text)
            }
        }
    }

    fun clearExportEvent() {
        _chatExportEvent.value = null
    }
}
