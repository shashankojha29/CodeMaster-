package com.example.ui.codelab

import androidx.compose.ui.graphics.Color

enum class CodeExecutionType {
    PYTHON,
    JAVASCRIPT,
    HTML,
    CSS,
    SQL,
    COMPILED_LANG
}

data class CodeLabLanguage(
    val id: String,
    val name: String,
    val extension: String,
    val color: Color,
    val iconEmoji: String,
    val executionType: CodeExecutionType,
    val defaultFileName: String,
    val starterCode: String,
    val keywords: List<String>,
    val commonFunctions: List<String>,
    val commonSnippets: List<Pair<String, String>>, // Title to snippet string
    val types: List<String> = emptyList()
)

object CodeLabLanguages {

    val PYTHON = CodeLabLanguage(
        id = "python",
        name = "Python",
        extension = ".py",
        color = Color(0xFF3776AB),
        iconEmoji = "🐍",
        executionType = CodeExecutionType.PYTHON,
        defaultFileName = "main.py",
        starterCode = """# Python Code Lab Workspace
def greet(name):
    return f"Hello, {name}! Welcome to CodeLab."

print(greet("Developer"))

# Try a loop
numbers = [1, 2, 3, 4, 5]
total = sum(numbers)
print(f"Sum of {numbers} = {total}")
""",
        keywords = listOf("def", "return", "if", "elif", "else", "for", "in", "while", "import", "from", "as", "class", "try", "except", "lambda", "True", "False", "None", "and", "or", "not", "with", "break", "continue"),
        commonFunctions = listOf("print()", "len()", "range()", "sum()", "min()", "max()", "sorted()", "input()", "int()", "str()", "list()", "dict()", "append()", "split()", "join()"),
        commonSnippets = listOf(
            "For Loop" to "for i in range(10):\n    print(i)",
            "Function" to "def calculate(a, b):\n    return a + b",
            "Class" to "class Student:\n    def __init__(self, name):\n        self.name = name"
        )
    )

    val JAVASCRIPT = CodeLabLanguage(
        id = "javascript",
        name = "JavaScript",
        extension = ".js",
        color = Color(0xFFF7DF1E),
        iconEmoji = "🟨",
        executionType = CodeExecutionType.JAVASCRIPT,
        defaultFileName = "index.js",
        starterCode = """// JavaScript Code Lab Workspace
function calculateFactorial(n) {
  if (n <= 1) return 1;
  return n * calculateFactorial(n - 1);
}

const num = 5;
console.log(`Factorial of ${'$'}{num} is:`, calculateFactorial(num));

const languages = ["JavaScript", "Python", "Kotlin", "Rust"];
console.log("Supported Languages:", languages.join(", "));
""",
        keywords = listOf("const", "let", "var", "function", "return", "if", "else", "for", "while", "switch", "case", "import", "export", "class", "async", "await", "try", "catch", "new", "this", "typeof"),
        commonFunctions = listOf("console.log()", "map()", "filter()", "reduce()", "push()", "pop()", "slice()", "splice()", "JSON.stringify()", "JSON.parse()", "Math.max()", "Math.min()", "Math.random()"),
        commonSnippets = listOf(
            "Arrow Function" to "const add = (a, b) => a + b;",
            "Array Loop" to "items.forEach(item => {\n  console.log(item);\n});",
            "Async Function" to "async function fetchData() {\n  const res = await fetch(url);\n  return res.json();\n}"
        )
    )

    val TYPESCRIPT = CodeLabLanguage(
        id = "typescript",
        name = "TypeScript",
        extension = ".ts",
        color = Color(0xFF3178C6),
        iconEmoji = "🔷",
        executionType = CodeExecutionType.JAVASCRIPT,
        defaultFileName = "main.ts",
        starterCode = """// TypeScript Code Lab Workspace
interface Developer {
  name: string;
  role: string;
  experienceYears: number;
}

const dev: Developer = {
  name: "Alex",
  role: "Fullstack Engineer",
  experienceYears: 4
};

function introduce(d: Developer): string {
  return `Hi, I am ${'$'}{d.name}, working as ${'$'}{d.role} with ${'$'}{d.experienceYears} years of experience.`;
}

console.log(introduce(dev));
""",
        keywords = listOf("interface", "type", "const", "let", "function", "return", "class", "implements", "extends", "enum", "readonly", "private", "public", "protected", "as", "any", "unknown", "never"),
        commonFunctions = listOf("console.log()", "Array.from()", "Object.keys()", "Object.values()", "Promise.resolve()", "map()", "filter()"),
        commonSnippets = listOf(
            "Interface" to "interface User {\n  id: number;\n  name: string;\n  email: string;\n}",
            "Type Alias" to "type Result<T> = { success: boolean; data: T };"
        )
    )

    val KOTLIN = CodeLabLanguage(
        id = "kotlin",
        name = "Kotlin",
        extension = ".kt",
        color = Color(0xFF7F52FF),
        iconEmoji = "🟣",
        executionType = CodeExecutionType.COMPILED_LANG,
        defaultFileName = "Main.kt",
        starterCode = """// Kotlin Code Lab Workspace
data class Course(val name: String, val level: String)

fun main() {
    val courses = listOf(
        Course("Android & Jetpack Compose", "Advanced"),
        Course("Kotlin Backend", "Intermediate")
    )
    
    println("Welcome to Kotlin CodeLab!")
    courses.forEach { course ->
        println("- ${'$'}{course.name} [${'$'}{course.level}]")
    }
}
""",
        keywords = listOf("fun", "val", "var", "class", "data", "object", "interface", "sealed", "when", "if", "else", "for", "while", "return", "import", "package", "is", "as", "by", "suspend", "override", "companion"),
        commonFunctions = listOf("println()", "listOf()", "mapOf()", "filter()", "map()", "forEach()", "takeIf()", "let()", "also()", "apply()", "run()"),
        commonSnippets = listOf(
            "Data Class" to "data class User(val id: String, val name: String)",
            "When Expression" to "val result = when(x) {\n    1 -> \"One\"\n    2 -> \"Two\"\n    else -> \"Other\"\n}"
        )
    )

    val JAVA = CodeLabLanguage(
        id = "java",
        name = "Java",
        extension = ".java",
        color = Color(0xFF007396),
        iconEmoji = "☕",
        executionType = CodeExecutionType.COMPILED_LANG,
        defaultFileName = "Main.java",
        starterCode = """// Java Code Lab Workspace
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World from Java!");
        
        int[] numbers = {10, 20, 30, 40, 50};
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        System.out.println("Array Sum: " + sum);
    }
}
""",
        keywords = listOf("public", "class", "static", "void", "int", "double", "boolean", "String", "if", "else", "for", "while", "return", "new", "import", "package", "extends", "implements", "final", "abstract", "try", "catch"),
        commonFunctions = listOf("System.out.println()", "Math.max()", "Math.min()", "Integer.parseInt()", "String.valueOf()", "Arrays.toString()", "ArrayList.add()"),
        commonSnippets = listOf(
            "Main Method" to "public static void main(String[] args) {\n    // Code here\n}",
            "Class Definition" to "public class Person {\n    private String name;\n    public Person(String name) { this.name = name; }\n}"
        )
    )

    val CPP = CodeLabLanguage(
        id = "cpp",
        name = "C++",
        extension = ".cpp",
        color = Color(0xFF00599C),
        iconEmoji = "🟦",
        executionType = CodeExecutionType.COMPILED_LANG,
        defaultFileName = "main.cpp",
        starterCode = """// C++ Code Lab Workspace
#include <iostream>
#include <vector>
#include <numeric>

using namespace std;

int main() {
    cout << "Welcome to C++ CodeLab!" << endl;
    
    vector<int> vec = {5, 10, 15, 20};
    int sum = 0;
    for (int x : vec) {
        sum += x;
    }
    cout << "Vector Sum = " << sum << endl;
    return 0;
}
""",
        keywords = listOf("#include", "using", "namespace", "std", "int", "float", "double", "char", "bool", "void", "if", "else", "for", "while", "return", "class", "struct", "template", "auto", "const", "vector", "cout", "cin"),
        commonFunctions = listOf("std::cout", "std::cin", "std::endl", "push_back()", "size()", "begin()", "end()", "std::sort()", "std::accumulate()"),
        commonSnippets = listOf(
            "Main Function" to "int main() {\n    // Code\n    return 0;\n}",
            "Vector Loop" to "for (const auto& item : vec) {\n    cout << item << endl;\n}"
        )
    )

    val C = CodeLabLanguage(
        id = "c",
        name = "C",
        extension = ".c",
        color = Color(0xFFA8B9CC),
        iconEmoji = "⚙️",
        executionType = CodeExecutionType.COMPILED_LANG,
        defaultFileName = "main.c",
        starterCode = """/* C Code Lab Workspace */
#include <stdio.h>

int main() {
    printf("Hello from C Programming!\n");
    
    int age = 22;
    printf("Student Age: %d\n", age);
    
    return 0;
}
""",
        keywords = listOf("#include", "int", "char", "float", "double", "void", "if", "else", "for", "while", "return", "struct", "typedef", "sizeof", "printf", "scanf"),
        commonFunctions = listOf("printf()", "scanf()", "malloc()", "free()", "strlen()", "strcpy()", "strcmp()", "fopen()", "fclose()"),
        commonSnippets = listOf(
            "C Main" to "#include <stdio.h>\n\nint main() {\n    printf(\"Hello\\n\");\n    return 0;\n}"
        )
    )

    val CSHARP = CodeLabLanguage(
        id = "csharp",
        name = "C#",
        extension = ".cs",
        color = Color(0xFF239120),
        iconEmoji = "💚",
        executionType = CodeExecutionType.COMPILED_LANG,
        defaultFileName = "Program.cs",
        starterCode = """// C# Code Lab Workspace
using System;

class Program {
    static void Main(string[] args) {
        Console.WriteLine("Welcome to C# CodeLab!");
        
        int a = 15;
        int b = 25;
        Console.WriteLine(${'$'}"Result: {a} + {b} = {a + b}");
    }
}
""",
        keywords = listOf("using", "namespace", "class", "static", "void", "int", "string", "bool", "if", "else", "for", "foreach", "in", "while", "return", "new", "public", "private", "async", "await", "var"),
        commonFunctions = listOf("Console.WriteLine()", "Console.ReadLine()", "Math.Max()", "Math.Min()", "String.Format()", "List.Add()"),
        commonSnippets = listOf(
            "C# Program" to "using System;\n\nclass Program {\n    static void Main() {\n        Console.WriteLine(\"Hello C#\");\n    }\n}"
        )
    )

    val GO = CodeLabLanguage(
        id = "go",
        name = "Go",
        extension = ".go",
        color = Color(0xFF00ADD8),
        iconEmoji = "🦫",
        executionType = CodeExecutionType.COMPILED_LANG,
        defaultFileName = "main.go",
        starterCode = """// Go Code Lab Workspace
package main

import "fmt"

func main() {
    fmt.Println("Hello, Go Gopher!")
    
    scores := []int{95, 88, 92, 100}
    sum := 0
    for _, score := range scores {
        sum += score
    }
    fmt.Printf("Average Score: %.2f\n", float64(sum)/float64(len(scores)))
}
""",
        keywords = listOf("package", "import", "func", "var", "const", "type", "struct", "interface", "if", "else", "for", "range", "return", "go", "select", "chan", "defer", "map", "slice"),
        commonFunctions = listOf("fmt.Println()", "fmt.Printf()", "fmt.Sprintf()", "append()", "len()", "make()", "make(map[string]int)"),
        commonSnippets = listOf(
            "Go Main" to "package main\nimport \"fmt\"\n\nfunc main() {\n    fmt.Println(\"Hello Go\")\n}"
        )
    )

    val RUST = CodeLabLanguage(
        id = "rust",
        name = "Rust",
        extension = ".rs",
        color = Color(0xFFCE412B),
        iconEmoji = "🦀",
        executionType = CodeExecutionType.COMPILED_LANG,
        defaultFileName = "main.rs",
        starterCode = """// Rust Code Lab Workspace
fn main() {
    println!("Hello from Rust!");

    let numbers = vec![1, 2, 3, 4, 5];
    let sum: i32 = numbers.iter().sum();
    println!("Sum of {:?} = {}", numbers, sum);
}
""",
        keywords = listOf("fn", "let", "mut", "const", "struct", "enum", "impl", "trait", "pub", "use", "mod", "if", "else", "loop", "while", "for", "in", "match", "return", "vec!"),
        commonFunctions = listOf("println!()", "format!()", "vec!()", "unwrap()", "expect()", "clone()", "collect()"),
        commonSnippets = listOf(
            "Rust Main" to "fn main() {\n    println!(\"Hello Rust\");\n}"
        )
    )

    val PHP = CodeLabLanguage(
        id = "php",
        name = "PHP",
        extension = ".php",
        color = Color(0xFF777BB4),
        iconEmoji = "🐘",
        executionType = CodeExecutionType.PYTHON, // Interpreted
        defaultFileName = "index.php",
        starterCode = """<?php
// PHP Code Lab Workspace
function greetUser(${'$'}name) {
    return "Hello, " . ${'$'}name . "! Welcome to PHP CodeLab.";
}

echo greetUser("Developer") . "\n";

${'$'}skills = ["PHP", "Laravel", "MySQL", "REST API"];
echo "Skills: " . implode(", ", ${'$'}skills) . "\n";
?>
""",
        keywords = listOf("function", "echo", "return", "if", "else", "elseif", "for", "foreach", "as", "while", "class", "public", "private", "protected", "use", "namespace", "array"),
        commonFunctions = listOf("echo", "var_dump()", "count()", "implode()", "explode()", "array_push()", "json_encode()", "json_decode()"),
        commonSnippets = listOf(
            "PHP Function" to "function add(${'$'}a, ${'$'}b) {\n    return ${'$'}a + ${'$'}b;\n}"
        )
    )

    val SWIFT = CodeLabLanguage(
        id = "swift",
        name = "Swift",
        extension = ".swift",
        color = Color(0xFFF05138),
        iconEmoji = "🦅",
        executionType = CodeExecutionType.COMPILED_LANG,
        defaultFileName = "main.swift",
        starterCode = """// Swift Code Lab Workspace
struct AppFeature {
    let name: String
    let isEnabled: Bool
}

let features = [
    AppFeature(name: "Code Editor", isEnabled: true),
    AppFeature(name: "AI Tutor", isEnabled: true)
]

print("Swift CodeLab Active Features:")
for f in features {
    if f.isEnabled {
        print("✓ \(f.name)")
    }
}
""",
        keywords = listOf("func", "let", "var", "struct", "class", "enum", "guard", "if", "else", "for", "in", "while", "switch", "case", "return", "import", "protocol", "extension"),
        commonFunctions = listOf("print()", "map()", "filter()", "append()", "compactMap()", "reduce()"),
        commonSnippets = listOf(
            "Swift Struct" to "struct User {\n    var id: String\n    var name: String\n}"
        )
    )

    val HTML = CodeLabLanguage(
        id = "html",
        name = "HTML",
        extension = ".html",
        color = Color(0xFFE34F26),
        iconEmoji = "🌐",
        executionType = CodeExecutionType.HTML,
        defaultFileName = "index.html",
        starterCode = """<!DOCTYPE html>
<html>
<head>
  <style>
    body { font-family: sans-serif; padding: 20px; background: #0f172a; color: #f8fafc; }
    h1 { color: #38bdf8; }
    .card { background: #1e293b; padding: 16px; border-radius: 12px; border: 1px solid #334155; }
    button { background: #38bdf8; color: #0f172a; border: none; padding: 10px 16px; border-radius: 8px; font-weight: bold; }
  </style>
</head>
<body>
  <h1>🚀 Code Lab Live Preview</h1>
  <div class="card">
    <p>Build and preview modern responsive web pages right inside CodeMaster!</p>
    <button>Interactive Button</button>
  </div>
</body>
</html>
""",
        keywords = listOf("<!DOCTYPE html>", "<html>", "<head>", "<body>", "<div>", "<span>", "<h1>", "<h2>", "<h3>", "<p>", "<a>", "<button>", "<ul>", "<li>", "<input>", "<img>", "<form>", "<script>", "<style>"),
        commonFunctions = listOf("document.getElementById()", "document.querySelector()", "addEventListener()"),
        commonSnippets = listOf(
            "HTML5 Skeleton" to "<!DOCTYPE html>\n<html>\n<head>\n  <title>App</title>\n</head>\n<body>\n  <h1>Title</h1>\n</body>\n</html>"
        )
    )

    val CSS = CodeLabLanguage(
        id = "css",
        name = "CSS",
        extension = ".css",
        color = Color(0xFF1572B6),
        iconEmoji = "🎨",
        executionType = CodeExecutionType.CSS,
        defaultFileName = "styles.css",
        starterCode = """/* CSS Code Lab Stylesheet */
body {
  font-family: 'Inter', system-ui, sans-serif;
  background-color: #0d1117;
  color: #c9d1d9;
  margin: 0;
  padding: 24px;
}

.hero-title {
  font-size: 28px;
  font-weight: 800;
  color: #58a6ff;
  margin-bottom: 12px;
}

.badge {
  display: inline-block;
  background: #238636;
  color: #ffffff;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}
""",
        keywords = listOf("color", "background", "font-family", "font-size", "margin", "padding", "display", "flex", "grid", "border", "border-radius", "width", "height", "box-shadow", "justify-content", "align-items"),
        commonFunctions = listOf("rgb()", "rgba()", "calc()", "var()"),
        commonSnippets = listOf(
            "Flex Center" to "display: flex;\njustify-content: center;\nalign-items: center;",
            "Card Shadow" to "box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);\nborder-radius: 12px;"
        )
    )

    val SQL = CodeLabLanguage(
        id = "sql",
        name = "SQL",
        extension = ".sql",
        color = Color(0xFFE48E00),
        iconEmoji = "🗄️",
        executionType = CodeExecutionType.SQL,
        defaultFileName = "queries.sql",
        starterCode = """-- SQL Code Lab Database Workspace
CREATE TABLE developers (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    language VARCHAR(30),
    xp INT
);

INSERT INTO developers VALUES (1, 'Alice', 'Python', 450);
INSERT INTO developers VALUES (2, 'Bob', 'Kotlin', 780);
INSERT INTO developers VALUES (3, 'Charlie', 'JavaScript', 320);

-- Query developers with XP > 400
SELECT name, language, xp 
FROM developers 
WHERE xp > 400 
ORDER BY xp DESC;
""",
        keywords = listOf("SELECT", "FROM", "WHERE", "INSERT", "INTO", "VALUES", "CREATE", "TABLE", "UPDATE", "SET", "DELETE", "JOIN", "ON", "GROUP", "BY", "ORDER", "BY", "HAVING", "LIMIT", "PRIMARY", "KEY", "FOREIGN", "AS", "ASC", "DESC", "INT", "VARCHAR", "TEXT"),
        commonFunctions = listOf("COUNT()", "SUM()", "AVG()", "MIN()", "MAX()", "UPPER()", "LOWER()", "COALESCE()"),
        commonSnippets = listOf(
            "Select Query" to "SELECT * FROM users WHERE active = 1 ORDER BY created_at DESC;",
            "Create Table" to "CREATE TABLE products (\n    id INT PRIMARY KEY,\n    title VARCHAR(100),\n    price DECIMAL(10,2)\n);"
        )
    )

    val ALL = listOf(
        PYTHON,
        JAVASCRIPT,
        TYPESCRIPT,
        KOTLIN,
        JAVA,
        CPP,
        C,
        CSHARP,
        GO,
        RUST,
        PHP,
        SWIFT,
        HTML,
        CSS,
        SQL
    )

    fun getById(id: String): CodeLabLanguage {
        return ALL.find { it.id.lowercase() == id.lowercase() } ?: PYTHON
    }
}
