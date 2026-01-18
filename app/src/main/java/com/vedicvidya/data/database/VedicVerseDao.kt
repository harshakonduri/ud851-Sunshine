package com.vedicvidya.data.database

import androidx.room.*
import com.vedicvidya.data.model.StorageTier
import com.vedicvidya.data.model.VedicVerse
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for VedicVerse
 */
@Dao
interface VedicVerseDao {

    @Query("SELECT * FROM vedic_verses WHERE id = :verseId")
    suspend fun getVerseById(verseId: String): VedicVerse?

    @Query("SELECT * FROM vedic_verses WHERE textId = :textId ORDER BY chapter, verse")
    fun getVersesByText(textId: String): Flow<List<VedicVerse>>

    @Query("SELECT * FROM vedic_verses WHERE textId = :textId AND chapter = :chapter ORDER BY verse")
    suspend fun getVersesByChapter(textId: String, chapter: Int): List<VedicVerse>

    @Query("SELECT * FROM vedic_verses WHERE storageTier = :tier ORDER BY accessCount DESC")
    suspend fun getVersesByTier(tier: StorageTier): List<VedicVerse>

    @Query("SELECT * FROM vedic_verses WHERE isFavorite = 1 ORDER BY lastAccessedAt DESC")
    fun getFavoriteVerses(): Flow<List<VedicVerse>>

    @Query("SELECT * FROM vedic_verses ORDER BY lastAccessedAt DESC LIMIT :limit")
    suspend fun getRecentVerses(limit: Int = 20): List<VedicVerse>

    @Query("SELECT * FROM vedic_verses ORDER BY accessCount DESC LIMIT :limit")
    suspend fun getPopularVerses(limit: Int = 100): List<VedicVerse>

    @Query("SELECT * FROM vedic_verses WHERE storageTier = 'HOT'")
    suspend fun getHotStorageVerses(): List<VedicVerse>

    @Query("SELECT * FROM vedic_verses")
    suspend fun getAllVerses(): List<VedicVerse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerse(verse: VedicVerse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<VedicVerse>)

    @Update
    suspend fun updateVerse(verse: VedicVerse)

    @Query("UPDATE vedic_verses SET accessCount = accessCount + 1, lastAccessedAt = :timestamp WHERE id = :verseId")
    suspend fun incrementAccessCount(verseId: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE vedic_verses SET isFavorite = :favorite WHERE id = :verseId")
    suspend fun setFavorite(verseId: String, favorite: Boolean)

    @Query("UPDATE vedic_verses SET storageTier = :tier WHERE id = :verseId")
    suspend fun updateStorageTier(verseId: String, tier: StorageTier)

    @Delete
    suspend fun deleteVerse(verse: VedicVerse)

    @Query("DELETE FROM vedic_verses WHERE storageTier = 'COLD' AND accessCount < :threshold AND isFavorite = 0")
    suspend fun evictColdVerses(threshold: Int = 1)

    @Query("SELECT COUNT(*) FROM vedic_verses WHERE textId = :textId")
    suspend fun getVerseCount(textId: String): Int

    // Full-text search (basic)
    @Query("SELECT * FROM vedic_verses WHERE translation LIKE '%' || :query || '%' OR transliteration LIKE '%' || :query || '%' LIMIT :limit")
    suspend fun searchVerses(query: String, limit: Int = 20): List<VedicVerse>
}
