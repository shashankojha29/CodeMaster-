package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ChallengeSubmission
import com.example.data.model.CareerCourseProgress
import com.example.data.model.CodeLabFileEntity
import com.example.data.model.CodeLabProjectEntity
import com.example.data.model.LessonProgress
import com.example.data.model.ProjectProgress
import com.example.data.model.QuizResult
import com.example.data.model.RoadmapProgress
import com.example.data.model.TypingResult
import com.example.data.model.UnlockedAchievement
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface CodeMasterDao {

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserProfile(profile: UserProfile)

    @Update
    suspend fun updateUserProfile(profile: UserProfile)

    // Lesson Progress
    @Query("SELECT * FROM lesson_progress")
    fun getAllLessonProgress(): Flow<List<LessonProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markLessonProgress(progress: LessonProgress)

    // Quiz Results
    @Query("SELECT * FROM quiz_result ORDER BY timestamp DESC")
    fun getAllQuizResults(): Flow<List<QuizResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResult)

    // Challenge Submissions
    @Query("SELECT * FROM challenge_submission")
    fun getAllChallengeSubmissions(): Flow<List<ChallengeSubmission>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallengeSubmission(submission: ChallengeSubmission)

    // Typing Results
    @Query("SELECT * FROM typing_result ORDER BY timestamp DESC")
    fun getAllTypingResults(): Flow<List<TypingResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTypingResult(result: TypingResult)

    // Roadmap Progress
    @Query("SELECT * FROM roadmap_progress")
    fun getAllRoadmapProgress(): Flow<List<RoadmapProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markRoadmapProgress(progress: RoadmapProgress)

    // Project Progress
    @Query("SELECT * FROM project_progress")
    fun getAllProjectProgress(): Flow<List<ProjectProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markProjectProgress(progress: ProjectProgress)

    // Achievements
    @Query("SELECT * FROM unlocked_achievement")
    fun getAllUnlockedAchievements(): Flow<List<UnlockedAchievement>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockAchievement(achievement: UnlockedAchievement)

    // Career Course Progress
    @Query("SELECT * FROM career_course_progress")
    fun getAllCareerCourseProgress(): Flow<List<CareerCourseProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markCareerCourseProgress(progress: CareerCourseProgress)

    // CodeLab Projects & Files
    @Query("SELECT * FROM codelab_projects ORDER BY updatedAt DESC")
    fun getAllCodeLabProjects(): Flow<List<CodeLabProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCodeLabProject(project: CodeLabProjectEntity)

    @Query("SELECT * FROM codelab_files WHERE projectId = :projectId")
    fun getCodeLabFilesForProject(projectId: String): Flow<List<CodeLabFileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCodeLabFile(file: CodeLabFileEntity)

    // Reset Progress
    @Query("DELETE FROM lesson_progress")
    suspend fun clearLessonProgress()

    @Query("DELETE FROM quiz_result")
    suspend fun clearQuizResults()

    @Query("DELETE FROM challenge_submission")
    suspend fun clearChallengeSubmissions()

    @Query("DELETE FROM typing_result")
    suspend fun clearTypingResults()

    @Query("DELETE FROM roadmap_progress")
    suspend fun clearRoadmapProgress()

    @Query("DELETE FROM project_progress")
    suspend fun clearProjectProgress()

    @Query("DELETE FROM unlocked_achievement")
    suspend fun clearAchievements()

    @Query("DELETE FROM career_course_progress")
    suspend fun clearCareerCourseProgress()
}
