package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BentoPrimary
import com.example.ui.theme.BentoPrimaryContainer

data class DailyPracticeData(
    val dayLabel: String,
    val hours: Float,
    val isToday: Boolean = false
)

@Composable
fun DailyPracticeHoursChart(
    modifier: Modifier = Modifier,
    weeklyData: List<DailyPracticeData> = defaultWeeklyPracticeData()
) {
    val totalHours = weeklyData.sumOf { it.hours.toDouble() }
    val avgHours = if (weeklyData.isNotEmpty()) totalHours / weeklyData.size else 0.0

    var selectedDayIndex by remember { mutableStateOf(weeklyData.indexOfFirst { it.isToday }.coerceAtLeast(weeklyData.lastIndex)) }
    val selectedData = weeklyData.getOrNull(selectedDayIndex) ?: weeklyData.last()

    val textMeasurer = rememberTextMeasurer()
    val lineColor = BentoPrimary
    val gradientColor = BentoPrimary.copy(alpha = 0.25f)
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val labelTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val highlightColor = MaterialTheme.colorScheme.primary

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_practice_line_chart_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth()
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BentoPrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Practice Hours",
                            tint = BentoPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "WEEKLY PRACTICE TIME",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = BentoPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "${"%.1f".format(totalHours)} hrs past 7 days",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BentoPrimaryContainer)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = "Average",
                            tint = BentoPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${"%.1f".format(avgHours)}h/day avg",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selected Day Tooltip Info Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Selected Day: ${selectedData.dayLabel}${if (selectedData.isToday) " (Today)" else ""}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${"%.1f".format(selectedData.hours)} hours",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BentoPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Line Chart Canvas Area
            val maxHours = (weeklyData.maxOfOrNull { it.hours } ?: 4.0f).coerceAtLeast(3.0f) * 1.15f

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .pointerInput(weeklyData) {
                        detectTapGestures { tapOffset ->
                            val width = size.width
                            val paddingLeft = 40.dp.toPx()
                            val paddingRight = 16.dp.toPx()
                            val chartWidth = width - paddingLeft - paddingRight
                            val stepX = chartWidth / (weeklyData.size - 1).coerceAtLeast(1)

                            val tappedX = tapOffset.x - paddingLeft
                            val closestIndex = (tappedX / stepX)
                                .toInt()
                                .coerceIn(0, weeklyData.lastIndex)

                            selectedDayIndex = closestIndex
                        }
                    }
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                val paddingLeft = 40.dp.toPx()
                val paddingRight = 16.dp.toPx()
                val paddingTop = 20.dp.toPx()
                val paddingBottom = 30.dp.toPx()

                val chartWidth = canvasWidth - paddingLeft - paddingRight
                val chartHeight = canvasHeight - paddingTop - paddingBottom

                // 1. Draw horizontal grid lines and Y-axis text labels (0h, 1h, 2h, 3h, 4h...)
                val gridSteps = 4
                for (i in 0..gridSteps) {
                    val value = maxHours * (i.toFloat() / gridSteps)
                    val y = paddingTop + chartHeight - (i.toFloat() / gridSteps) * chartHeight

                    // Grid line
                    drawLine(
                        color = gridColor,
                        start = Offset(paddingLeft, y),
                        end = Offset(canvasWidth - paddingRight, y),
                        strokeWidth = 1.dp.toPx()
                    )

                    // Y Label
                    val labelText = "%.1fh".format(value)
                    val textLayoutResult = textMeasurer.measure(
                        text = labelText,
                        style = TextStyle(
                            fontSize = 9.sp,
                            color = labelTextColor,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(
                            x = paddingLeft - textLayoutResult.size.width - 6.dp.toPx(),
                            y = y - textLayoutResult.size.height / 2f
                        )
                    )
                }

                // 2. Compute Points for Line
                val stepX = chartWidth / (weeklyData.size - 1).coerceAtLeast(1)
                val points = weeklyData.mapIndexed { index, data ->
                    val x = paddingLeft + index * stepX
                    val y = paddingTop + chartHeight - ((data.hours / maxHours) * chartHeight)
                    Offset(x, y)
                }

                // 3. Build Smooth Path (Cubic Bezier)
                val linePath = Path()
                val fillPath = Path()

                if (points.isNotEmpty()) {
                    linePath.moveTo(points.first().x, points.first().y)
                    fillPath.moveTo(points.first().x, paddingTop + chartHeight)
                    fillPath.lineTo(points.first().x, points.first().y)

                    for (i in 0 until points.size - 1) {
                        val p1 = points[i]
                        val p2 = points[i + 1]

                        val controlX1 = p1.x + (p2.x - p1.x) / 2f
                        val controlY1 = p1.y
                        val controlX2 = p1.x + (p2.x - p1.x) / 2f
                        val controlY2 = p2.y

                        linePath.cubicTo(controlX1, controlY1, controlX2, controlY2, p2.x, p2.y)
                        fillPath.cubicTo(controlX1, controlY1, controlX2, controlY2, p2.x, p2.y)
                    }

                    fillPath.lineTo(points.last().x, paddingTop + chartHeight)
                    fillPath.close()

                    // Draw Fill Gradient
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                gradientColor,
                                gradientColor.copy(alpha = 0.0f)
                            ),
                            startY = paddingTop,
                            endY = paddingTop + chartHeight
                        )
                    )

                    // Draw Line
                    drawPath(
                        path = linePath,
                        color = lineColor,
                        style = Stroke(
                            width = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }

                // 4. Draw X-axis Day Labels and Data Point Markers
                weeklyData.forEachIndexed { index, data ->
                    val point = points[index]
                    val isSelected = index == selectedDayIndex

                    // Draw X Day Label below chart
                    val dayLabelTextResult = textMeasurer.measure(
                        text = data.dayLabel,
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (isSelected) highlightColor else labelTextColor
                        )
                    )
                    drawText(
                        textLayoutResult = dayLabelTextResult,
                        topLeft = Offset(
                            x = point.x - dayLabelTextResult.size.width / 2f,
                            y = paddingTop + chartHeight + 8.dp.toPx()
                        )
                    )

                    // Draw vertical guide line for selected point
                    if (isSelected) {
                        drawLine(
                            color = highlightColor.copy(alpha = 0.35f),
                            start = Offset(point.x, paddingTop),
                            end = Offset(point.x, paddingTop + chartHeight),
                            strokeWidth = 1.5.dp.toPx()
                        )
                    }

                    // Outer node circle
                    drawCircle(
                        color = if (isSelected) highlightColor else lineColor,
                        radius = if (isSelected) 7.dp.toPx() else 4.dp.toPx(),
                        center = point
                    )

                    // Inner node dot
                    drawCircle(
                        color = Color.White,
                        radius = if (isSelected) 3.5.dp.toPx() else 2.dp.toPx(),
                        center = point
                    )
                }
            }
        }
    }
}

fun defaultWeeklyPracticeData(): List<DailyPracticeData> {
    return listOf(
        DailyPracticeData("Mon", 1.5f),
        DailyPracticeData("Tue", 2.2f),
        DailyPracticeData("Wed", 3.0f),
        DailyPracticeData("Thu", 1.8f),
        DailyPracticeData("Fri", 2.5f),
        DailyPracticeData("Sat", 4.0f),
        DailyPracticeData("Sun", 2.8f, isToday = true)
    )
}
