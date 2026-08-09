package com.example.ui.codelab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CodeLabProjectExplorer(
    currentProject: CodeLabProject,
    allProjects: List<CodeLabProject>,
    files: List<WorkspaceFile>,
    activeFileId: String,
    onSelectFile: (WorkspaceFile) -> Unit,
    onCreateFile: (name: String, isFolder: Boolean) -> Unit,
    onRenameFile: (fileId: String, newName: String) -> Unit,
    onDeleteFile: (fileId: String) -> Unit,
    onCreateProject: (name: String, languageId: String) -> Unit,
    onSwitchProject: (CodeLabProject) -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateFileDialog by remember { mutableStateOf(false) }
    var isNewFolderDialog by remember { mutableStateOf(false) }
    var newFileName by remember { mutableStateOf("") }

    var showCreateProjectDialog by remember { mutableStateOf(false) }
    var newProjectName by remember { mutableStateOf("") }
    var selectedProjectLangId by remember { mutableStateOf(currentProject.languageId) }

    var editingFileId by remember { mutableStateOf<String?>(null) }
    var renameFileName by remember { mutableStateOf("") }

    var showProjectDropdown by remember { mutableStateOf(false) }

    Surface(
        color = Color(0xFF181818),
        contentColor = Color(0xFFCCCCCC),
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.85f)
            .widthIn(max = 280.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // --- PROJECT HEADER & SWITCHER ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF252526))
                    .clickable { showProjectDropdown = !showProjectDropdown }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.FolderOpen,
                        contentDescription = "Project",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = currentProject.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Lang: ${currentProject.languageId.uppercase()}",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }

                IconButton(
                    onClick = { showProjectDropdown = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Projects", tint = Color.LightGray)
                }

                DropdownMenu(
                    expanded = showProjectDropdown,
                    onDismissRequest = { showProjectDropdown = false }
                ) {
                    Text(
                        text = "Projects Workspace",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                    allProjects.forEach { proj ->
                        DropdownMenuItem(
                            text = { Text(proj.name + if (proj.id == currentProject.id) " (Active)" else "") },
                            onClick = {
                                showProjectDropdown = false
                                onSwitchProject(proj)
                            }
                        )
                    }
                    Divider()
                    DropdownMenuItem(
                        text = { Text("+ Create New Project", color = MaterialTheme.colorScheme.primary) },
                        onClick = {
                            showProjectDropdown = false
                            showCreateProjectDialog = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- EXPLORER ACTION TOOLBAR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "FILES & FOLDERS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Row {
                    IconButton(
                        onClick = {
                            isNewFolderDialog = false
                            newFileName = ""
                            showCreateFileDialog = true
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "New File", tint = Color.LightGray)
                    }

                    IconButton(
                        onClick = {
                            isNewFolderDialog = true
                            newFileName = ""
                            showCreateFileDialog = true
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.CreateNewFolder, contentDescription = "New Folder", tint = Color.LightGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- FILE TREE LIST ---
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(files) { file ->
                    val isActive = file.id == activeFileId
                    val isFolder = file.isDirectory

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isActive) Color(0xFF37373D) else Color.Transparent)
                            .clickable { if (!isFolder) onSelectFile(file) }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (isFolder) Icons.Default.Folder else Icons.Default.Description,
                                contentDescription = null,
                                tint = if (isFolder) Color(0xFFE5C07B) else if (isActive) MaterialTheme.colorScheme.primary else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = file.name,
                                fontSize = 13.sp,
                                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isActive) Color.White else Color(0xFFCCCCCC),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // File Quick Menu
                        Row {
                            IconButton(
                                onClick = {
                                    editingFileId = file.id
                                    renameFileName = file.name
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Rename", tint = Color.Gray, modifier = Modifier.size(12.dp))
                            }

                            if (files.size > 1) {
                                IconButton(
                                    onClick = { onDeleteFile(file.id) },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE57373), modifier = Modifier.size(12.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- CREATE FILE / FOLDER DIALOG ---
    if (showCreateFileDialog) {
        AlertDialog(
            onDismissRequest = { showCreateFileDialog = false },
            title = { Text(if (isNewFolderDialog) "Create New Folder" else "Create New File") },
            text = {
                OutlinedTextField(
                    value = newFileName,
                    onValueChange = { newFileName = it },
                    label = { Text(if (isNewFolderDialog) "Folder Name" else "File Name (e.g., utils.py)") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newFileName.isNotBlank()) {
                            onCreateFile(newFileName.trim(), isNewFolderDialog)
                            showCreateFileDialog = false
                        }
                    }
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateFileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // --- RENAME FILE DIALOG ---
    if (editingFileId != null) {
        AlertDialog(
            onDismissRequest = { editingFileId = null },
            title = { Text("Rename File") },
            text = {
                OutlinedTextField(
                    value = renameFileName,
                    onValueChange = { renameFileName = it },
                    label = { Text("New Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val fileId = editingFileId
                        if (fileId != null && renameFileName.isNotBlank()) {
                            onRenameFile(fileId, renameFileName.trim())
                            editingFileId = null
                        }
                    }
                ) {
                    Text("Rename")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingFileId = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // --- CREATE PROJECT DIALOG ---
    if (showCreateProjectDialog) {
        AlertDialog(
            onDismissRequest = { showCreateProjectDialog = false },
            title = { Text("New Code Lab Project") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newProjectName,
                        onValueChange = { newProjectName = it },
                        label = { Text("Project Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newProjectName.isNotBlank()) {
                            onCreateProject(newProjectName.trim(), selectedProjectLangId)
                            showCreateProjectDialog = false
                        }
                    }
                ) {
                    Text("Create Project")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateProjectDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
