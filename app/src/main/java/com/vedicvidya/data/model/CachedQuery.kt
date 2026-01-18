package com.vedicvidya.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Cache for query results to enable offline functionality
 * and reduce API costs
 */
@Entity(tableName = "cached_queries")
data class CachedQuery(
    @PrimaryKey
    val id: String,

    // Query
    val queryText: String,
    val queryHash: String,                   // For fast lookups

    // Response
    val answerId: String,                    // Reference to Answer entity

    // Metadata
    val cachedAt: Long = System.currentTimeMillis(),
    val accessCount: Int = 0,
    val lastAccessedAt: Long = System.currentTimeMillis(),

    // Cache management
    val expiresAt: Long = Long.MAX_VALUE,    // When to invalidate
    val priority: CachePriority = CachePriority.NORMAL
)

enum class CachePriority {
    HIGH,      // Never evict (user saved)
    NORMAL,    // Evict based on LRU
    LOW        // Evict first if space needed
}
