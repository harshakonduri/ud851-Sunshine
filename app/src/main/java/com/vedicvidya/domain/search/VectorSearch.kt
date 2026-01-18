package com.vedicvidya.domain.search

import com.vedicvidya.data.model.TextSummary
import com.vedicvidya.data.model.VedicVerse
import kotlin.math.sqrt

/**
 * Vector-based semantic search engine for verses and summaries
 * Uses cosine similarity for finding semantically similar content
 */
class VectorSearchEngine {

    /**
     * Search for similar verses using cosine similarity
     *
     * @param queryEmbedding The embedding vector of the search query
     * @param verses List of verses to search through
     * @param topK Number of top results to return
     * @return List of verses sorted by similarity (descending)
     */
    fun searchVerses(
        queryEmbedding: FloatArray,
        verses: List<VedicVerse>,
        topK: Int = 10
    ): List<SearchResult<VedicVerse>> {
        return verses
            .map { verse ->
                val similarity = cosineSimilarity(
                    queryEmbedding,
                    verse.getEmbeddingArray()
                )
                SearchResult(verse, similarity)
            }
            .sortedByDescending { it.score }
            .take(topK)
    }

    /**
     * Search for relevant text summaries (Tier 1 - coarse search)
     *
     * @param queryEmbedding The embedding vector of the search query
     * @param summaries List of summaries to search through
     * @param topK Number of top results to return
     * @return List of summaries sorted by similarity
     */
    fun searchSummaries(
        queryEmbedding: FloatArray,
        summaries: List<TextSummary>,
        topK: Int = 5
    ): List<SearchResult<TextSummary>> {
        return summaries
            .map { summary ->
                val similarity = cosineSimilarity(
                    queryEmbedding,
                    summary.getEmbeddingArray()
                )
                SearchResult(summary, similarity)
            }
            .sortedByDescending { it.score }
            .take(topK)
    }

    /**
     * Calculate cosine similarity between two vectors
     * Returns a score between -1 and 1 (typically 0 to 1 for text embeddings)
     */
    fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
        require(a.size == b.size) { "Vectors must have same dimension: ${a.size} vs ${b.size}" }

        var dotProduct = 0.0
        var magnitudeA = 0.0
        var magnitudeB = 0.0

        for (i in a.indices) {
            dotProduct += a[i] * b[i]
            magnitudeA += a[i] * a[i]
            magnitudeB += b[i] * b[i]
        }

        magnitudeA = sqrt(magnitudeA)
        magnitudeB = sqrt(magnitudeB)

        return if (magnitudeA == 0.0 || magnitudeB == 0.0) {
            0f
        } else {
            (dotProduct / (magnitudeA * magnitudeB)).toFloat()
        }
    }

    /**
     * Calculate Euclidean distance between two vectors
     * Lower distance = more similar
     */
    fun euclideanDistance(a: FloatArray, b: FloatArray): Float {
        require(a.size == b.size) { "Vectors must have same dimension" }

        var sum = 0.0
        for (i in a.indices) {
            val diff = a[i] - b[i]
            sum += diff * diff
        }

        return sqrt(sum).toFloat()
    }

    /**
     * Normalize a vector to unit length
     */
    fun normalize(vector: FloatArray): FloatArray {
        var magnitude = 0.0
        for (value in vector) {
            magnitude += value * value
        }
        magnitude = sqrt(magnitude)

        return if (magnitude == 0.0) {
            vector
        } else {
            FloatArray(vector.size) { i -> (vector[i] / magnitude).toFloat() }
        }
    }
}

/**
 * Search result with similarity score
 */
data class SearchResult<T>(
    val item: T,
    val score: Float
) {
    /**
     * Check if this result meets minimum relevance threshold
     */
    fun isRelevant(threshold: Float = 0.7f): Boolean {
        return score >= threshold
    }
}
