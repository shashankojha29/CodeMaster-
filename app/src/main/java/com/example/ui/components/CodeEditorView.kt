package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.SecondaryCyan
import com.example.ui.theme.SuccessGreen

@Composable
fun CodeEditorView(
    code: String,
    onCodeChange: (String) -> Unit,
    hint: String,
    expectedKeywords: List<String>,
    onRunCode: () -> Unit,
    onSubmitCode: () -> Unit,
    outputMessage: String?,
    isPassed: Boolean?,
    modifier: Modifier = Modifier
) {
    var showHintDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(DarkSurface)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(SecondaryCyan.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "PYTHON EDITOR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryCyan
                    )
                }
            }

            OutlinedButton(
                onClick = { showHintDialog = true },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .height(32.dp)
                    .testTag("code_editor_hint_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Hint",
                    modifier = Modifier.padding(end = 4.dp),
                    tint = SecondaryCyan
                )
                Text("Hint", fontSize = 12.sp)
            }
        }

        // Code Editor Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(DarkBackground)
                .padding(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Line Numbers
                val linesCount = code.lineSequence().count().coerceAtLeast(1)
                Column(
                    modifier = Modifier.padding(end = 12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    for (i in 1..linesCount) {
                        Text(
                            text = "$i",
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        )
                    }
                }

                // Text Field
                BasicTextField(
                    value = code,
                    onValueChange = onCodeChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("code_editor_text_input"),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = Color(0xFF38BDF8)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
                )
            }
        }

        // Run & Submit Buttons Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                .background(DarkSurface)
                .padding(12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onRunCode,
                modifier = Modifier.testTag("run_code_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Run",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Run Test")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onSubmitCode,
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                modifier = Modifier.testTag("submit_code_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Submit",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Submit (+25 XP)", color = Color.White)
            }
        }

        // Output Result Console
        if (outputMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isPassed == true) SuccessGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.errorContainer
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isPassed == true) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Passed",
                            tint = SuccessGreen
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = outputMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isPassed == true) SuccessGreen else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (showHintDialog) {
        AlertDialog(
            onDismissRequest = { showHintDialog = false },
            title = { Text("Challenge Hint") },
            text = { Text(hint) },
            confirmButton = {
                TextButton(onClick = { showHintDialog = false }) {
                    Text("Got it")
                }
            }
        )
    }
}
