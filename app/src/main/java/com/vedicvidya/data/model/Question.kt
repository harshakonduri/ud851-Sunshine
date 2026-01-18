package com.vedicvidya.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vedicvidya.data.database.Converters

/**
 * User question in Q&A system
 */
@Entity(tableName = "questions")
@TypeConverters(Converters::class)
data class Question(
    @PrimaryKey
    val id: String,

    // Question content
    val questionText: String,
    val category: QuestionCategory,
    val tags: List<String> = emptyList(),

    // Embedding for similarity search
    val embedding: String? = null,           // Generated on first search

    // Metadata
    val askedAt: Long = System.currentTimeMillis(),
    val askedByUser: Boolean = true,         // false if from curated Q&A
    val viewCount: Int = 0,
    val isPopular: Boolean = false,

    // Answer reference
    val hasAnswer: Boolean = false,
    val answerId: String? = null
)

enum class QuestionCategory {
    PHILOSOPHY,
    RITUALS,
    MYTHOLOGY,
    FESTIVALS,
    DAILY_LIFE,
    MEDITATION,
    YOGA,
    GENERAL
}
