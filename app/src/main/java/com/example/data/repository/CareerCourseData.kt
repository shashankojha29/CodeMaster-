package com.example.data.repository

import com.example.data.model.CareerCourse
import com.example.data.model.CareerCourseModule
import com.example.data.model.PracticeQuestionItem
import com.example.data.model.QuizQuestion

object CareerCourseData {

    val careerCourses: List<CareerCourse> = listOf(
        // ------------------------------------------------------------------------
        // 1. SOFTWARE ENGINEER COURSE (Complete 18-Module Learning Path)
        // ------------------------------------------------------------------------
        CareerCourse(
            id = "software_engineer",
            title = "Software Engineer",
            category = "Core Engineering & Systems",
            iconEmoji = "👨‍💻",
            description = "Master complete software engineering from computer architecture and clean code to system design and real-world capstone projects.",
            modules = listOf(
                CareerCourseModule(
                    id = "se_mod_1",
                    careerId = "software_engineer",
                    orderNumber = 1,
                    title = "1. Computer & Software Basics",
                    tier = "Beginner",
                    description = "Learn how computers process information, memory organization, operating systems, and compilation vs execution.",
                    clearTheory = """
                        Computers operate using binary logic (0s and 1s). The Central Processing Unit (CPU) executes instructions stored in RAM (Random Access Memory).
                        Software is divided into System Software (operating systems like Linux, Windows, Android) and Application Software (browsers, mobile apps, IDEs).
                        When you write source code, a Compiler converts high-level code into Machine Code before execution, whereas an Interpreter executes source code line-by-line.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "// Binary & Byte representation\n1 Byte = 8 Bits\nValue 255 in binary = 11111111",
                        "// High level vs Machine level\nSource Code: print('Hello World') -> Machine Code: 01001000 01100101"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is the main difference between RAM and storage?", "RAM is volatile high-speed temporary memory, while storage (SSD/HDD) is non-volatile permanent memory."),
                        PracticeQuestionItem("What does a compiler do?", "A compiler translates high-level programming code into executable machine code for the target processor.")
                    ),
                    codingExercisePrompt = "Define a variable 'memory_mb' with value 1024 and print 'RAM is 1024 MB'.",
                    starterCode = "memory_mb = 1024\n# Write your code below:\n",
                    solutionKeyword = "print",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_1_1", "software_engineer", "Which computer component executes software instructions?", listOf("RAM", "CPU", "Hard Drive", "GPU"), 1, "The CPU is the central processor responsible for running instructions."),
                        QuizQuestion("se_q_1_2", "software_engineer", "How many bits are in one Byte?", listOf("4", "8", "16", "32"), 1, "There are 8 bits in a single byte.")
                    ),
                    miniProjectTitle = "Mini Project: System Spec Checker",
                    miniProjectDescription = "Build a simple script that evaluates RAM capacity and classifies system performance tier.",
                    miniProjectTasks = listOf("Check if RAM >= 16GB", "Display system tier badge", "Print diagnostic summary")
                ),

                CareerCourseModule(
                    id = "se_mod_2",
                    careerId = "software_engineer",
                    orderNumber = 2,
                    title = "2. Programming Fundamentals",
                    tier = "Beginner",
                    description = "Variables, data types, control structures, loops, and functional modularization.",
                    clearTheory = """
                        Programming fundamentals form the baseline of every software application.
                        1. Variables store data values in named memory locations.
                        2. Control structures (if/else, switch) branch execution logic.
                        3. Loops (for, while) repeat code blocks without redundancy.
                        4. Functions package reusable code to prevent repetition (DRY principle).
                    """.trimIndent(),
                    codeExamples = listOf(
                        "x = 10\ny = 20\nif x + y == 30:\n    print('Correct calculation')",
                        "def calculate_total(prices):\n    total = 0\n    for p in prices:\n        total += p\n    return total"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What does DRY stand for in software engineering?", "Don't Repeat Yourself - avoiding duplicate code by using functions and abstractions."),
                        PracticeQuestionItem("When should you use a for loop vs a while loop?", "Use a 'for' loop when the number of iterations is known in advance, and a 'while' loop when repeating until a dynamic condition changes.")
                    ),
                    codingExercisePrompt = "Write a loop that calculates the sum of numbers from 1 to 5.",
                    starterCode = "total = 0\nfor i in range(1, 6):\n    # add i to total\n    pass\nprint(total)",
                    solutionKeyword = "total",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_2_1", "software_engineer", "Which loop is best when iteration count is known beforehand?", listOf("while loop", "for loop", "infinite loop", "do-while"), 1, "For loops are specifically designed for bounded iterations.")
                    ),
                    miniProjectTitle = "Mini Project: Command Line Calculator",
                    miniProjectDescription = "Create a modular calculator function supporting arithmetic and validation.",
                    miniProjectTasks = listOf("Parse inputs", "Execute operations", "Return calculated result")
                ),

                CareerCourseModule(
                    id = "se_mod_3",
                    careerId = "software_engineer",
                    orderNumber = 3,
                    title = "3. Python Programming",
                    tier = "Beginner",
                    description = "Syntax, list comprehensions, dictionary operations, modules, and error handling in Python.",
                    clearTheory = """
                        Python is one of the most versatile languages in modern software engineering.
                        Key features include dynamic typing, clean indentation-based syntax, rich built-in data structures (lists, tuples, dicts, sets), and an extensive standard library.
                        Python is widely used in Web Backends, Data Engineering, Automation, and Artificial Intelligence.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "# List Comprehension\nsquares = [x**2 for x in range(10) if x % 2 == 0]\nprint(squares)",
                        "# Dictionary Operations\nstudent = {'name': 'Alice', 'score': 98}\nprint(student.get('score'))"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is list comprehension in Python?", "A concise, readable syntax for creating a new list by applying an expression to each item in an iterable."),
                        PracticeQuestionItem("How do try/except blocks handle errors?", "Code in the try block runs first; if an exception occurs, execution safely jumps to the except block instead of crashing.")
                    ),
                    codingExercisePrompt = "Write a list comprehension that creates a list of double values for [1, 2, 3].",
                    starterCode = "nums = [1, 2, 3]\ndoubles = [x * 2 for x in nums]\nprint(doubles)",
                    solutionKeyword = "doubles",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_3_1", "software_engineer", "Which block handles runtime exceptions in Python?", listOf("catch", "except", "error", "handle"), 1, "In Python, 'except' catches exceptions thrown in a 'try' block.")
                    ),
                    miniProjectTitle = "Mini Project: Student Grade Manager",
                    miniProjectDescription = "Build a Python script that calculates class averages and grade distributions.",
                    miniProjectTasks = listOf("Store student records in dicts", "Compute average scores", "Filter honor roll students")
                ),

                CareerCourseModule(
                    id = "se_mod_4",
                    careerId = "software_engineer",
                    orderNumber = 4,
                    title = "4. Object-Oriented Programming",
                    tier = "Intermediate",
                    description = "Encapsulation, Inheritance, Polymorphism, Abstraction, and Class Design.",
                    clearTheory = """
                        Object-Oriented Programming (OOP) models software around real-world objects containing data (attributes) and code (methods).
                        The Four Pillars of OOP:
                        1. Encapsulation: Bundling data and methods together while restricting direct internal state access.
                        2. Inheritance: Deriving new classes from existing classes to promote code reuse.
                        3. Polymorphism: Allowing different classes to respond to the same method call in specialized ways.
                        4. Abstraction: Hiding internal complex logic behind clean, high-level interfaces.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "class Employee:\n    def __init__(self, name, salary):\n        self.name = name\n        self._salary = salary\n\n    def get_role(self):\n        return 'Employee'",
                        "class Engineer(Employee):\n    def get_role(self):\n        return 'Software Engineer'"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is Encapsulation?", "Encapsulation hides sensitive data fields inside objects and forces interaction via public getter/setter methods."),
                        PracticeQuestionItem("What is method overriding?", "When a subclass provides its own specific implementation of a method defined in its superclass.")
                    ),
                    codingExercisePrompt = "Create a Car class with attribute 'brand' and a method 'drive()'.",
                    starterCode = "class Car:\n    def __init__(self, brand):\n        self.brand = brand\n    def drive(self):\n        return f'{self.brand} is driving'\n\nc = Car('Tesla')\nprint(c.drive())",
                    solutionKeyword = "class",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_4_1", "software_engineer", "Which OOP pillar derives new classes from base classes?", listOf("Polymorphism", "Encapsulation", "Inheritance", "Abstraction"), 2, "Inheritance allows child classes to inherit attributes and methods from parent classes.")
                    ),
                    miniProjectTitle = "Mini Project: Bank Account Manager",
                    miniProjectDescription = "Design a BankAccount class with deposit, withdraw, and audit logging methods.",
                    miniProjectTasks = listOf("Implement private balance field", "Enforce withdrawal overdraft rules", "Generate transaction history")
                ),

                CareerCourseModule(
                    id = "se_mod_5",
                    careerId = "software_engineer",
                    orderNumber = 5,
                    title = "5. Data Structures & Algorithms",
                    tier = "Intermediate",
                    description = "Arrays, Linked Lists, Stacks, Queues, Hash Tables, Trees, Sorting, and Big-O Notation.",
                    clearTheory = """
                        Data structures dictate how information is stored in memory, while algorithms are step-by-step procedures to solve computation problems.
                        - Big-O Notation measures efficiency: O(1) Constant, O(log N) Logarithmic, O(N) Linear, O(N log N) Linearithmic, O(N²) Quadratic.
                        - Linear Structures: Arrays (fast index lookup), Linked Lists (fast insertion), Stacks (LIFO), Queues (FIFO).
                        - Nonlinear Structures: Hash Tables (O(1) key-value lookup), Trees & Graphs (hierarchical and interconnected networks).
                    """.trimIndent(),
                    codeExamples = listOf(
                        "# Binary Search O(log N)\ndef binary_search(arr, target):\n    low, high = 0, len(arr) - 1\n    while low <= high:\n        mid = (low + high) // 2\n        if arr[mid] == target:\n            return mid\n        elif arr[mid] < target:\n            low = mid + 1\n        else:\n            high = mid - 1\n    return -1"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is the time complexity of searching a Hash Table?", "Average O(1) constant time complexity."),
                        PracticeQuestionItem("What is the difference between a Stack and a Queue?", "A Stack is Last-In First-Out (LIFO), whereas a Queue is First-In First-Out (FIFO).")
                    ),
                    codingExercisePrompt = "Implement a simple stack with push and pop methods using a list.",
                    starterCode = "stack = []\nstack.append('A')\nstack.append('B')\npopped = stack.pop()\nprint(f'Popped: {popped}')",
                    solutionKeyword = "pop",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_5_1", "software_engineer", "What is the average time complexity of Binary Search?", listOf("O(N)", "O(1)", "O(log N)", "O(N²)"), 2, "Binary Search cuts the search space in half each step, giving O(log N).")
                    ),
                    miniProjectTitle = "Mini Project: Fast Search Engine Indexer",
                    miniProjectDescription = "Build a keyword index using hash maps for instant term lookup.",
                    miniProjectTasks = listOf("Tokenize documents", "Map keywords to document IDs", "Perform O(1) query lookups")
                ),

                CareerCourseModule(
                    id = "se_mod_6",
                    careerId = "software_engineer",
                    orderNumber = 6,
                    title = "6. Git & GitHub",
                    tier = "Beginner",
                    description = "Version control, branching, commits, pull requests, merge conflict resolution, and CI/CD basics.",
                    clearTheory = """
                        Git is a distributed version control system that tracks changes in source code over time.
                        Key workflow steps:
                        1. `git init` / `git clone`: Initialize or clone a repository.
                        2. `git add .` & `git commit -m "msg"`: Stage and snapshot changes locally.
                        3. `git branch dev` & `git checkout dev`: Create and switch isolated feature branches.
                        4. `git pull` & `git push`: Sync local commits with remote repositories like GitHub.
                        5. Pull Requests (PRs) allow code review before merging into the production branch.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "# Git command sequence\ngit checkout -b feature/auth\ngit add .\ngit commit -m 'Add OAuth login logic'\ngit push origin feature/auth"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is a merge conflict?", "When two branches modify the same line of code in conflicting ways, requiring developer manual resolution."),
                        PracticeQuestionItem("Why should developers work on feature branches?", "Feature branches keep work isolated from production, preventing broken code from affecting main.")
                    ),
                    codingExercisePrompt = "Write the git command to create and switch to a new branch named 'feature/api'.",
                    starterCode = "# Type the git checkout command below:\n# git checkout -b feature/api",
                    solutionKeyword = "checkout",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_6_1", "software_engineer", "Which command stages changes for commit in Git?", listOf("git commit", "git add", "git stage", "git push"), 1, "git add moves changes from working directory to the staging area.")
                    ),
                    miniProjectTitle = "Mini Project: Collaborative Repo Setup",
                    miniProjectDescription = "Simulate branch management, commit messages, and PR review workflow.",
                    miniProjectTasks = listOf("Create feature branch", "Make atomic commit", "Simulate PR approval")
                ),

                CareerCourseModule(
                    id = "se_mod_7",
                    careerId = "software_engineer",
                    orderNumber = 7,
                    title = "7. HTML & CSS",
                    tier = "Beginner",
                    description = "Semantic markup, modern layout models (Flexbox & CSS Grid), media queries, and responsive web design.",
                    clearTheory = """
                        HTML provides structural markup, while CSS handles presentation and visual design.
                        - Semantic HTML elements (<header>, <main>, <article>, <nav>, <footer>) improve web accessibility and SEO.
                        - CSS Box Model consists of Content, Padding, Border, and Margin.
                        - Flexbox arranges items along a 1D axis, while CSS Grid handles complex 2D layouts effortlessly.
                        - Responsive web design uses media queries (@media) to adapt layouts smoothly to mobile, tablet, and desktop screens.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "/* Flexbox Center */\n.container {\n  display: flex;\n  justify-content: center;\n  align-items: center;\n}"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is the CSS Box Model?", "A box wrapping every HTML element composed of Content, Padding, Border, and Margin."),
                        PracticeQuestionItem("Why use semantic HTML?", "It conveys meaning to screen readers, search engines, and browsers, making code more accessible.")
                    ),
                    codingExercisePrompt = "Write a CSS declaration to set container display to flex.",
                    starterCode = ".card {\n  display: flex;\n  padding: 16px;\n}",
                    solutionKeyword = "flex",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_7_1", "software_engineer", "Which CSS property enables Flexbox layout?", listOf("position: flex", "display: flex", "layout: flex", "align: flex"), 1, "display: flex sets container to flexbox layout context.")
                    ),
                    miniProjectTitle = "Mini Project: Responsive Developer Portfolio",
                    miniProjectDescription = "Build a responsive web landing page showcasing developer skills and projects.",
                    miniProjectTasks = listOf("Structure semantic tags", "Style flexbox hero banner", "Apply dark mode palette")
                ),

                CareerCourseModule(
                    id = "se_mod_8",
                    careerId = "software_engineer",
                    orderNumber = 8,
                    title = "8. JavaScript",
                    tier = "Beginner",
                    description = "ES6+ features, DOM manipulation, asynchronous programming (Promises, async/await), and event handling.",
                    clearTheory = """
                        JavaScript is the dynamic runtime language powering interactive client-side web applications and server backends via Node.js.
                        Modern ES6+ features include const/let block scoping, arrow functions, template literals, destructuring, and modules.
                        Asynchronous operations (fetching network data, file I/O) are handled using Promises and async/await syntax, keeping the event loop unblocked.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "// Async/Await API Fetch\nasync function fetchUserData(userId) {\n  const res = await fetch(`/api/users/\${userId}`);\n  const data = await res.json();\n  return data;\n}"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is the Event Loop in JavaScript?", "The single-threaded execution mechanism that manages asynchronous callback execution queues without blocking."),
                        PracticeQuestionItem("What is the difference between let and const?", "Variables declared with 'let' can be reassigned, whereas 'const' creates read-only reference bindings.")
                    ),
                    codingExercisePrompt = "Write an async function that returns fetched user data.",
                    starterCode = "async function getData() {\n  return 'Success';\n}\ngetData().then(res => console.log(res));",
                    solutionKeyword = "async",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_8_1", "software_engineer", "Which keyword defines an asynchronous function in JS?", listOf("await", "async", "promise", "defer"), 1, "The 'async' keyword marks a function as returning a Promise.")
                    ),
                    miniProjectTitle = "Mini Project: Real-time Weather Widget",
                    miniProjectDescription = "Build a JavaScript module that fetches and renders live API weather data.",
                    miniProjectTasks = listOf("Fetch JSON endpoint", "Parse temperature data", "Render UI cards dynamically")
                ),

                CareerCourseModule(
                    id = "se_mod_9",
                    careerId = "software_engineer",
                    orderNumber = 9,
                    title = "9. Backend Development",
                    tier = "Intermediate",
                    description = "Server architecture, HTTP request lifecycle, routing, middleware, and backend runtime environments.",
                    clearTheory = """
                        Backend systems handle application logic, user authentication, database operations, and external system integration.
                        - HTTP Client-Server Architecture: Client sends HTTP Requests (GET, POST, PUT, DELETE); Server processes and returns HTTP Status Responses (200 OK, 400 Bad Request, 401 Unauthorized, 500 Error).
                        - Middleware functions intercept incoming requests for authentication, logging, CORS, and request body parsing before reaching controller handlers.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "// Node.js Express Server\nconst express = require('express');\nconst app = express();\n\napp.use(express.json());\napp.get('/api/health', (req, res) => res.json({ status: 'ok' }));"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is middleware in web backends?", "Functions executed sequentially during the request-response cycle to perform logging, auth, or input validation."),
                        PracticeQuestionItem("What does HTTP status code 404 mean?", "Not Found - the requested server route or resource does not exist.")
                    ),
                    codingExercisePrompt = "Define a GET route '/status' that returns status ok.",
                    starterCode = "# Server Route Handler\ndef get_status():\n    return {'status': 200, 'msg': 'OK'}\nprint(get_status())",
                    solutionKeyword = "status",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_9_1", "software_engineer", "Which HTTP status code indicates a successful server response?", listOf("200 OK", "404 Not Found", "500 Internal Error", "301 Redirect"), 0, "HTTP 200 OK signifies successful request execution.")
                    ),
                    miniProjectTitle = "Mini Project: Task Management Microservice",
                    miniProjectDescription = "Build a REST backend endpoint supporting task CRUD operations.",
                    miniProjectTasks = listOf("Setup HTTP router", "Create middleware logger", "Handle POST payload validation")
                ),

                CareerCourseModule(
                    id = "se_mod_10",
                    careerId = "software_engineer",
                    orderNumber = 10,
                    title = "10. APIs & Integration",
                    tier = "Intermediate",
                    description = "RESTful API design conventions, JSON formatting, OpenAPI specs, authentication (JWT/OAuth2), and rate limiting.",
                    clearTheory = """
                        Application Programming Interfaces (APIs) allow different software systems to communicate seamlessly.
                        - REST (Representational State Transfer) uses standard HTTP verbs (GET, POST, PUT, DELETE) operating on resource URIs.
                        - Authentication: Token-based authentication using JWT (JSON Web Tokens) or OAuth2 secures API endpoints against unauthorized access.
                        - API Best Practices: Versioning (/v1/), pagination, input validation, structured JSON error payloads, and rate limiting.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "// JWT Authorization Header\nAuthorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "// REST Endpoint Naming\nGET /api/v1/users/42/orders"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is the purpose of JWT token authentication?", "JWT allows stateless verification of user identity across requests without database session lookups on every request."),
                        PracticeQuestionItem("Which HTTP method should be used to update an existing resource?", "PUT or PATCH methods.")
                    ),
                    codingExercisePrompt = "Write a JSON payload structure containing 'user_id' and 'token'.",
                    starterCode = "payload = {'user_id': 101, 'token': 'abc_jwt_token'}\nprint(payload['token'])",
                    solutionKeyword = "token",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_10_1", "software_engineer", "Which header delivers bearer tokens for API authentication?", listOf("Content-Type", "Authorization", "Accept", "User-Agent"), 1, "Authorization: Bearer <token> delivers secure tokens.")
                    ),
                    miniProjectTitle = "Mini Project: Third-Party API Gateway",
                    miniProjectDescription = "Build an API wrapper that validates tokens and proxies request data.",
                    miniProjectTasks = listOf("Verify Bearer token", "Format JSON payload", "Return rate-limit headers")
                ),

                CareerCourseModule(
                    id = "se_mod_11",
                    careerId = "software_engineer",
                    orderNumber = 11,
                    title = "11. Databases & SQL",
                    tier = "Intermediate",
                    description = "Relational database design, Normalization (1NF, 2NF, 3NF), SQL Queries, JOINs, Indexing, and Transactions (ACID).",
                    clearTheory = """
                        Databases store application data persistently. Relational Database Management Systems (PostgreSQL, MySQL, SQLite) organize data in structured tables linked by foreign keys.
                        - ACID Guarantees: Atomicity (all-or-nothing), Consistency (schema valid), Isolation (concurrent safety), Durability (persisted to disk).
                        - SQL JOINs: INNER JOIN, LEFT JOIN, RIGHT JOIN combine data across related tables.
                        - Database Indexing: B-Tree indexes speed up SELECT query performance from O(N) full table scans to O(log N).
                    """.trimIndent(),
                    codeExamples = listOf(
                        "-- SQL Join Query\nSELECT u.username, o.total_amount \nFROM users u \nJOIN orders o ON u.id = o.user_id \nWHERE o.total_amount > 100;"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What does ACID stand for in databases?", "Atomicity, Consistency, Isolation, Durability."),
                        PracticeQuestionItem("How do database indexes improve query performance?", "Indexes create fast search trees (e.g. B-Trees) allowing the database engine to locate records without scanning all rows.")
                    ),
                    codingExercisePrompt = "Write a SQL query selecting all columns from 'employees' where dept = 'Engineering'.",
                    starterCode = "-- SQL SELECT query\nSELECT * FROM employees WHERE dept = 'Engineering';",
                    solutionKeyword = "SELECT",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_11_1", "software_engineer", "Which SQL clause merges rows from two or more tables?", listOf("GROUP BY", "JOIN", "UNION", "MERGE"), 1, "JOIN combines columns from multiple tables based on related keys.")
                    ),
                    miniProjectTitle = "Mini Project: E-Commerce DB Schema",
                    miniProjectDescription = "Design normalized tables for Users, Products, and Orders with foreign keys.",
                    miniProjectTasks = listOf("Create tables with constraints", "Add foreign key relations", "Write index on email field")
                ),

                CareerCourseModule(
                    id = "se_mod_12",
                    careerId = "software_engineer",
                    orderNumber = 12,
                    title = "12. Software Engineering Principles",
                    tier = "Intermediate",
                    description = "SOLID principles, Agile/Scrum methodologies, code reviews, technical debt, and architectural patterns.",
                    clearTheory = """
                        Software Engineering Principles guide teams in building scalable, maintainable, and robust systems.
                        SOLID Principles:
                        - Single Responsibility Principle (SRP): A class should have only one reason to change.
                        - Open/Closed Principle (OCP): Open for extension, closed for modification.
                        - Liskov Substitution Principle (LSP): Subtypes must be substitutable for base types.
                        - Interface Segregation Principle (ISP): Smaller, targeted interfaces over giant interfaces.
                        - Dependency Inversion Principle (DIP): Depend on abstractions, not concrete implementations.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "# Dependency Inversion Principle\nclass EmailNotifier:\n    def send(self, msg):\n        print(f'Sending: {msg}')\n\nclass UserService:\n    def __init__(self, notifier):\n        self.notifier = notifier"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is Technical Debt?", "The implied cost of additional rework caused by choosing an easy or quick solution now instead of using a better approach."),
                        PracticeQuestionItem("Explain Single Responsibility Principle.", "Each module or class should focus exclusively on one business function or responsibility.")
                    ),
                    codingExercisePrompt = "Refactor code to inject a Logger dependency into a Processor class.",
                    starterCode = "class Processor:\n    def __init__(self, logger):\n        self.logger = logger\n    def run(self):\n        self.logger('Processing complete')\nprint('Refactored')",
                    solutionKeyword = "logger",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_12_1", "software_engineer", "What does 'S' in SOLID principles stand for?", listOf("System Design", "Single Responsibility", "Software Scalability", "Service Oriented"), 1, "Single Responsibility Principle dictates one reason to change per class.")
                    ),
                    miniProjectTitle = "Mini Project: SOLID Refactoring Audit",
                    miniProjectDescription = "Audit a monolithic service class and decompose it into focused single-responsibility services.",
                    miniProjectTasks = listOf("Identify SRP violations", "Extract notification service", "Inject dependencies")
                ),

                CareerCourseModule(
                    id = "se_mod_13",
                    careerId = "software_engineer",
                    orderNumber = 13,
                    title = "13. Testing & Debugging",
                    tier = "Intermediate",
                    description = "Unit testing (JUnit/PyTest), Integration testing, Test-Driven Development (TDD), mocking, and debugging techniques.",
                    clearTheory = """
                        Software testing ensures application correctness, prevents regressions, and increases code deployment confidence.
                        - Unit Tests: Verify individual functions or methods in isolation.
                        - Integration Tests: Verify interaction between modules (e.g. database, network).
                        - Test-Driven Development (TDD): Red-Green-Refactor cycle (Write failing test first -> write code to pass test -> refactor code).
                        - Mocking: Replacing external dependencies (APIs, DBs) with controllable test double objects.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "# PyTest Unit Test\ndef add(a, b):\n    return a + b\n\ndef test_add():\n    assert add(2, 3) == 5\n    assert add(-1, 1) == 0"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is Test-Driven Development (TDD)?", "A software development approach where tests are written before writing the actual implementation code."),
                        PracticeQuestionItem("Why do engineers use Mocks in unit tests?", "Mocks isolate unit tests from external networks, databases, or slow dependencies.")
                    ),
                    codingExercisePrompt = "Write an assertion verifying that calculate_tax(100) equals 10.",
                    starterCode = "def calculate_tax(amount):\n    return amount * 0.10\n\nassert calculate_tax(100) == 10\nprint('Test Passed!')",
                    solutionKeyword = "assert",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_13_1", "software_engineer", "What cycle describes Test-Driven Development?", listOf("Plan-Build-Ship", "Red-Green-Refactor", "Code-Test-Deploy", "Draft-Review-Merge"), 1, "TDD follows Red (fail), Green (pass), and Refactor.")
                    ),
                    miniProjectTitle = "Mini Project: Automated Test Suite",
                    miniProjectDescription = "Build a suite of unit and integration tests with test doubles for a payment module.",
                    miniProjectTasks = listOf("Write boundary unit tests", "Mock payment gateway API", "Verify edge case handling")
                ),

                CareerCourseModule(
                    id = "se_mod_14",
                    careerId = "software_engineer",
                    orderNumber = 14,
                    title = "14. Clean Code & Design Patterns",
                    tier = "Advanced",
                    description = "Creational (Singleton, Factory), Structural (Adapter, Decorator), and Behavioral (Observer, Strategy) design patterns.",
                    clearTheory = """
                        Design patterns are standardized reusable solutions to common software design problems.
                        Categories:
                        1. Creational: Singleton (single instance), Factory Method (instantiation logic abstraction), Builder.
                        2. Structural: Adapter (interface translation), Decorator (dynamic behavior addition), Facade.
                        3. Behavioral: Observer (event pub/sub), Strategy (interchangeable algorithm family), State.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "# Singleton Pattern in Python\nclass DatabaseConnection:\n    _instance = None\n    def __new__(cls):\n        if cls._instance is None:\n            cls._instance = super().__new__(cls)\n        return cls._instance"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("When should you use the Factory pattern?", "When creation logic is complex or depends on runtime configurations, isolating creation from callers."),
                        PracticeQuestionItem("What problem does the Observer pattern solve?", "It establishes a 1-to-many subscription model so multiple objects automatically react to state changes in a subject.")
                    ),
                    codingExercisePrompt = "Implement a Singleton class pattern template.",
                    starterCode = "class Config:\n    _inst = None\n    def get_instance():\n        return 'Singleton'\nprint(Config.get_instance())",
                    solutionKeyword = "Singleton",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_14_1", "software_engineer", "Which design pattern ensures a class has only one instance globally?", listOf("Factory", "Observer", "Singleton", "Adapter"), 2, "Singleton guarantees a single shared instance throughout application lifecycle.")
                    ),
                    miniProjectTitle = "Mini Project: Notification Event Bus",
                    miniProjectDescription = "Implement an Observer-pattern Event Bus dispatching SMS, Email, and Push alerts.",
                    miniProjectTasks = listOf("Define Subject and Observer traits", "Register notification listeners", "Dispatch async events")
                ),

                CareerCourseModule(
                    id = "se_mod_15",
                    careerId = "software_engineer",
                    orderNumber = 15,
                    title = "15. System Design Basics",
                    tier = "Advanced",
                    description = "Scalability, Load Balancing, Horizontal Scaling, Caching (Memcached/Redis), Message Queues, and Sharding.",
                    clearTheory = """
                        System Design focuses on architecting large-scale distributed applications that handle millions of users reliably.
                        - Vertical Scaling (Scale Up) vs Horizontal Scaling (Scale Out with Load Balancers).
                        - Caching: Storing hot read data in memory (Redis/Memcached) to minimize database hit load.
                        - Asynchronous Decoupling: Using Message Queues (Kafka, RabbitMQ) for background processing.
                        - Database Partitioning: Sharding data across multiple database nodes to scale storage capacity.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "// Cache-Aside Pattern\nasync function getProduct(id) {\n  let data = await redis.get(`prod:\${id}`);\n  if (!data) {\n    data = await db.query('SELECT * FROM products WHERE id = ?', [id]);\n    await redis.set(`prod:\${id}`, JSON.stringify(data), 'EX', 3600);\n  }\n  return data;\n}"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is the difference between Vertical and Horizontal Scaling?", "Vertical scaling adds more CPU/RAM to a single server; Horizontal scaling adds more independent server instances behind a load balancer."),
                        PracticeQuestionItem("What is the CAP Theorem?", "In a distributed data store, you can only simultaneously guarantee at most 2 out of 3: Consistency, Availability, Partition Tolerance.")
                    ),
                    codingExercisePrompt = "Write pseudo code for a cache-aside pattern lookup.",
                    starterCode = "def get_user(id):\n    # Check cache first\n    cached = 'Cache Hit'\n    return cached\nprint(get_user(1))",
                    solutionKeyword = "Cache",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_15_1", "software_engineer", "Which component distributes incoming network traffic across multiple servers?", listOf("Database Shard", "Load Balancer", "Reverse Proxy", "Message Queue"), 1, "Load Balancers distribute load across server pools.")
                    ),
                    miniProjectTitle = "Mini Project: High-Traffic URL Shortener Design",
                    miniProjectDescription = "Design a scalable URL shortener system architecture with caching and hashing.",
                    miniProjectTasks = listOf("Estimate QPS and storage", "Define Base62 key generation", "Specify Redis cache layer")
                ),

                CareerCourseModule(
                    id = "se_mod_16",
                    careerId = "software_engineer",
                    orderNumber = 16,
                    title = "16. Advanced Software Engineering",
                    tier = "Advanced",
                    description = "Containerization (Docker), Orchestration (Kubernetes), Microservices, Observability (Logging, Metrics, Tracing), and Security.",
                    clearTheory = """
                        Advanced software engineering involves managing cloud-native infrastructure and complex distributed deployments.
                        - Docker containers package applications with all dependencies, guaranteeing identical execution from local dev to cloud production.
                        - Microservices decompose monoliths into independent loosely-coupled services communicating over lightweight REST or gRPC APIs.
                        - Observability: Logs (ELK stack), Metrics (Prometheus + Grafana), Tracing (Jaeger) monitor system health in real-time.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "# Sample Dockerfile\nFROM python:3.11-slim\nWORKDIR /app\nCOPY requirements.txt .\nRUN pip install -r requirements.txt\nCOPY . .\nCMD [\"python\", \"main.py\"]"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("Why containerize applications with Docker?", "Containers eliminate 'works on my machine' issues by bundling runtime, libraries, and OS config into immutable images."),
                        PracticeQuestionItem("What are the 3 pillars of Observability?", "Logs (event records), Metrics (numeric measurements over time), and Traces (end-to-end request path across services).")
                    ),
                    codingExercisePrompt = "Write a Dockerfile CMD instruction running python app.py.",
                    starterCode = "# Dockerfile instruction\nCMD [\"python\", \"app.py\"]",
                    solutionKeyword = "CMD",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_16_1", "software_engineer", "Which tool containerizes applications into reproducible environments?", listOf("Kubernetes", "Docker", "Jenkins", "Terraform"), 1, "Docker builds and runs lightweight application containers.")
                    ),
                    miniProjectTitle = "Mini Project: Containerized Microservice Setup",
                    miniProjectDescription = "Write a Dockerfile and docker-compose.yml file powering a Python API and Redis service.",
                    miniProjectTasks = listOf("Write multi-stage Dockerfile", "Configure docker-compose network", "Verify container health check")
                ),

                CareerCourseModule(
                    id = "se_mod_17",
                    careerId = "software_engineer",
                    orderNumber = 17,
                    title = "17. Real-World Projects",
                    tier = "Advanced",
                    description = "End-to-end full-stack software development, API design, security, persistence, and continuous integration.",
                    clearTheory = """
                        Real-world software development integrates all engineering disciplines: architecture planning, clean frontend UI, robust backend services, secure authentication, database design, automated testing, and CI/CD pipelines.
                        Building production-grade applications requires considering edge cases, security vulnerabilities (OWASP Top 10), performance monitoring, and fault tolerance.
                    """.trimIndent(),
                    codeExamples = listOf(
                        "# Full Stack Integration Example\nAPI -> Authentication Middleware -> Business Logic -> ORM Query -> Database"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is Continuous Integration (CI)?", "Automated process of building and running tests automatically every time code is committed to a repository."),
                        PracticeQuestionItem("How do you protect against SQL Injection?", "Use parameterized SQL queries or ORM abstractions instead of concatenating raw user input strings into queries.")
                    ),
                    codingExercisePrompt = "Write a parameterized SQL query pattern to prevent injection.",
                    starterCode = "# Safe parameterized query\nquery = 'SELECT * FROM users WHERE id = %s'\nprint('Parameterized Query Prepared')",
                    solutionKeyword = "SELECT",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_17_1", "software_engineer", "How do parameterized queries prevent SQL Injection?", listOf("By encrypting tables", "By separating executable code from user data input", "By blocking HTTP requests", "By disabling database joins"), 1, "Parameterized queries treat user input strictly as data, never as executable SQL code.")
                    ),
                    miniProjectTitle = "Mini Project: Full-Stack Issue Tracker",
                    miniProjectDescription = "Build an end-to-end task tracking system with authentication and database storage.",
                    miniProjectTasks = listOf("Design API spec", "Implement user login", "Persist tasks to database")
                ),

                CareerCourseModule(
                    id = "se_mod_18",
                    careerId = "software_engineer",
                    orderNumber = 18,
                    title = "18. Final Capstone Project",
                    tier = "Advanced",
                    description = "Complete Software Engineer Capstone: Architect, build, test, and deploy a enterprise-ready cloud platform.",
                    clearTheory = """
                        Congratulations on reaching the Final Capstone Project of the Software Engineer Course!
                        In this capstone, you apply all 18 modules:
                        1. Architecture & System Design Plan
                        2. Clean OOP & Data Structure Implementations
                        3. REST APIs with JWT Security
                        4. Database Schemas with SQL Indexes
                        5. Automated Test Suite (Unit & Integration)
                        6. Containerized Deployment setup with Git & CI/CD
                    """.trimIndent(),
                    codeExamples = listOf(
                        "// Capstone Production Release Architecture\nClient UI <-> Load Balancer <-> Auth API <-> Service Mesh <-> Database Cluster"
                    ),
                    practiceQuestions = listOf(
                        PracticeQuestionItem("What is the primary objective of a Capstone Project?", "Demonstrate mastery over the entire software engineering lifecycle from requirement analysis to production deployment."),
                        PracticeQuestionItem("What defines enterprise-ready software?", "Security, scalability, maintainability, test coverage, fault tolerance, and comprehensive documentation.")
                    ),
                    codingExercisePrompt = "Write the capstone completion statement: print('Capstone Completed Successfully!')",
                    starterCode = "print('Capstone Completed Successfully!')",
                    solutionKeyword = "Capstone",
                    quizQuestions = listOf(
                        QuizQuestion("se_q_18_1", "software_engineer", "Which milestone marks software readiness for end-user production?", listOf("Alpha Build", "Beta Test", "Production Release (GA)", "Proof of Concept"), 2, "General Availability (GA) / Production Release is the final milestone.")
                    ),
                    miniProjectTitle = "Final Capstone: Enterprise Cloud Platform",
                    miniProjectDescription = "Deliver a complete production platform with microservices, security, and automated tests.",
                    miniProjectTasks = listOf("Design system architecture diagram", "Implement backend REST APIs with JWT", "Deploy containerized services with test suite")
                )
            )
        ),

        // ------------------------------------------------------------------------
        // 2. WEB DEVELOPER COURSE
        // ------------------------------------------------------------------------
        CareerCourse(
            id = "web_developer",
            title = "Web Developer",
            category = "Web & Cloud Applications",
            iconEmoji = "🌐",
            description = "Build responsive, modern web platforms from frontend interfaces to backend servers and web performance tuning.",
            modules = listOf(
                CareerCourseModule(
                    id = "web_mod_1",
                    careerId = "web_developer",
                    orderNumber = 1,
                    title = "1. Web Foundations (HTML5 & Modern CSS)",
                    tier = "Beginner",
                    description = "Semantic tags, CSS Grid, Flexbox, media queries, and accessibility best practices.",
                    clearTheory = "Learn how browsers parse HTML documents into the DOM tree and apply CSS rendering cascade.",
                    codeExamples = listOf("div { display: grid; grid-template-columns: repeat(3, 1fr); }"),
                    practiceQuestions = listOf(PracticeQuestionItem("What is the DOM?", "Document Object Model representation of web elements.")),
                    codingExercisePrompt = "Set display to grid for .gallery class.",
                    starterCode = ".gallery {\n  display: grid;\n}",
                    solutionKeyword = "grid",
                    quizQuestions = listOf(QuizQuestion("web_q_1", "web_developer", "Which CSS display creates a 2D layout?", listOf("block", "inline", "grid", "flex"), 2, "Grid creates 2D row/column layouts.")),
                    miniProjectTitle = "Mini Project: Modern Landing Page",
                    miniProjectDescription = "Build a mobile-first responsive landing page with CSS grid.",
                    miniProjectTasks = listOf("Markup semantic structure", "Apply grid layout", "Test mobile viewport")
                ),
                CareerCourseModule(
                    id = "web_mod_2",
                    careerId = "web_developer",
                    orderNumber = 2,
                    title = "2. Modern JavaScript & DOM API",
                    tier = "Beginner",
                    description = "ES6+, Event handling, DOM selection, local storage, and async fetch requests.",
                    clearTheory = "JavaScript brings interactivity to static web pages by listening to events and updating DOM nodes dynamically.",
                    codeExamples = listOf("document.querySelector('#btn').addEventListener('click', () => alert('Clicked!'));"),
                    practiceQuestions = listOf(PracticeQuestionItem("What does addEventListener do?", "Attaches an event handler function to an HTML element.")),
                    codingExercisePrompt = "Write querySelector for id '#app'.",
                    starterCode = "const el = document.querySelector('#app');\nconsole.log(el);",
                    solutionKeyword = "querySelector",
                    quizQuestions = listOf(QuizQuestion("web_q_2", "web_developer", "Which method selects an element by ID?", listOf("getElementsByClassName", "querySelector", "getElementByTag", "find"), 1, "querySelector('#id') selects by ID.")),
                    miniProjectTitle = "Mini Project: Interactive Web Dashboard",
                    miniProjectDescription = "Build a dynamic web app with local storage state persistence.",
                    miniProjectTasks = listOf("Attach event listeners", "Save state to localStorage", "Update DOM elements")
                ),
                CareerCourseModule(
                    id = "web_mod_3",
                    careerId = "web_developer",
                    orderNumber = 3,
                    title = "3. Frontend Frameworks (React & Next.js)",
                    tier = "Intermediate",
                    description = "Components, state hooks, virtual DOM, SSR vs CSR, and Tailwind CSS styling.",
                    clearTheory = "React organizes UI into reusable functional components driven by state hooks like useState and useEffect.",
                    codeExamples = listOf("const [count, setCount] = useState(0);"),
                    practiceQuestions = listOf(PracticeQuestionItem("What is the Virtual DOM?", "A lightweight in-memory representation of the real DOM used to minimize expensive re-renders.")),
                    codingExercisePrompt = "Initialize count state using useState(0).",
                    starterCode = "# React useState\ncount, set_count = 0, None\nprint('State initialized')",
                    solutionKeyword = "State",
                    quizQuestions = listOf(QuizQuestion("web_q_3", "web_developer", "Which hook manages state in React?", listOf("useEffect", "useState", "useContext", "useRef"), 1, "useState holds component reactive state.")),
                    miniProjectTitle = "Mini Project: Full Frontend Web App",
                    miniProjectDescription = "Build a multi-page web application with component routing and state.",
                    miniProjectTasks = listOf("Build navbar & cards", "Implement search filter", "Deploy to Vercel/Netlify")
                )
            )
        ),

        // ------------------------------------------------------------------------
        // 3. APP DEVELOPER COURSE
        // ------------------------------------------------------------------------
        CareerCourse(
            id = "app_developer",
            title = "App Developer",
            category = "Mobile Engineering",
            iconEmoji = "📱",
            description = "Build high-performance native Android & iOS mobile applications using Kotlin, Jetpack Compose, and modern mobile architecture.",
            modules = listOf(
                CareerCourseModule(
                    id = "app_mod_1",
                    careerId = "app_developer",
                    orderNumber = 1,
                    title = "1. Mobile Architecture & Kotlin",
                    tier = "Beginner",
                    description = "Kotlin programming language, null safety, coroutines, and Android Activity lifecycles.",
                    clearTheory = "Kotlin is Android's official modern language featuring concise syntax, null safety (val name: String?), and coroutines for async UI operations.",
                    codeExamples = listOf("val name: String? = null\nval len = name?.length ?: 0"),
                    practiceQuestions = listOf(PracticeQuestionItem("What is the elvis operator in Kotlin?", "The '?:' operator provides a fallback default value if an expression evaluates to null.")),
                    codingExercisePrompt = "Write a Kotlin safe call on name?.length.",
                    starterCode = "val name: String? = 'Android'\nval len = name?.length\nprintln(len)",
                    solutionKeyword = "length",
                    quizQuestions = listOf(QuizQuestion("app_q_1", "app_developer", "Which operator provides a null-safe fallback in Kotlin?", listOf("??", "?:", "||", "!?"), 1, "The Elvis operator ?: handles null fallbacks.")),
                    miniProjectTitle = "Mini Project: Mobile Calculator App",
                    miniProjectDescription = "Build a responsive mobile app screen with Compose state.",
                    miniProjectTasks = listOf("Design Compose column layout", "Bind state variables", "Handle button clicks")
                ),
                CareerCourseModule(
                    id = "app_mod_2",
                    careerId = "app_developer",
                    orderNumber = 2,
                    title = "2. Jetpack Compose & M3 UI",
                    tier = "Intermediate",
                    description = "Declarative UI, Scaffold, LazyColumn, Material 3 design, custom themes, and state hoisting.",
                    clearTheory = "Jetpack Compose replaces old XML layouts with a modern declarative Kotlin UI engine.",
                    codeExamples = listOf("@Composable\nfun Greeting(name: String) {\n    Text(text = \"Hello \$name!\")\n}"),
                    practiceQuestions = listOf(PracticeQuestionItem("What is State Hoisting?", "Moving state to a common composable parent to make child composables stateless and reusable.")),
                    codingExercisePrompt = "Define a Composable function UserCard.",
                    starterCode = "// Composable function\nfun UserCard() {\n    print('Composable UI')\n}",
                    solutionKeyword = "UserCard",
                    quizQuestions = listOf(QuizQuestion("app_q_2", "app_developer", "Which composable lists large dynamic scrollable items efficiently?", listOf("Column", "LazyColumn", "ScrollView", "ListView"), 1, "LazyColumn lazily renders visible items for high performance.")),
                    miniProjectTitle = "Mini Project: Mobile Habit Tracker App",
                    miniProjectDescription = "Build a complete M3 Compose app with streak cards and bottom bar navigation.",
                    miniProjectTasks = listOf("Setup Scaffold & BottomBar", "Create LazyColumn habit items", "Add streak progress bar")
                )
            )
        ),

        // ------------------------------------------------------------------------
        // 4. DATA SCIENTIST COURSE
        // ------------------------------------------------------------------------
        CareerCourse(
            id = "data_scientist",
            title = "Data Scientist",
            category = "Data Analytics & Intelligence",
            iconEmoji = "📊",
            description = "Extract actionable business intelligence using statistical modeling, SQL, Python Pandas, and machine learning visualization.",
            modules = listOf(
                CareerCourseModule(
                    id = "ds_mod_1",
                    careerId = "data_scientist",
                    orderNumber = 1,
                    title = "1. Data Wrangling (Pandas & SQL)",
                    tier = "Beginner",
                    description = "Data cleaning, aggregation, SQL window functions, missing value imputation, and DataFrames.",
                    clearTheory = "Data Wrangling transforms raw messy data into clean structured tables ready for statistical modeling and visualization.",
                    codeExamples = listOf("import pandas as pd\ndf = pd.read_csv('sales.csv')\ndf_clean = df.dropna()"),
                    practiceQuestions = listOf(PracticeQuestionItem("What is a DataFrame?", "A 2D labeled data structure with columns of potentially different types, similar to a spreadsheet or SQL table.")),
                    codingExercisePrompt = "Call dropna() on dataframe df.",
                    starterCode = "import pandas as pd\ndf = pd.DataFrame({'a': [1, None, 3]})\ndf_clean = df.dropna()\nprint(df_clean)",
                    solutionKeyword = "dropna",
                    quizQuestions = listOf(QuizQuestion("ds_q_1", "data_scientist", "Which Python library provides DataFrames for data analysis?", listOf("NumPy", "Pandas", "Requests", "Flask"), 1, "Pandas is the primary library for tabular data manipulation.")),
                    miniProjectTitle = "Mini Project: Customer Churn Data Cleaner",
                    miniProjectDescription = "Clean messy raw customer records and export a cleaned dataset for modeling.",
                    miniProjectTasks = listOf("Fill missing numerical values", "Encode categorical variables", "Export cleaned CSV")
                )
            )
        ),

        // ------------------------------------------------------------------------
        // 5. AI / ML ENGINEER COURSE
        // ------------------------------------------------------------------------
        CareerCourse(
            id = "ai_ml_engineer",
            title = "AI / ML Engineer",
            category = "Artificial Intelligence",
            iconEmoji = "🤖",
            description = "Train neural networks, build intelligent prediction models, fine-tune LLMs, and integrate Gemini generative AI APIs.",
            modules = listOf(
                CareerCourseModule(
                    id = "ai_mod_1",
                    careerId = "ai_ml_engineer",
                    orderNumber = 1,
                    title = "1. Machine Learning Foundations",
                    tier = "Beginner",
                    description = "Supervised vs unsupervised learning, regression, classification, decision trees, and model evaluation.",
                    clearTheory = "Machine learning algorithms learn patterns directly from dataset examples rather than relying on hardcoded rule logic.",
                    codeExamples = listOf("from sklearn.linear_model import LogisticRegression\nmodel = LogisticRegression()\nmodel.fit(X_train, y_train)"),
                    practiceQuestions = listOf(PracticeQuestionItem("What is Supervised Learning?", "Training models using labeled datasets where target outputs are known during training.")),
                    codingExercisePrompt = "Fit a Scikit-Learn model on X_train and y_train.",
                    starterCode = "# Model training\nmodel.fit(X_train, y_train)\nprint('Model Trained')",
                    solutionKeyword = "fit",
                    quizQuestions = listOf(QuizQuestion("ai_q_1", "ai_ml_engineer", "Which learning type uses labeled target datasets?", listOf("Unsupervised", "Supervised", "Reinforcement", "Clustering"), 1, "Supervised learning relies on labeled target outputs.")),
                    miniProjectTitle = "Mini Project: Predictive Classifier Model",
                    miniProjectDescription = "Build and evaluate a machine learning model predicting user conversion.",
                    miniProjectTasks = listOf("Split train/test data", "Train classification model", "Evaluate accuracy score")
                ),
                CareerCourseModule(
                    id = "ai_mod_2",
                    careerId = "ai_ml_engineer",
                    orderNumber = 2,
                    title = "2. Deep Learning & GenAI Integrations",
                    tier = "Advanced",
                    description = "Neural networks, Transformer architecture, prompt engineering, and Gemini REST API integration.",
                    clearTheory = "Transformers utilize self-attention mechanisms to process text and multimodal data, powering Generative AI applications.",
                    codeExamples = listOf("const model = googleAI.getGenerativeModel({ model: 'gemini-2.5-flash' });"),
                    practiceQuestions = listOf(PracticeQuestionItem("What is a Transformer model?", "A deep learning architecture relying on self-attention to process sequence data in parallel.")),
                    codingExercisePrompt = "Specify model name 'gemini-2.5-flash'.",
                    starterCode = "model_name = 'gemini-2.5-flash'\nprint(f'Using {model_name}')",
                    solutionKeyword = "gemini",
                    quizQuestions = listOf(QuizQuestion("ai_q_2", "ai_ml_engineer", "Which architecture powers modern Large Language Models?", listOf("CNN", "RNN", "Transformer", "SVM"), 2, "Transformers are the core architecture of LLMs.")),
                    miniProjectTitle = "Mini Project: AI Tutor Chat Assistant",
                    miniProjectDescription = "Integrate Generative AI endpoints into an interactive chat app interface.",
                    miniProjectTasks = listOf("Format prompt templates", "Send API request", "Render streaming response")
                )
            )
        ),

        // ------------------------------------------------------------------------
        // 6. GAME DEVELOPER COURSE
        // ------------------------------------------------------------------------
        CareerCourse(
            id = "game_developer",
            title = "Game Developer",
            category = "Interactive Media & Games",
            iconEmoji = "🎮",
            description = "Program 2D/3D game physics, gameplay loops, collision detection, graphics rendering, and interactive mechanics.",
            modules = listOf(
                CareerCourseModule(
                    id = "gm_mod_1",
                    careerId = "game_developer",
                    orderNumber = 1,
                    title = "1. Game Loop & 2D Physics Engine",
                    tier = "Beginner",
                    description = "Game loops (Update/Render), frame rates, vector math, collision detection, and sprite animation.",
                    clearTheory = "A Game Loop runs continuously (typically 60 FPS), performing Input Processing, State Updating, and Frame Rendering every cycle.",
                    codeExamples = listOf("while (isRunning) {\n  processInput();\n  update(deltaTime);\n  render();\n}"),
                    practiceQuestions = listOf(PracticeQuestionItem("What is Delta Time?", "The time elapsed between current and previous frame, ensuring smooth movement independent of framerate.")),
                    codingExercisePrompt = "Write a update loop updating position with velocity * deltaTime.",
                    starterCode = "pos_x += vel_x * delta_time\nprint(f'New position: {pos_x}')",
                    solutionKeyword = "delta_time",
                    quizQuestions = listOf(QuizQuestion("gm_q_1", "game_developer", "What are the core steps of a game loop?", listOf("Compile-Run-Exit", "Input-Update-Render", "Load-Save-Quit", "Pause-Resume-Stop"), 1, "Input processing, state update, and frame rendering form the loop.")),
                    miniProjectTitle = "Mini Project: 2D Arcade Platformer",
                    miniProjectDescription = "Program jump physics, gravity acceleration, and collision bounds.",
                    miniProjectTasks = listOf("Apply gravity acceleration", "Detect floor collisions", "Render animated player sprite")
                )
            )
        ),

        // ------------------------------------------------------------------------
        // 7. CYBERSECURITY DEVELOPER COURSE
        // ------------------------------------------------------------------------
        CareerCourse(
            id = "cybersecurity_eng",
            title = "Cybersecurity",
            category = "Security Engineering",
            iconEmoji = "🛡️",
            description = "Protect applications, audit software vulnerabilities, build cryptographic engines, and implement OWASP security controls.",
            modules = listOf(
                CareerCourseModule(
                    id = "sec_mod_1",
                    careerId = "cybersecurity_eng",
                    orderNumber = 1,
                    title = "1. Application Security & Cryptography",
                    tier = "Beginner",
                    description = "OWASP Top 10 vulnerabilities, hashing algorithms (SHA-256, bcrypt), symmetric/asymmetric encryption, and secure tokens.",
                    clearTheory = "Application Security prevents unauthorized access by sanitizing inputs, hashing passwords with salt, and encrypting data in transit.",
                    codeExamples = listOf("import bcrypt\nhashed = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())"),
                    practiceQuestions = listOf(PracticeQuestionItem("Why should passwords be salted before hashing?", "Salting adds random data to each password before hashing to defend against rainbow table attacks.")),
                    codingExercisePrompt = "Write bcrypt gensalt() salt generation call.",
                    starterCode = "# Password hashing\nsalt = bcrypt.gensalt()\nprint('Salt generated')",
                    solutionKeyword = "gensalt",
                    quizQuestions = listOf(QuizQuestion("sec_q_1", "cybersecurity_eng", "Which algorithm is recommended for password hashing?", listOf("MD5", "SHA-1", "bcrypt", "DES"), 2, "bcrypt incorporates work factor salt to slow down brute force attacks.")),
                    miniProjectTitle = "Mini Project: Security Audit & Vulnerability Scanner",
                    miniProjectDescription = "Build a Python script that audits web endpoints for missing security headers and SQLi risks.",
                    miniProjectTasks = listOf("Check CORS headers", "Verify HTTPS TLS cipher", "Generate security report")
                )
            )
        )
    )

    fun getCourseById(courseId: String): CareerCourse? {
        return careerCourses.find { it.id == courseId || it.id.contains(courseId, ignoreCase = true) }
            ?: careerCourses.firstOrNull()
    }
}
