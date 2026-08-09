package com.example.ui.codelab

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

object SyntaxHighlighter {

    /**
     * Auto-detects the programming language based on a filename, extension, or code snippet content.
     */
    fun detectLanguage(input: String): CodeLabLanguage {
        val trimmed = input.trim()
        val lower = trimmed.lowercase()

        // 1. Check file extension or exact filename match
        when {
            lower.endsWith(".py") -> return CodeLabLanguages.PYTHON
            lower.endsWith(".js") || lower.endsWith(".mjs") || lower.endsWith(".cjs") -> return CodeLabLanguages.JAVASCRIPT
            lower.endsWith(".ts") || lower.endsWith(".tsx") -> return CodeLabLanguages.TYPESCRIPT
            lower.endsWith(".kt") || lower.endsWith(".kts") -> return CodeLabLanguages.KOTLIN
            lower.endsWith(".java") -> return CodeLabLanguages.JAVA
            lower.endsWith(".cpp") || lower.endsWith(".hpp") || lower.endsWith(".cc") || lower.endsWith(".cxx") -> return CodeLabLanguages.CPP
            lower.endsWith(".c") || lower.endsWith(".h") -> return CodeLabLanguages.C
            lower.endsWith(".cs") -> return CodeLabLanguages.CSHARP
            lower.endsWith(".go") -> return CodeLabLanguages.GO
            lower.endsWith(".rs") -> return CodeLabLanguages.RUST
            lower.endsWith(".php") -> return CodeLabLanguages.PHP
            lower.endsWith(".swift") -> return CodeLabLanguages.SWIFT
            lower.endsWith(".html") || lower.endsWith(".htm") -> return CodeLabLanguages.HTML
            lower.endsWith(".css") -> return CodeLabLanguages.CSS
            lower.endsWith(".sql") -> return CodeLabLanguages.SQL
        }

        // 2. Check for language ID match
        CodeLabLanguages.ALL.find { it.id.equals(lower, ignoreCase = true) }?.let { return it }

        // 3. Code content heuristics
        return when {
            trimmed.startsWith("<?php") || trimmed.contains("echo $") -> CodeLabLanguages.PHP
            trimmed.contains("<!DOCTYPE html>") || trimmed.contains("<html") || trimmed.contains("<div") -> CodeLabLanguages.HTML
            trimmed.contains("SELECT ") && trimmed.contains("FROM ") -> CodeLabLanguages.SQL
            trimmed.contains("CREATE TABLE ") || trimmed.contains("INSERT INTO ") -> CodeLabLanguages.SQL
            trimmed.contains("fun main(") || (trimmed.contains("val ") && trimmed.contains(": String")) -> CodeLabLanguages.KOTLIN
            trimmed.contains("public static void main") || trimmed.contains("System.out.println") -> CodeLabLanguages.JAVA
            trimmed.contains("#include <") && trimmed.contains("std::") -> CodeLabLanguages.CPP
            trimmed.contains("#include <stdio.h>") -> CodeLabLanguages.C
            trimmed.contains("using System;") || trimmed.contains("Console.WriteLine") -> CodeLabLanguages.CSHARP
            trimmed.contains("package main") || trimmed.contains("func main(") -> CodeLabLanguages.GO
            trimmed.contains("fn main()") || trimmed.contains("println!(") -> CodeLabLanguages.RUST
            trimmed.contains("def ") || trimmed.contains("import sys") || trimmed.contains("print(f\"") -> CodeLabLanguages.PYTHON
            trimmed.contains("interface ") && trimmed.contains(": string") -> CodeLabLanguages.TYPESCRIPT
            trimmed.contains("const ") || trimmed.contains("let ") || trimmed.contains("console.log") -> CodeLabLanguages.JAVASCRIPT
            else -> CodeLabLanguages.PYTHON
        }
    }

    /**
     * Highlights syntax in the given code string using the language definition and theme color configuration.
     */
    fun highlight(
        code: String,
        language: CodeLabLanguage,
        colorConfig: SyntaxColorConfig
    ): AnnotatedString {
        if (code.isEmpty()) return buildAnnotatedString { }

        return buildAnnotatedString {
            append(code)

            try {
                // Track ranges that are already styled (e.g. comments and strings)
                val maskedRanges = mutableListOf<IntRange>()

                fun isMasked(start: Int, end: Int): Boolean {
                    return maskedRanges.any { mask -> start < mask.last && end > mask.first }
                }

                // 1. Highlight Comments (highest precedence to prevent inner matching)
                val commentRegexes = getCommentRegexes(language.id)
                for (regex in commentRegexes) {
                    for (match in regex.findAll(code)) {
                        val start = match.range.first.coerceIn(0, code.length)
                        val end = (match.range.last + 1).coerceIn(0, code.length)
                        if (start < end) {
                            addStyle(
                                SpanStyle(
                                    color = colorConfig.commentColor,
                                    fontStyle = FontStyle.Italic
                                ),
                                start,
                                end
                            )
                            maskedRanges.add(start until end)
                        }
                    }
                }

                // 2. Highlight Strings
                val stringRegexes = getStringRegexes(language.id)
                for (regex in stringRegexes) {
                    for (match in regex.findAll(code)) {
                        val start = match.range.first.coerceIn(0, code.length)
                        val end = (match.range.last + 1).coerceIn(0, code.length)
                        if (start < end && !isMasked(start, end)) {
                            addStyle(SpanStyle(color = colorConfig.stringColor), start, end)
                            maskedRanges.add(start until end)
                        }
                    }
                }

                // 3. Highlight Numbers
                val numberRegex = Regex("\\b(0x[0-9a-fA-F]+|0b[01]+|\\d+(\\.\\d+)?([eE][+-]?\\d+)?)\\b")
                for (match in numberRegex.findAll(code)) {
                    val start = match.range.first.coerceIn(0, code.length)
                    val end = (match.range.last + 1).coerceIn(0, code.length)
                    if (start < end && !isMasked(start, end)) {
                        addStyle(SpanStyle(color = colorConfig.numberColor), start, end)
                    }
                }

                // 4. Highlight Types
                val typeList = getTypeKeywords(language)
                for (typeKw in typeList) {
                    if (typeKw.isBlank()) continue
                    val typeRegex = Regex("\\b${Regex.escape(typeKw)}\\b")
                    for (match in typeRegex.findAll(code)) {
                        val start = match.range.first.coerceIn(0, code.length)
                        val end = (match.range.last + 1).coerceIn(0, code.length)
                        if (start < end && !isMasked(start, end)) {
                            addStyle(
                                SpanStyle(
                                    color = colorConfig.typeColor,
                                    fontWeight = FontWeight.Medium
                                ),
                                start,
                                end
                            )
                        }
                    }
                }

                // PascalCase Type Detection (Class names, interfaces, structs)
                val pascalCaseTypeRegex = Regex("\\b[A-Z][a-zA-Z0-9_]*\\b")
                for (match in pascalCaseTypeRegex.findAll(code)) {
                    val word = match.value
                    if (word !in language.keywords) {
                        val start = match.range.first.coerceIn(0, code.length)
                        val end = (match.range.last + 1).coerceIn(0, code.length)
                        if (start < end && !isMasked(start, end)) {
                            addStyle(
                                SpanStyle(
                                    color = colorConfig.typeColor,
                                    fontWeight = FontWeight.Medium
                                ),
                                start,
                                end
                            )
                        }
                    }
                }

                // 5. Highlight Keywords
                for (kw in language.keywords) {
                    if (kw.isBlank()) continue
                    val kwRegex = if (kw.startsWith("<") || kw.endsWith(">") || kw.startsWith("#") || kw.startsWith("<!")) {
                        Regex(Regex.escape(kw))
                    } else {
                        Regex("\\b${Regex.escape(kw)}\\b")
                    }
                    for (match in kwRegex.findAll(code)) {
                        val start = match.range.first.coerceIn(0, code.length)
                        val end = (match.range.last + 1).coerceIn(0, code.length)
                        if (start < end && !isMasked(start, end)) {
                            addStyle(
                                SpanStyle(
                                    color = colorConfig.keywordColor,
                                    fontWeight = FontWeight.Bold
                                ),
                                start,
                                end
                            )
                        }
                    }
                }

                // 6. Highlight Functions & Function Calls
                val fnNames = language.commonFunctions.map { it.replace("()", "").trim() }
                for (fn in fnNames) {
                    if (fn.isBlank()) continue
                    val fnRegex = Regex("\\b${Regex.escape(fn)}\\b")
                    for (match in fnRegex.findAll(code)) {
                        val start = match.range.first.coerceIn(0, code.length)
                        val end = (match.range.last + 1).coerceIn(0, code.length)
                        if (start < end && !isMasked(start, end)) {
                            addStyle(SpanStyle(color = colorConfig.functionColor), start, end)
                        }
                    }
                }

                // Generic function invocation matching (e.g. `fnName(`)
                val funcCallRegex = Regex("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*(?=\\()")
                for (match in funcCallRegex.findAll(code)) {
                    val group = match.groups[1] ?: continue
                    val word = group.value
                    if (word !in language.keywords && word !in typeList) {
                        val start = group.range.first.coerceIn(0, code.length)
                        val end = (group.range.last + 1).coerceIn(0, code.length)
                        if (start < end && !isMasked(start, end)) {
                            addStyle(SpanStyle(color = colorConfig.functionColor), start, end)
                        }
                    }
                }

                // 7. Highlight Punctuation & Operators
                val punctuationRegex = Regex("[{}()\\[\\];,.]|[-+*/=<>!&|%^:~?]")
                for (match in punctuationRegex.findAll(code)) {
                    val start = match.range.first.coerceIn(0, code.length)
                    val end = (match.range.last + 1).coerceIn(0, code.length)
                    if (start < end && !isMasked(start, end)) {
                        addStyle(SpanStyle(color = colorConfig.punctuationColor), start, end)
                    }
                }

            } catch (_: Exception) {
                // Fallback gracefully on parsing edge cases
            }
        }
    }

    private fun getCommentRegexes(languageId: String): List<Regex> {
        return when (languageId.lowercase()) {
            "python" -> listOf(Regex("#.*"))
            "sql" -> listOf(Regex("--.*"), Regex("(?s)/\\*.*?\\*/"))
            "html" -> listOf(Regex("(?s)<!--.*?-->"))
            "css" -> listOf(Regex("(?s)/\\*.*?\\*/"))
            "php" -> listOf(Regex("//.*"), Regex("#.*"), Regex("(?s)/\\*.*?\\*/"))
            else -> listOf(Regex("//.*"), Regex("(?s)/\\*.*?\\*/"))
        }
    }

    private fun getStringRegexes(languageId: String): List<Regex> {
        return when (languageId.lowercase()) {
            "python", "kotlin", "java", "swift" -> listOf(
                Regex("(?s)\"\"\"[^\"]*\"\"\"|'''[^']*'''"),
                Regex("\"([^\"\\\\]|\\\\.)*\""),
                Regex("'([^'\\\\]|\\\\.)*'")
            )
            "javascript", "typescript" -> listOf(
                Regex("(?s)`([^`\\\\]|\\\\.)*`"),
                Regex("\"([^\"\\\\]|\\\\.)*\""),
                Regex("'([^'\\\\]|\\\\.)*'")
            )
            else -> listOf(
                Regex("\"([^\"\\\\]|\\\\.)*\""),
                Regex("'([^'\\\\]|\\\\.)*'")
            )
        }
    }

    private fun getTypeKeywords(language: CodeLabLanguage): List<String> {
        if (language.types.isNotEmpty()) return language.types

        return when (language.id.lowercase()) {
            "python" -> listOf("int", "float", "str", "bool", "list", "dict", "set", "tuple", "bytes", "object")
            "javascript" -> listOf("String", "Number", "Boolean", "Array", "Object", "Promise", "Symbol")
            "typescript" -> listOf("string", "number", "boolean", "any", "unknown", "never", "void", "object", "Array")
            "kotlin" -> listOf("Int", "Long", "Float", "Double", "Boolean", "Char", "String", "Byte", "Short", "Unit", "Any")
            "java" -> listOf("int", "long", "float", "double", "boolean", "char", "byte", "short", "void", "String", "Object", "List", "Map")
            "cpp", "c" -> listOf("int", "long", "float", "double", "bool", "char", "void", "size_t", "string", "vector")
            "csharp" -> listOf("int", "long", "float", "double", "bool", "char", "string", "object", "decimal", "void", "Task")
            "go" -> listOf("int", "int64", "float64", "string", "bool", "byte", "rune", "error")
            "rust" -> listOf("i32", "i64", "f64", "bool", "char", "str", "String", "Vec", "Option", "Result", "usize")
            "swift" -> listOf("Int", "Double", "Float", "Bool", "String", "Character", "Array", "Dictionary")
            "sql" -> listOf("INT", "INTEGER", "VARCHAR", "TEXT", "BOOLEAN", "DECIMAL", "FLOAT", "DATE", "DATETIME")
            else -> emptyList()
        }
    }
}
