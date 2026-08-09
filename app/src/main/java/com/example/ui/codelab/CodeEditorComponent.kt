package com.example.ui.codelab

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun CodeEditorComponent(
    codeValue: String,
    onCodeChange: (String) -> Unit,
    language: CodeLabLanguage,
    theme: EditorTheme = EditorTheme.VS_CODE_DARK,
    modifier: Modifier = Modifier,
    onAskAiAboutCode: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val colorConfig = CodeLabThemes.getColorConfig(theme)

    // Editor Undo/Redo state history
    val history = remember { mutableStateListOf<String>() }
    var historyIndex by remember { mutableStateOf(-1) }

    // Find & Replace state
    var showFindReplace by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var replaceQuery by remember { mutableStateOf("") }

    // TextField state
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = codeValue))
    }

    LaunchedEffect(codeValue) {
        if (textFieldValue.text != codeValue) {
            val safeSelectionIndex = codeValue.length.coerceAtMost(textFieldValue.selection.max)
            textFieldValue = textFieldValue.copy(
                text = codeValue,
                selection = androidx.compose.ui.text.TextRange(safeSelectionIndex)
            )
        }
    }

    // Scroll states
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    // Add to history if new
    fun updateCodeWithHistory(newText: String) {
        if (newText != codeValue) {
            if (historyIndex < history.size - 1) {
                while (history.size > historyIndex + 1) {
                    history.removeAt(history.size - 1)
                }
            }
            history.add(newText)
            if (history.size > 50) history.removeAt(0)
            historyIndex = history.size - 1

            onCodeChange(newText)
            textFieldValue = TextFieldValue(text = newText, selection = androidx.compose.ui.text.TextRange(newText.length))
        }
    }

    // Initialize history once
    LaunchedEffect(Unit) {
        if (history.isEmpty()) {
            history.add(codeValue)
            historyIndex = 0
        }
    }

    // Undo / Redo
    fun undo() {
        if (historyIndex > 0) {
            historyIndex--
            val prevText = history[historyIndex]
            onCodeChange(prevText)
            textFieldValue = TextFieldValue(text = prevText, selection = androidx.compose.ui.text.TextRange(prevText.length))
        }
    }

    fun redo() {
        if (historyIndex < history.size - 1) {
            historyIndex++
            val nextText = history[historyIndex]
            onCodeChange(nextText)
            textFieldValue = TextFieldValue(text = nextText, selection = androidx.compose.ui.text.TextRange(nextText.length))
        }
    }

    // Auto Format Code
    fun formatCode() {
        val lines = codeValue.lines()
        var indentLevel = 0
        val formattedLines = mutableListOf<String>()

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("}") || trimmed.startsWith("]") || trimmed.startsWith(")")) {
                indentLevel = (indentLevel - 1).coerceAtLeast(0)
            }
            val indent = "    ".repeat(indentLevel)
            formattedLines.add(if (trimmed.isEmpty()) "" else "$indent$trimmed")
            if (trimmed.endsWith("{") || trimmed.endsWith(":") || trimmed.endsWith("(")) {
                indentLevel++
            }
        }
        val formatted = formattedLines.joinToString("\n")
        updateCodeWithHistory(formatted)
    }

    // Copy / Paste
    fun copyAll() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("code", codeValue)
        clipboard.setPrimaryClip(clip)
    }

    fun paste() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.hasPrimaryClip()) {
            val item = clipboard.primaryClip?.getItemAt(0)
            val pastedText = item?.text?.toString() ?: ""
            if (pastedText.isNotEmpty()) {
                val newCode = codeValue + "\n" + pastedText
                updateCodeWithHistory(newCode)
            }
        }
    }

    // Suggestions list
    val suggestions = remember(language, codeValue) {
        getAutocompleteSuggestions(codeValue, language)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorConfig.editorBackground)
    ) {
        // --- EDITOR TOOLBAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorConfig.currentLineBackground)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Quick action chips
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { undo() },
                    enabled = historyIndex > 0,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Undo,
                        contentDescription = "Undo",
                        tint = if (historyIndex > 0) colorConfig.editorTextColor else colorConfig.lineNumberColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { redo() },
                    enabled = historyIndex < history.size - 1,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Redo,
                        contentDescription = "Redo",
                        tint = if (historyIndex < history.size - 1) colorConfig.editorTextColor else colorConfig.lineNumberColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { formatCode() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.AutoFixHigh,
                        contentDescription = "Format Code",
                        tint = colorConfig.editorTextColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { showFindReplace = !showFindReplace },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Find & Replace",
                        tint = if (showFindReplace) colorConfig.keywordColor else colorConfig.editorTextColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { copyAll() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = "Copy All",
                        tint = colorConfig.editorTextColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { paste() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.ContentPaste,
                        contentDescription = "Paste",
                        tint = colorConfig.editorTextColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // --- FIND & REPLACE BAR ---
        AnimatedVisibility(visible = showFindReplace) {
            Surface(
                color = colorConfig.currentLineBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Find text...", color = colorConfig.lineNumberColor) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = colorConfig.editorTextColor,
                                unfocusedTextColor = colorConfig.editorTextColor,
                                focusedBorderColor = colorConfig.keywordColor,
                                unfocusedBorderColor = colorConfig.lineNumberColor
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = replaceQuery,
                            onValueChange = { replaceQuery = it },
                            placeholder = { Text("Replace with...", color = colorConfig.lineNumberColor) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = colorConfig.editorTextColor,
                                unfocusedTextColor = colorConfig.editorTextColor,
                                focusedBorderColor = colorConfig.keywordColor,
                                unfocusedBorderColor = colorConfig.lineNumberColor
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = { showFindReplace = false },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close Find", tint = colorConfig.editorTextColor)
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                if (searchQuery.isNotEmpty() && codeValue.contains(searchQuery)) {
                                    val newText = codeValue.replaceFirst(searchQuery, replaceQuery)
                                    updateCodeWithHistory(newText)
                                }
                            }
                        ) {
                            Text("Replace Next", color = colorConfig.keywordColor)
                        }

                        TextButton(
                            onClick = {
                                if (searchQuery.isNotEmpty() && codeValue.contains(searchQuery)) {
                                    val newText = codeValue.replace(searchQuery, replaceQuery)
                                    updateCodeWithHistory(newText)
                                }
                            }
                        ) {
                            Text("Replace All", color = colorConfig.keywordColor)
                        }
                    }
                }
            }
        }

        // --- MAIN CODE EDITOR AREA (Line Numbers + Highlighting TextField) ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScroll)
            ) {
                // Line Numbers Gutter
                val lineCount = codeValue.lines().size.coerceAtLeast(1)
                Column(
                    modifier = Modifier
                        .background(colorConfig.editorBackground.copy(alpha = 0.95f))
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    for (i in 1..lineCount) {
                        Text(
                            text = "$i",
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = colorConfig.lineNumberColor,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }

                // Vertical Divider Line
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(colorConfig.lineNumberColor.copy(alpha = 0.3f))
                )

                // Editable Code Field with Syntax Highlighting
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .horizontalScroll(horizontalScroll)
                ) {
                    BasicTextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            textFieldValue = newValue
                            if (newValue.text != codeValue) {
                                onCodeChange(newValue.text)
                                if (historyIndex < history.size - 1) {
                                    while (history.size > historyIndex + 1) {
                                        history.removeAt(history.size - 1)
                                    }
                                }
                                history.add(newValue.text)
                                if (history.size > 50) history.removeAt(0)
                                historyIndex = history.size - 1
                            }
                        },
                        textStyle = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            color = colorConfig.editorTextColor,
                            lineHeight = 20.sp
                        ),
                        cursorBrush = SolidColor(colorConfig.keywordColor),
                        keyboardOptions = KeyboardOptions(
                            autoCorrectEnabled = false,
                            imeAction = ImeAction.Default
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = { text ->
                            val highlighted = SyntaxHighlighter.highlight(text.text, language, colorConfig)
                            androidx.compose.ui.text.input.TransformedText(
                                highlighted,
                                androidx.compose.ui.text.input.OffsetMapping.Identity
                            )
                        }
                    )
                }
            }
        }

        // --- SMART CODE SUGGESTIONS / AUTOCOMPLETE CHIP BAR ---
        if (suggestions.isNotEmpty()) {
            Surface(
                color = colorConfig.currentLineBackground,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    Text(
                        text = "✨ Smart Suggestions (${language.name}):",
                        style = TextStyle(fontSize = 10.sp, color = colorConfig.lineNumberColor),
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 6.dp)
                    ) {
                        items(suggestions) { item ->
                            SuggestionChip(
                                suggestion = item,
                                colorConfig = colorConfig,
                                onClick = {
                                    val currentText = codeValue
                                    val newText = if (currentText.isBlank()) {
                                        item.snippet
                                    } else {
                                        "$currentText ${item.snippet}"
                                    }
                                    updateCodeWithHistory(newText)
                                }
                            )
                        }
                    }
                }
            }
        }

        // --- QUICK SYMBOL TOOLBAR FOR MOBILE KEYBOARD ---
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(colorConfig.editorBackground)
                .border(1.dp, colorConfig.lineNumberColor.copy(alpha = 0.2f))
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            val symbolKeys = listOf("Tab", "{", "}", "(", ")", "[", "]", "\"", "'", ":", ";", "=", "+", "-", "*", "/", "<", ">", "_", "!")
            items(symbolKeys) { symbol ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(colorConfig.currentLineBackground)
                        .clickable {
                            val insert = if (symbol == "Tab") "    " else symbol
                            val newText = codeValue + insert
                            updateCodeWithHistory(newText)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = symbol,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorConfig.editorTextColor
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionChip(
    suggestion: AutocompleteSuggestion,
    colorConfig: SyntaxColorConfig,
    onClick: () -> Unit
) {
    val chipColor = when (suggestion.type) {
        SuggestionType.KEYWORD -> colorConfig.keywordColor
        SuggestionType.FUNCTION -> colorConfig.functionColor
        SuggestionType.SNIPPET -> colorConfig.typeColor
        else -> colorConfig.stringColor
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(chipColor.copy(alpha = 0.15f))
            .border(1.dp, chipColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = suggestion.text,
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = chipColor
            )
        )
        if (suggestion.description.isNotBlank()) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = suggestion.description,
                style = TextStyle(fontSize = 10.sp, color = colorConfig.lineNumberColor)
            )
        }
    }
}

private fun highlightSyntax(
    code: String,
    language: CodeLabLanguage,
    colorConfig: SyntaxColorConfig
): AnnotatedString {
    return SyntaxHighlighter.highlight(code, language, colorConfig)
}

private fun getAutocompleteSuggestions(
    code: String,
    language: CodeLabLanguage
): List<AutocompleteSuggestion> {
    val list = mutableListOf<AutocompleteSuggestion>()

    // Keywords
    for (kw in language.keywords.take(8)) {
        list.add(AutocompleteSuggestion(kw, SuggestionType.KEYWORD, "keyword", kw))
    }

    // Common functions
    for (fn in language.commonFunctions.take(6)) {
        list.add(AutocompleteSuggestion(fn, SuggestionType.FUNCTION, "function", fn))
    }

    // Snippets
    for ((title, snippet) in language.commonSnippets) {
        list.add(AutocompleteSuggestion(title, SuggestionType.SNIPPET, "snippet", snippet))
    }

    return list
}
