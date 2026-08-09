package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizQuestion
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.SecondaryCyan
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber

/**
 * Reusable Course Quiz Component for career courses and modules.
 * Supports multiple-choice questions, auto-grading, and displays an explanation after each submission to reinforce learning.
 */
@Composable
fun CourseQuizComponent(
    quizQuestions: List<QuizQuestion>,
    modifier: Modifier = Modifier,
    initialAnswers: Map<Int, Int>? = null,
    isAlreadySubmitted: Boolean = false,
    onQuizSubmitted: ((scorePercentage: Int, correctCount: Int, totalCount: Int) -> Unit)? = null,
    onResetQuiz: (() -> Unit)? = null
) {
    if (quizQuestions.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No quiz questions available for this module.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var userAnswers = remember {
        mutableStateMapOf<Int, Int>().apply {
            initialAnswers?.forEach { (k, v) -> put(k, v) }
        }
    }
    var isSubmitted by remember { mutableStateOf(isAlreadySubmitted) }
    var currentQuestionAnswered by remember { mutableStateOf(userAnswers.containsKey(currentIndex)) }

    // Calculated stats
    val totalCount = quizQuestions.size
    val currentQ = quizQuestions.getOrNull(currentIndex)
    val selectedOptionIndex = userAnswers[currentIndex]

    var correctCount by remember(isSubmitted, userAnswers.size) {
        mutableIntStateOf(
            userAnswers.count { (qIdx, optIdx) ->
                quizQuestions.getOrNull(qIdx)?.correctIndex == optIdx
            }
        )
    }

    val scorePercentage = if (totalCount > 0) ((correctCount.toFloat() / totalCount.toFloat()) * 100).toInt() else 0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header Status Bar ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryIndigo.copy(alpha = 0.15f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                    contentDescription = "Quiz",
                                    tint = PrimaryIndigo,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Course Knowledge Assessment",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isSubmitted) "Auto-Graded Results" else "Question ${currentIndex + 1} of $totalCount",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (isSubmitted) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (scorePercentage >= 70) SuccessGreen.copy(alpha = 0.2f) else WarningAmber.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "$scorePercentage% Score",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontWeight = FontWeight.ExtraBold,
                                color = if (scorePercentage >= 70) SuccessGreen else WarningAmber,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        Text(
                            text = "${userAnswers.size}/$totalCount Answered",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryIndigo
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { if (isSubmitted) 1f else (currentIndex + 1).toFloat() / totalCount.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = if (isSubmitted && scorePercentage >= 70) SuccessGreen else PrimaryIndigo,
                    trackColor = MaterialTheme.colorScheme.surface
                )
            }
        }

        if (isSubmitted) {
            // --- Finished Quiz Auto-Graded Summary Screen ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = if (scorePercentage >= 70) WarningAmber else SecondaryCyan,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (scorePercentage >= 70) "Quiz Passed! 🎉" else "Quiz Complete!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "You scored $correctCount out of $totalCount correct ($scorePercentage%)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Accuracy", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$scorePercentage%", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = SuccessGreen)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("XP Earned", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("+${correctCount * 15} XP", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = PrimaryIndigo)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Status", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                if (scorePercentage >= 70) "PASS" else "RETRY",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = if (scorePercentage >= 70) SuccessGreen else WarningAmber
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedButton(
                        onClick = {
                            userAnswers.clear()
                            isSubmitted = false
                            currentIndex = 0
                            currentQuestionAnswered = false
                            onResetQuiz?.invoke()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quiz_retry_button")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Retry")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Retake Quiz")
                    }
                }
            }

            // Breakdown of all questions & explanations
            Text(
                text = "Detailed Answer Breakdown & Explanations",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )

            quizQuestions.forEachIndexed { qIdx, question ->
                val userOpt = userAnswers[qIdx]
                val isCorrect = userOpt == question.correctIndex

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) SuccessGreen.copy(alpha = 0.08f) else ErrorRed.copy(alpha = 0.08f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isCorrect) SuccessGreen.copy(alpha = 0.3f) else ErrorRed.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "Q${qIdx + 1}: ${question.question}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = if (isCorrect) "Correct" else "Incorrect",
                                tint = if (isCorrect) SuccessGreen else ErrorRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Your Answer: ${userOpt?.let { question.options.getOrNull(it) } ?: "Not answered"}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isCorrect) SuccessGreen else ErrorRed
                        )

                        if (!isCorrect) {
                            Text(
                                text = "Correct Answer: ${question.options.getOrNull(question.correctIndex)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SuccessGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Explanation card
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = "Explanation",
                                    tint = WarningAmber,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Explanation & Learning Note:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = WarningAmber
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = question.explanation,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

        } else if (currentQ != null) {
            // --- Active Step-by-Step Question Screen ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PrimaryIndigo.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "QUESTION ${currentIndex + 1}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryIndigo,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        if (selectedOptionIndex != null) {
                            val isChosenCorrect = selectedOptionIndex == currentQ.correctIndex
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isChosenCorrect) SuccessGreen.copy(alpha = 0.2f) else ErrorRed.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (isChosenCorrect) "✓ Correct" else "❌ Incorrect",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChosenCorrect) SuccessGreen else ErrorRed,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentQ.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // --- Multiple Choice Options List ---
            Text(
                text = "Select your answer:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            currentQ.options.forEachIndexed { optIndex, optionText ->
                val isOptionSelected = selectedOptionIndex == optIndex
                val isOptionCorrect = optIndex == currentQ.correctIndex

                val optionBg = when {
                    selectedOptionIndex == null -> MaterialTheme.colorScheme.surfaceVariant
                    isOptionSelected && isOptionCorrect -> SuccessGreen.copy(alpha = 0.2f)
                    isOptionSelected && !isOptionCorrect -> ErrorRed.copy(alpha = 0.2f)
                    isOptionCorrect && selectedOptionIndex != null -> SuccessGreen.copy(alpha = 0.12f)
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                }

                val optionBorderColor = when {
                    isOptionSelected && isOptionCorrect -> SuccessGreen
                    isOptionSelected && !isOptionCorrect -> ErrorRed
                    isOptionCorrect && selectedOptionIndex != null -> SuccessGreen.copy(alpha = 0.5f)
                    else -> Color.Transparent
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            width = if (isOptionSelected || (selectedOptionIndex != null && isOptionCorrect)) 1.5.dp else 0.dp,
                            color = optionBorderColor,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable(enabled = selectedOptionIndex == null) {
                            userAnswers[currentIndex] = optIndex
                            currentQuestionAnswered = true
                        }
                        .testTag("quiz_option_card_$optIndex"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = optionBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            // Letter badge A, B, C, D
                            Surface(
                                shape = CircleShape,
                                color = when {
                                    isOptionSelected && isOptionCorrect -> SuccessGreen
                                    isOptionSelected && !isOptionCorrect -> ErrorRed
                                    isOptionCorrect && selectedOptionIndex != null -> SuccessGreen
                                    else -> PrimaryIndigo.copy(alpha = 0.12f)
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${('A' + optIndex)}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedOptionIndex != null && (isOptionSelected || isOptionCorrect)) Color.White else PrimaryIndigo
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isOptionSelected) FontWeight.Bold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (selectedOptionIndex != null) {
                            if (isOptionCorrect) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = SuccessGreen
                                )
                            } else if (isOptionSelected) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Incorrect",
                                    tint = ErrorRed
                                )
                            }
                        }
                    }
                }
            }

            // --- Immediate Explanation Card after selecting option ---
            AnimatedVisibility(
                visible = selectedOptionIndex != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedOptionIndex == currentQ.correctIndex)
                            SuccessGreen.copy(alpha = 0.12f)
                        else
                            WarningAmber.copy(alpha = 0.12f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (selectedOptionIndex == currentQ.correctIndex) SuccessGreen.copy(alpha = 0.4f) else WarningAmber.copy(alpha = 0.4f)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Lightbulb",
                                tint = if (selectedOptionIndex == currentQ.correctIndex) SuccessGreen else WarningAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Explanation & Key Concept:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (selectedOptionIndex == currentQ.correctIndex) SuccessGreen else WarningAmber
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = currentQ.explanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // --- Navigation / Submit Action Bar ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentIndex > 0) {
                            currentIndex--
                            currentQuestionAnswered = userAnswers.containsKey(currentIndex)
                        }
                    },
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Previous")
                }

                if (currentIndex < totalCount - 1) {
                    Button(
                        onClick = {
                            currentIndex++
                            currentQuestionAnswered = userAnswers.containsKey(currentIndex)
                        },
                        enabled = selectedOptionIndex != null,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("quiz_next_button")
                    ) {
                        Text("Next Question")
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                    }
                } else {
                    Button(
                        onClick = {
                            // Calculate auto-grade results
                            var totalCorrect = 0
                            quizQuestions.forEachIndexed { idx, q ->
                                if (userAnswers[idx] == q.correctIndex) totalCorrect++
                            }
                            correctCount = totalCorrect
                            val score = ((totalCorrect.toFloat() / totalCount.toFloat()) * 100).toInt()
                            isSubmitted = true
                            onQuizSubmitted?.invoke(score, totalCorrect, totalCount)
                        },
                        enabled = selectedOptionIndex != null,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("quiz_submit_button")
                    ) {
                        Text("Finish & Grade Quiz")
                    }
                }
            }
        }
    }
}
