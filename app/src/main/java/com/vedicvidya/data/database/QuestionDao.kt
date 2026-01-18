package com.vedicvidya.data.database

import androidx.room.*
import com.vedicvidya.data.model.Question
import com.vedicvidya.data.model.QuestionCategory
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Questions
 */
@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getQuestionById(questionId: String): Question?

    @Query("SELECT * FROM questions ORDER BY askedAt DESC LIMIT :limit")
    fun getRecentQuestions(limit: Int = 50): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE isPopular = 1 ORDER BY viewCount DESC LIMIT :limit")
    fun getPopularQuestions(limit: Int = 50): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE category = :category ORDER BY askedAt DESC")
    fun getQuestionsByCategory(category: QuestionCategory): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE hasAnswer = 1 ORDER BY askedAt DESC")
    fun getAnsweredQuestions(): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE hasAnswer = 0 ORDER BY askedAt DESC")
    fun getUnansweredQuestions(): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question): Long

    @Update
    suspend fun updateQuestion(question: Question)

    @Query("UPDATE questions SET viewCount = viewCount + 1 WHERE id = :questionId")
    suspend fun incrementViewCount(questionId: String)

    @Query("UPDATE questions SET hasAnswer = 1, answerId = :answerId WHERE id = :questionId")
    suspend fun markAsAnswered(questionId: String, answerId: String)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("SELECT * FROM questions WHERE questionText LIKE '%' || :query || '%' LIMIT :limit")
    suspend fun searchQuestions(query: String, limit: Int = 20): List<Question>
}
