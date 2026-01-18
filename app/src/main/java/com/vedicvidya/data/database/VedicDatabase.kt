package com.vedicvidya.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vedicvidya.data.model.*

/**
 * Main Room database for Vedic Vidya app
 *
 * Architecture:
 * - Tier 1 (HOT): TextSummary - Always loaded, fast coarse search
 * - Tier 2 (WARM): VedicVerse (popular) - Cached, frequently accessed
 * - Tier 3 (COLD): VedicVerse (rare) - Fetched from cloud on demand
 */
@Database(
    entities = [
        VedicVerse::class,
        TextSummary::class,
        VedicText::class,
        Question::class,
        Answer::class,
        CachedQuery::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class VedicDatabase : RoomDatabase() {

    abstract fun vedicVerseDao(): VedicVerseDao
    abstract fun textSummaryDao(): TextSummaryDao
    abstract fun vedicTextDao(): VedicTextDao
    abstract fun questionDao(): QuestionDao
    abstract fun answerDao(): AnswerDao
    abstract fun cachedQueryDao(): CachedQueryDao

    companion object {
        const val DATABASE_NAME = "vedic_vidya.db"
    }
}
