package com.vedicvidya.data.database

import androidx.room.*
import com.vedicvidya.data.model.SummaryLevel
import com.vedicvidya.data.model.TextSummary
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for TextSummary (Tier 1 - always on device)
 */
@Dao
interface TextSummaryDao {

    @Query("SELECT * FROM text_summaries WHERE id = :summaryId")
    suspend fun getSummaryById(summaryId: String): TextSummary?

    @Query("SELECT * FROM text_summaries WHERE textId = :textId AND level = :level ORDER BY chapter")
    suspend fun getSummariesByText(textId: String, level: SummaryLevel): List<TextSummary>

    @Query("SELECT * FROM text_summaries WHERE level = 'BOOK'")
    fun getAllBookSummaries(): Flow<List<TextSummary>>

    @Query("SELECT * FROM text_summaries WHERE textId = :textId AND level = 'SECTION'")
    suspend fun getSectionSummaries(textId: String): List<TextSummary>

    @Query("SELECT * FROM text_summaries WHERE textId = :textId AND chapter = :chapter AND level = 'CHAPTER'")
    suspend fun getChapterSummary(textId: String, chapter: Int): TextSummary?

    @Query("SELECT * FROM text_summaries")
    suspend fun getAllSummaries(): List<TextSummary>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: TextSummary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummaries(summaries: List<TextSummary>)

    @Update
    suspend fun updateSummary(summary: TextSummary)

    @Delete
    suspend fun deleteSummary(summary: TextSummary)

    @Query("UPDATE text_summaries SET popularityScore = :score WHERE id = :summaryId")
    suspend fun updatePopularityScore(summaryId: String, score: Float)

    @Query("SELECT * FROM text_summaries WHERE keyThemes LIKE '%' || :theme || '%'")
    suspend fun searchByTheme(theme: String): List<TextSummary>
}
