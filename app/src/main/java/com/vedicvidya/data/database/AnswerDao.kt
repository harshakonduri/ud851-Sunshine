package com.vedicvidya.data.database

import androidx.room.*
import com.vedicvidya.data.model.Answer
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Answers
 */
@Dao
interface AnswerDao {

    @Query("SELECT * FROM answers WHERE id = :answerId")
    suspend fun getAnswerById(answerId: String): Answer?

    @Query("SELECT * FROM answers WHERE questionId = :questionId ORDER BY confidence DESC")
    suspend fun getAnswersForQuestion(questionId: String): List<Answer>

    @Query("SELECT * FROM answers WHERE verified = 1 ORDER BY createdAt DESC LIMIT :limit")
    fun getVerifiedAnswers(limit: Int = 50): Flow<List<Answer>>

    @Query("SELECT * FROM answers ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentAnswers(limit: Int = 50): Flow<List<Answer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: Answer): Long

    @Update
    suspend fun updateAnswer(answer: Answer)

    @Query("UPDATE answers SET upvotes = upvotes + 1 WHERE id = :answerId")
    suspend fun upvoteAnswer(answerId: String)

    @Query("UPDATE answers SET verified = 1, verificationCount = verificationCount + 1 WHERE id = :answerId")
    suspend fun verifyAnswer(answerId: String)

    @Delete
    suspend fun deleteAnswer(answer: Answer)
}
