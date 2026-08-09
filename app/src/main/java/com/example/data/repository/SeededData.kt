package com.example.data.repository

import com.example.data.model.BadgeItem
import com.example.data.model.CareerRoadmap
import com.example.data.model.CareerStage
import com.example.data.model.ChallengeDifficulty
import com.example.data.model.CodingChallenge
import com.example.data.model.LanguageInfo
import com.example.data.model.LessonItem
import com.example.data.model.ProjectItem
import com.example.data.model.ProjectTask
import com.example.data.model.QuizQuestion
import com.example.data.model.TypingLesson

object SeededData {

    val languages = listOf(
        LanguageInfo(
            id = "python",
            name = "Python",
            iconRes = "🐍",
            description = "High-level language known for clean syntax, data science, and AI.",
            colorHex = "#3776AB",
            category = "General & AI"
        ),
        LanguageInfo(
            id = "c",
            name = "C",
            iconRes = "⚡",
            description = "Fast low-level language powering operating systems and hardware.",
            colorHex = "#A8B9CC",
            category = "Systems"
        ),
        LanguageInfo(
            id = "cpp",
            name = "C++",
            iconRes = "🚀",
            description = "High-performance object-oriented programming for systems and game dev.",
            colorHex = "#00599C",
            category = "Systems & Games"
        ),
        LanguageInfo(
            id = "java",
            name = "Java",
            iconRes = "☕",
            description = "Enterprise class-based language powering Android and backend services.",
            colorHex = "#5382A1",
            category = "Enterprise & Android"
        ),
        LanguageInfo(
            id = "javascript",
            name = "JavaScript",
            iconRes = "🌐",
            description = "The dynamic language of the web for client-side and Node.js backend.",
            colorHex = "#F7DF1E",
            category = "Web"
        ),
        LanguageInfo(
            id = "html",
            name = "HTML",
            iconRes = "🧱",
            description = "HyperText Markup Language: the structural backbone of web pages.",
            colorHex = "#E34F26",
            category = "Web Design"
        ),
        LanguageInfo(
            id = "css",
            name = "CSS",
            iconRes = "🎨",
            description = "Cascading Style Sheets: style, layout, and visual presentation.",
            colorHex = "#1572B6",
            category = "Web Design"
        ),
        LanguageInfo(
            id = "sql",
            name = "SQL",
            iconRes = "🗄️",
            description = "Structured Query Language for querying and managing relational databases.",
            colorHex = "#336791",
            category = "Databases"
        )
    )

    val lessons = listOf(
        // --- PYTHON LESSONS ---
        LessonItem(
            id = "py_intro",
            languageId = "python",
            title = "Introduction to Python",
            tier = "Beginner",
            explanation = "Python is an interpreted, high-level, general-purpose programming language created by Guido van Rossum. Its design philosophy emphasizes code readability.",
            concepts = listOf("Interpreted vs Compiled", "Indent-based blocks", "Dynamic typing", "Python REPL"),
            syntaxSnippet = "print(\"Hello, CodeMaster!\")",
            codeExample = "# First Python Program\nname = \"Alex\"\nprint(f\"Welcome to Python, {name}!\")",
            expectedOutput = "Welcome to Python, Alex!",
            commonMistakes = "Forgetting matching quotes or introducing inconsistent tab/space indentation.",
            practiceQuestion = "What function is used to print text in Python?",
            miniChallenge = "Write a statement that prints your favorite programming language."
        ),
        LessonItem(
            id = "py_vars",
            languageId = "python",
            title = "Variables & Data Types",
            tier = "Beginner",
            explanation = "Variables in Python store values in memory. Python automatically detects data types like integers, floats, strings, and booleans.",
            concepts = listOf("Variable Assignment", "int, float, str, bool", "type() function", "Dynamic Typing"),
            syntaxSnippet = "age = 20\nprice = 19.99\nuser = \"Maria\"\nis_active = True",
            codeExample = "x = 10\ny = 2.5\nlabel = \"Items: \"\nprint(label + str(x))\nprint(f\"Total cost: {x * y}\")",
            expectedOutput = "Items: 10\nTotal cost: 25.0",
            commonMistakes = "Trying to concatenate strings with integers without explicit str() conversion.",
            practiceQuestion = "Which operator creates a string formatted variable in Python?",
            miniChallenge = "Create a variable 'score' with value 100 and print 'Score is 100'."
        ),
        LessonItem(
            id = "py_conditions",
            languageId = "python",
            title = "Conditional Logic (if/elif/else)",
            tier = "Beginner",
            explanation = "Control the flow of your program based on true/false boolean expressions.",
            concepts = listOf("if statement", "elif and else", "Boolean logic (and, or, not)", "Indentation"),
            syntaxSnippet = "if score >= 90:\n    print(\"Grade: A\")\nelif score >= 75:\n    print(\"Grade: B\")\nelse:\n    print(\"Keep practicing!\")",
            codeExample = "speed = 65\nif speed > 70:\n    print(\"Over speed limit!\")\nelif speed >= 50:\n    print(\"Normal driving speed\")\nelse:\n    print(\"Driving slow\")",
            expectedOutput = "Normal driving speed",
            commonMistakes = "Missing the trailing colon (:) at the end of if/elif/else statements.",
            practiceQuestion = "What symbol must follow an if condition in Python?",
            miniChallenge = "Write an if statement that checks if x > 0 and prints 'Positive'."
        ),
        LessonItem(
            id = "py_loops",
            languageId = "python",
            title = "Loops (for & while)",
            tier = "Beginner",
            explanation = "Loops repeat a block of code multiple times. Use 'for' when iteration count is known and 'while' when repeating until a condition changes.",
            concepts = listOf("range() function", "for item in list", "while condition", "break and continue"),
            syntaxSnippet = "for i in range(5):\n    print(i)\n\ncount = 3\nwhile count > 0:\n    print(count)\n    count -= 1",
            codeExample = "total = 0\nfor num in [10, 20, 30]:\n    total += num\nprint(f\"Sum = {total}\")",
            expectedOutput = "Sum = 60",
            commonMistakes = "Creating an infinite while loop by forgetting to update the loop counter variable.",
            practiceQuestion = "How many times does range(3) loop?",
            miniChallenge = "Print numbers from 1 to 5 using a for loop."
        ),
        LessonItem(
            id = "py_functions",
            languageId = "python",
            title = "Functions & Parameters",
            tier = "Intermediate",
            explanation = "Functions break code into reusable, modular blocks. They accept input arguments and return results.",
            concepts = listOf("def keyword", "Arguments & Parameters", "Return values", "Default values"),
            syntaxSnippet = "def calculate_area(width, height=10):\n    return width * height",
            codeExample = "def greet(name, language=\"Python\"):\n    return f\"Hello {name}, enjoy {language}!\"\n\nprint(greet(\"Dev\"))",
            expectedOutput = "Hello Dev, enjoy Python!",
            commonMistakes = "Forgetting to write 'return' when expecting a value back from the function.",
            practiceQuestion = "Which keyword defines a function in Python?",
            miniChallenge = "Write a function square(x) that returns x * x."
        ),
        LessonItem(
            id = "py_collections",
            languageId = "python",
            title = "Lists & Dictionaries",
            tier = "Intermediate",
            explanation = "Lists store ordered sequences of items. Dictionaries store key-value pairs for ultra-fast lookup.",
            concepts = listOf("List indexing & slicing", "append(), pop()", "Dict keys & values", "Iterating dicts"),
            syntaxSnippet = "fruits = [\"apple\", \"banana\"]\nstudent = {\"name\": \"Leo\", \"grade\": \"A\"}",
            codeExample = "languages = [\"Python\", \"C++\", \"Java\"]\nlanguages.append(\"Rust\")\nprint(f\"First: {languages[0]}, Total: {len(languages)}\")",
            expectedOutput = "First: Python, Total: 4",
            commonMistakes = "Accessing list index out of range or using non-existent keys in dictionaries.",
            practiceQuestion = "How do you add a new element to a Python list?",
            miniChallenge = "Create a list of 3 items and print the last item using index -1."
        ),
        LessonItem(
            id = "py_oop",
            languageId = "python",
            title = "Object-Oriented Programming (OOP)",
            tier = "Advanced",
            explanation = "OOP organizes code into classes and objects. Classes act as blueprints containing properties (attributes) and behaviors (methods).",
            concepts = listOf("Class definition", "__init__ constructor", "self parameter", "Inheritance"),
            syntaxSnippet = "class Student:\n    def __init__(self, name, level):\n        self.name = name\n        self.level = level",
            codeExample = "class Developer:\n    def __init__(self, name, lang):\n        self.name = name\n        self.lang = lang\n    def code(self):\n        return f\"{self.name} is writing {self.lang}!\"\n\ndev = Developer(\"Sarah\", \"Python\")\nprint(dev.code())",
            expectedOutput = "Sarah is writing Python!",
            commonMistakes = "Forgetting 'self' as the first parameter in class methods.",
            practiceQuestion = "What special method initializes a new object instance in Python?",
            miniChallenge = "Define a Car class with 'brand' and a method 'drive()'."
        ),

        // --- C LESSONS ---
        LessonItem(
            id = "c_intro",
            languageId = "c",
            title = "Introduction to C & Memory",
            tier = "Beginner",
            explanation = "C is a foundational procedural programming language created by Dennis Ritchie. It provides direct memory manipulation through pointers.",
            concepts = listOf("Main function entry", "printf & scanf", "Compilation process", "Header files <stdio.h>"),
            syntaxSnippet = "#include <stdio.h>\n\nint main() {\n    printf(\"Hello C!\\n\");\n    return 0;\n}",
            codeExample = "#include <stdio.h>\n\nint main() {\n    int score = 95;\n    printf(\"Player score: %d\\n\", score);\n    return 0;\n}",
            expectedOutput = "Player score: 95",
            commonMistakes = "Omitting semicolon at the end of statements or forgetting #include <stdio.h>.",
            practiceQuestion = "What header file is required for printf in C?",
            miniChallenge = "Write a main function that prints your age using %d."
        ),
        LessonItem(
            id = "c_pointers",
            languageId = "c",
            title = "Pointers & Memory Addresses",
            tier = "Intermediate",
            explanation = "Pointers hold the physical memory address of another variable. They enable efficient array handling and dynamic memory allocation.",
            concepts = listOf("& address-of operator", "* dereference operator", "Pointer variables", "NULL pointers"),
            syntaxSnippet = "int num = 42;\nint *ptr = &num;\nprintf(\"Value = %d\\n\", *ptr);",
            codeExample = "#include <stdio.h>\n\nint main() {\n    int val = 100;\n    int *p = &val;\n    *p = 200;\n    printf(\"Updated val = %d\\n\", val);\n    return 0;\n}",
            expectedOutput = "Updated val = 200",
            commonMistakes = "Dereferencing uninitialized or NULL pointers leading to Segmentation Faults.",
            practiceQuestion = "Which operator gets the memory address of a variable in C?",
            miniChallenge = "Declare an int variable and a pointer pointing to it."
        ),

        // --- C++ LESSONS ---
        LessonItem(
            id = "cpp_intro",
            languageId = "cpp",
            title = "C++ Basics & I/O",
            tier = "Beginner",
            explanation = "C++ extends C with classes, object-oriented concepts, template metaprogramming, and rich standard library containers (STL).",
            concepts = listOf("std::cout & std::cin", "<iostream> header", "Namespaces", "References & Const"),
            syntaxSnippet = "#include <iostream>\nusing namespace std;\n\nint main() {\n    cout << \"Hello C++\" << endl;\n    return 0;\n}",
            codeExample = "#include <iostream>\n#include <string>\nusing namespace std;\n\nint main() {\n    string dev = \"Coder\";\n    cout << \"Ready to learn, \" << dev << \"!\" << endl;\n    return 0;\n}",
            expectedOutput = "Ready to learn, Coder!",
            commonMistakes = "Confusing insertion operator (<<) with extraction operator (>>).",
            practiceQuestion = "What object is used for output stream in C++?",
            miniChallenge = "Write cout to print numbers 10 and 20."
        ),

        // --- JAVA LESSONS ---
        LessonItem(
            id = "java_intro",
            languageId = "java",
            title = "Java Classes & JVM",
            tier = "Beginner",
            explanation = "Java compiles to bytecode executed on the Java Virtual Machine (JVM). Everything in Java lives inside a class.",
            concepts = listOf("Class declaration", "public static void main(String[] args)", "System.out.println", "Strong typing"),
            syntaxSnippet = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Java Rules!\");\n    }\n}",
            codeExample = "public class Demo {\n    public static void main(String[] args) {\n        int x = 15;\n        System.out.println(\"Value of x is \" + x);\n    }\n}",
            expectedOutput = "Value of x is 15",
            commonMistakes = "Class name mismatch with filename or missing String[] args in main method.",
            practiceQuestion = "What Virtual Machine runs compiled Java bytecode?",
            miniChallenge = "Write a System.out.println statement."
        ),

        // --- JAVASCRIPT LESSONS ---
        LessonItem(
            id = "js_intro",
            languageId = "javascript",
            title = "JavaScript Syntax & ES6",
            tier = "Beginner",
            explanation = "JavaScript is the core scripting language of the modern web, running in web browsers and servers via Node.js.",
            concepts = listOf("let & const vs var", "console.log", "Arrow functions", "Template literals"),
            syntaxSnippet = "const name = \"Master\";\nconsole.log(`Hello, \${name}!`);",
            codeExample = "const add = (a, b) => a + b;\nconsole.log(\"Sum:\", add(5, 7));",
            expectedOutput = "Sum: 12",
            commonMistakes = "Attempting to reassign a variable declared with 'const'.",
            practiceQuestion = "Which keyword declares a block-scoped constant variable in JS?",
            miniChallenge = "Define an arrow function multiply(a, b)."
        ),

        // --- HTML LESSONS ---
        LessonItem(
            id = "html_basics",
            languageId = "html",
            title = "HTML Page Structure & Tags",
            tier = "Beginner",
            explanation = "HTML defines the structural layout of content on web pages using element tags.",
            concepts = listOf("<!DOCTYPE html>", "<html>, <head>, <body>", "Headings <h1>..<h6>", "Paragraphs <p> & Links <a>"),
            syntaxSnippet = "<!DOCTYPE html>\n<html>\n  <body>\n    <h1>Title</h1>\n  </body>\n</html>",
            codeExample = "<h1>Welcome to CodeMaster</h1>\n<p>Start learning <strong>programming</strong> today!</p>",
            expectedOutput = "Heading: Welcome to CodeMaster\nParagraph: Start learning programming today!",
            commonMistakes = "Forgetting opening or closing angle brackets or nesting tags incorrectly.",
            practiceQuestion = "Which tag defines the main heading in HTML?",
            miniChallenge = "Write a paragraph tag containing your name."
        ),

        // --- CSS LESSONS ---
        LessonItem(
            id = "css_basics",
            languageId = "css",
            title = "CSS Selectors & Flexbox",
            tier = "Beginner",
            explanation = "CSS styles the layout, colors, typography, and responsive positioning of HTML elements.",
            concepts = listOf("Selectors (class, id, element)", "Box Model (margin, padding, border)", "Colors & Backgrounds", "Flexbox layout"),
            syntaxSnippet = ".card {\n  background-color: #1E293B;\n  padding: 16px;\n  border-radius: 8px;\n}",
            codeExample = "h1 {\n  color: #6366F1;\n  font-size: 24px;\n}",
            expectedOutput = "Applies indigo color and 24px font size to h1 elements.",
            commonMistakes = "Forgetting dot (.) for class selectors or hash (#) for ID selectors.",
            practiceQuestion = "What symbol is used to select elements by class name in CSS?",
            miniChallenge = "Write a CSS rule to set text color of .btn class to white."
        ),

        // --- SQL LESSONS ---
        LessonItem(
            id = "sql_basics",
            languageId = "sql",
            title = "SQL SELECT Queries & Filtering",
            tier = "Beginner",
            explanation = "SQL is used to query, insert, update, and manipulate data stored in relational databases.",
            concepts = listOf("SELECT * FROM table", "WHERE clause", "ORDER BY & LIMIT", "COUNT & SUM aggregations"),
            syntaxSnippet = "SELECT name, score FROM students WHERE score >= 80 ORDER BY score DESC;",
            codeExample = "-- Fetching top developers\nSELECT id, name, lang FROM developers WHERE lang = 'Python';",
            expectedOutput = "Table results matching Python developers.",
            commonMistakes = "Forgetting quotes around string values in WHERE clauses.",
            practiceQuestion = "Which keyword filters rows in a SQL query?",
            miniChallenge = "Write a query to select all columns from 'users'."
        )
    )

    val quizzes = listOf(
        QuizQuestion(
            id = "q_py_1",
            languageId = "python",
            question = "Which keyword is used to define a function in Python?",
            options = listOf("function", "def", "func", "define"),
            correctIndex = 1,
            explanation = "In Python, 'def' is the reserved keyword used to define user functions."
        ),
        QuizQuestion(
            id = "q_py_2",
            languageId = "python",
            question = "What is the output of len(['a', 'b', 'c']) in Python?",
            options = listOf("2", "3", "4", "Error"),
            correctIndex = 1,
            explanation = "The len() function returns the total number of items in a sequence."
        ),
        QuizQuestion(
            id = "q_c_1",
            languageId = "c",
            question = "Which operator is used to get the memory address of a variable in C?",
            options = listOf("*", "&", "->", "%"),
            correctIndex = 1,
            explanation = "The '&' (address-of) operator retrieves the memory location address of a variable."
        ),
        QuizQuestion(
            id = "q_js_1",
            languageId = "javascript",
            question = "Which of the following is NOT a valid JS variable declaration?",
            options = listOf("let x = 5;", "const y = 10;", "var z = 15;", "dim w = 20;"),
            correctIndex = 3,
            explanation = "'dim' is used in languages like Visual Basic, not JavaScript."
        ),
        QuizQuestion(
            id = "q_sql_1",
            languageId = "sql",
            question = "Which SQL clause is used to filter records based on a specified condition?",
            options = listOf("ORDER BY", "GROUP BY", "WHERE", "SELECT"),
            correctIndex = 2,
            explanation = "The WHERE clause is used to filter records so only rows meeting criteria are returned."
        )
    )

    val challenges = listOf(
        CodingChallenge(
            id = "c_add_two",
            title = "Add Two Numbers",
            languageId = "python",
            difficulty = ChallengeDifficulty.EASY,
            problemStatement = "Write a function or code that adds two integers 'a' and 'b' and returns the result.",
            exampleInput = "a = 5, b = 10",
            exampleOutput = "15",
            hint = "Use the '+' addition operator.",
            starterCode = "def add(a, b):\n    # Write your code here\n    return a + b",
            expectedKeywords = listOf("def", "+", "return")
        ),
        CodingChallenge(
            id = "c_even_odd",
            title = "Even or Odd",
            languageId = "python",
            difficulty = ChallengeDifficulty.EASY,
            problemStatement = "Determine if an integer 'n' is even or odd. Return 'Even' or 'Odd'.",
            exampleInput = "n = 7",
            exampleOutput = "\"Odd\"",
            hint = "Use the modulo operator '%' to check if remainder when divided by 2 equals 0.",
            starterCode = "def check_even_odd(n):\n    if n % 2 == 0:\n        return \"Even\"\n    return \"Odd\"",
            expectedKeywords = listOf("%", "if", "return")
        ),
        CodingChallenge(
            id = "c_largest_num",
            title = "Find Largest Number",
            languageId = "python",
            difficulty = ChallengeDifficulty.EASY,
            problemStatement = "Given three numbers a, b, and c, return the maximum value among them.",
            exampleInput = "a = 12, b = 45, c = 23",
            exampleOutput = "45",
            hint = "You can use max(a, b, c) or comparative if/else statements.",
            starterCode = "def find_max(a, b, c):\n    return max(a, b, c)",
            expectedKeywords = listOf("max", "return")
        ),
        CodingChallenge(
            id = "c_factorial",
            title = "Factorial of a Number",
            languageId = "python",
            difficulty = ChallengeDifficulty.MEDIUM,
            problemStatement = "Calculate n! (n factorial) for a non-negative integer 'n'.",
            exampleInput = "n = 5",
            exampleOutput = "120",
            hint = "Factorial of 5 is 5 * 4 * 3 * 2 * 1 = 120. Handle base case 0! = 1.",
            starterCode = "def factorial(n):\n    result = 1\n    for i in range(1, n + 1):\n        result *= i\n    return result",
            expectedKeywords = listOf("for", "range", "return")
        ),
        CodingChallenge(
            id = "c_fibonacci",
            title = "Fibonacci Sequence",
            languageId = "python",
            difficulty = ChallengeDifficulty.MEDIUM,
            problemStatement = "Return the n-th Fibonacci number where Fib(0)=0 and Fib(1)=1.",
            exampleInput = "n = 6",
            exampleOutput = "8",
            hint = "Each term is the sum of the two preceding terms.",
            starterCode = "def fibonacci(n):\n    if n <= 1:\n        return n\n    a, b = 0, 1\n    for _ in range(2, n + 1):\n        a, b = b, a + b\n    return b",
            expectedKeywords = listOf("if", "for", "return")
        ),
        CodingChallenge(
            id = "c_reverse_str",
            title = "Reverse a String",
            languageId = "python",
            difficulty = ChallengeDifficulty.EASY,
            problemStatement = "Reverse the string 's' and return the reversed string.",
            exampleInput = "s = \"hello\"",
            exampleOutput = "\"olleh\"",
            hint = "In Python, string slicing `s[::-1]` reverses a string instantly.",
            starterCode = "def reverse_string(s):\n    return s[::-1]",
            expectedKeywords = listOf("[::-1]", "return")
        ),
        CodingChallenge(
            id = "c_count_vowels",
            title = "Count Vowels",
            languageId = "python",
            difficulty = ChallengeDifficulty.EASY,
            problemStatement = "Count how many vowels (a, e, i, o, u) are present in string 'text'.",
            exampleInput = "text = \"CodeMaster\"",
            exampleOutput = "4",
            hint = "Iterate characters and check `if char.lower() in 'aeiou'`.",
            starterCode = "def count_vowels(text):\n    vowels = 'aeiou'\n    return sum(1 for char in text.lower() if char in vowels)",
            expectedKeywords = listOf("in", "return")
        ),
        CodingChallenge(
            id = "c_prime",
            title = "Prime Number Check",
            languageId = "python",
            difficulty = ChallengeDifficulty.HARD,
            problemStatement = "Return True if n is a prime number (> 1 with no divisors other than 1 and itself), otherwise False.",
            exampleInput = "n = 29",
            exampleOutput = "True",
            hint = "Check divisibility from 2 up to sqrt(n).",
            starterCode = "def is_prime(n):\n    if n < 2:\n        return False\n    for i in range(2, int(n**0.5) + 1):\n        if n % i == 0:\n            return False\n    return True",
            expectedKeywords = listOf("for", "if", "return")
        )
    )

    val typingLessons = listOf(
        TypingLesson(
            id = "t_1",
            title = "1. Keyboard Basics & Home Row",
            level = "Beginner",
            description = "Place index fingers on 'F' and 'J'. Practice home row keys: A S D F  J K L ;",
            targetKeys = "a s d f j k l ;",
            practicePassage = "asdf jkl; asdf jkl; a s d f j k l ; a f s d j ; k l"
        ),
        TypingLesson(
            id = "t_2",
            title = "2. Top Row Keys",
            level = "Beginner",
            description = "Reach upper row keys with correct finger placement: Q W E R T  Y U I O P",
            targetKeys = "q w e r t y u i o p",
            practicePassage = "qwer tyui op qwer tyui op quiet power write report"
        ),
        TypingLesson(
            id = "t_3",
            title = "3. Bottom Row Keys",
            level = "Beginner",
            description = "Reach lower row keys: Z X C V B  N M , . /",
            targetKeys = "z x c v b n m",
            practicePassage = "zxcv bnm zxcv bnm scan text zoom voice baseline"
        ),
        TypingLesson(
            id = "t_4",
            title = "4. Programming Symbols & Brackets",
            level = "Programming",
            description = "Master developer key combinations: { } [ ] ( ) ; : < > = + - * /",
            targetKeys = "{ } [ ] ( ) ; = + -",
            practicePassage = "if (x == 10) { return array[i] + val; } else { count++; }",
            isCodeMode = true
        ),
        TypingLesson(
            id = "t_5",
            title = "5. Python Developer Snippet",
            level = "Programming",
            description = "Practice typing clean Python code with indentation and formatting.",
            targetKeys = "def if for in range return :",
            practicePassage = "def calculate_total(items):\n    total = 0\n    for item in items:\n        total += item.price\n    return total",
            isCodeMode = true
        )
    )

    val careerRoadmaps = listOf(
        CareerRoadmap(
            id = "software_dev",
            title = "Software Developer",
            category = "Core Software Engineering",
            description = "Build robust applications, systems software, and client-facing technology solutions.",
            stages = listOf(
                CareerStage(
                    stageNumber = 1,
                    key = "sw_s1",
                    title = "Computer Fundamentals",
                    description = "Understand binary, CPU architecture, memory management, and OS basics.",
                    recommendedLanguages = listOf("C", "Python"),
                    tools = listOf("Terminal", "Git", "VS Code"),
                    concepts = listOf("Memory addresses", "Data structures", "Algorithms"),
                    projects = listOf("Command Line Utility")
                ),
                CareerStage(
                    stageNumber = 2,
                    key = "sw_s2",
                    title = "Programming & OOP",
                    description = "Master object-oriented programming principles and clean modular design.",
                    recommendedLanguages = listOf("Java", "C++", "Python"),
                    tools = listOf("IDE", "Maven/Gradle", "Debugger"),
                    concepts = listOf("Encapsulation", "Inheritance", "Polymorphism", "Abstraction"),
                    projects = listOf("Student Management System")
                ),
                CareerStage(
                    stageNumber = 3,
                    key = "sw_s3",
                    title = "Data Structures & Algorithms",
                    description = "Master arrays, linked lists, trees, graphs, sorting, and Big-O time complexity.",
                    recommendedLanguages = listOf("Python", "C++"),
                    tools = listOf("LeetCode", "Algorithm Visualizer"),
                    concepts = listOf("Trees & Graphs", "Dynamic Programming", "Recursion"),
                    projects = listOf("Pathfinding Visualizer")
                ),
                CareerStage(
                    stageNumber = 4,
                    key = "sw_s4",
                    title = "Databases & APIs",
                    description = "Learn relational SQL, RESTful API design, and asynchronous networking.",
                    recommendedLanguages = listOf("SQL", "Java", "JavaScript"),
                    tools = listOf("PostgreSQL", "Postman", "Docker"),
                    concepts = listOf("Database Normalization", "Indexing", "HTTP Methods"),
                    projects = listOf("REST API Backend")
                )
            )
        ),
        CareerRoadmap(
            id = "fullstack_web",
            title = "Full-Stack Web Developer",
            category = "Web Development",
            description = "Design and build end-to-end web applications from UI components to server backend.",
            stages = listOf(
                CareerStage(
                    stageNumber = 1,
                    key = "fs_s1",
                    title = "Frontend Basics",
                    description = "Master structure, layout, typography, and responsive styles.",
                    recommendedLanguages = listOf("HTML", "CSS", "JavaScript"),
                    tools = listOf("Browser DevTools", "VS Code"),
                    concepts = listOf("DOM Manipulation", "Flexbox & Grid", "ES6 Async/Await"),
                    projects = listOf("Portfolio Website", "Interactive Dashboard")
                ),
                CareerStage(
                    stageNumber = 2,
                    key = "fs_s2",
                    title = "Modern Frontend Frameworks",
                    description = "Build single page apps with state management and component lifecycle.",
                    recommendedLanguages = listOf("JavaScript", "TypeScript"),
                    tools = listOf("React", "Vite", "Tailwind CSS"),
                    concepts = listOf("Component State", "Hooks", "Routing"),
                    projects = listOf("Task Manager App")
                ),
                CareerStage(
                    stageNumber = 3,
                    key = "fs_s3",
                    title = "Backend & Databases",
                    description = "Server architecture, authentication, databases, and deployment.",
                    recommendedLanguages = listOf("JavaScript", "Python", "SQL"),
                    tools = listOf("Node.js", "Express", "PostgreSQL"),
                    concepts = listOf("RESTful Services", "JWT Auth", "ORMs"),
                    projects = listOf("Full-Stack E-Commerce Platform")
                )
            )
        ),
        CareerRoadmap(
            id = "frontend_dev",
            title = "Frontend Developer",
            category = "Web UI & UX",
            description = "Build responsive, beautiful, and interactive modern user interfaces for web and web apps.",
            stages = listOf(
                CareerStage(
                    stageNumber = 1,
                    key = "fe_s1",
                    title = "HTML, CSS & JS Fundamentals",
                    description = "Master semantical HTML5, CSS layout grids, flexbox, animations, and modern JS.",
                    recommendedLanguages = listOf("HTML", "CSS", "JavaScript"),
                    tools = listOf("VS Code", "Chrome DevTools", "Figma"),
                    concepts = listOf("Responsive Design", "DOM API", "CSS Grid/Flexbox"),
                    projects = listOf("Landing Page Mockup")
                ),
                CareerStage(
                    stageNumber = 2,
                    key = "fe_s2",
                    title = "Component Frameworks",
                    description = "Build dynamic UIs using state management, reactive rendering, and modular architecture.",
                    recommendedLanguages = listOf("JavaScript", "TypeScript"),
                    tools = listOf("React", "Next.js", "Tailwind CSS"),
                    concepts = listOf("State Hooks", "Virtual DOM", "API Integration"),
                    projects = listOf("Interactive SaaS Dashboard")
                )
            )
        ),
        CareerRoadmap(
            id = "backend_dev",
            title = "Backend Developer",
            category = "Server Systems",
            description = "Power application servers, microservices, databases, authentication, and secure APIs.",
            stages = listOf(
                CareerStage(
                    stageNumber = 1,
                    key = "be_s1",
                    title = "Server Languages & SQL",
                    description = "Learn server-side execution, relational database modeling, and SQL queries.",
                    recommendedLanguages = listOf("Java", "Python", "SQL"),
                    tools = listOf("PostgreSQL", "Postman", "Docker"),
                    concepts = listOf("HTTP Request/Response", "Database Schema", "CRUD Operations"),
                    projects = listOf("User Management REST Service")
                ),
                CareerStage(
                    stageNumber = 2,
                    key = "be_s2",
                    title = "Microservices & Cloud",
                    description = "Scale services using caching, message queues, containerization, and cloud hosting.",
                    recommendedLanguages = listOf("Java", "Python", "JavaScript"),
                    tools = listOf("Redis", "Docker", "AWS / Cloud SQL"),
                    concepts = listOf("Caching", "OAuth2 & JWT", "Load Balancing"),
                    projects = listOf("High-Throughput Gateway")
                )
            )
        ),
        CareerRoadmap(
            id = "app_dev",
            title = "App Developer",
            category = "Mobile Engineering",
            description = "Build high-performance native and cross-platform mobile apps for Android and iOS devices.",
            stages = listOf(
                CareerStage(
                    stageNumber = 1,
                    key = "app_s1",
                    title = "Kotlin & Android Jetpack",
                    description = "Master Kotlin programming, modern Jetpack Compose UI, and Activity lifecycles.",
                    recommendedLanguages = listOf("Java", "Python"),
                    tools = listOf("Android Studio", "Gradle", "Emulator"),
                    concepts = listOf("Compose Layouts", "ViewModel & State", "Coroutines"),
                    projects = listOf("Mobile Habit Tracker")
                ),
                CareerStage(
                    stageNumber = 2,
                    key = "app_s2",
                    title = "Local DB & Cloud Sync",
                    description = "Persist data offline with Room database and sync with cloud APIs.",
                    recommendedLanguages = listOf("Java", "SQL"),
                    tools = listOf("Room DB", "Retrofit", "Firebase"),
                    concepts = listOf("Offline First Architecture", "Background Workers", "Push Notifications"),
                    projects = listOf("Full Featured Mobile Reader App")
                )
            )
        ),
        CareerRoadmap(
            id = "game_dev",
            title = "Game Developer",
            category = "Interactive Systems",
            description = "Program 2D/3D physics engines, gameplay logic, graphics rendering, and multiplayer mechanics.",
            stages = listOf(
                CareerStage(
                    stageNumber = 1,
                    key = "gm_s1",
                    title = "C++ & Game Loop Logic",
                    description = "Learn low-level memory control, vectors, collision detection, and frame rendering.",
                    recommendedLanguages = listOf("C++", "C"),
                    tools = listOf("Visual Studio", "Unreal Engine", "Unity"),
                    concepts = listOf("Game Loop", "Collision Physics", "Object Pooling"),
                    projects = listOf("2D Arcade Platformer")
                ),
                CareerStage(
                    stageNumber = 2,
                    key = "gm_s2",
                    title = "3D Graphics & Shader Math",
                    description = "Master 3D transformations, mesh rendering, lighting shaders, and particle systems.",
                    recommendedLanguages = listOf("C++"),
                    tools = listOf("OpenGL", "HLSL/GLSL", "Blender"),
                    concepts = listOf("Matrices & Quaternions", "Lighting Models", "Multiplayer Networking"),
                    projects = listOf("3D Physics Sandbox")
                )
            )
        ),
        CareerRoadmap(
            id = "ai_ml_dev",
            title = "AI / Machine Learning Developer",
            category = "Artificial Intelligence",
            description = "Train neural networks, build intelligent prediction models, and integrate generative AI.",
            stages = listOf(
                CareerStage(
                    stageNumber = 1,
                    key = "ai_s1",
                    title = "Python & Math Foundations",
                    description = "Linear algebra, calculus, statistics, and fluency in Python.",
                    recommendedLanguages = listOf("Python"),
                    tools = listOf("Jupyter Notebooks", "NumPy", "Pandas"),
                    concepts = listOf("Matrix Operations", "Probability", "Data Cleaning"),
                    projects = listOf("Exploratory Data Analysis")
                ),
                CareerStage(
                    stageNumber = 2,
                    key = "ai_s2",
                    title = "Machine Learning Models",
                    description = "Supervised & unsupervised learning, regression, classification, clustering.",
                    recommendedLanguages = listOf("Python"),
                    tools = listOf("Scikit-Learn", "Matplotlib"),
                    concepts = listOf("Model Evaluation", "Cross-Validation", "Feature Engineering"),
                    projects = listOf("Predictive Model")
                ),
                CareerStage(
                    stageNumber = 3,
                    key = "ai_s3",
                    title = "Deep Learning & GenAI",
                    description = "Neural networks, transformers, LLMs, and vision models.",
                    recommendedLanguages = listOf("Python"),
                    tools = listOf("PyTorch", "Hugging Face", "Gemini API"),
                    concepts = listOf("Transformers", "Prompt Engineering", "Fine-tuning"),
                    projects = listOf("AI Assistant App")
                )
            )
        ),
        CareerRoadmap(
            id = "data_scientist",
            title = "Data Scientist",
            category = "Data Analytics",
            description = "Extract actionable intelligence from big data using statistical modeling and machine learning.",
            stages = listOf(
                CareerStage(
                    stageNumber = 1,
                    key = "ds_s1",
                    title = "SQL & Data Wrangling",
                    description = "Master complex SQL joins, aggregation, Python Pandas, and statistical distribution.",
                    recommendedLanguages = listOf("Python", "SQL"),
                    tools = listOf("Jupyter", "Pandas", "PostgreSQL"),
                    concepts = listOf("Hypothesis Testing", "Data Cleansing", "Correlation Analysis"),
                    projects = listOf("Customer Churn Analytics")
                ),
                CareerStage(
                    stageNumber = 2,
                    key = "ds_s2",
                    title = "Statistical Inference & Modeling",
                    description = "Build predictive statistical models, A/B testing, and interactive dashboard visuals.",
                    recommendedLanguages = listOf("Python", "SQL"),
                    tools = listOf("Seaborn", "Statsmodels", "PowerBI"),
                    concepts = listOf("Regression Analysis", "A/B Testing", "Time Series Forecasting"),
                    projects = listOf("Market Trend Forecasting Tool")
                )
            )
        ),
        CareerRoadmap(
            id = "cybersecurity_dev",
            title = "Cybersecurity Developer",
            category = "Security Engineering",
            description = "Protect systems, audit software vulnerabilities, build encryption engines, and secure networks.",
            stages = listOf(
                CareerStage(
                    stageNumber = 1,
                    key = "sec_s1",
                    title = "Networking & Low-Level C/Python",
                    description = "Master TCP/IP networking, packet inspection, C memory safety, and security scripting.",
                    recommendedLanguages = listOf("C", "Python"),
                    tools = listOf("Wireshark", "Linux Terminal", "Nmap"),
                    concepts = listOf("TCP/IP Stack", "Buffer Overflows", "Cryptographic Hashing"),
                    projects = listOf("Network Packet Analyzer")
                ),
                CareerStage(
                    stageNumber = 2,
                    key = "sec_s2",
                    title = "Web Security & Penetration Testing",
                    description = "Identify OWASP Top 10 vulnerabilities, implement secure authentication, and automate security audits.",
                    recommendedLanguages = listOf("Python", "JavaScript"),
                    tools = listOf("Burp Suite", "Kali Linux", "OWASP ZAP"),
                    concepts = listOf("SQL Injection Prevention", "XSS Mitigation", "AES/RSA Encryption"),
                    projects = listOf("Vulnerability Scanner Utility")
                )
            )
        )
    )

    val projects = listOf(
        ProjectItem(
            id = "proj_calculator",
            title = "Interactive Calculator",
            category = "Beginner",
            description = "Build a responsive calculator supporting basic arithmetic operations (+, -, *, /) and input clear.",
            requiredSkills = listOf("Variables", "Operators", "Conditional Logic"),
            technologies = listOf("Python", "JavaScript", "HTML/CSS"),
            tasks = listOf(
                ProjectTask("c_t1", "Design Calculator Layout", "Create buttons for digits 0-9 and operators."),
                ProjectTask("c_t2", "Implement Operator Logic", "Handle addition, subtraction, multiplication, and division."),
                ProjectTask("c_t3", "Handle Edge Cases", "Prevent division by zero and handle decimal input.")
            )
        ),
        ProjectItem(
            id = "proj_todo",
            title = "To-Do List App",
            category = "Beginner",
            description = "Create a task tracking application with create, mark complete, and delete functionalities.",
            requiredSkills = listOf("Arrays/Lists", "Functions", "UI Events"),
            technologies = listOf("JavaScript", "Python"),
            tasks = listOf(
                ProjectTask("td_t1", "Setup Task Data Structure", "Store tasks as list of objects with title and completed status."),
                ProjectTask("td_t2", "Add Task Handler", "Implement function to push new items."),
                ProjectTask("td_t3", "Toggle & Delete", "Allow users to mark tasks done or remove them.")
            )
        ),
        ProjectItem(
            id = "proj_expense",
            title = "Expense Tracker",
            category = "Intermediate",
            description = "Build a personal finance tracker that categorizes expenses and calculates total balance.",
            requiredSkills = listOf("Dictionaries/Objects", "Filtering", "Chart Visualization"),
            technologies = listOf("Python", "SQL", "JavaScript"),
            tasks = listOf(
                ProjectTask("et_t1", "Create Expense Model", "Store title, amount, category, date."),
                ProjectTask("et_t2", "Calculate Totals", "Compute total spending per category."),
                ProjectTask("et_t3", "Filter & Sort", "Filter expenses by date range or category.")
            )
        ),
        ProjectItem(
            id = "proj_fullstack",
            title = "Full-Stack Portal with Auth & DB",
            category = "Advanced",
            description = "Develop a web portal with user registration, secure login, database storage, and API REST endpoints.",
            requiredSkills = listOf("Full-Stack Architecture", "Databases", "Authentication"),
            technologies = listOf("Java/Node", "SQL", "HTML/CSS/JS"),
            tasks = listOf(
                ProjectTask("fs_t1", "Design Database Schema", "Create relational SQL tables for Users and Posts."),
                ProjectTask("fs_t2", "Implement Auth API", "Create signup and login endpoints with password hashing."),
                ProjectTask("fs_t3", "Connect Frontend UI", "Build responsive screens with live API fetching.")
            )
        )
    )

    val badges = listOf(
        BadgeItem(
            id = "badge_first_lesson",
            name = "First Lesson",
            description = "Completed your first programming lesson!",
            iconSymbol = "🏅",
            requiredXpOrAction = "Complete 1 lesson"
        ),
        BadgeItem(
            id = "badge_7_day_streak",
            name = "7-Day Streak",
            description = "Maintained a 7-day coding learning streak!",
            iconSymbol = "🔥",
            requiredXpOrAction = "7 consecutive days"
        ),
        BadgeItem(
            id = "badge_first_challenge",
            name = "First Challenge",
            description = "Solved your first coding challenge!",
            iconSymbol = "💻",
            requiredXpOrAction = "Complete 1 coding challenge"
        ),
        BadgeItem(
            id = "badge_40_wpm",
            name = "40 WPM Typist",
            description = "Achieved 40 WPM typing speed!",
            iconSymbol = "⌨️",
            requiredXpOrAction = "Reach 40 WPM in typing test"
        ),
        BadgeItem(
            id = "badge_python_beg",
            name = "Python Beginner",
            description = "Mastered all Python beginner topics!",
            iconSymbol = "🐍",
            requiredXpOrAction = "Complete all Python beginner lessons"
        ),
        BadgeItem(
            id = "badge_first_project",
            name = "First Project",
            description = "Completed all tasks for a project!",
            iconSymbol = "🚀",
            requiredXpOrAction = "Complete 1 project task set"
        )
    )
}
