package com.vedicvidya.domain.agent

import com.vedicvidya.data.model.AgentType
import com.vedicvidya.data.model.SourceCitation

/**
 * Core interface for the Vedic Knowledge Agent
 *
 * The agent uses RAG (Retrieval-Augmented Generation) to:
 * 1. Search relevant verses from the corpus
 * 2. Build rich context with translations and commentaries
 * 3. Synthesize accurate answers with citations
 */
interface VedicAgent {

    /**
     * Ask a question and get an answer with sources
     *
     * @param question User's question
     * @return Agent response with answer and citations
     */
    suspend fun ask(question: String): AgentResponse

    /**
     * Get related verses for a query without full answer generation
     * (Faster, useful for browsing)
     */
    suspend fun findRelatedVerses(query: String, limit: Int = 10): List<SourceCitation>

    /**
     * Check if agent is available (online/offline, API key configured, etc.)
     */
    suspend fun isAvailable(): Boolean

    /**
     * Get agent capabilities and configuration
     */
    fun getCapabilities(): AgentCapabilities
}

/**
 * Response from the Vedic Agent
 */
data class AgentResponse(
    val question: String,
    val answer: String,
    val sources: List<SourceCitation>,
    val confidence: Float,
    val agentType: AgentType,
    val disclaimerText: String? = null,
    val perspectives: Map<String, String>? = null,  // Different philosophical schools
    val relatedTopics: List<String> = emptyList(),
    val processingTimeMs: Long = 0
) {
    fun hasHighConfidence(): Boolean = confidence >= 0.8f
    fun hasMediumConfidence(): Boolean = confidence in 0.5f..0.8f
    fun hasLowConfidence(): Boolean = confidence < 0.5f
}

/**
 * Agent capabilities and configuration
 */
data class AgentCapabilities(
    val type: AgentType,
    val supportsMultipleTranslations: Boolean,
    val supportsCommentaries: Boolean,
    val supportsSanskritAnalysis: Boolean,
    val supportsOfflineMode: Boolean,
    val maxContextSize: Int,
    val averageResponseTimeMs: Long
)
