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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RoadmapProgress
import com.example.data.repository.SeededData
import com.example.ui.theme.SecondaryCyan
import com.example.ui.theme.SuccessGreen

@Composable
fun RoadmapsScreen(
    roadmapProgressList: List<RoadmapProgress>,
    onToggleRoadmapStage: (String, String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRoadmapId by remember { mutableStateOf(SeededData.careerRoadmaps.first().id) }
    val activeRoadmap = SeededData.careerRoadmaps.find { it.id == selectedRoadmapId } ?: SeededData.careerRoadmaps.first()

    val totalStages = activeRoadmap.stages.size
    val completedStages = activeRoadmap.stages.count { stage ->
        roadmapProgressList.any { p -> p.stageKey == stage.key && p.isCompleted }
    }
    val roadmapPercent = if (totalStages > 0) completedStages.toFloat() / totalStages.toFloat() else 0f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "🗺️ Career Learning Roadmaps",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Roadmaps Horizontal List Selector
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SeededData.careerRoadmaps) { roadmap ->
                    val isSelected = roadmap.id == activeRoadmap.id
                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .clickable { selectedRoadmapId = roadmap.id }
                            .testTag("roadmap_tab_${roadmap.id}"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = roadmap.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = roadmap.category,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Selected Roadmap Overview Header
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
                            text = activeRoadmap.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SuccessGreen.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${(roadmapPercent * 100).toInt()}% Done",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = activeRoadmap.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { roadmapPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = SuccessGreen
                    )
                }
            }
        }

        // Roadmap Stages Timeline
        item {
            Text(
                text = "📌 Roadmap Career Stages",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(activeRoadmap.stages) { stage ->
            val isCompleted = roadmapProgressList.any { p -> p.stageKey == stage.key && p.isCompleted }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onToggleRoadmapStage(stage.key, activeRoadmap.id, !isCompleted)
                    }
                    .testTag("roadmap_stage_${stage.key}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCompleted) SuccessGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isCompleted) SuccessGreen else MaterialTheme.colorScheme.primary
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${stage.stageNumber}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = androidx.compose.ui.graphics.Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stage.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Complete",
                            tint = if (isCompleted) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stage.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("Languages: ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(stage.recommendedLanguages.joinToString(", "), fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        Row {
                            Text("Tools: ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(stage.tools.joinToString(", "), fontSize = 11.sp, color = SecondaryCyan)
                        }
                        Row {
                            Text("Key Concepts: ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(stage.concepts.joinToString(" • "), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
