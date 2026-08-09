package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "CodeMaster Student",
    val username: String = "codemaster",
    val email: String = "student@codemaster.dev",
    val avatarId: String = "avatar_1",
    val xp: Int = 150,
    val streak: Int = 5,
    val lastActiveTimestamp: Long = System.currentTimeMillis(),
    val isDarkMode: Boolean = true,
    val soundEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val currentRoadmapId: String = "software_dev",
    val targetCareer: String = "Software Developer",
    val isLoggedIn: Boolean = false
)

@Entity(tableName = "lesson_progress")
data class LessonProgress(
    @PrimaryKey val lessonId: String, // e.g. "python_intro", "c_pointers"
    val languageId: String,
    val isCompleted: Boolean = false,
    val completedTimestamp: Long = 0
)

@Entity(tableName = "quiz_result")
data class QuizResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val languageId: String,
    val score: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "challenge_submission")
data class ChallengeSubmission(
    @PrimaryKey val challengeId: String,
    val userCode: String,
    val isPassed: Boolean = false,
    val passedTimestamp: Long = 0
)

@Entity(tableName = "typing_result")
data class TypingResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val testType: String, // "general", "code_snippet"
    val durationSeconds: Int,
    val wpm: Int,
    val accuracy: Double,
    val totalChars: Int,
    val correctChars: Int,
    val mistakes: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "roadmap_progress")
data class RoadmapProgress(
    @PrimaryKey val stageKey: String, // e.g. "software_dev_stage_1"
    val roadmapId: String,
    val isCompleted: Boolean = false
)

@Entity(tableName = "project_progress")
data class ProjectProgress(
    @PrimaryKey val projectTaskId: String, // e.g. "calculator_task_1"
    val projectId: String,
    val isCompleted: Boolean = false
)

@Entity(tableName = "unlocked_achievement")
data class UnlockedAchievement(
    @PrimaryKey val badgeId: String,
    val unlockedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "career_course_progress")
data class CareerCourseProgress(
    @PrimaryKey val moduleId: String, // e.g. "se_mod_1", "web_mod_1"
    val careerId: String,             // e.g. "software_engineer", "web_developer"
    val isCompleted: Boolean = false,
    val quizScore: Int = 0,
    val isProjectCompleted: Boolean = false,
    val completedTimestamp: Long = 0
)

@Entity(tableName = "codelab_projects")
data class CodeLabProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val languageId: String,
    val description: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "codelab_files")
data class CodeLabFileEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val name: String,
    val content: String,
    val languageId: String,
    val isDirectory: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)

// UI Data Models for dynamic curriculum
data class LanguageInfo(
    val id: String,
    val name: String,
    val iconRes: String,
    val description: String,
    val colorHex: String,
    val category: String
)

data class LessonItem(
    val id: String,
    val languageId: String,
    val title: String,
    val tier: String, // "Beginner", "Intermediate", "Advanced"
    val explanation: String,
    val concepts: List<String>,
    val syntaxSnippet: String,
    val codeExample: String,
    val expectedOutput: String,
    val commonMistakes: String,
    val practiceQuestion: String,
    val miniChallenge: String
)

data class QuizQuestion(
    val id: String,
    val languageId: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

enum class ChallengeDifficulty { EASY, MEDIUM, HARD }

data class CodingChallenge(
    val id: String,
    val title: String,
    val languageId: String,
    val difficulty: ChallengeDifficulty,
    val problemStatement: String,
    val exampleInput: String,
    val exampleOutput: String,
    val hint: String,
    val starterCode: String,
    val expectedKeywords: List<String>
)

data class TypingLesson(
    val id: String,
    val title: String,
    val level: String, // "Beginner", "Intermediate", "Programming"
    val description: String,
    val targetKeys: String,
    val practicePassage: String,
    val isCodeMode: Boolean = false
)

data class CareerStage(
    val stageNumber: Int,
    val key: String,
    val title: String,
    val description: String,
    val recommendedLanguages: List<String>,
    val tools: List<String>,
    val concepts: List<String>,
    val projects: List<String>
)

data class CareerRoadmap(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val stages: List<CareerStage>
)

data class ProjectTask(
    val id: String,
    val title: String,
    val description: String
)

data class ProjectItem(
    val id: String,
    val title: String,
    val category: String, // "Beginner", "Intermediate", "Advanced"
    val description: String,
    val requiredSkills: List<String>,
    val technologies: List<String>,
    val tasks: List<ProjectTask>
)

data class BadgeItem(
    val id: String,
    val name: String,
    val description: String,
    val iconSymbol: String,
    val requiredXpOrAction: String
)

data class PracticeQuestionItem(
    val question: String,
    val answer: String
)

data class CareerCourseModule(
    val id: String,                  // e.g. "se_mod_1"
    val careerId: String,            // e.g. "software_engineer"
    val orderNumber: Int,            // 1 to 18
    val title: String,               // e.g. "Computer & Software Basics"
    val tier: String,                // "Beginner", "Intermediate", "Advanced"
    val description: String,
    val clearTheory: String,         // Detailed theory & beginner friendly explanation
    val codeExamples: List<String>,  // Snippets
    val practiceQuestions: List<PracticeQuestionItem>,
    val codingExercisePrompt: String,
    val starterCode: String,
    val solutionKeyword: String,     // Expected keyword check
    val quizQuestions: List<QuizQuestion>,
    val miniProjectTitle: String,
    val miniProjectDescription: String,
    val miniProjectTasks: List<String>
)

data class CareerCourse(
    val id: String,                  // e.g. "software_engineer"
    val title: String,               // e.g. "Software Engineer"
    val category: String,            // "Engineering & Architecture"
    val iconEmoji: String,           // "👨‍💻"
    val description: String,
    val modules: List<CareerCourseModule>
)
