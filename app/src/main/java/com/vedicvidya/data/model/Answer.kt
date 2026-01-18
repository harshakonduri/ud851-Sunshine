package com.vedicvidya.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vedicvidya.data.database.Converters

/**
 * Answer to a question with source citations
 */
@Entity(tableName = "answers")
@TypeConverters(Converters::class)
data class Answer(
    @PrimaryKey
    val id: String,

    // Reference
    val questionId: String,

    // Answer content
    val answerText: String,
    val sources: List<SourceCitation>,       // Verse citations

    // Agent metadata
    val generatedBy: AgentType,
    val confidence: Float,                   // 0.0 to 1.0
    val disclaimerText: String? = null,      // For AI-generated answers

    // Quality
    val verified: Boolean = false,           // Community/expert verification
    val verificationCount: Int = 0,
    val upvotes: Int = 0,

    // Perspectives from different schools (optional)
    val perspectives: String? = null,        // JSON map of school -> interpretation

    // Metadata
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

    // Caching
    val isCached: Boolean = true
)

/**
 * Citation to a specific verse
 */
data class SourceCitation(
    val verseId: String,                     // e.g., "BG_2_47"
    val textName: String,                    // e.g., "Bhagavad Gita"
    val reference: String,                   // e.g., "BG 2.47"
    val relevanceScore: Float,               // How relevant to the answer
    val excerpt: String? = null              // Short quote from the verse
)

enum class AgentType {
    CLOUD_LLM,        // Claude/Gemini API
    ON_DEVICE_LLM,    // Local model
    CACHED,           // Pre-cached response
    MOCK,             // Demo/testing mode
    CURATED           // Human-written
}
