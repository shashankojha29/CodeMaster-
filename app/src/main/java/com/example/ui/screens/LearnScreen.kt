package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CareerCourseProgress
import com.example.data.model.LanguageInfo
import com.example.data.model.LessonItem
import com.example.data.model.LessonProgress
import com.example.data.repository.SeededData
import com.example.ui.theme.SecondaryCyan
import com.example.ui.theme.SuccessGreen

@Composable
fun LearnScreen(
    selectedLanguageId: String,
    lessonProgress: List<LessonProgress>,
    currentCareerTitle: String = "Software Developer",
    careerCourseProgress: List<CareerCourseProgress> = emptyList(),
    onSelectLanguage: (String) -> Unit,
    onSelectLesson: (String) -> Unit,
    onStartQuiz: (String) -> Unit,
    onSaveCareerProgress: (moduleId: String, careerId: String, isCompleted: Boolean, quizScore: Int, isProjectCompleted: Boolean) -> Unit = { _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    var activeCategoryTab by remember { mutableIntStateOf(0) } // 0 = Language Courses, 1 = Career Courses
    var selectedTierIndex by remember { mutableIntStateOf(0) }
    val tiers = listOf("Beginner", "Intermediate", "Advanced")

    val currentLang = SeededData.languages.find { it.id == selectedLanguageId } ?: SeededData.languages.first()
    val langLessons = SeededData.lessons.filter { it.languageId == currentLang.id }

    val filteredLessons = langLessons.filter { it.tier == tiers[selectedTierIndex] }

    val langCompletedCount = langLessons.count { l ->
        lessonProgress.any { p -> p.lessonId == l.id && p.isCompleted }
    }
    val langTotalCount = langLessons.size.coerceAtLeast(1)
    val langProgressPercent = langCompletedCount.toFloat() / langTotalCount.toFloat()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // --- Category Selection Tabs ---
        TabRow(
            selectedTabIndex = activeCategoryTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Tab(
                selected = activeCategoryTab == 0,
                onClick = { activeCategoryTab = 0 },
                text = { Text("💻 Language Courses", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                modifier = Modifier.testTag("tab_language_courses")
            )
            Tab(
                selected = activeCategoryTab == 1,
                onClick = { activeCategoryTab = 1 },
                text = { Text("🎯 Career Courses", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                modifier = Modifier.testTag("tab_career_courses")
            )
        }

        if (activeCategoryTab == 1) {
            CareerCoursesScreen(
                currentCareerTitle = currentCareerTitle,
                careerCourseProgressList = careerCourseProgress,
                onSaveProgress = onSaveCareerProgress
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
        // Languages horizontal selector
        item {
            Column {
                Text(
                    text = "💻 Select Language",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SeededData.languages) { lang ->
                        val isSelected = lang.id == currentLang.id
                        Card(
                            modifier = Modifier
                                .width(110.dp)
                                .clickable { onSelectLanguage(lang.id) }
                                .testTag("lang_card_${lang.id}"),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = lang.iconRes,
                                    fontSize = 24.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = lang.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Language Overview Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = currentLang.iconRes, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = currentLang.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = currentLang.category,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = { onStartQuiz(currentLang.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryCyan),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("take_quiz_button_${currentLang.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Quiz,
                                contentDescription = "Quiz",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Take Quiz", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentLang.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Course Progress: $langCompletedCount / $langTotalCount Lessons",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${(langProgressPercent * 100).toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { langProgressPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = SuccessGreen
                    )
                }
            }
        }

        // Tier Tabs (Beginner, Intermediate, Advanced)
        item {
            TabRow(
                selectedTabIndex = selectedTierIndex,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                tiers.forEachIndexed { index, tierName ->
                    Tab(
                        selected = selectedTierIndex == index,
                        onClick = { selectedTierIndex = index },
                        text = {
                            Text(
                                text = tierName,
                                fontWeight = if (selectedTierIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        // Lessons List for selected tier
        if (filteredLessons.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Coming soon",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "More $tiers[selectedTierIndex] lessons for ${currentLang.name} arriving soon!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredLessons) { lesson ->
                val isDone = lessonProgress.any { p -> p.lessonId == lesson.id && p.isCompleted }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectLesson(lesson.id) }
                        .testTag("lesson_item_${lesson.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isDone) SuccessGreen.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isDone) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.MenuBook,
                                    contentDescription = "Status",
                                    tint = if (isDone) SuccessGreen else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = lesson.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = lesson.explanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }

                        Text(
                            text = if (isDone) "Done ✓" else "Start >",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDone) SuccessGreen else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
}
}
