package com.vedicvidya.domain.search

/**
 * Interface for generating embeddings from text
 * Can be implemented with:
 * - Cloud API (OpenAI, Cohere, etc.)
 * - On-device model (ONNX, TFLite)
 * - Pre-computed embeddings (for static content)
 */
interface EmbeddingGenerator {

    /**
     * Generate embedding vector for text
     *
     * @param text Input text to embed
     * @return Embedding vector (dimension depends on implementation)
     */
    suspend fun generateEmbedding(text: String): FloatArray

    /**
     * Get the dimension of embeddings produced by this generator
     */
    fun getDimension(): Int

    /**
     * Batch generate embeddings (more efficient)
     */
    suspend fun generateEmbeddings(texts: List<String>): List<FloatArray> {
        return texts.map { generateEmbedding(it) }
    }
}

/**
 * Mock implementation for testing and demo purposes
 * Generates simple hash-based embeddings
 */
class MockEmbeddingGenerator(
    private val dimension: Int = 128
) : EmbeddingGenerator {

    override suspend fun generateEmbedding(text: String): FloatArray {
        // Simple hash-based embedding for demo
        // In production, use real embedding model
        val hash = text.lowercase().hashCode()
        val random = java.util.Random(hash.toLong())

        return FloatArray(dimension) {
            random.nextFloat() * 2f - 1f  // Values between -1 and 1
        }
    }

    override fun getDimension(): Int = dimension
}

/**
 * Pre-computed embedding provider for static content
 * Embeddings are generated offline and stored in the database
 */
class PrecomputedEmbeddingProvider : EmbeddingGenerator {

    override suspend fun generateEmbedding(text: String): FloatArray {
        // For Vedic texts, embeddings are pre-computed and stored
        // This should not be called during runtime
        throw UnsupportedOperationException(
            "Embeddings are pre-computed. Use database to retrieve."
        )
    }

    override fun getDimension(): Int = 128

    companion object {
        /**
         * Parse embedding string from database (comma-separated values)
         */
        fun parseEmbedding(embeddingString: String): FloatArray {
            return embeddingString.split(",")
                .map { it.trim().toFloat() }
                .toFloatArray()
        }

        /**
         * Serialize embedding for database storage
         */
        fun serializeEmbedding(embedding: FloatArray): String {
            return embedding.joinToString(",")
        }
    }
}

/**
 * Cloud-based embedding generator (placeholder for API integration)
 */
class CloudEmbeddingGenerator(
    private val apiKey: String,
    private val dimension: Int = 384
) : EmbeddingGenerator {

    override suspend fun generateEmbedding(text: String): FloatArray {
        // TODO: Implement API call to embedding service
        // Options:
        // - OpenAI text-embedding-ada-002 (1536 dim)
        // - Cohere embed-english-v3.0 (1024 dim)
        // - Voyage AI voyage-2 (1024 dim)
        // - Google PaLM embeddings

        // For now, return mock
        return MockEmbeddingGenerator(dimension).generateEmbedding(text)
    }

    override fun getDimension(): Int = dimension
}
