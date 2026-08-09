package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CareerCourse
import com.example.data.model.CareerCourseModule
import com.example.data.model.CareerCourseProgress
import com.example.data.repository.CareerCourseData
import com.example.ui.components.CourseQuizComponent
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.SecondaryCyan
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareerCoursesScreen(
    currentCareerTitle: String,
    careerCourseProgressList: List<CareerCourseProgress>,
    onSaveProgress: (moduleId: String, careerId: String, isCompleted: Boolean, quizScore: Int, isProjectCompleted: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Determine initial selected course matching target career or default to Software Engineer
    var selectedCourseId by remember(currentCareerTitle) {
        val matched = CareerCourseData.careerCourses.find {
            it.title.equals(currentCareerTitle, ignoreCase = true) ||
                    it.id.equals(currentCareerTitle, ignoreCase = true) ||
                    (currentCareerTitle.contains("Software", ignoreCase = true) && it.id == "software_engineer")
        }
        mutableStateOf(matched?.id ?: "software_engineer")
    }

    var selectedModuleForDetail by remember { mutableStateOf<CareerCourseModule?>(null) }
    var selectedTierFilter by remember { mutableStateOf("All") }

    val activeCourse = CareerCourseData.getCourseById(selectedCourseId) ?: CareerCourseData.careerCourses.first()

    if (selectedModuleForDetail != null) {
        CareerModuleDetailScreen(
            module = selectedModuleForDetail!!,
            course = activeCourse,
            existingProgress = careerCourseProgressList.find { it.moduleId == selectedModuleForDetail!!.id },
            onBack = { selectedModuleForDetail = null },
            onCompleteModule = { moduleId, quizScore, isProjDone ->
                onSaveProgress(moduleId, activeCourse.id, true, quizScore, isProjDone)
                selectedModuleForDetail = null
            }
        )
        return
    }

    // Filter modules based on selected tier
    val filteredModules = when (selectedTierFilter) {
        "Beginner" -> activeCourse.modules.filter { it.tier == "Beginner" }
        "Intermediate" -> activeCourse.modules.filter { it.tier == "Intermediate" }
        "Advanced" -> activeCourse.modules.filter { it.tier == "Advanced" }
        else -> activeCourse.modules
    }

    // Calculate completion metrics
    val totalModules = activeCourse.modules.size.coerceAtLeast(1)
    val completedModulesCount = activeCourse.modules.count { mod ->
        careerCourseProgressList.any { p -> p.moduleId == mod.id && p.isCompleted }
    }
    val overallProgressPercent = completedModulesCount.toFloat() / totalModules.toFloat()
    val totalXpEarnedFromCourse = completedModulesCount * 30

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header Banner ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🎯", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Career-Based Courses",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Zero to Professional Curriculum",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "XP",
                                    tint = WarningAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+$totalXpEarnedFromCourse XP",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress metrics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${activeCourse.title} Progress",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$completedModulesCount / $totalModules Modules (${(overallProgressPercent * 100).toInt()}%)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { overallProgressPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = SuccessGreen,
                        trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                    )
                }
            }
        }

        // --- Career Course Selector (Horizontal List) ---
        item {
            Column {
                Text(
                    text = "💼 Select Your Target Career Path",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(CareerCourseData.careerCourses) { course ->
                        val isSelected = course.id == activeCourse.id
                        val courseCompletedCount = course.modules.count { mod ->
                            careerCourseProgressList.any { p -> p.moduleId == mod.id && p.isCompleted }
                        }

                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .clickable { selectedCourseId = course.id }
                                .testTag("career_course_card_${course.id}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = course.iconEmoji, fontSize = 24.sp)
                                    if (courseCompletedCount > 0) {
                                        Text(
                                            text = "$courseCompletedCount/${course.modules.size}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else SuccessGreen
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = course.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${course.modules.size} Modules",
                                    fontSize = 11.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Tier Filter Chips ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                listOf("All", "Beginner", "Intermediate", "Advanced").forEach { tier ->
                    val isSelected = selectedTierFilter == tier
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTierFilter = tier },
                        label = { Text(tier, fontSize = 11.sp) },
                        modifier = Modifier.testTag("tier_chip_$tier")
                    )
                }
            }
        }

        // --- Modules List Section ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📚 ${activeCourse.title} Modules (${filteredModules.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "🔒 Progressive Unlock",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        itemsIndexed(filteredModules) { index, module ->
            val progress = careerCourseProgressList.find { it.moduleId == module.id }
            val isCompleted = progress?.isCompleted == true

            // Progressive Unlocking Logic:
            // Module 1 is always unlocked.
            // Module N is unlocked if Module N-1 is completed.
            val moduleIndexInFullList = activeCourse.modules.indexOfFirst { it.id == module.id }
            val isUnlocked = if (moduleIndexInFullList <= 0) {
                true
            } else {
                val previousModuleId = activeCourse.modules[moduleIndexInFullList - 1].id
                careerCourseProgressList.any { it.moduleId == previousModuleId && it.isCompleted }
            }

            CareerModuleCard(
                module = module,
                isCompleted = isCompleted,
                isUnlocked = isUnlocked,
                progress = progress,
                onClick = {
                    if (isUnlocked) {
                        selectedModuleForDetail = module
                    }
                }
            )
        }
    }
}

@Composable
fun CareerModuleCard(
    module: CareerCourseModule,
    isCompleted: Boolean,
    isUnlocked: Boolean,
    progress: CareerCourseProgress?,
    onClick: () -> Unit
) {
    val containerColor = when {
        isCompleted -> MaterialTheme.colorScheme.surfaceVariant
        isUnlocked -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isUnlocked) { onClick() }
            .testTag("module_card_${module.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 2.dp else 0.dp)
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
                // Status Icon
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> SuccessGreen.copy(alpha = 0.2f)
                                isUnlocked -> PrimaryIndigo.copy(alpha = 0.2f)
                                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            isCompleted -> Icons.Default.CheckCircle
                            isUnlocked -> Icons.Default.PlayArrow
                            else -> Icons.Default.Lock
                        },
                        contentDescription = "Status",
                        tint = when {
                            isCompleted -> SuccessGreen
                            isUnlocked -> PrimaryIndigo
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = module.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Tier Chip
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when (module.tier) {
                                "Beginner" -> SuccessGreen.copy(alpha = 0.2f)
                                "Intermediate" -> WarningAmber.copy(alpha = 0.2f)
                                else -> PrimaryIndigo.copy(alpha = 0.2f)
                            }
                        ) {
                            Text(
                                text = module.tier,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (module.tier) {
                                    "Beginner" -> SuccessGreen
                                    "Intermediate" -> WarningAmber
                                    else -> PrimaryIndigo
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = module.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }

                    if (isCompleted && progress != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "✓ Passed | Quiz: ${progress.quizScore}% | Project Done",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (!isUnlocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Text(
                    text = if (isCompleted) "Review >" else "Start >",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCompleted) SuccessGreen else PrimaryIndigo
                )
            }
        }
    }
}

@Composable
fun CareerModuleDetailScreen(
    module: CareerCourseModule,
    course: CareerCourse,
    existingProgress: CareerCourseProgress?,
    onBack: () -> Unit,
    onCompleteModule: (moduleId: String, quizScore: Int, isProjectCompleted: Boolean) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("📖 Theory", "❓ Practice", "💻 Exercise", "📝 Quiz", "🚀 Project")

    // Interactive Coding Exercise State
    var userCodeInput by remember { mutableStateOf(module.starterCode) }
    var exerciseOutput by remember { mutableStateOf("") }
    var isExercisePassed by remember { mutableStateOf(false) }

    // Quiz State
    val quizAnswers = remember { mutableStateMapOf<Int, Int>() }
    var quizScore by remember { mutableIntStateOf(existingProgress?.quizScore ?: 0) }
    var quizSubmitted by remember { mutableStateOf(existingProgress?.isCompleted == true) }

    // Mini Project Tasks Checklists
    val taskChecklist = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            module.miniProjectTasks.indices.forEach { idx -> put(idx, existingProgress?.isProjectCompleted == true) }
        }
    }

    val allTasksDone = module.miniProjectTasks.isNotEmpty() && taskChecklist.values.count { it } == module.miniProjectTasks.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- Top Bar ---
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_module_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                    Text(
                        text = "${course.title} • ${module.tier}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = {
                        val score = if (quizSubmitted) quizScore else 100
                        onCompleteModule(module.id, score, allTasksDone)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("complete_module_button")
                ) {
                    Text("Complete (+30 XP)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- Tabs Row ---
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        // --- Tab Content ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (selectedTab) {
                // 0: THEORY & EXPLANATION
                0 -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "💡 Clear Theory & Beginner Explanation",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryIndigo
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = module.clearTheory,
                                        style = MaterialTheme.typography.bodyMedium,
                                        lineHeight = 22.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        if (module.codeExamples.isNotEmpty()) {
                            item {
                                Text(
                                    text = "💻 Code Examples",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            items(module.codeExamples) { snippet ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = DarkBackground
                                ) {
                                    Text(
                                        text = snippet,
                                        modifier = Modifier.padding(14.dp),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = SecondaryCyan
                                    )
                                }
                            }
                        }
                    }
                }

                // 1: PRACTICE QUESTIONS
                1 -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "🧠 Practice & Concept Questions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        items(module.practiceQuestions) { pq ->
                            var expanded by remember { mutableStateOf(false) }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expanded = !expanded },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Q: ${pq.question}",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(
                                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = "Expand"
                                        )
                                    }

                                    AnimatedVisibility(visible = expanded) {
                                        Column {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = SuccessGreen.copy(alpha = 0.15f)
                                            ) {
                                                Text(
                                                    text = "Answer: ${pq.answer}",
                                                    modifier = Modifier.padding(10.dp),
                                                    fontSize = 12.sp,
                                                    color = SuccessGreen,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 2: CODING EXERCISE
                2 -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "⚡ Coding Exercise",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SecondaryCyan
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = module.codingExercisePrompt,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = userCodeInput,
                                onValueChange = { userCodeInput = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .testTag("module_code_editor"),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp
                                ),
                                label = { Text("Code Editor") }
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        if (userCodeInput.contains(module.solutionKeyword, ignoreCase = true)) {
                                            exerciseOutput = "✓ Output: Solution verified! Required keyword '${module.solutionKeyword}' detected."
                                            isExercisePassed = true
                                        } else {
                                            exerciseOutput = "❌ Verification Failed: Code must include keyword '${module.solutionKeyword}'."
                                            isExercisePassed = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("run_exercise_code_button")
                                ) {
                                    Icon(imageVector = Icons.Default.Terminal, contentDescription = "Run")
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Run Code")
                                }

                                if (isExercisePassed) {
                                    Text(
                                        text = "Exercise Passed ✓",
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        if (exerciseOutput.isNotBlank()) {
                            item {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = DarkBackground
                                ) {
                                    Text(
                                        text = exerciseOutput,
                                        modifier = Modifier.padding(12.dp),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = if (isExercisePassed) SuccessGreen else MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }

                // 3: QUIZ TEST
                3 -> {
                    CourseQuizComponent(
                        quizQuestions = module.quizQuestions,
                        initialAnswers = quizAnswers,
                        isAlreadySubmitted = quizSubmitted,
                        onQuizSubmitted = { score, _, _ ->
                            quizScore = score
                            quizSubmitted = true
                        },
                        onResetQuiz = {
                            quizAnswers.clear()
                            quizSubmitted = false
                            quizScore = 0
                        }
                    )
                }

                // 4: MINI / CAPSTONE PROJECT
                4 -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("🚀", fontSize = 24.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = module.miniProjectTitle,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = module.miniProjectDescription,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        item {
                            Text(
                                text = "📋 Required Project Deliverables",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        itemsIndexed(module.miniProjectTasks) { tIdx, taskName ->
                            val isChecked = taskChecklist[tIdx] == true

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { taskChecklist[tIdx] = it },
                                        colors = CheckboxDefaults.colors(checkedColor = SuccessGreen)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = taskName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isChecked) SuccessGreen else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = {
                                    onCompleteModule(module.id, if (quizSubmitted) quizScore else 100, true)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (allTasksDone) SuccessGreen else PrimaryIndigo
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (allTasksDone) "Project Completed ✓ (Mark Module Done)" else "Mark Project Complete",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
