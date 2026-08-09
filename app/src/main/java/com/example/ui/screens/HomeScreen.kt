package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.ui.components.DailyPracticeData
import com.example.ui.components.DailyPracticeHoursChart
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LessonProgress
import com.example.data.model.TypingResult
import com.example.data.model.UserProfile
import com.example.data.repository.SeededData
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.BentoOnPrimaryContainer
import com.example.ui.theme.BentoOnSageContainer
import com.example.ui.theme.BentoOnSecondaryContainer
import com.example.ui.theme.BentoOnTertiaryContainer
import com.example.ui.theme.BentoPrimary
import com.example.ui.theme.BentoPrimaryContainer
import com.example.ui.theme.BentoSageContainer
import com.example.ui.theme.BentoSecondary
import com.example.ui.theme.BentoSecondaryContainer
import com.example.ui.theme.BentoTertiary
import com.example.ui.theme.BentoTertiaryContainer
import com.example.ui.theme.BentoTutorContainer
import com.example.ui.theme.SecondaryCyan
import com.example.ui.theme.SuccessGreen

@Composable
fun HomeScreen(
    profile: UserProfile,
    lessonProgress: List<LessonProgress>,
    typingResults: List<TypingResult>,
    onNavigate: (String) -> Unit,
    onSelectLanguage: (String) -> Unit,
    onSelectChallenge: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedLessonsCount = lessonProgress.count { it.isCompleted }
    val totalLessonsCount = SeededData.lessons.size
    val progressPercent = if (totalLessonsCount > 0) (completedLessonsCount.toFloat() / totalLessonsCount.toFloat()) else 0f

    val avgWpm = if (typingResults.isNotEmpty()) typingResults.map { it.wpm }.average().toInt() else 0
    val firstName = profile.name.split(" ").firstOrNull() ?: "Developer"
    val level = (profile.xp / 100) + 1
    val xpToNextLevel = 100 - (profile.xp % 100)

    val todayCodingChallenge = SeededData.challenges.firstOrNull()
    val recommendedLesson = SeededData.lessons.firstOrNull { l ->
        lessonProgress.none { p -> p.lessonId == l.id && p.isCompleted }
    } ?: SeededData.lessons.first()

    val weeklyPracticeData = remember(lessonProgress, typingResults) {
        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val baseHours = listOf(1.5f, 2.2f, 3.0f, 1.8f, 2.5f, 4.0f, 2.8f)
        val extraBonusHours = (completedLessonsCount * 0.3f + typingResults.size * 0.15f) / 7f
        days.mapIndexed { index, day ->
            val isToday = index == 6
            val h = (baseHours[index] + if (isToday) (completedLessonsCount * 0.4f) else extraBonusHours).coerceAtLeast(0.5f)
            DailyPracticeData(dayLabel = day, hours = h, isToday = isToday)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Welcome Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WELCOME BACK",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = if (profile.username.isNotBlank()) "@${profile.username}" else profile.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = BentoPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BentoPrimaryContainer)
                        .clickable { onNavigate("profile") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = BentoOnPrimaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Selected Target Career Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate("career_selection") }
                    .testTag("home_target_career_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BentoPrimaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = "Career Roadmap",
                                tint = BentoPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "TARGET CAREER",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoPrimary,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = profile.targetCareer.ifBlank { "Software Developer" },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoOnPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tap to view or change career roadmap",
                            fontSize = 11.sp,
                            color = BentoPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(BentoPrimary)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Change",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // BENTO GRID ROW 1: Streak (col-4) + Total XP (col-2)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Streak Card (2/3 width)
                Card(
                    modifier = Modifier
                        .weight(2f)
                        .height(115.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoPrimaryContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = BentoPrimary
                            )
                            Text(
                                text = "${profile.streak} DAY STREAK",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoPrimary
                            )
                        }

                        Text(
                            text = "You're on fire, $firstName! $xpToNextLevel XP to Level ${level + 1}.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoOnPrimaryContainer,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Total XP Card (1/3 width)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(115.dp)
                        .clickable { onNavigate("analytics") },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${profile.xp}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = BentoPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "TOTAL XP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // BENTO GRID LINE CHART: Daily Coding Practice Hours Over Past Week
        item {
            DailyPracticeHoursChart(
                weeklyData = weeklyPracticeData
            )
        }

        // BENTO GRID ROW 2: Coding Practice Hero + Typing & Roadmap
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Coding Practice Bento Tile
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(156.dp)
                        .clickable { onNavigate("practice") }
                        .testTag("qc_practice"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoPrimary)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = "Terminal",
                                tint = BentoPrimaryContainer,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Coding\nPractice",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = 22.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "12 CHALLENGES",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Right Column: Typing + Roadmap
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Typing Bento
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .clickable { onNavigate("typing") }
                            .testTag("qc_typing"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = BentoTertiaryContainer)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BentoTertiary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Keyboard,
                                    contentDescription = "Typing",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "TYPING",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BentoOnTertiaryContainer
                                )
                                Text(
                                    text = if (avgWpm > 0) "$avgWpm WPM" else "Speed Test",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnTertiaryContainer
                                )
                            }
                        }
                    }

                    // Roadmap Bento
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .clickable { onNavigate("roadmaps") }
                            .testTag("qc_roadmaps"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = BentoSecondaryContainer)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BentoSecondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = "Roadmap",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "ROADMAP",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BentoOnSecondaryContainer
                                )
                                Text(
                                    text = profile.currentRoadmapId.replace("_", " ").split(" ")
                                        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnSecondaryContainer,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // BENTO GRID ROW 3: Recommended Lesson Full Width Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp))
                    .clickable { onSelectLanguage(recommendedLesson.languageId) }
                    .testTag("home_continue_learning_button"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "RECOMMENDED LESSON",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = BentoPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${recommendedLesson.languageId.uppercase()}: ${recommendedLesson.title}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progressPercent },
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = BentoPrimary
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Start Lesson",
                        tint = BentoPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // BENTO GRID ROW 4: AI Tutor + Project Lab / Daily Quests
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // AI Tutor Bento Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                        .clickable { onNavigate("ai_tutor") }
                        .testTag("qc_ai_tutor"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoTutorContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "AI TUTOR",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Ask about syntax & logic...",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Project Lab Bento Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                        .clickable { onNavigate("project_lab") },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoSageContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = "Project Lab",
                                tint = BentoOnSageContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PROJECT LAB",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoOnSageContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Portfolio Projects",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoOnSageContainer
                        )
                    }
                }
            }
        }

        // Quick Horizontal Scrollable Access Row
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "EXPLORE ALL MODULES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    BentoChip(
                        title = "Learn",
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        onClick = { onNavigate("learn") },
                        testTag = "qc_learn"
                    )
                }
                item {
                    BentoChip(
                        title = "Practice",
                        icon = Icons.Default.Code,
                        onClick = { onNavigate("practice") },
                        testTag = "qc_practice"
                    )
                }
                item {
                    BentoChip(
                        title = "Typing",
                        icon = Icons.Default.Keyboard,
                        onClick = { onNavigate("typing") },
                        testTag = "qc_typing"
                    )
                }
                item {
                    BentoChip(
                        title = "Roadmaps",
                        icon = Icons.Default.Map,
                        onClick = { onNavigate("roadmaps") },
                        testTag = "qc_roadmaps"
                    )
                }
                item {
                    BentoChip(
                        title = "AI Tutor",
                        icon = Icons.Default.Psychology,
                        onClick = { onNavigate("ai_tutor") },
                        testTag = "qc_ai_tutor"
                    )
                }
                item {
                    BentoChip(
                        title = "Analytics",
                        icon = Icons.Default.BarChart,
                        onClick = { onNavigate("analytics") },
                        testTag = "qc_analytics"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // REQUIRED CREATOR CREDIT AT BOTTOM OF DASHBOARD
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Made by Shashank Ojha",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BentoPrimary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BentoChip(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = BentoPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
