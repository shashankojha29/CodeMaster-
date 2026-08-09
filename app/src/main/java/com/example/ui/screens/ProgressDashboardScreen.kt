package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.ui.components.DailyPracticeData
import com.example.ui.components.DailyPracticeHoursChart
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChallengeSubmission
import com.example.data.model.LessonProgress
import com.example.data.model.QuizResult
import com.example.data.model.TypingResult
import com.example.data.model.UserProfile
import com.example.data.repository.SeededData
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.SecondaryCyan
import com.example.ui.theme.SuccessGreen

@Composable
fun ProgressDashboardScreen(
    profile: UserProfile,
    lessonProgressList: List<LessonProgress>,
    quizResults: List<QuizResult>,
    challengeSubmissions: List<ChallengeSubmission>,
    typingResults: List<TypingResult>,
    modifier: Modifier = Modifier
) {
    val level = (profile.xp / 100) + 1
    val currentLevelXp = profile.xp % 100

    val completedLessons = lessonProgressList.count { it.isCompleted }
    val solvedChallenges = challengeSubmissions.count { it.isPassed }
    val totalQuizzes = quizResults.size

    val avgWpm = if (typingResults.isNotEmpty()) typingResults.map { it.wpm }.average().toInt() else 0
    val avgAcc = if (typingResults.isNotEmpty()) typingResults.map { it.accuracy }.average() else 0.0

    val weeklyPracticeData = remember(lessonProgressList, challengeSubmissions, typingResults) {
        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val baseHours = listOf(1.5f, 2.2f, 3.0f, 1.8f, 2.5f, 4.0f, 2.8f)
        val extraBonusHours = (completedLessons * 0.3f + solvedChallenges * 0.4f + typingResults.size * 0.15f) / 7f
        days.mapIndexed { index, day ->
            val isToday = index == 6
            val h = (baseHours[index] + if (isToday) (completedLessons * 0.4f + solvedChallenges * 0.5f) else extraBonusHours).coerceAtLeast(0.5f)
            DailyPracticeData(dayLabel = day, hours = h, isToday = isToday)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Analytics",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "📊 Progress & Analytics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Level & XP Progress Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Current Status",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Level $level Code Master",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = "XP", tint = AccentOrange)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${profile.xp} Total XP",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Level Progress ($currentLevelXp / 100 XP to Level ${level + 1})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { currentLevelXp / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = AccentOrange
                    )
                }
            }
        }

        // Metrics Summary Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricBox(
                    title = "Lessons",
                    value = "$completedLessons",
                    icon = Icons.Default.CheckCircle,
                    color = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricBox(
                    title = "Challenges",
                    value = "$solvedChallenges",
                    icon = Icons.Default.EmojiEvents,
                    color = AccentOrange,
                    modifier = Modifier.weight(1f)
                )
                MetricBox(
                    title = "Typing WPM",
                    value = "$avgWpm",
                    icon = Icons.Default.Speed,
                    color = SecondaryCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Daily Coding Practice Hours Line Chart
        item {
            DailyPracticeHoursChart(
                weeklyData = weeklyPracticeData
            )
        }

        // Language breakdown
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🌐 Language Mastery Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    SeededData.languages.forEach { lang ->
                        val langLessons = SeededData.lessons.filter { it.languageId == lang.id }
                        val doneCount = langLessons.count { l ->
                            lessonProgressList.any { p -> p.lessonId == l.id && p.isCompleted }
                        }
                        val totalCount = langLessons.size.coerceAtLeast(1)
                        val percent = doneCount.toFloat() / totalCount.toFloat()

                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "${lang.iconRes} ${lang.name}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "$doneCount / $totalCount Lessons", fontSize = 11.sp, color = SuccessGreen)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            LinearProgressIndicator(
                                progress = { percent },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = SuccessGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Text(text = title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
