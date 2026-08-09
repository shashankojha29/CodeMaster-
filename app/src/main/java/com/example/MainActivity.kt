package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.db.CodeMasterDatabase
import com.example.data.repository.CodeMasterRepository
import com.example.ui.codelab.CodeLabScreen
import com.example.ui.components.CodeMasterBottomNavigation
import com.example.ui.components.TopBarHeader
import com.example.ui.screens.AiTutorScreen
import com.example.ui.screens.CareerSelectionScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.LessonDetailScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressDashboardScreen
import com.example.ui.screens.ProjectLabScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.RoadmapsScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SignUpScreen
import com.example.ui.screens.TypingScreen
import com.example.ui.theme.CodeMasterTheme
import com.example.ui.viewmodel.CodeMasterViewModel
import com.example.ui.viewmodel.CodeMasterViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = CodeMasterDatabase.getInstance(applicationContext)
        val repository = CodeMasterRepository(db.codeMasterDao())
        val viewModelFactory = CodeMasterViewModelFactory(repository)

        setContent {
            val viewModel: CodeMasterViewModel = viewModel(factory = viewModelFactory)
            val profile by viewModel.userProfile.collectAsStateWithLifecycle()

            CodeMasterTheme(darkTheme = profile.isDarkMode) {
                CodeMasterApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CodeMasterApp(viewModel: CodeMasterViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rawRoute = navBackStackEntry?.destination?.route ?: "home"
    val baseRoute = rawRoute.substringBefore("?").substringBefore("/")

    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val lessonProgress by viewModel.lessonProgress.collectAsStateWithLifecycle()
    val quizResults by viewModel.quizResults.collectAsStateWithLifecycle()
    val challengeSubmissions by viewModel.challengeSubmissions.collectAsStateWithLifecycle()
    val typingResults by viewModel.typingResults.collectAsStateWithLifecycle()
    val roadmapProgress by viewModel.roadmapProgress.collectAsStateWithLifecycle()
    val projectProgress by viewModel.projectProgress.collectAsStateWithLifecycle()
    val unlockedAchievements by viewModel.unlockedAchievements.collectAsStateWithLifecycle()
    val careerCourseProgress by viewModel.careerCourseProgress.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isTutorLoading by viewModel.isTutorLoading.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    val showBottomBar = baseRoute in listOf("home", "learn", "practice", "code_lab", "typing", "roadmaps", "profile")
    val startDestination = if (profile.isLoggedIn) "home" else "login"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showBottomBar) {
                TopBarHeader(
                    profile = profile,
                    onSearchClick = { navController.navigate("search") },
                    onAiTutorClick = { navController.navigate("ai_tutor") },
                    onProfileClick = { navController.navigate("profile") }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                CodeMasterBottomNavigation(
                    currentRoute = baseRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { emailOrUsername ->
                        viewModel.loginUser(emailOrUsername)
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = {
                        navController.navigate("signup")
                    }
                )
            }

            composable("signup") {
                SignUpScreen(
                    onSignUpSuccess = { fullName, username, email ->
                        viewModel.signUpUser(fullName, username, email)
                        navController.navigate("career_selection") {
                            popUpTo("signup") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo("signup") { inclusive = true }
                        }
                    }
                )
            }

            composable("career_selection") {
                CareerSelectionScreen(
                    currentSelectedCareer = profile.targetCareer,
                    onCareerSelected = { careerTitle, roadmapId ->
                        viewModel.setTargetCareer(careerTitle, roadmapId)
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                HomeScreen(
                    profile = profile,
                    lessonProgress = lessonProgress,
                    typingResults = typingResults,
                    onNavigate = { route -> navController.navigate(route) },
                    onSelectLanguage = { langId -> navController.navigate("learn?langId=$langId") },
                    onSelectChallenge = { chalId -> navController.navigate("practice?chalId=$chalId") }
                )
            }

            composable(
                route = "learn?langId={langId}",
                arguments = listOf(navArgument("langId") {
                    type = NavType.StringType
                    defaultValue = "python"
                })
            ) { backStack ->
                val selectedLang = backStack.arguments?.getString("langId") ?: "python"
                LearnScreen(
                    selectedLanguageId = selectedLang,
                    lessonProgress = lessonProgress,
                    currentCareerTitle = profile.targetCareer,
                    careerCourseProgress = careerCourseProgress,
                    onSelectLanguage = { langId -> navController.navigate("learn?langId=$langId") },
                    onSelectLesson = { lessonId -> navController.navigate("lesson_detail/$lessonId") },
                    onStartQuiz = { langId -> navController.navigate("quiz/$langId") },
                    onSaveCareerProgress = { moduleId, careerId, isCompleted, quizScore, isProjectCompleted ->
                        viewModel.saveCareerCourseProgress(moduleId, careerId, isCompleted, quizScore, isProjectCompleted)
                    }
                )
            }

            composable("lesson_detail/{lessonId}") { backStack ->
                val lessonId = backStack.arguments?.getString("lessonId") ?: "py_intro"
                LessonDetailScreen(
                    lessonId = lessonId,
                    lessonProgress = lessonProgress,
                    onToggleCompleted = { lId, langId, completed ->
                        viewModel.toggleLessonCompleted(lId, langId, completed)
                    },
                    onAskAiTutor = { prompt ->
                        viewModel.sendTutorQuestion(prompt)
                        navController.navigate("ai_tutor")
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("quiz/{languageId}") { backStack ->
                val langId = backStack.arguments?.getString("languageId") ?: "python"
                QuizScreen(
                    languageId = langId,
                    onSaveQuizResult = { lId, score, total ->
                        viewModel.saveQuizResult(lId, score, total)
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(
                route = "practice?chalId={chalId}",
                arguments = listOf(navArgument("chalId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStack ->
                val chalId = backStack.arguments?.getString("chalId")
                PracticeScreen(
                    selectedChallengeId = chalId,
                    challengeSubmissions = challengeSubmissions,
                    onSubmitCode = { challengeId, code, isPassed ->
                        viewModel.submitChallengeCode(challengeId, code, isPassed)
                    },
                    onSelectChallenge = { id ->
                        if (id != null) {
                            navController.navigate("practice?chalId=$id")
                        } else {
                            navController.navigate("practice")
                        }
                    }
                )
            }

            composable("typing") {
                TypingScreen(
                    typingResults = typingResults,
                    onSaveTypingResult = { testType, duration, wpm, acc, total, correct, mistakes ->
                        viewModel.saveTypingResult(testType, duration, wpm, acc, total, correct, mistakes)
                    }
                )
            }

            composable("roadmaps") {
                RoadmapsScreen(
                    roadmapProgressList = roadmapProgress,
                    onToggleRoadmapStage = { stageKey, roadmapId, completed ->
                        viewModel.toggleRoadmapStage(stageKey, roadmapId, completed)
                    }
                )
            }

            composable("project_lab") {
                ProjectLabScreen(
                    projectProgressList = projectProgress,
                    onToggleProjectTask = { taskId, projectId, completed ->
                        viewModel.toggleProjectTask(taskId, projectId, completed)
                    }
                )
            }

            composable(
                route = "code_lab?code={code}&lang={lang}&title={title}",
                arguments = listOf(
                    navArgument("code") { type = NavType.StringType; nullable = true; defaultValue = null },
                    navArgument("lang") { type = NavType.StringType; nullable = true; defaultValue = null },
                    navArgument("title") { type = NavType.StringType; nullable = true; defaultValue = null }
                )
            ) { backStack ->
                val initialCode = backStack.arguments?.getString("code")
                val initialLang = backStack.arguments?.getString("lang")
                val initialTitle = backStack.arguments?.getString("title")

                CodeLabScreen(
                    initialCode = initialCode,
                    initialLanguageId = initialLang,
                    initialTitle = initialTitle,
                    onRequestAiAnswer = { prompt -> viewModel.getAiAnswerForCodeLab(prompt) },
                    onSaveUserProject = { proj, files ->
                        viewModel.saveCodeLabProject(
                            proj.id,
                            proj.name,
                            proj.languageId,
                            files.map { it.name to it.content }
                        )
                    }
                )
            }

            composable("ai_tutor") {
                AiTutorScreen(
                    chatMessages = chatMessages,
                    isLoading = isTutorLoading,
                    onSendMessage = { prompt -> viewModel.sendTutorQuestion(prompt) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("analytics") {
                ProgressDashboardScreen(
                    profile = profile,
                    lessonProgressList = lessonProgress,
                    quizResults = quizResults,
                    challengeSubmissions = challengeSubmissions,
                    typingResults = typingResults
                )
            }

            composable("profile") {
                ProfileScreen(
                    profile = profile,
                    unlockedAchievements = unlockedAchievements,
                    onUpdateProfile = { name, avatar, roadmapId ->
                        viewModel.updateProfile(name, avatar, roadmapId)
                    },
                    onNavigateSettings = { navController.navigate("settings") }
                )
            }

            composable("settings") {
                SettingsScreen(
                    profile = profile,
                    onUpdateSettings = { dark, sound, notifs ->
                        viewModel.updateSettings(dark, sound, notifs)
                    },
                    onResetProgress = { viewModel.resetAllProgress() },
                    onLogout = {
                        viewModel.logoutUser()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("search") {
                SearchScreen(
                    searchQuery = searchQuery,
                    searchResults = searchResults,
                    onQueryChange = { q -> viewModel.onSearchQueryChange(q) },
                    onSelectLanguage = { langId -> navController.navigate("learn?langId=$langId") },
                    onSelectLesson = { lessonId -> navController.navigate("lesson_detail/$lessonId") },
                    onSelectChallenge = { chalId -> navController.navigate("practice?chalId=$chalId") },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
