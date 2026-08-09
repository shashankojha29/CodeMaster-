package com.example.service.gemini

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiTutorService {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun getTutorResponse(userPrompt: String, contextInfo: String = ""): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
                val systemInstruction = "You are CodeMaster AI Tutor, a friendly, encouraging programming teacher for students. Explain concepts simply, give hints for errors or challenges without spoiling complete solutions immediately, and suggest next steps."

                val requestJson = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().put("text", "$contextInfo\n\nUser Question: $userPrompt"))
                            })
                        })
                    })
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", systemInstruction))
                        })
                    })
                }

                val body = requestJson.toString().toRequestBody("application/json".toMediaType())
                val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

                val response = okHttpClient.newCall(request).execute()
                val responseBody = response.body?.string() ?: ""

                if (response.isSuccessful && responseBody.isNotBlank()) {
                    val root = JSONObject(responseBody)
                    val candidates = root.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCand = candidates.getJSONObject(0)
                        val content = firstCand.optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val text = parts.getJSONObject(0).optString("text", "")
                            if (text.isNotBlank()) return@withContext text
                        }
                    }
                }
            } catch (e: Exception) {
                // Fallback to client-side smart tutor
            }
        }

        // Educational Client-Side Smart Fallback Tutor
        generateSmartFallback(userPrompt)
    }

    private fun generateSmartFallback(prompt: String): String {
        val p = prompt.lowercase()
        return when {
            p.contains("loop") -> {
                "💡 **Python Loops Explained:**\n\n" +
                "Loops repeat code automatically!\n" +
                "• **for loop**: Use when you know how many times to repeat (e.g. `for i in range(5):` repeats 5 times).\n" +
                "• **while loop**: Repeats as long as a condition stays true (e.g. `while speed < 60:`).\n\n" +
                "**Pro-Tip**: Don't forget to update loop counters in while loops to prevent infinite loops!"
            }
            p.contains("error") || p.contains("bug") || p.contains("fix") -> {
                "🔍 **Debugging Checklist:**\n\n" +
                "1. **Syntax Check**: Ensure all quotes, parentheses, brackets `() [] {}` are properly closed.\n" +
                "2. **Indentation**: In Python, make sure statements under `if`, `for`, and `def` are consistently indented with 4 spaces.\n" +
                "3. **Variable Names**: Check for typos or cases (e.g. `Score` vs `score`).\n" +
                "4. **Data Types**: Make sure you aren't adding a String directly to an Integer without converting!"
            }
            p.contains("hint") || p.contains("challenge") -> {
                "🧠 **Challenge Hint:**\n\n" +
                "• Break the problem into 3 steps: Input -> Logic -> Output.\n" +
                "• Test your logic with small numbers first (like 0, 1, or 2).\n" +
                "• Look closely at the example input/output provided!"
            }
            p.contains("c") && (p.contains("pointer") || p.contains("memory")) -> {
                "⚡ **C Pointers Quick Summary:**\n\n" +
                "A pointer stores a physical memory address!\n" +
                "• `&var` gets the address of `var`.\n" +
                "• `int *ptr = &var` creates a pointer `ptr` holding `var`'s address.\n" +
                "• `*ptr` dereferences the pointer to access or modify the actual value."
            }
            p.contains("sql") -> {
                "🗄️ **SQL Query Basics:**\n\n" +
                "• `SELECT col1, col2 FROM table_name;` fetches specific columns.\n" +
                "• `WHERE condition` filters rows.\n" +
                "• `ORDER BY col DESC` sorts results descending."
            }
            else -> {
                "🤖 **CodeMaster AI Tutor:**\n\n" +
                "Great question! Here is a core principle for programming success:\n\n" +
                "1. Keep functions small and focused on a single task.\n" +
                "2. Use descriptive variable names that explain what the data represents.\n" +
                "3. Practice typing brackets and operators in our **Typing Academy** to increase your coding speed!\n\n" +
                "Feel free to ask me to explain loops, variables, functions, C pointers, or SQL queries!"
            }
        }
    }
}
