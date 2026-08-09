package com.example.data.repository

import com.example.data.db.CodeMasterDao
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
import kotlinx.coroutines.flow.firstOrNull

class CodeMasterRepository(private val dao: CodeMasterDao) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val lessonProgressList: Flow<List<LessonProgress>> = dao.getAllLessonProgress()
    val quizResults: Flow<List<QuizResult>> = dao.getAllQuizResults()
    val challengeSubmissions: Flow<List<ChallengeSubmission>> = dao.getAllChallengeSubmissions()
    val typingResults: Flow<List<TypingResult>> = dao.getAllTypingResults()
    val roadmapProgressList: Flow<List<RoadmapProgress>> = dao.getAllRoadmapProgress()
    val projectProgressList: Flow<List<ProjectProgress>> = dao.getAllProjectProgress()
    val unlockedAchievements: Flow<List<UnlockedAchievement>> = dao.getAllUnlockedAchievements()
    val careerCourseProgressList: Flow<List<CareerCourseProgress>> = dao.getAllCareerCourseProgress()

    suspend fun ensureUserProfileCreated() {
        val current = dao.getUserProfile().firstOrNull()
        if (current == null) {
            dao.insertOrUpdateUserProfile(UserProfile())
        }
    }

    suspend fun addXp(amount: Int) {
        val current = dao.getUserProfile().firstOrNull() ?: UserProfile()
        val newXp = current.xp + amount
        val updated = current.copy(xp = newXp)
        dao.insertOrUpdateUserProfile(updated)
        checkBadges(updated)
    }

    suspend fun toggleLessonCompleted(lessonId: String, languageId: String, completed: Boolean) {
        dao.markLessonProgress(
            LessonProgress(
                lessonId = lessonId,
                languageId = languageId,
                isCompleted = completed,
                completedTimestamp = if (completed) System.currentTimeMillis() else 0
            )
        )
        if (completed) {
            addXp(10)
        }
    }

    suspend fun saveQuizResult(languageId: String, score: Int, total: Int) {
        val xpEarned = score * 10
        dao.insertQuizResult(
            QuizResult(
                languageId = languageId,
                score = score,
                totalQuestions = total,
                xpEarned = xpEarned
            )
        )
        addXp(xpEarned)
    }

    suspend fun submitChallengeCode(challengeId: String, code: String, isPassed: Boolean) {
        dao.insertChallengeSubmission(
            ChallengeSubmission(
                challengeId = challengeId,
                userCode = code,
                isPassed = isPassed,
                passedTimestamp = if (isPassed) System.currentTimeMillis() else 0
            )
        )
        if (isPassed) {
            addXp(25)
        }
    }

    suspend fun saveTypingResult(
        testType: String,
        durationSeconds: Int,
        wpm: Int,
        accuracy: Double,
        totalChars: Int,
        correctChars: Int,
        mistakes: Int
    ) {
        dao.insertTypingResult(
            TypingResult(
                testType = testType,
                durationSeconds = durationSeconds,
                wpm = wpm,
                accuracy = accuracy,
                totalChars = totalChars,
                correctChars = correctChars,
                mistakes = mistakes
            )
        )
        val xpReward = if (testType == "code_snippet") 20 else 10
        addXp(xpReward)
    }

    suspend fun toggleRoadmapStage(stageKey: String, roadmapId: String, isCompleted: Boolean) {
        dao.markRoadmapProgress(
            RoadmapProgress(
                stageKey = stageKey,
                roadmapId = roadmapId,
                isCompleted = isCompleted
            )
        )
        if (isCompleted) {
            addXp(15)
        }
    }

    suspend fun toggleProjectTask(taskId: String, projectId: String, isCompleted: Boolean) {
        dao.markProjectProgress(
            ProjectProgress(
                projectTaskId = taskId,
                projectId = projectId,
                isCompleted = isCompleted
            )
        )
        if (isCompleted) {
            addXp(20)
        }
    }

    suspend fun saveCareerCourseProgress(
        moduleId: String,
        careerId: String,
        isCompleted: Boolean,
        quizScore: Int = 0,
        isProjectCompleted: Boolean = false
    ) {
        dao.markCareerCourseProgress(
            CareerCourseProgress(
                moduleId = moduleId,
                careerId = careerId,
                isCompleted = isCompleted,
                quizScore = quizScore,
                isProjectCompleted = isProjectCompleted,
                completedTimestamp = System.currentTimeMillis()
            )
        )
        if (isCompleted) {
            addXp(30)
        }
    }

    suspend fun updateSettings(isDarkMode: Boolean, soundEnabled: Boolean, notificationsEnabled: Boolean) {
        val current = dao.getUserProfile().firstOrNull() ?: UserProfile()
        dao.insertOrUpdateUserProfile(
            current.copy(
                isDarkMode = isDarkMode,
                soundEnabled = soundEnabled,
                notificationsEnabled = notificationsEnabled
            )
        )
    }

    suspend fun updateProfile(name: String, avatarId: String, currentRoadmapId: String) {
        val current = dao.getUserProfile().firstOrNull() ?: UserProfile()
        dao.insertOrUpdateUserProfile(
            current.copy(
                name = name,
                avatarId = avatarId,
                currentRoadmapId = currentRoadmapId
            )
        )
    }

    suspend fun loginUser(emailOrUsername: String) {
        val current = dao.getUserProfile().firstOrNull() ?: UserProfile()
        val uname = emailOrUsername.split("@").firstOrNull() ?: emailOrUsername
        val email = if (emailOrUsername.contains("@")) emailOrUsername else "$emailOrUsername@codemaster.dev"
        val displayName = if (current.name.isBlank() || current.name == "CodeMaster Student") uname.replaceFirstChar { it.uppercase() } else current.name
        dao.insertOrUpdateUserProfile(
            current.copy(
                username = uname,
                email = email,
                name = displayName,
                isLoggedIn = true
            )
        )
    }

    suspend fun signUpUser(fullName: String, username: String, email: String) {
        val current = dao.getUserProfile().firstOrNull() ?: UserProfile()
        dao.insertOrUpdateUserProfile(
            current.copy(
                name = fullName.ifBlank { username },
                username = username,
                email = email,
                isLoggedIn = true
            )
        )
    }

    suspend fun setTargetCareer(careerTitle: String, roadmapId: String) {
        val current = dao.getUserProfile().firstOrNull() ?: UserProfile()
        dao.insertOrUpdateUserProfile(
            current.copy(
                targetCareer = careerTitle,
                currentRoadmapId = roadmapId
            )
        )
    }

    suspend fun logoutUser() {
        val current = dao.getUserProfile().firstOrNull() ?: UserProfile()
        dao.insertOrUpdateUserProfile(
            current.copy(
                isLoggedIn = false
            )
        )
    }

    suspend fun checkBadges(user: UserProfile) {
        // Unlock badges based on XP or milestones
        if (user.xp >= 10) unlockBadge("badge_first_lesson")
        if (user.streak >= 7) unlockBadge("badge_7_day_streak")
        if (user.xp >= 200) unlockBadge("badge_first_challenge")
        if (user.xp >= 500) unlockBadge("badge_40_wpm")
        if (user.xp >= 800) unlockBadge("badge_python_beg")
        if (user.xp >= 1000) unlockBadge("badge_first_project")
    }

    private suspend fun unlockBadge(badgeId: String) {
        dao.unlockAchievement(UnlockedAchievement(badgeId = badgeId))
    }

    suspend fun saveCodeLabProject(
        projectId: String,
        projectName: String,
        languageId: String,
        files: List<Pair<String, String>> // fileName to content
    ) {
        dao.insertCodeLabProject(
            CodeLabProjectEntity(
                id = projectId,
                name = projectName,
                languageId = languageId,
                updatedAt = System.currentTimeMillis()
            )
        )
        for ((fileName, content) in files) {
            dao.insertCodeLabFile(
                CodeLabFileEntity(
                    id = "${projectId}_$fileName",
                    projectId = projectId,
                    name = fileName,
                    content = content,
                    languageId = languageId,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun resetAllProgress() {
        dao.clearLessonProgress()
        dao.clearQuizResults()
        dao.clearChallengeSubmissions()
        dao.clearTypingResults()
        dao.clearRoadmapProgress()
        dao.clearProjectProgress()
        dao.clearAchievements()
        dao.clearCareerCourseProgress()
        dao.insertOrUpdateUserProfile(UserProfile())
    }
}
