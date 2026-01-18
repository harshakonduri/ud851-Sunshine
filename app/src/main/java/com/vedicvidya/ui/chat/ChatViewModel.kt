package com.vedicvidya.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vedicvidya.domain.agent.VedicAgent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val vedicAgent: VedicAgent
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun askQuestion(question: String) {
        // Add user message
        _messages.value = _messages.value + ChatMessage(
            text = question,
            isUser = true
        )

        // Get agent response
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = vedicAgent.ask(question)

                // Add agent response
                _messages.value = _messages.value + ChatMessage(
                    text = response.answer,
                    isUser = false,
                    sources = response.sources.map { it.reference }
                )
            } catch (e: Exception) {
                _messages.value = _messages.value + ChatMessage(
                    text = "Sorry, I encountered an error: ${e.message}",
                    isUser = false
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}
