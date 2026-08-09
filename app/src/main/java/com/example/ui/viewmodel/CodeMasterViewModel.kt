package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.CareerRoadmap
import com.example.data.model.ChallengeDifficulty
import com.example.data.model.CodingChallenge
import com.example.data.model.LanguageInfo
import com.example.data.model.LessonItem
import com.example.data.model.ProjectItem
import com.example.data.model.QuizQuestion
import com.example.data.model.TypingLesson
import com.example.data.model.UserProfile
import com.example.data.repository.CodeMasterRepository
import com.example.data.repository.SeededData
import com.example.service.gemini.GeminiTutorService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatMessage(
    val sender: String, // "user" or "tutor"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class SearchResults(
    val languages: List<LanguageInfo> = emptyList(),
    val lessons: List<LessonItem> = emptyList(),
    val challenges: List<CodingChallenge> = emptyList(),
    val roadmaps: List<CareerRoadmap> = emptyList(),
    val projects: List<ProjectItem> = emptyList(),
    val typingLessons: List<TypingLesson> = emptyList()
)

class CodeMasterViewModel(val repository: CodeMasterRepository) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .combine(MutableStateFlow(Unit)) { profile, _ ->
            profile ?: UserProfile()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    val lessonProgress = repository.lessonProgressList.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val quizResults = repository.quizResults.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val challengeSubmissions = repository.challengeSubmissions.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val typingResults = repository.typingResults.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val roadmapProgress = repository.roadmapProgressList.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val projectProgress = repository.projectProgressList.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val unlockedAchievements = repository.unlockedAchievements.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val careerCourseProgress = repository.careerCourseProgressList.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    // Chat messages state for AI Tutor
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "tutor",
                text = "👋 Hi there! I'm your CodeMaster AI Tutor. Ask me any programming question, request code hints, or paste an error message!"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isTutorLoading = MutableStateFlow(false)
    val isTutorLoading: StateFlow<Boolean> = _isTutorLoading.asStateFlow()

    // Global Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: StateFlow<SearchResults> = _searchQuery
        .combine(MutableStateFlow(Unit)) { query, _ ->
            if (query.isBlank()) {
                SearchResults()
            } else {
                val q = query.lowercase().trim()
                SearchResults(
                    languages = SeededData.languages.filter { it.name.lowercase().contains(q) || it.description.lowercase().contains(q) },
                    lessons = SeededData.lessons.filter { it.title.lowercase().contains(q) || it.explanation.lowercase().contains(q) },
                    challenges = SeededData.challenges.filter { it.title.lowercase().contains(q) || it.problemStatement.lowercase().contains(q) },
                    roadmaps = SeededData.careerRoadmaps.filter { it.title.lowercase().contains(q) || it.description.lowercase().contains(q) },
                    projects = SeededData.projects.filter { it.title.lowercase().contains(q) || it.description.lowercase().contains(q) },
                    typingLessons = SeededData.typingLessons.filter { it.title.lowercase().contains(q) || it.description.lowercase().contains(q) }
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchResults())

    init {
        viewModelScope.launch {
            repository.ensureUserProfileCreated()
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun toggleLessonCompleted(lessonId: String, languageId: String, completed: Boolean) {
        viewModelScope.launch {
            repository.toggleLessonCompleted(lessonId, languageId, completed)
        }
    }

    fun saveQuizResult(languageId: String, score: Int, total: Int) {
        viewModelScope.launch {
            repository.saveQuizResult(languageId, score, total)
        }
    }

    fun submitChallengeCode(challengeId: String, code: String, isPassed: Boolean) {
        viewModelScope.launch {
            repository.submitChallengeCode(challengeId, code, isPassed)
        }
    }

    fun saveTypingResult(
        testType: String,
        durationSeconds: Int,
        wpm: Int,
        accuracy: Double,
        totalChars: Int,
        correctChars: Int,
        mistakes: Int
    ) {
        viewModelScope.launch {
            repository.saveTypingResult(
                testType, durationSeconds, wpm, accuracy, totalChars, correctChars, mistakes
            )
        }
    }

    fun toggleRoadmapStage(stageKey: String, roadmapId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleRoadmapStage(stageKey, roadmapId, isCompleted)
        }
    }

    fun toggleProjectTask(taskId: String, projectId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleProjectTask(taskId, projectId, isCompleted)
        }
    }

    fun saveCareerCourseProgress(
        moduleId: String,
        careerId: String,
        isCompleted: Boolean,
        quizScore: Int = 0,
        isProjectCompleted: Boolean = false
    ) {
        viewModelScope.launch {
            repository.saveCareerCourseProgress(
                moduleId = moduleId,
                careerId = careerId,
                isCompleted = isCompleted,
                quizScore = quizScore,
                isProjectCompleted = isProjectCompleted
            )
        }
    }

    fun sendTutorQuestion(prompt: String, contextInfo: String = "") {
        if (prompt.isBlank()) return
        val userMsg = ChatMessage(sender = "user", text = prompt)
        _chatMessages.value = _chatMessages.value + userMsg
        _isTutorLoading.value = true

        viewModelScope.launch {
            val reply = GeminiTutorService.getTutorResponse(prompt, contextInfo)
            _chatMessages.value = _chatMessages.value + ChatMessage(sender = "tutor", text = reply)
            _isTutorLoading.value = false
        }
    }

    suspend fun getAiAnswerForCodeLab(prompt: String): String {
        return GeminiTutorService.getTutorResponse(prompt, "User is in Code Lab IDE")
    }

    fun saveCodeLabProject(projectId: String, projectName: String, languageId: String, files: List<Pair<String, String>>) {
        viewModelScope.launch {
            repository.saveCodeLabProject(projectId, projectName, languageId, files)
        }
    }

    fun updateSettings(isDarkMode: Boolean, soundEnabled: Boolean, notificationsEnabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings(isDarkMode, soundEnabled, notificationsEnabled)
        }
    }

    fun updateProfile(name: String, avatarId: String, currentRoadmapId: String) {
        viewModelScope.launch {
            repository.updateProfile(name, avatarId, currentRoadmapId)
        }
    }

    fun loginUser(emailOrUsername: String) {
        viewModelScope.launch {
            repository.loginUser(emailOrUsername)
        }
    }

    fun signUpUser(fullName: String, username: String, email: String) {
        viewModelScope.launch {
            repository.signUpUser(fullName, username, email)
        }
    }

    fun setTargetCareer(careerTitle: String, roadmapId: String) {
        viewModelScope.launch {
            repository.setTargetCareer(careerTitle, roadmapId)
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            repository.logoutUser()
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
            _chatMessages.value = listOf(
                ChatMessage(
                    sender = "tutor",
                    text = "👋 Progress reset! Ready to start fresh. What would you like to learn today?"
                )
            )
        }
    }
}

class CodeMasterViewModelFactory(private val repository: CodeMasterRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodeMasterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CodeMasterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
