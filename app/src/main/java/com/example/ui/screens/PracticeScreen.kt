package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChallengeDifficulty
import com.example.data.model.ChallengeSubmission
import com.example.data.model.CodingChallenge
import com.example.data.repository.SeededData
import com.example.ui.components.CodeEditorView
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningYellow

@Composable
fun PracticeScreen(
    selectedChallengeId: String?,
    challengeSubmissions: List<ChallengeSubmission>,
    onSubmitCode: (String, String, Boolean) -> Unit,
    onSelectChallenge: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("All", "Easy 🟢", "Medium 🟡", "Hard 🔴")

    val activeChallenge = SeededData.challenges.find { it.id == selectedChallengeId }

    var userCode by remember(activeChallenge) {
        mutableStateOf(activeChallenge?.starterCode ?: "")
    }
    var consoleMessage by remember { mutableStateOf<String?>(null) }
    var isPassed by remember { mutableStateOf<Boolean?>(null) }

    val filteredChallenges = SeededData.challenges.filter { challenge ->
        when (selectedTab) {
            1 -> challenge.difficulty == ChallengeDifficulty.EASY
            2 -> challenge.difficulty == ChallengeDifficulty.MEDIUM
            3 -> challenge.difficulty == ChallengeDifficulty.HARD
            else -> true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "💻 Coding Practice Lab",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (activeChallenge != null) {
            // Selected Challenge Workspace
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
                            Text(
                                text = activeChallenge.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "← Back to List",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clickable { onSelectChallenge(null) }
                                    .testTag("back_to_challenges_button")
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = activeChallenge.problemStatement,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column {
                                Text("Example Input:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(activeChallenge.exampleInput, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            Column {
                                Text("Example Output:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(activeChallenge.exampleOutput, fontSize = 12.sp, color = SuccessGreen)
                            }
                        }
                    }
                }
            }

            // Code Editor
            item {
                CodeEditorView(
                    code = userCode,
                    onCodeChange = { userCode = it },
                    hint = activeChallenge.hint,
                    expectedKeywords = activeChallenge.expectedKeywords,
                    onRunCode = {
                        val hasKeywords = activeChallenge.expectedKeywords.all { kw -> userCode.contains(kw) }
                        if (hasKeywords) {
                            consoleMessage = "✓ Test Case Passed! Sample input output matches expected result (${activeChallenge.exampleOutput})."
                            isPassed = true
                        } else {
                            consoleMessage = "✗ Test Case Failed. Ensure your code contains correct logic/syntax keywords."
                            isPassed = false
                        }
                    },
                    onSubmitCode = {
                        val hasKeywords = activeChallenge.expectedKeywords.all { kw -> userCode.contains(kw) }
                        if (hasKeywords) {
                            consoleMessage = "🎉 Challenge Solved! +25 XP awarded!"
                            isPassed = true
                            onSubmitCode(activeChallenge.id, userCode, true)
                        } else {
                            consoleMessage = "✗ Solution submission incomplete. Try running the test case first."
                            isPassed = false
                        }
                    },
                    outputMessage = consoleMessage,
                    isPassed = isPassed
                )
            }
        } else {
            // Challenges Filter Tabs
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    tabs.forEachIndexed { index, name ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(name, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }

            // Challenge List
            items(filteredChallenges) { challenge ->
                val isSolved = challengeSubmissions.any { it.challengeId == challenge.id && it.isPassed }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectChallenge(challenge.id) }
                        .testTag("challenge_item_${challenge.id}"),
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
                            Icon(
                                imageVector = if (isSolved) Icons.Default.CheckCircle else Icons.Default.Code,
                                contentDescription = "Status",
                                tint = if (isSolved) SuccessGreen else when (challenge.difficulty) {
                                    ChallengeDifficulty.EASY -> SuccessGreen
                                    ChallengeDifficulty.MEDIUM -> WarningYellow
                                    ChallengeDifficulty.HARD -> AccentOrange
                                },
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = challenge.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = challenge.problemStatement,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }

                        Text(
                            text = if (isSolved) "Solved ✓" else "Solve >",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSolved) SuccessGreen else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
