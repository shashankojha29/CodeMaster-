package com.example.ui.codelab

import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

enum class ConsoleTab {
    OUTPUT,
    ERRORS,
    INPUT,
    LIVE_PREVIEW
}

@Composable
fun CodeLabConsole(
    executionOutput: ExecutionOutput,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onClearConsole: () -> Unit,
    onSendStdin: (String) -> Unit,
    isHtmlCssLanguage: Boolean = false,
    maxConsoleHeight: androidx.compose.ui.unit.Dp = 220.dp,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember(isHtmlCssLanguage) {
        mutableStateOf(if (isHtmlCssLanguage) ConsoleTab.LIVE_PREVIEW else ConsoleTab.OUTPUT)
    }

    var stdinText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .border(1.dp, Color(0xFF333333))
    ) {
        // --- CONSOLE HEADER TOOLBAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF252526))
                .clickable { onToggleExpand() }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Terminal,
                    contentDescription = "Console",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "OUTPUT CONSOLE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Status Badge
                StatusBadge(status = executionOutput.status, timeMs = executionOutput.executionTimeMs)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onClearConsole,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray, modifier = Modifier.size(16.dp))
                }

                IconButton(
                    onClick = onToggleExpand,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                        contentDescription = "Expand/Collapse",
                        tint = Color.White
                    )
                }
            }
        }

        // --- CONSOLE CONTENT (Expanded) ---
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxConsoleHeight)
            ) {
                // Console Tabs
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = Color(0xFF2D2D2D),
                    contentColor = MaterialTheme.colorScheme.primary,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    if (isHtmlCssLanguage) {
                        Tab(
                            selected = selectedTab == ConsoleTab.LIVE_PREVIEW,
                            onClick = { selectedTab = ConsoleTab.LIVE_PREVIEW },
                            text = { Text("🌐 Live Preview", fontSize = 11.sp, color = Color.White) }
                        )
                    }

                    Tab(
                        selected = selectedTab == ConsoleTab.OUTPUT,
                        onClick = { selectedTab = ConsoleTab.OUTPUT },
                        text = { Text("Output (${if (executionOutput.stdout.isNotBlank()) "Active" else "Empty"})", fontSize = 11.sp, color = Color.White) }
                    )

                    Tab(
                        selected = selectedTab == ConsoleTab.ERRORS,
                        onClick = { selectedTab = ConsoleTab.ERRORS },
                        text = { Text("Errors (${if (executionOutput.stderr.isNotBlank()) "1" else "0"})", fontSize = 11.sp, color = if (executionOutput.stderr.isNotBlank()) Color(0xFFF44336) else Color.White) }
                    )

                    Tab(
                        selected = selectedTab == ConsoleTab.INPUT,
                        onClick = { selectedTab = ConsoleTab.INPUT },
                        text = { Text("STDIN Input", fontSize = 11.sp, color = Color.White) }
                    )
                }

                // Tab Content Body
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1E1E1E))
                        .padding(8.dp)
                ) {
                    when (selectedTab) {
                        ConsoleTab.LIVE_PREVIEW -> {
                            val html = executionOutput.htmlResult ?: "<h3>No HTML content rendered.</h3>"
                            AndroidView(
                                factory = { context ->
                                    WebView(context).apply {
                                        webViewClient = WebViewClient()
                                        settings.javaScriptEnabled = true
                                    }
                                },
                                update = { webView ->
                                    webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        ConsoleTab.OUTPUT -> {
                            val textToShow = executionOutput.stdout.ifBlank {
                                "Console output ready. Click 'Run Code' ▶️ to execute."
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollState)
                            ) {
                                Text(
                                    text = textToShow,
                                    style = TextStyle(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = if (executionOutput.stdout.isBlank()) Color.Gray else Color(0xFF00FF66),
                                        lineHeight = 18.sp
                                    )
                                )
                            }
                        }

                        ConsoleTab.ERRORS -> {
                            val errText = executionOutput.stderr.ifBlank { "No errors reported." }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollState)
                            ) {
                                Text(
                                    text = errText,
                                    style = TextStyle(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = if (executionOutput.stderr.isBlank()) Color.Gray else Color(0xFFFF5252),
                                        lineHeight = 18.sp
                                    )
                                )
                            }
                        }

                        ConsoleTab.INPUT -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Provide input arguments for interactive programs (e.g. input() or Scanner):",
                                    fontSize = 11.sp,
                                    color = Color.LightGray
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = stdinText,
                                        onValueChange = { stdinText = it },
                                        placeholder = { Text("Enter input values...", color = Color.Gray) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            unfocusedBorderColor = Color.Gray
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )

                                    Button(
                                        onClick = {
                                            if (stdinText.isNotBlank()) {
                                                onSendStdin(stdinText)
                                                stdinText = ""
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: ExecutionStatus, timeMs: Long) {
    val (color, text, icon) = when (status) {
        ExecutionStatus.IDLE -> Triple(Color.Gray, "Idle", null)
        ExecutionStatus.RUNNING -> Triple(Color(0xFF2196F3), "Executing...", null)
        ExecutionStatus.SUCCESS -> Triple(Color(0xFF4CAF50), "Passed (${timeMs}ms)", Icons.Default.CheckCircle)
        ExecutionStatus.ERROR -> Triple(Color(0xFFF44336), "Failed", Icons.Default.Error)
        ExecutionStatus.STOPPED -> Triple(Color(0xFFFF9800), "Stopped", null)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(text = text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
