package com.vedicvidya.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vedicvidya.data.database.Converters

/**
 * Tier 1 storage: Book/Chapter summaries for fast coarse-grained search
 * Always stored on device (~1MB total for entire corpus)
 */
@Entity(tableName = "text_summaries")
@TypeConverters(Converters::class)
data class TextSummary(
    @PrimaryKey
    val id: String,                          // e.g., "BG" or "RAM_SUNDARA"

    // Identification
    val textName: String,                    // e.g., "Bhagavad Gita"
    val textId: String,                      // e.g., "BG"
    val level: SummaryLevel,                 // BOOK, SECTION, or CHAPTER

    // Hierarchy
    val parentId: String? = null,            // Parent summary ID
    val chapter: Int? = null,
    val section: String? = null,             // For texts like Ramayana (Kandas)

    // Summary content
    val summary: String,                     // 2-3 sentence summary
    val detailedSummary: String? = null,     // Longer description
    val keyThemes: List<String>,             // Main themes/concepts
    val keyCharacters: List<String> = emptyList(),  // For narrative texts

    // Vector embedding for semantic search
    val embedding: String,                   // Lighter embedding (64 or 128 dim)

    // Metadata
    val verseCount: Int,                     // Number of verses in this section
    val verseRange: String,                  // e.g., "1-72" or "1.1-1.46"

    // Stats for smart caching
    val popularityScore: Float = 0f,         // How often this is accessed
    val downloadedLocally: Boolean = true    // Always true for summaries
) {
    fun getEmbeddingArray(): FloatArray {
        return embedding.split(",").map { it.toFloat() }.toFloatArray()
    }
}

enum class SummaryLevel {
    BOOK,      // Entire text summary (e.g., whole Bhagavad Gita)
    SECTION,   // Major section (e.g., Sundara Kanda in Ramayana)
    CHAPTER    // Individual chapter
}
