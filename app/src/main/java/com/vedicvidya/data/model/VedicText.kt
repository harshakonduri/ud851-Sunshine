package com.vedicvidya.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vedicvidya.data.database.Converters

/**
 * Represents a complete Vedic text (e.g., Bhagavad Gita, Ramayana)
 * Catalog of available texts
 */
@Entity(tableName = "vedic_texts")
@TypeConverters(Converters::class)
data class VedicText(
    @PrimaryKey
    val id: String,                          // e.g., "BG", "RAM", "MBH"

    // Text information
    val name: String,                        // e.g., "Bhagavad Gita"
    val namesanskrit: String,                // e.g., "श्रीमद्भगवद्गीता"
    val author: String,                      // e.g., "Veda Vyasa"
    val category: TextCategory,

    // Structure
    val totalChapters: Int,
    val totalVerses: Int,
    val hasSections: Boolean = false,        // True for Ramayana (Kandas), Mahabharata (Parvas)
    val sections: List<String> = emptyList(), // e.g., ["Bala Kanda", "Ayodhya Kanda"]

    // Content availability
    val downloadStatus: DownloadStatus,
    val downloadedVerses: Int = 0,           // How many verses are locally available
    val downloadSizeMB: Float,               // Estimated size for full download

    // Display
    val description: String,
    val coverImageUrl: String? = null,
    val iconUrl: String? = null,

    // Metadata
    val language: String = "Sanskrit",
    val estimatedReadingHours: Int,
    val difficulty: DifficultyLevel,

    // User engagement
    val isFavorite: Boolean = false,
    val lastReadAt: Long = 0L,
    val readingProgress: Float = 0f         // 0.0 to 1.0
)

enum class TextCategory {
    ITIHASAS,          // Ramayana, Mahabharata
    BHAKTI,            // Bhagavad Gita, Bhagavata Purana
    VEDAS,             // Rig, Yajur, Sama, Atharva
    UPANISHADS,        // Isha, Kena, Katha, etc.
    PURANAS,           // 18 Mahapuranas
    SUTRAS,            // Brahma Sutras, Yoga Sutras
    DHARMA_SHASTRAS,   // Manu Smriti, etc.
    OTHER
}

enum class DownloadStatus {
    NOT_DOWNLOADED,    // Only summary available
    PARTIAL,           // Popular verses downloaded
    COMPLETE           // Fully downloaded
}

enum class DifficultyLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    SCHOLAR
}
