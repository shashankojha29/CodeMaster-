package com.example.ui.codelab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeLabScreen(
    initialCode: String? = null,
    initialLanguageId: String? = null,
    initialTitle: String? = null,
    onRequestAiAnswer: suspend (prompt: String) -> String = { "AI Tutor unavailable." },
    onSaveUserProject: (CodeLabProject, List<WorkspaceFile>) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Active Language
    var selectedLanguage by remember {
        mutableStateOf(CodeLabLanguages.getById(initialLanguageId ?: "python"))
    }

    // Active Theme
    var selectedTheme by remember { mutableStateOf(EditorTheme.VS_CODE_DARK) }
    var showThemeMenu by remember { mutableStateOf(false) }

    // Projects & File Workspace State
    var currentProject by remember {
        mutableStateOf(
            CodeLabProject(
                name = initialTitle ?: "${selectedLanguage.name} Workspace",
                languageId = selectedLanguage.id
            )
        )
    }

    val allProjects = remember {
        mutableStateListOf(
            currentProject,
            CodeLabProject(name = "Web Sandbox", languageId = "html"),
            CodeLabProject(name = "Kotlin Demo", languageId = "kotlin")
        )
    }

    val files = remember {
        mutableStateListOf(
            WorkspaceFile(
                name = selectedLanguage.defaultFileName,
                content = initialCode ?: selectedLanguage.starterCode,
                languageId = selectedLanguage.id
            )
        )
    }

    var activeFileId by remember { mutableStateOf(files.first().id) }

    val activeFile = files.find { it.id == activeFileId } ?: files.first()

    // Editor Code State
    var currentCode by remember(activeFile.id) { mutableStateOf(activeFile.content) }

    // Execution & Console state
    var executionOutput by remember { mutableStateOf(ExecutionOutput()) }
    var isConsoleExpanded by remember { mutableStateOf(false) }
    var runningJob by remember { mutableStateOf<Job?>(null) }

    // UI Drawers
    var isFileExplorerOpen by remember { mutableStateOf(false) }
    var isAiAssistantOpen by remember { mutableStateOf(false) }

    // Synchronize active code back into file
    fun updateCode(newCode: String) {
        currentCode = newCode
        val index = files.indexOfFirst { it.id == activeFile.id }
        if (index >= 0) {
            files[index] = files[index].copy(content = newCode, isModified = true)
        }
    }

    // Language change callback
    fun switchLanguage(newLang: CodeLabLanguage) {
        selectedLanguage = newLang
        currentProject = currentProject.copy(languageId = newLang.id)

        // Check if file exists for language or reset starter
        val defaultFile = WorkspaceFile(
            name = newLang.defaultFileName,
            content = newLang.starterCode,
            languageId = newLang.id
        )
        files.clear()
        files.add(defaultFile)
        activeFileId = defaultFile.id
        currentCode = defaultFile.content
    }

    // Run Code Action
    fun runCode() {
        if (runningJob?.isActive == true) return

        isConsoleExpanded = true
        executionOutput = ExecutionOutput(status = ExecutionStatus.RUNNING)

        runningJob = coroutineScope.launch {
            val result = CodeExecutionEngine.executeCode(
                code = currentCode,
                language = selectedLanguage,
                files = files
            )
            executionOutput = result
        }
    }

    // Stop Execution
    fun stopCode() {
        runningJob?.cancel()
        executionOutput = executionOutput.copy(
            status = ExecutionStatus.STOPPED,
            stderr = "Execution aborted by user."
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "💻 Code Lab",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Language Badge
                        Text(
                            text = selectedLanguage.iconEmoji + " " + selectedLanguage.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = selectedLanguage.color,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(selectedLanguage.color.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                },
                actions = {
                    // Save Project
                    IconButton(
                        onClick = {
                            onSaveUserProject(currentProject, files)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Project saved locally!")
                            }
                        }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save Project", tint = Color.White)
                    }

                    // Theme Picker
                    Box {
                        IconButton(onClick = { showThemeMenu = true }) {
                            Icon(Icons.Default.Palette, contentDescription = "Editor Theme", tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = showThemeMenu,
                            onDismissRequest = { showThemeMenu = false }
                        ) {
                            EditorTheme.values().forEach { th ->
                                DropdownMenuItem(
                                    text = { Text(th.displayName + if (th == selectedTheme) " ✓" else "") },
                                    onClick = {
                                        selectedTheme = th
                                        showThemeMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Toggle AI Assistant Drawer
                    IconButton(
                        onClick = { isAiAssistantOpen = !isAiAssistantOpen }
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = "AI Assistant",
                            tint = if (isAiAssistantOpen) Color(0xFFCBA6F7) else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E1E1E))
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { isAiAssistantOpen = !isAiAssistantOpen },
                icon = {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Tutor",
                        tint = Color(0xFF11111B)
                    )
                },
                text = {
                    Text(
                        text = if (isAiAssistantOpen) "Close AI Tutor" else "AI Tutor",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF11111B)
                    )
                },
                containerColor = Color(0xFFCBA6F7),
                contentColor = Color(0xFF11111B),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                modifier = Modifier.testTag("ai_tutor_fab")
            )
        },
        containerColor = Color(0xFF121212)
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val screenHeight = maxHeight
            val maxConsoleHeight = if (screenHeight < 550.dp) (screenHeight * 0.35f).coerceIn(120.dp, 180.dp) else 220.dp

            Column(modifier = Modifier.fillMaxSize()) {

                // --- LANGUAGE SELECTOR BAR ---
                Surface(
                    color = Color(0xFF181818),
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        items(CodeLabLanguages.ALL) { lang ->
                            val isSelected = lang.id == selectedLanguage.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) lang.color.copy(alpha = 0.3f) else Color(0xFF2D2D2D))
                                    .border(
                                        width = if (isSelected) 1.5.dp else 0.dp,
                                        color = if (isSelected) lang.color else Color.Transparent,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable { switchLanguage(lang) }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "${lang.iconEmoji} ${lang.name}",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color.LightGray
                                )
                            }
                        }
                    }
                }

                // --- EDITOR WORKSPACE HEADER (File Explorer Button + Open File Tabs + Run Button) ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF252526))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // File Explorer Toggle Button
                        IconButton(
                            onClick = { isFileExplorerOpen = !isFileExplorerOpen },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Folder,
                                contentDescription = "Explorer",
                                tint = if (isFileExplorerOpen) MaterialTheme.colorScheme.primary else Color.LightGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Active File Tab
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(Color(0xFF1E1E1E))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = activeFile.name + if (activeFile.isModified) " •" else "",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // RUN CODE / STOP BUTTON
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (executionOutput.status == ExecutionStatus.RUNNING) {
                            Button(
                                onClick = { stopCode() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.Stop, contentDescription = "Stop", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Stop", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = { runCode() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Run", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Run Code", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // --- CODE EDITOR COMPONENT ---
                Box(modifier = Modifier.weight(1f)) {
                    CodeEditorComponent(
                        codeValue = currentCode,
                        onCodeChange = { updateCode(it) },
                        language = selectedLanguage,
                        theme = selectedTheme,
                        onAskAiAboutCode = { prompt ->
                            isAiAssistantOpen = true
                        }
                    )
                }

                // --- OUTPUT CONSOLE ---
                CodeLabConsole(
                    executionOutput = executionOutput,
                    isExpanded = isConsoleExpanded,
                    onToggleExpand = { isConsoleExpanded = !isConsoleExpanded },
                    onClearConsole = { executionOutput = ExecutionOutput() },
                    onSendStdin = { stdin ->
                        coroutineScope.launch {
                            executionOutput = CodeExecutionEngine.executeCode(
                                code = currentCode,
                                language = selectedLanguage,
                                stdinInput = stdin,
                                files = files
                            )
                        }
                    },
                    isHtmlCssLanguage = selectedLanguage.executionType == CodeExecutionType.HTML || selectedLanguage.executionType == CodeExecutionType.CSS,
                    maxConsoleHeight = maxConsoleHeight
                )
            }

            // --- FILE EXPLORER BACKDROP SCRIM ---
            AnimatedVisibility(
                visible = isFileExplorerOpen,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { isFileExplorerOpen = false }
                )
            }

            // --- FILE EXPLORER DRAWER SLIDE-IN OVERLAY ---
            AnimatedVisibility(
                visible = isFileExplorerOpen,
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(),
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                CodeLabProjectExplorer(
                    currentProject = currentProject,
                    allProjects = allProjects,
                    files = files,
                    activeFileId = activeFileId,
                    onSelectFile = { file ->
                        activeFileId = file.id
                        currentCode = file.content
                        isFileExplorerOpen = false
                    },
                    onCreateFile = { name, isFolder ->
                        val newFile = WorkspaceFile(
                            name = name,
                            content = "// New file in ${selectedLanguage.name}\n",
                            languageId = selectedLanguage.id,
                            isDirectory = isFolder
                        )
                        files.add(newFile)
                        if (!isFolder) {
                            activeFileId = newFile.id
                            currentCode = newFile.content
                        }
                    },
                    onRenameFile = { fId, newName ->
                        val idx = files.indexOfFirst { it.id == fId }
                        if (idx >= 0) {
                            files[idx] = files[idx].copy(name = newName)
                        }
                    },
                    onDeleteFile = { fId ->
                        if (files.size > 1) {
                            files.removeAll { it.id == fId }
                            if (activeFileId == fId) {
                                activeFileId = files.first().id
                                currentCode = files.first().content
                            }
                        }
                    },
                    onCreateProject = { projName, langId ->
                        val newLang = CodeLabLanguages.getById(langId)
                        val newProj = CodeLabProject(name = projName, languageId = langId)
                        allProjects.add(newProj)
                        currentProject = newProj
                        switchLanguage(newLang)
                        isFileExplorerOpen = false
                    },
                    onSwitchProject = { proj ->
                        currentProject = proj
                        switchLanguage(CodeLabLanguages.getById(proj.languageId))
                        isFileExplorerOpen = false
                    },
                    onCloseDrawer = { isFileExplorerOpen = false }
                )
            }

            // --- AI ASSISTANT BACKDROP SCRIM ---
            AnimatedVisibility(
                visible = isAiAssistantOpen,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { isAiAssistantOpen = false }
                )
            }

            // --- AI ASSISTANT DRAWER SLIDE-IN OVERLAY ---
            AnimatedVisibility(
                visible = isAiAssistantOpen,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                CodeLabAiAssistantSheet(
                    currentCode = currentCode,
                    language = selectedLanguage,
                    lastError = if (executionOutput.status == ExecutionStatus.ERROR) executionOutput.stderr else null,
                    onApplyCodeToEditor = { newCode ->
                        updateCode(newCode)
                        isAiAssistantOpen = false
                    },
                    onCloseSheet = { isAiAssistantOpen = false },
                    onRequestAiAnswer = onRequestAiAnswer
                )
            }
        }
    }
}
