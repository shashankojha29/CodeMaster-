package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BentoOnPrimaryContainer
import com.example.ui.theme.BentoPrimary
import com.example.ui.theme.BentoPrimaryContainer

data class CareerOptionData(
    val title: String,
    val roadmapId: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun CareerSelectionScreen(
    currentSelectedCareer: String,
    onCareerSelected: (careerTitle: String, roadmapId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val careerOptions = remember {
        listOf(
            CareerOptionData("Software Developer", "software_dev", "Build robust desktop, enterprise, and system software.", Icons.Default.Computer),
            CareerOptionData("Full-Stack Web Developer", "fullstack_web", "Build modern web apps from frontend UI to backend APIs.", Icons.Default.Web),
            CareerOptionData("Frontend Developer", "frontend_dev", "Craft pixel-perfect, responsive web interfaces and user experiences.", Icons.Default.Code),
            CareerOptionData("Backend Developer", "backend_dev", "Power high-performance APIs, database queries, and microservices.", Icons.Default.Storage),
            CareerOptionData("App Developer", "app_dev", "Build native and cross-platform Android & iOS applications.", Icons.Default.PhoneAndroid),
            CareerOptionData("Game Developer", "game_dev", "Program 2D/3D physics engines, gameplay graphics, and logic.", Icons.Default.SportsEsports),
            CareerOptionData("AI/ML Developer", "ai_ml_dev", "Train neural networks, predictions, and generative AI models.", Icons.Default.Psychology),
            CareerOptionData("Data Scientist", "data_scientist", "Extract insights from big data with statistical models and analytics.", Icons.Default.Analytics),
            CareerOptionData("Cybersecurity Developer", "cybersecurity_dev", "Protect systems, audit software security, and implement encryption.", Icons.Default.Security)
        )
    }

    var selectedCareerTitle by remember {
        mutableStateOf(
            careerOptions.firstOrNull { it.title.equals(currentSelectedCareer, ignoreCase = true) }?.title
                ?: careerOptions.first().title
        )
    }

    val selectedOption = careerOptions.first { it.title == selectedCareerTitle }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        // Header
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "What do you want to become? 🎯",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = BentoPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Select your target career path. We'll customize your learning roadmap, challenges, and project recommendations.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Options List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(careerOptions) { option ->
                val isSelected = option.title == selectedCareerTitle

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) BentoPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedCareerTitle = option.title }
                        .testTag("career_option_${option.roadmapId}"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) BentoPrimaryContainer else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) BentoPrimary else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = option.icon,
                                contentDescription = option.title,
                                tint = if (isSelected) Color.White else BentoPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = option.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) BentoOnPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = option.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }

                        if (isSelected) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = BentoPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Continue Button
        Button(
            onClick = {
                onCareerSelected(selectedOption.title, selectedOption.roadmapId)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("career_continue_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary)
        ) {
            Text(
                text = "Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
