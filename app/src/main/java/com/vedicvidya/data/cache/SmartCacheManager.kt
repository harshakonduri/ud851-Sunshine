package com.vedicvidya.data.cache

import com.vedicvidya.data.model.StorageTier
import com.vedicvidya.data.model.VedicVerse
import java.util.concurrent.ConcurrentHashMap

/**
 * Smart cache manager with LRU eviction and priority-based retention
 *
 * Features:
 * - LRU (Least Recently Used) eviction
 * - Priority-based retention (favorites never evicted)
 * - Access frequency tracking
 * - Size-based limits
 */
class SmartCacheManager(
    private val maxSizeMB: Int = 200
) {

    private val cache = ConcurrentHashMap<String, CachedVerse>()
    private var currentSizeBytes: Long = 0

    /**
     * Put a verse in cache
     */
    fun put(verse: VedicVerse) {
        synchronized(this) {
            val cached = CachedVerse(
                verse = verse,
                accessCount = cache[verse.id]?.accessCount ?: 0,
                lastAccessed = System.currentTimeMillis(),
                priority = calculatePriority(verse)
            )

            // Add to cache
            val existing = cache.put(verse.id, cached)

            // Update size
            val verseSize = estimateVerseSize(verse)
            if (existing != null) {
                currentSizeBytes -= estimateVerseSize(existing.verse)
            }
            currentSizeBytes += verseSize

            // Evict if necessary
            while (currentSizeBytes > maxSizeMB * 1024 * 1024 && cache.size > 1) {
                evictOne()
            }
        }
    }

    /**
     * Get a verse from cache
     */
    fun get(verseId: String): VedicVerse? {
        val cached = cache[verseId] ?: return null

        synchronized(this) {
            // Update access tracking
            val updated = cached.copy(
                accessCount = cached.accessCount + 1,
                lastAccessed = System.currentTimeMillis()
            )
            cache[verseId] = updated
        }

        return cached.verse
    }

    /**
     * Check if verse is in cache
     */
    fun contains(verseId: String): Boolean {
        return cache.containsKey(verseId)
    }

    /**
     * Remove verse from cache
     */
    fun remove(verseId: String) {
        synchronized(this) {
            cache.remove(verseId)?.let { removed ->
                currentSizeBytes -= estimateVerseSize(removed.verse)
            }
        }
    }

    /**
     * Clear all cache
     */
    fun clear() {
        synchronized(this) {
            cache.clear()
            currentSizeBytes = 0
        }
    }

    /**
     * Get cache statistics
     */
    fun getStats(): CacheStats {
        return CacheStats(
            entryCount = cache.size,
            sizeMB = currentSizeBytes / (1024 * 1024).toFloat(),
            maxSizeMB = maxSizeMB.toFloat(),
            utilizationPercent = (currentSizeBytes.toFloat() / (maxSizeMB * 1024 * 1024)) * 100
        )
    }

    /**
     * Evict least valuable item based on priority and LRU
     */
    private fun evictOne() {
        val candidate = cache.values
            .filter { it.priority != CachePriority.NEVER_EVICT }
            .minByOrNull { calculateEvictionScore(it) }

        candidate?.let {
            cache.remove(it.verse.id)
            currentSizeBytes -= estimateVerseSize(it.verse)
        }
    }

    /**
     * Calculate eviction score (lower = evict first)
     * Factors: recency, frequency, priority
     */
    private fun calculateEvictionScore(cached: CachedVerse): Float {
        val recencyScore = (System.currentTimeMillis() - cached.lastAccessed) / (1000 * 60 * 60 * 24f) // Days
        val frequencyScore = cached.accessCount.toFloat()
        val priorityWeight = when (cached.priority) {
            CachePriority.NEVER_EVICT -> Float.MAX_VALUE
            CachePriority.HIGH -> 10f
            CachePriority.NORMAL -> 1f
            CachePriority.LOW -> 0.1f
        }

        // Lower score = more likely to evict
        return (frequencyScore * priorityWeight) / (recencyScore + 1)
    }

    /**
     * Calculate priority based on verse characteristics
     */
    private fun calculatePriority(verse: VedicVerse): CachePriority {
        return when {
            verse.isFavorite -> CachePriority.NEVER_EVICT
            verse.storageTier == StorageTier.HOT -> CachePriority.HIGH
            verse.accessCount > 10 -> CachePriority.HIGH
            verse.storageTier == StorageTier.WARM -> CachePriority.NORMAL
            else -> CachePriority.LOW
        }
    }

    /**
     * Estimate memory size of a verse
     */
    private fun estimateVerseSize(verse: VedicVerse): Long {
        var size = 0L

        // Text content
        size += verse.sanskrit.length * 2L // UTF-16 chars
        size += verse.transliteration.length * 2L
        size += verse.translation.length * 2L
        size += (verse.commentary?.length ?: 0) * 2L

        // Embedding
        size += verse.embeddingDimension * 4L // Float = 4 bytes

        // Other fields (rough estimate)
        size += 500L

        return size
    }
}

data class CachedVerse(
    val verse: VedicVerse,
    val accessCount: Int,
    val lastAccessed: Long,
    val priority: CachePriority
)

enum class CachePriority {
    NEVER_EVICT,   // User favorites, HOT tier
    HIGH,          // Frequently accessed
    NORMAL,        // Standard caching
    LOW            // Rarely accessed, evict first
}

data class CacheStats(
    val entryCount: Int,
    val sizeMB: Float,
    val maxSizeMB: Float,
    val utilizationPercent: Float
)
