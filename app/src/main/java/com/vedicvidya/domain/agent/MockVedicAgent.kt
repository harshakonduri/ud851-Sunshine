package com.vedicvidya.domain.agent

import com.vedicvidya.data.database.VedicDatabase
import com.vedicvidya.data.model.AgentType
import com.vedicvidya.data.model.SourceCitation
import com.vedicvidya.domain.intelligence.QueryAnalyzer
import com.vedicvidya.domain.intelligence.QueryType
import com.vedicvidya.domain.search.VectorSearchEngine
import kotlinx.coroutines.delay

/**
 * Mock implementation of VedicAgent for demo and testing
 * Returns pre-written responses based on query patterns
 */
class MockVedicAgent(
    private val database: VedicDatabase,
    private val searchEngine: VectorSearchEngine = VectorSearchEngine(),
    private val queryAnalyzer: QueryAnalyzer = QueryAnalyzer()
) : VedicAgent {

    override suspend fun ask(question: String): AgentResponse {
        val startTime = System.currentTimeMillis()

        // Simulate thinking time
        delay(500)

        // Analyze query
        val analysis = queryAnalyzer.analyze(question)

        // Get relevant verses
        val sources = findRelatedVerses(question, limit = 5)

        // Generate mock response based on query type
        val answer = generateMockAnswer(question, analysis.type, sources)

        return AgentResponse(
            question = question,
            answer = answer,
            sources = sources,
            confidence = 0.85f,
            agentType = AgentType.MOCK,
            disclaimerText = "This is a demo response. Connect an LLM API for real answers.",
            processingTimeMs = System.currentTimeMillis() - startTime
        )
    }

    override suspend fun findRelatedVerses(query: String, limit: Int): List<SourceCitation> {
        // For mock: return sample verses from Bhagavad Gita
        val verses = database.vedicVerseDao().getPopularVerses(limit)

        return verses.take(limit).map { verse ->
            SourceCitation(
                verseId = verse.id,
                textName = verse.textName,
                reference = verse.getReference(),
                relevanceScore = 0.8f,
                excerpt = verse.translation.take(100) + "..."
            )
        }
    }

    override suspend fun isAvailable(): Boolean = true

    override fun getCapabilities(): AgentCapabilities {
        return AgentCapabilities(
            type = AgentType.MOCK,
            supportsMultipleTranslations = false,
            supportsCommentaries = false,
            supportsSanskritAnalysis = false,
            supportsOfflineMode = true,
            maxContextSize = 1000,
            averageResponseTimeMs = 500
        )
    }

    private fun generateMockAnswer(
        question: String,
        queryType: QueryType,
        sources: List<SourceCitation>
    ): String {
        return when (queryType) {
            QueryType.PHILOSOPHICAL -> {
                """
                Based on the Vedic scriptures, this question touches upon fundamental philosophical concepts.
                The texts provide multiple perspectives on this topic:

                ${sources.firstOrNull()?.textName ?: "The scriptures"} teaches that ${getMockPhilosophicalStatement(question)}.

                This understanding is further elaborated in ${sources.getOrNull(1)?.reference ?: "other verses"},
                which emphasizes the importance of ${getMockConcept(question)}.

                Note: This is a simplified demo answer. For authentic interpretation with exact verse citations,
                please configure an LLM API in the settings.
                """.trimIndent()
            }

            QueryType.NARRATIVE -> {
                """
                This refers to a significant episode in the ${sources.firstOrNull()?.textName ?: "scripture"}.

                According to the traditional narrative, ${getMockNarrativeSummary(question)}.

                This story illustrates important values of ${getMockValue(question)}.

                For detailed verse-by-verse exploration, please use the Library tab to read the full chapter.

                Note: This is a demo summary. Connect an LLM API for detailed, citation-backed narratives.
                """.trimIndent()
            }

            QueryType.PRACTICAL -> {
                """
                The Vedic texts provide guidance on this practice:

                1. ${getMockStep1(question)}
                2. ${getMockStep2(question)}
                3. ${getMockStep3(question)}

                As mentioned in ${sources.firstOrNull()?.reference ?: "the scriptures"},
                ${getMockPracticalGuidance(question)}.

                Important: For authoritative ritual guidance, please consult with a qualified teacher or priest.

                Note: This is a simplified demo. Real implementation will provide exact verse references.
                """.trimIndent()
            }

            else -> {
                """
                Thank you for your question about Hindu knowledge and Vedic wisdom.

                The scriptures address this topic in several places. According to ${sources.firstOrNull()?.reference ?: "the texts"}:

                ${sources.firstOrNull()?.excerpt ?: "The fundamental teaching emphasizes dharma, karma, and spiritual growth."}

                This concept is intimately connected with the broader themes of ${getMockThemes(question)}.

                To explore this topic further, you can:
                • Read the full verses in the Library tab
                • Browse related questions in the Q&A section
                • Search for specific Sanskrit terms

                Note: This is a demo mode response. Configure an LLM API (Claude, Gemini, or GPT)
                in settings for detailed, accurate answers with complete citations.
                """.trimIndent()
            }
        }
    }

    // Mock response generators (simplified for demo)
    private fun getMockPhilosophicalStatement(question: String): String {
        return when {
            question.contains("dharma", ignoreCase = true) ->
                "one's dharma (duty) must be performed with dedication, without attachment to results"
            question.contains("karma", ignoreCase = true) ->
                "karma (action) should be performed selflessly, as an offering to the Divine"
            question.contains("moksha", ignoreCase = true) ->
                "moksha (liberation) is achieved through knowledge, devotion, and righteous action"
            else ->
                "all beings are fundamentally connected to the Supreme Reality"
        }
    }

    private fun getMockConcept(question: String): String {
        return when {
            question.contains("duty", ignoreCase = true) -> "performing one's prescribed duty"
            question.contains("meditation", ignoreCase = true) -> "regular meditation and self-reflection"
            else -> "living according to dharma"
        }
    }

    private fun getMockNarrativeSummary(question: String): String {
        return "the characters faced moral dilemmas that tested their devotion and understanding of dharma"
    }

    private fun getMockValue(question: String): String {
        return "devotion, courage, and adherence to dharma even in difficult circumstances"
    }

    private fun getMockStep1(question: String): String = "Begin with proper purification and preparation"
    private fun getMockStep2(question: String): String = "Invoke the deities with prescribed mantras"
    private fun getMockStep3(question: String): String = "Complete the practice with offerings and prayers"

    private fun getMockPracticalGuidance(question: String): String {
        return "the practice should be performed with sincerity, proper pronunciation, and understanding of meaning"
    }

    private fun getMockThemes(question: String): String {
        return "dharma (righteousness), karma (action), bhakti (devotion), and self-realization"
    }
}
