package com.vedicvidya.di

import android.content.Context
import androidx.room.Room
import com.vedicvidya.data.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Hilt module for database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideDatabaseCallback(
        scope: CoroutineScope
    ): DatabaseCallback {
        return DatabaseCallback(scope)
    }

    @Provides
    @Singleton
    fun provideVedicDatabase(
        @ApplicationContext context: Context,
        callback: DatabaseCallback
    ): VedicDatabase {
        val database = Room.databaseBuilder(
            context,
            VedicDatabase::class.java,
            VedicDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

        // Populate database with sample data on first run
        callback.populateDatabase(database)

        return database
    }

    @Provides
    fun provideVedicVerseDao(database: VedicDatabase): VedicVerseDao {
        return database.vedicVerseDao()
    }

    @Provides
    fun provideTextSummaryDao(database: VedicDatabase): TextSummaryDao {
        return database.textSummaryDao()
    }

    @Provides
    fun provideVedicTextDao(database: VedicDatabase): VedicTextDao {
        return database.vedicTextDao()
    }

    @Provides
    fun provideQuestionDao(database: VedicDatabase): QuestionDao {
        return database.questionDao()
    }

    @Provides
    fun provideAnswerDao(database: VedicDatabase): AnswerDao {
        return database.answerDao()
    }

    @Provides
    fun provideCachedQueryDao(database: VedicDatabase): CachedQueryDao {
        return database.cachedQueryDao()
    }
}
