package com.vedicvidya.di

import com.vedicvidya.data.cache.SmartCacheManager
import com.vedicvidya.data.database.VedicDatabase
import com.vedicvidya.domain.agent.MockVedicAgent
import com.vedicvidya.domain.agent.VedicAgent
import com.vedicvidya.domain.intelligence.QueryAnalyzer
import com.vedicvidya.domain.search.EmbeddingGenerator
import com.vedicvidya.domain.search.MockEmbeddingGenerator
import com.vedicvidya.domain.search.VectorSearchEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Main application module for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideVectorSearchEngine(): VectorSearchEngine {
        return VectorSearchEngine()
    }

    @Provides
    @Singleton
    fun provideQueryAnalyzer(): QueryAnalyzer {
        return QueryAnalyzer()
    }

    @Provides
    @Singleton
    fun provideEmbeddingGenerator(): EmbeddingGenerator {
        // For demo, use mock generator
        // In production, replace with CloudEmbeddingGenerator or PrecomputedEmbeddingProvider
        return MockEmbeddingGenerator(dimension = 128)
    }

    @Provides
    @Singleton
    fun provideSmartCacheManager(): SmartCacheManager {
        return SmartCacheManager(maxSizeMB = 200)
    }

    @Provides
    @Singleton
    fun provideVedicAgent(
        database: VedicDatabase,
        searchEngine: VectorSearchEngine,
        queryAnalyzer: QueryAnalyzer
    ): VedicAgent {
        // For demo, use MockVedicAgent
        // To enable cloud: return CloudVedicAgent(database, searchEngine, embeddingGenerator, queryAnalyzer, apiKey)
        return MockVedicAgent(database, searchEngine, queryAnalyzer)
    }
}
