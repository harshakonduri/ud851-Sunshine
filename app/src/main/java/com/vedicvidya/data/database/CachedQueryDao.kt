package com.vedicvidya.data.database

import androidx.room.*
import com.vedicvidya.data.model.CachedQuery

/**
 * Data Access Object for CachedQuery (for offline and cost optimization)
 */
@Dao
interface CachedQueryDao {

    @Query("SELECT * FROM cached_queries WHERE queryHash = :hash")
    suspend fun getCachedQueryByHash(hash: String): CachedQuery?

    @Query("SELECT * FROM cached_queries WHERE queryText = :queryText LIMIT 1")
    suspend fun getCachedQueryByText(queryText: String): CachedQuery?

    @Query("SELECT * FROM cached_queries ORDER BY lastAccessedAt DESC LIMIT :limit")
    suspend fun getRecentCachedQueries(limit: Int = 100): List<CachedQuery>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedQuery(query: CachedQuery)

    @Update
    suspend fun updateCachedQuery(query: CachedQuery)

    @Query("UPDATE cached_queries SET accessCount = accessCount + 1, lastAccessedAt = :timestamp WHERE id = :queryId")
    suspend fun incrementAccessCount(queryId: String, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM cached_queries WHERE expiresAt < :currentTime")
    suspend fun deleteExpiredQueries(currentTime: Long = System.currentTimeMillis())

    @Query("DELETE FROM cached_queries WHERE id IN (SELECT id FROM cached_queries WHERE priority = 'LOW' ORDER BY lastAccessedAt ASC LIMIT :count)")
    suspend fun evictLowPriorityQueries(count: Int)

    @Delete
    suspend fun deleteCachedQuery(query: CachedQuery)
}
