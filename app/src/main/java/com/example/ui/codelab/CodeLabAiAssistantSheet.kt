package com.example.ui.codelab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.util.UUID

data class AiChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: String, // "user" or "tutor"
    val text: String,
    val suggestedCode: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun CodeLabAiAssistantSheet(
    currentCode: String,
    language: CodeLabLanguage,
    lastError: String?,
    onApplyCodeToEditor: (String) -> Unit,
    onCloseSheet: () -> Unit,
    onRequestAiAnswer: suspend (prompt: String) -> String,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val listState = rememberLazyListState()

    var userPrompt by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var appliedCodeMap by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

    val chatHistory = remember { mutableStateListOf<AiChatMessage>() }

    val quickPrompts = remember(lastError, language) {
        val list = mutableListOf(
            "📖 Explain Code" to "Explain how this ${language.name} code works step-by-step.",
            "💡 Refactor Code" to "Suggest clean code improvements and best practices for this ${language.name} snippet.",
            "🐛 Debug Check" to "Check this ${language.name} code for potential runtime bugs or logic issues.",
            "⚡ Optimize Performance" to "Optimize the speed and memory efficiency of this code.",
            "🧪 Unit Tests" to "Generate test cases for this code."
        )
        if (!lastError.isNullOrBlank()) {
            list.add(0, "🔍 Fix Execution Error" to "Analyze and fix this error: $lastError")
        }
        list
    }

    suspend fun sendQuery(prompt: String) {
        if (prompt.isBlank() || isLoading) return

        // Append user message
        val userMsg = AiChatMessage(sender = "user", text = prompt)
        chatHistory.add(userMsg)
        isLoading = true

        coroutineScope.launch {
            if (chatHistory.isNotEmpty()) {
                listState.animateScrollToItem(chatHistory.size - 1)
            }
        }

        val fullPrompt = """
            Context: The user is writing ${language.name} code in Code Lab.
            Filename: ${language.defaultFileName}
            
            Current Code:
            ```${language.id}
            $currentCode
            ```
            ${if (!lastError.isNullOrBlank()) "Last Output / Error:\n$lastError" else ""}
            
            User Question: $prompt
            
            Instructions: Provide real-time coding assistance and context-aware feedback. Keep explanations concise and educational. If offering improved code, format it inside a code block (` ``` `).
        """.trimIndent()

        val reply = onRequestAiAnswer(fullPrompt)

        // Extract code block if suggested
        var extractedCode: String? = null
        if (reply.contains("```")) {
            val codeBlock = reply.substringAfter("```").substringBefore("```")
            val cleanCode = codeBlock.substringAfter("\n").ifBlank { codeBlock }
            if (cleanCode.isNotBlank()) {
                extractedCode = cleanCode
            }
        }

        val cleanExplanation = reply.replace(Regex("```[a-zA-Z]*"), "").replace("```", "").trim()
        val tutorMsg = AiChatMessage(
            sender = "tutor",
            text = cleanExplanation,
            suggestedCode = extractedCode
        )
        chatHistory.add(tutorMsg)
        isLoading = false

        coroutineScope.launch {
            if (chatHistory.isNotEmpty()) {
                listState.animateScrollToItem(chatHistory.size - 1)
            }
        }
    }

    Surface(
        color = Color(0xFF181825),
        contentColor = Color.White,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.9f)
            .widthIn(max = 360.dp)
            .border(1.dp, Color(0xFF313244))
            .testTag("ai_tutor_overlay")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Sheet Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFCBA6F7).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Psychology,
                            contentDescription = "AI Tutor",
                            tint = Color(0xFFCBA6F7),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Gemini AI Tutor",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Context-Aware Coding Companion",
                            fontSize = 10.sp,
                            color = Color(0xFFA6ADC8)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (chatHistory.isNotEmpty()) {
                        IconButton(
                            onClick = { chatHistory.clear() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear Chat", tint = Color(0xFFF38BA8))
                        }
                    }
                    IconButton(onClick = onCloseSheet, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close AI Tutor", tint = Color.Gray)
                    }
                }
            }

            // Quick Context Prompt Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                items(quickPrompts) { (label, promptText) ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF313244))
                            .clickable {
                                coroutineScope.launch {
                                    sendQuery(promptText)
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFCDD6F4)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main Chat Conversation Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF11111B))
                    .border(1.dp, Color(0xFF313244), RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                if (chatHistory.isEmpty() && !isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Color(0xFFF9E2AF),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Welcome to Gemini AI Tutor!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ask any questions about your ${language.name} code, request debugging, or select a quick action above.",
                            fontSize = 11.sp,
                            color = Color(0xFFA6ADC8),
                            modifier = Modifier.padding(horizontal = 16.dp),
                            lineHeight = 16.sp
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(chatHistory, key = { it.id }) { msg ->
                            val isUser = msg.sender == "user"
                            val isApplied = appliedCodeMap[msg.id] == true

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                            ) {
                                // Message Bubble
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(if (isUser) 0.85f else 0.95f)
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 12.dp,
                                                topEnd = 12.dp,
                                                bottomStart = if (isUser) 12.dp else 2.dp,
                                                bottomEnd = if (isUser) 2.dp else 12.dp
                                            )
                                        )
                                        .background(if (isUser) Color(0xFF313244) else Color(0xFF1E1E2E))
                                        .border(
                                            1.dp,
                                            if (isUser) Color(0xFF45475A) else Color(0xFF313244),
                                            RoundedCornerShape(
                                                topStart = 12.dp,
                                                topEnd = 12.dp,
                                                bottomStart = if (isUser) 12.dp else 2.dp,
                                                bottomEnd = if (isUser) 2.dp else 12.dp
                                            )
                                        )
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = if (isUser) "You" else "Gemini Tutor",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = if (isUser) Color(0xFF89B4FA) else Color(0xFFCBA6F7)
                                            )

                                            IconButton(
                                                onClick = {
                                                    clipboardManager.setText(AnnotatedString(msg.text))
                                                },
                                                modifier = Modifier.size(20.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.ContentCopy,
                                                    contentDescription = "Copy Text",
                                                    tint = Color.Gray,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        SelectionContainer {
                                            Text(
                                                text = msg.text,
                                                fontSize = 12.sp,
                                                color = Color(0xFFCDD6F4),
                                                lineHeight = 17.sp
                                            )
                                        }

                                        // Suggested Code Block
                                        if (!msg.suggestedCode.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFF11111B))
                                                    .border(1.dp, Color(0xFF45475A), RoundedCornerShape(6.dp))
                                                    .padding(8.dp)
                                            ) {
                                                Text(
                                                    text = "💡 Suggested Code:",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFA6E3A1)
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                SelectionContainer {
                                                    Text(
                                                        text = msg.suggestedCode,
                                                        fontFamily = FontFamily.Monospace,
                                                        fontSize = 10.sp,
                                                        color = Color(0xFFBAC2DE),
                                                        lineHeight = 14.sp
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))

                                                Button(
                                                    onClick = {
                                                        onApplyCodeToEditor(msg.suggestedCode)
                                                        appliedCodeMap = appliedCodeMap + (msg.id to true)
                                                    },
                                                    enabled = !isApplied,
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFFA6E3A1),
                                                        contentColor = Color(0xFF11111B)
                                                    ),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Icon(
                                                        imageVector = if (isApplied) Icons.Default.Check else Icons.Default.Code,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = if (isApplied) "Applied to Editor" else "Apply to Editor",
                                                        fontSize = 11.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (isLoading) {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    CircularProgressIndicator(
                                        color = Color(0xFFCBA6F7),
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Gemini AI is analyzing code...",
                                        fontSize = 11.sp,
                                        color = Color(0xFFA6ADC8)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // User Input Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = userPrompt,
                    onValueChange = { userPrompt = it },
                    placeholder = { Text("Ask Gemini AI Tutor...", color = Color.Gray, fontSize = 12.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFCBA6F7),
                        unfocusedBorderColor = Color(0xFF45475A)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_tutor_input")
                )

                Button(
                    onClick = {
                        val promptToRun = userPrompt
                        if (promptToRun.isNotBlank()) {
                            userPrompt = ""
                            coroutineScope.launch {
                                sendQuery(promptToRun)
                            }
                        }
                    },
                    enabled = userPrompt.isNotBlank() && !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFCBA6F7),
                        contentColor = Color(0xFF11111B)
                    ),
                    modifier = Modifier.testTag("ai_tutor_send_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
