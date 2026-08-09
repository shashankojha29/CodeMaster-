package com.example.ui.codelab

import androidx.compose.ui.graphics.Color
import java.util.UUID

data class WorkspaceFile(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val path: String = name,
    val content: String = "",
    val languageId: String = "python",
    val isDirectory: Boolean = false,
    val parentId: String? = null,
    val isModified: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)

data class CodeLabProject(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val languageId: String = "python",
    val description: String = "Code Lab Workspace Project",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class ExecutionStatus {
    IDLE,
    RUNNING,
    SUCCESS,
    ERROR,
    STOPPED
}

data class ExecutionOutput(
    val status: ExecutionStatus = ExecutionStatus.IDLE,
    val stdout: String = "",
    val stderr: String = "",
    val executionTimeMs: Long = 0,
    val exitCode: Int = 0,
    val htmlResult: String? = null
)

enum class EditorTheme(val displayName: String, val isDark: Boolean) {
    VS_CODE_DARK("VS Code Dark", true),
    VS_CODE_LIGHT("VS Code Light", false),
    ONE_DARK("One Dark Pro", true),
    MONOKAI("Monokai", true),
    SOLARIZED_LIGHT("Solarized Light", false)
}

data class SyntaxColorConfig(
    val keywordColor: Color,
    val stringColor: Color,
    val numberColor: Color,
    val commentColor: Color,
    val functionColor: Color,
    val typeColor: Color,
    val punctuationColor: Color,
    val editorBackground: Color,
    val editorTextColor: Color,
    val lineNumberColor: Color,
    val currentLineBackground: Color,
    val selectionBackground: Color
)

object CodeLabThemes {
    val VsCodeDark = SyntaxColorConfig(
        keywordColor = Color(0xFF569CD6),
        stringColor = Color(0xFFCE9178),
        numberColor = Color(0xFFB5CEA8),
        commentColor = Color(0xFF6A9955),
        functionColor = Color(0xDCDCAA00),
        typeColor = Color(0xFF4EC9B0),
        punctuationColor = Color(0xFFD4D4D4),
        editorBackground = Color(0xFF1E1E1E),
        editorTextColor = Color(0xFFD4D4D4),
        lineNumberColor = Color(0xFF858585),
        currentLineBackground = Color(0xFF282828),
        selectionBackground = Color(0xFF264F78)
    )

    val VsCodeLight = SyntaxColorConfig(
        keywordColor = Color(0xFF0000FF),
        stringColor = Color(0xFFA31515),
        numberColor = Color(0xFF098658),
        commentColor = Color(0xFF008000),
        functionColor = Color(0xFF795E26),
        typeColor = Color(0xFF267F99),
        punctuationColor = Color(0xFF000000),
        editorBackground = Color(0xFFFFFFFF),
        editorTextColor = Color(0xFF000000),
        lineNumberColor = Color(0xFF237804),
        currentLineBackground = Color(0xFFF3F3F3),
        selectionBackground = Color(0xFFA6D2FF)
    )

    val OneDark = SyntaxColorConfig(
        keywordColor = Color(0xFFC678DD),
        stringColor = Color(0xFF98C379),
        numberColor = Color(0xD67000FF),
        commentColor = Color(0xFF5C6370),
        functionColor = Color(0xFF61AFEF),
        typeColor = Color(0xFFE5C07B),
        punctuationColor = Color(0xFFABB2BF),
        editorBackground = Color(0xFF282C34),
        editorTextColor = Color(0xFFABB2BF),
        lineNumberColor = Color(0xFF4B5263),
        currentLineBackground = Color(0xFF2C313C),
        selectionBackground = Color(0xFF3E4451)
    )

    fun getColorConfig(theme: EditorTheme): SyntaxColorConfig {
        return when (theme) {
            EditorTheme.VS_CODE_DARK -> VsCodeDark
            EditorTheme.VS_CODE_LIGHT -> VsCodeLight
            EditorTheme.ONE_DARK -> OneDark
            EditorTheme.MONOKAI -> VsCodeDark.copy(
                editorBackground = Color(0xFF272822),
                keywordColor = Color(0xFFF92672),
                stringColor = Color(0xFFE6DB74),
                functionColor = Color(0xFFA6E22E)
            )
            EditorTheme.SOLARIZED_LIGHT -> VsCodeLight.copy(
                editorBackground = Color(0xFFFDF6E3),
                editorTextColor = Color(0xFF657B83),
                keywordColor = Color(0xFF859900)
            )
        }
    }
}

data class AutocompleteSuggestion(
    val text: String,
    val type: SuggestionType,
    val description: String = "",
    val snippet: String = text
)

enum class SuggestionType {
    KEYWORD,
    FUNCTION,
    VARIABLE,
    CLASS,
    SNIPPET,
    METHOD
}
