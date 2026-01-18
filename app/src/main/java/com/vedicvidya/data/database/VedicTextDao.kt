package com.vedicvidya.data.database

import androidx.room.*
import com.vedicvidya.data.model.DownloadStatus
import com.vedicvidya.data.model.TextCategory
import com.vedicvidya.data.model.VedicText
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for VedicText catalog
 */
@Dao
interface VedicTextDao {

    @Query("SELECT * FROM vedic_texts WHERE id = :textId")
    suspend fun getTextById(textId: String): VedicText?

    @Query("SELECT * FROM vedic_texts ORDER BY name")
    fun getAllTexts(): Flow<List<VedicText>>

    @Query("SELECT * FROM vedic_texts WHERE category = :category ORDER BY name")
    fun getTextsByCategory(category: TextCategory): Flow<List<VedicText>>

    @Query("SELECT * FROM vedic_texts WHERE isFavorite = 1 ORDER BY lastReadAt DESC")
    fun getFavoriteTexts(): Flow<List<VedicText>>

    @Query("SELECT * FROM vedic_texts WHERE downloadStatus = :status")
    fun getTextsByDownloadStatus(status: DownloadStatus): Flow<List<VedicText>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertText(text: VedicText)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTexts(texts: List<VedicText>)

    @Update
    suspend fun updateText(text: VedicText)

    @Query("UPDATE vedic_texts SET isFavorite = :favorite WHERE id = :textId")
    suspend fun setFavorite(textId: String, favorite: Boolean)

    @Query("UPDATE vedic_texts SET lastReadAt = :timestamp, readingProgress = :progress WHERE id = :textId")
    suspend fun updateReadingProgress(textId: String, progress: Float, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE vedic_texts SET downloadStatus = :status, downloadedVerses = :count WHERE id = :textId")
    suspend fun updateDownloadStatus(textId: String, status: DownloadStatus, count: Int)

    @Delete
    suspend fun deleteText(text: VedicText)
}
