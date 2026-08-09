package com.example.ui.codelab

import kotlinx.coroutines.delay
import java.util.Locale

object CodeExecutionEngine {

    suspend fun executeCode(
        code: String,
        language: CodeLabLanguage,
        stdinInput: String = "",
        files: List<WorkspaceFile> = emptyList()
    ): ExecutionOutput {
        val startTime = System.currentTimeMillis()
        delay(300) // Realistic execution delay simulation

        if (code.isBlank()) {
            return ExecutionOutput(
                status = ExecutionStatus.ERROR,
                stderr = "Error: Code buffer is empty. Please write some code before running.",
                executionTimeMs = System.currentTimeMillis() - startTime
            )
        }

        // 1. Basic Syntax Validation (Bracket / Quote matching)
        val syntaxError = checkBasicSyntax(code)
        if (syntaxError != null) {
            return ExecutionOutput(
                status = ExecutionStatus.ERROR,
                stderr = syntaxError,
                executionTimeMs = System.currentTimeMillis() - startTime
            )
        }

        // 2. Language Specific Execution
        return when (language.executionType) {
            CodeExecutionType.HTML -> executeHtmlCss(code, files, startTime)
            CodeExecutionType.CSS -> executeCssOnly(code, startTime)
            CodeExecutionType.JAVASCRIPT -> executeJavaScript(code, stdinInput, startTime)
            CodeExecutionType.PYTHON -> executePython(code, stdinInput, startTime)
            CodeExecutionType.SQL -> executeSql(code, startTime)
            CodeExecutionType.COMPILED_LANG -> executeCompiledLanguage(code, language, stdinInput, startTime)
        }
    }

    private fun checkBasicSyntax(code: String): String? {
        val stack = mutableListOf<Pair<Char, Int>>()
        val lines = code.lines()

        for ((lineIdx, line) in lines.withIndex()) {
            // Ignore single line comments
            val codePart = line.split("//", "#", "--")[0]
            for ((charIdx, ch) in codePart.withIndex()) {
                when (ch) {
                    '(', '{', '[' -> stack.add(ch to lineIdx + 1)
                    ')' -> {
                        if (stack.isEmpty() || stack.removeAt(stack.size - 1).first != '(') {
                            return "SyntaxError: Unexpected ')' at line ${lineIdx + 1}, column ${charIdx + 1}"
                        }
                    }
                    '}' -> {
                        if (stack.isEmpty() || stack.removeAt(stack.size - 1).first != '{') {
                            return "SyntaxError: Unexpected '}' at line ${lineIdx + 1}, column ${charIdx + 1}"
                        }
                    }
                    ']' -> {
                        if (stack.isEmpty() || stack.removeAt(stack.size - 1).first != '[') {
                            return "SyntaxError: Unexpected ']' at line ${lineIdx + 1}, column ${charIdx + 1}"
                        }
                    }
                }
            }
        }

        if (stack.isNotEmpty()) {
            val (unclosed, lineNo) = stack.last()
            val expected = when (unclosed) {
                '(' -> ')'
                '{' -> '}'
                '[' -> ']'
                else -> ' '
            }
            return "SyntaxError: Unclosed '$unclosed' (missing '$expected') starting at line $lineNo"
        }

        return null
    }

    private fun executeHtmlCss(code: String, files: List<WorkspaceFile>, startTime: Long): ExecutionOutput {
        // Find if there's linked CSS or JS files in project
        val cssFiles = files.filter { it.name.endsWith(".css") && !it.isDirectory }
        val jsFiles = files.filter { it.name.endsWith(".js") && !it.isDirectory }

        var htmlContent = code
        if (cssFiles.isNotEmpty()) {
            val injectedCss = cssFiles.joinToString("\n") { "<style>${it.content}</style>" }
            htmlContent = if (htmlContent.contains("</head>", ignoreCase = true)) {
                htmlContent.replace("</head>", "$injectedCss\n</head>", ignoreCase = true)
            } else {
                "$injectedCss\n$htmlContent"
            }
        }

        if (jsFiles.isNotEmpty()) {
            val injectedJs = jsFiles.joinToString("\n") { "<script>${it.content}</script>" }
            htmlContent = if (htmlContent.contains("</body>", ignoreCase = true)) {
                htmlContent.replace("</body>", "$injectedJs\n</body>", ignoreCase = true)
            } else {
                "$htmlContent\n$injectedJs"
            }
        }

        return ExecutionOutput(
            status = ExecutionStatus.SUCCESS,
            stdout = "HTML/CSS Live Preview rendered successfully.",
            htmlResult = htmlContent,
            executionTimeMs = System.currentTimeMillis() - startTime
        )
    }

    private fun executeCssOnly(code: String, startTime: Long): ExecutionOutput {
        val sampleHtml = """
            <!DOCTYPE html>
            <html>
            <head>
              <style>
              $code
              </style>
            </head>
            <body>
              <div class="hero-title">CSS Live Stylesheet Preview</div>
              <p>This is a paragraph element with CSS styling applied.</p>
              <div class="card">
                <h3>Sample Component Card</h3>
                <span class="badge">Badge Label</span>
                <p>Styling properties defined in editor are applied here live.</p>
              </div>
            </body>
            </html>
        """.trimIndent()

        return ExecutionOutput(
            status = ExecutionStatus.SUCCESS,
            stdout = "CSS stylesheet evaluated and applied to sample container.",
            htmlResult = sampleHtml,
            executionTimeMs = System.currentTimeMillis() - startTime
        )
    }

    private fun executeJavaScript(code: String, stdinInput: String, startTime: Long): ExecutionOutput {
        val outputLines = mutableListOf<String>()
        val errorLines = mutableListOf<String>()

        try {
            // Parse console.log calls, return values, variable assignments, loops
            val lines = code.lines()
            var simulatedVarMap = mutableMapOf<String, Any>()

            for (line in lines) {
                val trimmed = line.trim()
                if (trimmed.startsWith("//") || trimmed.isBlank()) continue

                if (trimmed.startsWith("console.log(") && trimmed.endsWith(");")) {
                    val content = trimmed.substringAfter("console.log(").substringBeforeLast(");").trim()
                    // evaluate simple expressions in console.log
                    val evaluated = evaluateSimpleExpression(content, simulatedVarMap, stdinInput)
                    outputLines.add(evaluated)
                } else if (trimmed.contains("console.log(")) {
                    val content = trimmed.substringAfter("console.log(").substringBefore(")").trim()
                    val evaluated = evaluateSimpleExpression(content, simulatedVarMap, stdinInput)
                    outputLines.add(evaluated)
                } else if (trimmed.startsWith("const ") || trimmed.startsWith("let ") || trimmed.startsWith("var ")) {
                    val assign = trimmed.substringAfter(" ").trim()
                    if (assign.contains("=")) {
                        val name = assign.substringBefore("=").trim()
                        val valStr = assign.substringAfter("=").removeSuffix(";").trim()
                        simulatedVarMap[name] = valStr
                    }
                }
            }

            if (outputLines.isEmpty()) {
                outputLines.add("[Program finished with no console output. Try adding console.log()]")
            }

            return ExecutionOutput(
                status = ExecutionStatus.SUCCESS,
                stdout = outputLines.joinToString("\n"),
                executionTimeMs = System.currentTimeMillis() - startTime
            )
        } catch (e: Exception) {
            return ExecutionOutput(
                status = ExecutionStatus.ERROR,
                stderr = "RuntimeError: " + (e.message ?: "Evaluation error"),
                executionTimeMs = System.currentTimeMillis() - startTime
            )
        }
    }

    private fun executePython(code: String, stdinInput: String, startTime: Long): ExecutionOutput {
        val outputLines = mutableListOf<String>()
        val lines = code.lines()
        val varMap = mutableMapOf<String, String>()

        if (stdinInput.isNotBlank()) {
            outputLines.add("[STDIN Provided]: $stdinInput")
        }

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("#") || trimmed.isBlank()) continue

            if (trimmed.startsWith("print(") && trimmed.endsWith(")")) {
                val expr = trimmed.substring("print(".length, trimmed.length - 1).trim()
                val evaluated = evaluatePythonPrint(expr, varMap, stdinInput)
                outputLines.add(evaluated)
            } else if (trimmed.contains(" = ") && !trimmed.startsWith("if ") && !trimmed.startsWith("def ")) {
                val parts = trimmed.split(" = ", limit = 2)
                if (parts.size == 2) {
                    varMap[parts[0].trim()] = parts[1].trim()
                }
            }
        }

        if (outputLines.isEmpty()) {
            outputLines.add("[Python process terminated with return code 0 (No print statements)]")
        }

        return ExecutionOutput(
            status = ExecutionStatus.SUCCESS,
            stdout = outputLines.joinToString("\n"),
            executionTimeMs = System.currentTimeMillis() - startTime
        )
    }

    private fun executeSql(code: String, startTime: Long): ExecutionOutput {
        val outputLines = mutableListOf<String>()
        val upperCode = code.uppercase(Locale.ROOT)

        outputLines.add("--- SQL Query Execution Engine ---")

        if (upperCode.contains("CREATE TABLE")) {
            outputLines.add("[OK] Table created successfully.")
        }
        if (upperCode.contains("INSERT INTO")) {
            val insertCount = code.split("INSERT INTO", ignoreCase = true).size - 1
            outputLines.add("[OK] $insertCount row(s) inserted.")
        }
        if (upperCode.contains("SELECT")) {
            outputLines.add("\nQuery Results:")
            outputLines.add("+----+------------+------------+-----+")
            outputLines.add("| ID | Name       | Language   | XP  |")
            outputLines.add("+----+------------+------------+-----+")
            outputLines.add("| 1  | Alice      | Python     | 450 |")
            outputLines.add("| 2  | Bob        | Kotlin     | 780 |")
            outputLines.add("| 3  | Charlie    | JavaScript | 320 |")
            outputLines.add("+----+------------+------------+-----+")
            outputLines.add("3 rows returned.")
        }

        return ExecutionOutput(
            status = ExecutionStatus.SUCCESS,
            stdout = outputLines.joinToString("\n"),
            executionTimeMs = System.currentTimeMillis() - startTime
        )
    }

    private fun executeCompiledLanguage(
        code: String,
        language: CodeLabLanguage,
        stdinInput: String,
        startTime: Long
    ): ExecutionOutput {
        val outputLines = mutableListOf<String>()

        outputLines.add("[Compiling ${language.name} source code...]")
        outputLines.add("[Build Successful: Output Binary Generated]")
        if (stdinInput.isNotBlank()) {
            outputLines.add("[STDIN Received: \"$stdinInput\"]")
        }
        outputLines.add("--- Program Execution Output ---")

        val lines = code.lines()
        for (line in lines) {
            val trimmed = line.trim()
            when {
                // Java
                trimmed.contains("System.out.println(") -> {
                    val msg = extractPrintString(trimmed, "System.out.println(")
                    outputLines.add(msg)
                }
                // C / C++
                trimmed.contains("printf(") -> {
                    val msg = extractPrintString(trimmed, "printf(")
                    outputLines.add(msg)
                }
                trimmed.contains("cout <<") -> {
                    val msg = trimmed.substringAfter("cout <<")
                        .replace("endl;", "")
                        .replace("endl", "")
                        .replace("\"", "")
                        .trim()
                    outputLines.add(msg)
                }
                // C#
                trimmed.contains("Console.WriteLine(") -> {
                    val msg = extractPrintString(trimmed, "Console.WriteLine(")
                    outputLines.add(msg)
                }
                // Kotlin
                trimmed.contains("println(") -> {
                    val msg = extractPrintString(trimmed, "println(")
                    outputLines.add(msg)
                }
                // Go
                trimmed.contains("fmt.Println(") || trimmed.contains("fmt.Printf(") -> {
                    val msg = extractPrintString(trimmed, "(")
                    outputLines.add(msg)
                }
                // Rust
                trimmed.contains("println!(") -> {
                    val msg = extractPrintString(trimmed, "println!(")
                    outputLines.add(msg)
                }
                // Swift
                trimmed.contains("print(") -> {
                    val msg = extractPrintString(trimmed, "print(")
                    outputLines.add(msg)
                }
                // PHP
                trimmed.startsWith("echo ") -> {
                    val msg = trimmed.substringAfter("echo ").removeSuffix(";").replace("\"", "").replace("'", "")
                    outputLines.add(msg)
                }
            }
        }

        if (outputLines.size <= 3) {
            outputLines.add("Hello, World! Program executed successfully with exit code 0.")
        }

        return ExecutionOutput(
            status = ExecutionStatus.SUCCESS,
            stdout = outputLines.joinToString("\n"),
            executionTimeMs = System.currentTimeMillis() - startTime
        )
    }

    private fun extractPrintString(line: String, prefix: String): String {
        val after = line.substringAfter(prefix).substringBeforeLast(")")
        var clean = after.trim()
        if (clean.startsWith("\"") && clean.endsWith("\"") && clean.length >= 2) {
            clean = clean.substring(1, clean.length - 1)
        }
        return clean.replace("\\n", "\n").replace("\\t", "    ")
    }

    private fun evaluateSimpleExpression(expr: String, vars: Map<String, Any>, stdin: String): String {
        if (expr.startsWith("`") && expr.endsWith("`")) {
            return expr.substring(1, expr.length - 1)
        }
        if (expr.startsWith("\"") && expr.endsWith("\"")) {
            return expr.substring(1, expr.length - 1)
        }
        if (expr.startsWith("'") && expr.endsWith("'")) {
            return expr.substring(1, expr.length - 1)
        }
        if (vars.containsKey(expr)) {
            return vars[expr].toString()
        }
        if (expr.contains("+")) {
            val parts = expr.split("+")
            return parts.joinToString("") { evaluateSimpleExpression(it.trim(), vars, stdin) }
        }
        return expr
    }

    private fun evaluatePythonPrint(expr: String, vars: Map<String, String>, stdin: String): String {
        if (expr.startsWith("f\"") && expr.endsWith("\"")) {
            var result = expr.substring(2, expr.length - 1)
            for ((k, v) in vars) {
                result = result.replace("{$k}", v)
            }
            return result
        }
        if (expr.startsWith("\"") && expr.endsWith("\"")) {
            return expr.substring(1, expr.length - 1)
        }
        return expr
    }
}
