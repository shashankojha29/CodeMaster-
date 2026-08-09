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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TypingLesson
import com.example.data.model.TypingResult
import com.example.data.repository.SeededData
import com.example.ui.components.TypingKeyboardGuide
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.SecondaryCyan
import com.example.ui.theme.SuccessGreen
import kotlinx.coroutines.delay

@Composable
fun TypingScreen(
    typingResults: List<TypingResult>,
    onSaveTypingResult: (String, Int, Int, Double, Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Lessons, 1: WPM Test, 2: Code Typing
    val tabs = listOf("Lessons", "WPM Test", "Code Typing")

    // Active Test State
    var durationOptionSeconds by remember { mutableIntStateOf(30) } // 30, 60, 120, 300
    var activePassage by remember { mutableStateOf(SeededData.typingLessons.first().practicePassage) }
    var userTypedInput by remember { mutableStateOf("") }
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var isTestRunning by remember { mutableStateOf(false) }
    var isTestCompleted by remember { mutableStateOf(false) }

    // Timer coroutine
    LaunchedEffect(isTestRunning, secondsElapsed) {
        if (isTestRunning && secondsElapsed < durationOptionSeconds) {
            delay(1000L)
            secondsElapsed++
            if (secondsElapsed >= durationOptionSeconds || userTypedInput.length >= activePassage.length) {
                isTestRunning = false
                isTestCompleted = true

                val correctChars = userTypedInput.zip(activePassage).count { (a, b) -> a == b }
                val mistakes = userTypedInput.length - correctChars
                val totalChars = userTypedInput.length
                val minutes = (secondsElapsed.toFloat() / 60f).coerceAtLeast(0.01f)
                val wpm = ((correctChars / 5f) / minutes).toInt()
                val acc = if (totalChars > 0) (correctChars.toDouble() / totalChars.toDouble()) * 100.0 else 0.0

                onSaveTypingResult(
                    if (selectedTab == 2) "code_snippet" else "general",
                    secondsElapsed,
                    wpm,
                    acc,
                    totalChars,
                    correctChars,
                    mistakes
                )
            }
        }
    }

    val activeChar = activePassage.getOrNull(userTypedInput.length)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "⌨️ Typing Academy",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Section Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                tabs.forEachIndexed { index, name ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            isTestRunning = false
                            isTestCompleted = false
                            userTypedInput = ""
                            secondsElapsed = 0
                            if (index == 2) {
                                activePassage = SeededData.typingLessons.first { it.isCodeMode }.practicePassage
                            } else {
                                activePassage = SeededData.typingLessons.first().practicePassage
                            }
                        },
                        text = { Text(name, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                }
            }
        }

        if (selectedTab == 0) {
            // Typing Lessons List & Finger Guide
            item {
                TypingKeyboardGuide(activeKey = null)
            }

            items(SeededData.typingLessons) { lesson ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            activePassage = lesson.practicePassage
                            selectedTab = if (lesson.isCodeMode) 2 else 1
                        }
                        .testTag("typing_lesson_${lesson.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = lesson.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (lesson.isCodeMode) SecondaryCyan.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = lesson.level,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (lesson.isCodeMode) SecondaryCyan else MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = lesson.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            // Active Practice / Test Screen (WPM Test or Code Typing)
            item {
                // Duration options bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Duration:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    listOf(30, 60, 120, 300).forEach { dur ->
                        val isSel = durationOptionSeconds == dur
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    durationOptionSeconds = dur
                                    isTestRunning = false
                                    isTestCompleted = false
                                    userTypedInput = ""
                                    secondsElapsed = 0
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (dur >= 60) "${dur / 60}m" else "${dur}s",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Finger guide keyboard overlay
            item {
                TypingKeyboardGuide(activeKey = activeChar)
            }

            // Live Timer & WPM HUD
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Time Left", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${durationOptionSeconds - secondsElapsed}s", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }

                        val correctChars = userTypedInput.zip(activePassage).count { (a, b) -> a == b }
                        val minutes = (secondsElapsed.toFloat() / 60f).coerceAtLeast(0.01f)
                        val liveWpm = ((correctChars / 5f) / minutes).toInt()

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Live WPM", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$liveWpm", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                        }

                        val acc = if (userTypedInput.isNotEmpty()) (correctChars.toDouble() / userTypedInput.length) * 100 else 100.0
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Accuracy", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${acc.toInt()}%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SecondaryCyan)
                        }
                    }
                }
            }

            // Target Text Box & User Input Box
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkBackground),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Target Passage:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = activePassage,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Type Here:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryCyan
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        BasicTextField(
                            value = userTypedInput,
                            onValueChange = { input ->
                                if (!isTestCompleted) {
                                    if (!isTestRunning && input.isNotEmpty()) {
                                        isTestRunning = true
                                    }
                                    userTypedInput = input
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B))
                                .padding(10.dp)
                                .testTag("typing_text_input"),
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                color = Color(0xFF38BDF8)
                            )
                        )
                    }
                }
            }

            // Results Card upon test completion
            if (isTestCompleted) {
                item {
                    val correctChars = userTypedInput.zip(activePassage).count { (a, b) -> a == b }
                    val totalChars = userTypedInput.length
                    val minutes = (secondsElapsed.toFloat() / 60f).coerceAtLeast(0.01f)
                    val finalWpm = ((correctChars / 5f) / minutes).toInt()
                    val finalAcc = if (totalChars > 0) (correctChars.toDouble() / totalChars.toDouble()) * 100.0 else 0.0

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎉 Test Complete!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "$finalWpm WPM • ${finalAcc.toInt()}% Accuracy",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "+${if (selectedTab == 2) 20 else 10} XP Awarded!",
                                fontWeight = FontWeight.Bold,
                                color = SecondaryCyan
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedButton(
                                onClick = {
                                    userTypedInput = ""
                                    secondsElapsed = 0
                                    isTestRunning = false
                                    isTestCompleted = false
                                },
                                modifier = Modifier.testTag("restart_typing_test_button")
                            ) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Retry")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Try Again")
                            }
                        }
                    }
                }
            }
        }
    }
}
