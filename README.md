# 🎓 Student Grade Tracker (Full-Stack Java)

A lightweight, robust full-stack Student Grade Tracker built with pure Java (`HttpServer`) on the backend and modern vanilla HTML/CSS/JavaScript on the frontend. Designed to run seamlessly in GitHub Codespaces without external heavy frameworks.

---

## ✨ Features

- **Unified Single-Port Architecture**: Java backend serves both static frontend assets and REST API endpoints on port `8080` (zero CORS issues).
- **Full CRUD Support**: Add, view, edit/update, and delete student records in real time.
- **Automated Grade & GPA Engine**:
  - **Weighted Breakdown**: Quiz (20%), Midterm (30%), Final Exam (50%).
  - **Auto-Calculated Metrics**: Total percentage, Letter Grade (A, B, C, F), GPA (up to 4.0), and Pass/Fail status.
- **Live Analytics Dashboard**: Dynamically computes Total Students, Class Average, and Highest Score.
- **Client-Side Live Filter**: Instant search filter by Student Name or Student ID.
- **Modern Dark UI**: Responsive, accessible interface styled with Tailwind-inspired design tokens.

---

## 🛠️ Tech Stack

- **Backend**: Java 17+ (`com.sun.net.httpserver.HttpServer`)
- **Frontend**: Vanilla HTML5, CSS3, JavaScript (ES6+ Fetch API)
- **Environment**: GitHub Codespaces / Linux

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone [https://github.com/Mubeenali-hub/StudentGradeTracker.git](https://github.com/Mubeenali-hub/StudentGradeTracker.git)
cd StudentGradeTracker

2. Compile and Run
javac backend/Main.java
java -cp . backend.Main

3. Open the App
http://localhost:8080
(In GitHub Codespaces, access port 8080 via the Ports tab and ensure port visibility is set to Public).

📡 API Reference
• GET    /api/students          -> Fetch all student records as JSON
• POST   /api/students          -> Create a new student record
• PUT    /api/students          -> Update an existing student record
• DELETE /api/students?id={id}  -> Delete a student by ID
