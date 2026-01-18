package com.vedicvidya.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vedicvidya.data.database.Converters

/**
 * Represents a single verse from Vedic scriptures with full metadata
 * Supports hierarchical multi-tier storage
 */
@Entity(tableName = "vedic_verses")
@TypeConverters(Converters::class)
data class VedicVerse(
    @PrimaryKey
    val id: String,                          // e.g., "BG_2_47"

    // Text identification
    val textName: String,                    // e.g., "Bhagavad Gita"
    val textId: String,                      // e.g., "BG"
    val chapter: Int,
    val verse: Int,
    val section: String? = null,             // e.g., "Sundara Kanda" for Ramayana

    // Content
    val sanskrit: String,                     // Devanagari script
    val transliteration: String,              // Roman script
    val translation: String,                  // Primary English translation
    val wordByWordMeaning: String? = null,    // Optional detailed word analysis
    val commentary: String? = null,           // Primary commentary

    // Multiple translations (JSON array of translator:text pairs)
    val additionalTranslations: String? = null,

    // Vector embedding for semantic search (serialized float array)
    val embedding: String,                    // Stored as comma-separated values
    val embeddingDimension: Int = 128,        // Default to 128-dim for mobile

    // Metadata
    val topics: List<String>,                 // e.g., ["dharma", "karma", "action"]
    val concepts: List<String>,               // e.g., ["Nishkama Karma"]
    val relatedVerses: List<String>,          // Related verse IDs

    // Quality tier for adaptive agent
    val qualityTier: QualityTier,

    // Caching and performance
    val accessCount: Int = 0,                 // For LRU cache
    val lastAccessedAt: Long = 0L,            // Timestamp
    val isFavorite: Boolean = false,

    // Storage tier
    val storageTier: StorageTier = StorageTier.WARM
) {
    /**
     * Get embedding as float array
     */
    fun getEmbeddingArray(): FloatArray {
        return embedding.split(",").map { it.toFloat() }.toFloatArray()
    }

    /**
     * Get reference string (e.g., "BG 2.47")
     */
    fun getReference(): String {
        return "$textId $chapter.$verse"
    }

    /**
     * Check if verse has high-quality data
     */
    fun isHighQuality(): Boolean {
        return qualityTier == QualityTier.HIGH &&
               commentary != null &&
               additionalTranslations != null
    }
}

enum class QualityTier {
    HIGH,      // Multiple translations + commentaries
    MEDIUM,    // Single translation + some commentary
    LOW        // Sanskrit only or AI-translated
}

enum class StorageTier {
    HOT,       // Always in memory, frequently accessed
    WARM,      // On device, cached
    COLD       // Cloud, fetched on demand
}
