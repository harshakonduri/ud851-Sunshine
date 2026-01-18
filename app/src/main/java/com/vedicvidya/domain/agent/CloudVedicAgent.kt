package com.vedicvidya.domain.agent

import com.vedicvidya.data.database.VedicDatabase
import com.vedicvidya.data.model.AgentType
import com.vedicvidya.data.model.QualityTier
import com.vedicvidya.data.model.SourceCitation
import com.vedicvidya.data.model.VedicVerse
import com.vedicvidya.domain.intelligence.QueryAnalyzer
import com.vedicvidya.domain.search.EmbeddingGenerator
import com.vedicvidya.domain.search.VectorSearchEngine

/**
 * Cloud-based Vedic Agent using LLM API (Claude, Gemini, GPT)
 *
 * Architecture:
 * 1. Analyze query and determine scope
 * 2. Search relevant verses using vector similarity
 * 3. Build rich context with translations and commentaries
 * 4. Send to LLM for synthesis
 * 5. Parse and validate response
 *
 * Ready for API integration - just add API key in configuration
 */
class CloudVedicAgent(
    private val database: VedicDatabase,
    private val searchEngine: VectorSearchEngine,
    private val embeddingGenerator: EmbeddingGenerator,
    private val queryAnalyzer: QueryAnalyzer,
    private val apiKey: String? = null
) : VedicAgent {

    override suspend fun ask(question: String): AgentResponse {
        val startTime = System.currentTimeMillis()

        // Step 1: Analyze query
        val analysis = queryAnalyzer.analyze(question)

        // Step 2: Retrieve relevant verses
        val sources = findRelatedVerses(question, limit = 10)
        val relevantVerses = sources.mapNotNull { citation ->
            database.vedicVerseDao().getVerseById(citation.verseId)
        }

        // Step 3: Build context
        val context = buildContext(relevantVerses)

        // Step 4: Call LLM API
        val llmResponse = if (apiKey != null) {
            callLLMAPI(question, context, analysis.type.toString())
        } else {
            // Fallback to mock if no API key
            return MockVedicAgent(database, searchEngine, queryAnalyzer).ask(question)
        }

        // Step 5: Structure response
        return AgentResponse(
            question = question,
            answer = llmResponse,
            sources = sources.take(5),
            confidence = calculateConfidence(relevantVerses),
            agentType = AgentType.CLOUD_LLM,
            disclaimerText = if (hasLowQualityVerses(relevantVerses)) {
                "Some referenced verses have AI-assisted translations. Please verify with Sanskrit scholars for authoritative interpretation."
            } else null,
            processingTimeMs = System.currentTimeMillis() - startTime
        )
    }

    override suspend fun findRelatedVerses(query: String, limit: Int): List<SourceCitation> {
        // Generate query embedding
        val queryEmbedding = embeddingGenerator.generateEmbedding(query)

        // Search in database
        val allVerses = database.vedicVerseDao().getAllVerses()
        val searchResults = searchEngine.searchVerses(queryEmbedding, allVerses, topK = limit)

        // Convert to citations
        return searchResults.map { result ->
            SourceCitation(
                verseId = result.item.id,
                textName = result.item.textName,
                reference = result.item.getReference(),
                relevanceScore = result.score,
                excerpt = result.item.translation.take(150) + "..."
            )
        }
    }

    override suspend fun isAvailable(): Boolean {
        return apiKey != null && apiKey.isNotBlank()
    }

    override fun getCapabilities(): AgentCapabilities {
        return AgentCapabilities(
            type = AgentType.CLOUD_LLM,
            supportsMultipleTranslations = true,
            supportsCommentaries = true,
            supportsSanskritAnalysis = true,
            supportsOfflineMode = false,
            maxContextSize = 100000,
            averageResponseTimeMs = 2000
        )
    }

    /**
     * Build rich context from verses for LLM
     */
    private fun buildContext(verses: List<VedicVerse>): String {
        return buildString {
            appendLine("AUTHENTICATED VEDIC KNOWLEDGE BASE:")
            appendLine("=" + "=".repeat(60))
            appendLine()

            verses.forEachIndexed { index, verse ->
                appendLine("VERSE ${index + 1}:")
                appendLine("Reference: ${verse.getReference()}")
                appendLine()

                appendLine("Sanskrit:")
                appendLine(verse.sanskrit)
                appendLine()

                appendLine("Transliteration:")
                appendLine(verse.transliteration)
                appendLine()

                appendLine("Translation:")
                appendLine(verse.translation)
                appendLine()

                verse.commentary?.let {
                    appendLine("Commentary:")
                    appendLine(it)
                    appendLine()
                }

                appendLine("-".repeat(60))
                appendLine()
            }
        }
    }

    /**
     * Call LLM API (Claude, Gemini, or GPT)
     * TODO: Implement actual API calls
     */
    private suspend fun callLLMAPI(
        question: String,
        context: String,
        queryType: String
    ): String {
        // TODO: Implement API call to:
        // - Anthropic Claude API (recommended)
        // - Google Gemini API
        // - OpenAI GPT API

        // For now, return placeholder
        return """
        [API Integration Placeholder]

        To enable real LLM responses:
        1. Add your API key in settings
        2. Choose provider (Claude/Gemini/GPT)
        3. Implement API call in CloudVedicAgent.callLLMAPI()

        Example implementation:
        ```kotlin
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.anthropic.com/v1/messages")
            .addHeader("x-api-key", apiKey)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        return parseResponse(response)
        ```

        Question received: $question
        Context size: ${context.length} characters
        Query type: $queryType
        """.trimIndent()
    }

    private fun calculateConfidence(verses: List<VedicVerse>): Float {
        if (verses.isEmpty()) return 0f

        val qualityScores = verses.map { verse ->
            when (verse.qualityTier) {
                QualityTier.HIGH -> 1.0f
                QualityTier.MEDIUM -> 0.7f
                QualityTier.LOW -> 0.4f
            }
        }

        return qualityScores.average().toFloat()
    }

    private fun hasLowQualityVerses(verses: List<VedicVerse>): Boolean {
        return verses.any { it.qualityTier == QualityTier.LOW }
    }
}
