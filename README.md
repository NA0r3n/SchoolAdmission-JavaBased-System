<div align="center">

# University of Makati
## Admission Management System

**A console-based Java application that verifies documents, evaluates applicants, and manages course slots, end to end.**

<br>

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Type](https://img.shields.io/badge/App-Console%20%2F%20CLI-0b0e14?style=for-the-badge&logo=gnubash&logoColor=white)
![Storage](https://img.shields.io/badge/Storage-CSV-2ea44f?style=for-the-badge&logo=files&logoColor=white)
![Dependencies](https://img.shields.io/badge/Dependencies-None-0366d6?style=for-the-badge)
![Colleges](https://img.shields.io/badge/Colleges-11-8957e5?style=for-the-badge)
![Programs](https://img.shields.io/badge/Programs-31-d73a49?style=for-the-badge)
![Version](https://img.shields.io/badge/Version-1.5-6e7681?style=for-the-badge)

<br>

[Features](#features) ·
[How it decides](#how-admission-decisions-are-made) ·
[Quick start](#quick-start) ·
[Architecture](#architecture) ·
[Screenshots](#screenshots) ·

<br>

<img src="screenshots/main_menu.png" alt="Main menu" width="528">

</div>

---

## At a Glance

| Colleges | Programs | Total Seats | Source Files | Menu Modules | Documents Checked |
|:---:|:---:|:---:|:---:|:---:|:---:|
| **11** | **31** | **2,360** | **7** | **8** | **7** (3 mandatory) |

---

## About

Manual admission processing tends to suffer from slow document verification, human error when checking minimum grades, and poor tracking of available slots. The **UMak Admission Management System** tackles all three:

1. **Verifies** mandatory and optional admission requirements *before* an application can proceed.
2. **Evaluates** each applicant automatically against General Weighted Average (GWA) thresholds and real-time course capacity.
3. **Issues** an official Student ID (`UMAK-YYYY-XXXX`) *only* when an applicant is approved.
4. **Persists** every record to CSV (`students.csv`) so nothing is lost between runs.

## Objectives

| Objective | What it means |
|---|---|
| **Enforce document verification** | A two-step workflow separates mandatory `[REQ]` documents from to-follow `[OPT]` documents. |
| **Automate admission evaluation** | Applicants are classified as `APPROVED`, `PENDING`, `WAITLISTED`, or `REJECTED` from GWA, documents, and slots. |
| **Centralize student records** | Register, view, search (ID or name), update, and delete, all backed by `students.csv`. |
| **Manage real-time capacity** | Total and remaining slots tracked across 11 colleges and 31 programs. |
| **Generate actionable reports** | Enrollment stats, acceptance rate, and per-course breakdowns, exportable to `Admission_Report.csv`. |

---

## Features

| # | Module | Highlights |
|:-:|---|---|
| **1** | **Register New Student** | 2-step flow: requirements checker, then profile, course and **application preview** with the predicted outcome before you confirm. |
| **2** | **View All Students** | Directory with Student ID, name, year level, course, GWA, status, admission date, and record count. |
| **3** | **Search Student** | Exact lookup by **Student ID** (full profile with address and contact) or partial, case-insensitive search by **name**. |
| **4** | **Update Student Status** | Find by ID or name (numbered pick-list for multiple matches). Approving assigns an ID and takes a slot; leaving `APPROVED` frees it. |
| **5** | **Delete Student Record** | Find by ID or name, confirm, and remove. An approved student's slot is released automatically. |
| **6** | **Course Summary** | Slots left (or `FULL`) and minimum GWA for every program, grouped by college. |
| **7** | **Admission Reports** | Totals, acceptance rate, status counts, per-course breakdown, with optional `Admission_Report.csv` export. |
| **8** | **Auto-persistence** | Loads `students.csv` on startup, re-syncs the ID counter, seeds sample applicants when no file exists, and saves on every change. |

<details>
<summary><b>Registration details (click to expand)</b></summary>

<br>

**Step 1: Requirements Checker.** Validates the applicant's name (letters, spaces, apostrophes and hyphens only), then runs a batch checklist. Enter the *missing* item numbers (e.g. `2 4`) or just press Enter if everything is complete.

**Step 2: Profile, Course & Preview.** Collects age, address, Philippine mobile number (`09XXXXXXXXX`), previous school, year level, college, course, and GWA, then shows an **Application Preview** with the predicted result before you submit.

</details>

---

## How Admission Decisions Are Made

Every application is run through the same rules, in this order. The **first rule that matches wins**.

```mermaid
flowchart TD
    A([Application submitted]) --> B{"Course exists?"}
    B -- No --> P1[/"PENDING<br/>unknown course"/]
    B -- Yes --> C{"GWA below 75.0?"}
    C -- Yes --> R[/"REJECTED"/]
    C -- No --> D{"GWA below course minimum?"}
    D -- Yes --> P2[/"PENDING<br/>below course minimum"/]
    D -- No --> E{"Slots remaining?"}
    E -- None --> W[/"WAITLISTED"/]
    E -- Available --> F{"All documents submitted?"}
    F -- No --> P3[/"PENDING<br/>to-follow documents"/]
    F -- Yes --> OK[/"APPROVED<br/>UMAK-YYYY-XXXX issued"/]

    style OK fill:#2ea44f,color:#fff,stroke:#2ea44f
    style R fill:#d73a49,color:#fff,stroke:#d73a49
    style W fill:#d29922,color:#fff,stroke:#d29922
    style P1 fill:#6e7681,color:#fff,stroke:#6e7681
    style P2 fill:#6e7681,color:#fff,stroke:#6e7681
    style P3 fill:#6e7681,color:#fff,stroke:#6e7681
```

### Document Checklist

| # | Document | Type | If missing |
|:-:|---|:-:|---|
| 1 | Notice of Admission | `[REQ]` | Registration **blocked** |
| 2 | Medical Certificate | `[OPT]` | To-follow, status `PENDING` |
| 3 | Form 138 / Report Card | `[REQ]` | Registration **blocked** |
| 4 | Form 137 | `[OPT]` | To-follow, status `PENDING` |
| 5 | Good Moral Certificate | `[OPT]` | To-follow, status `PENDING` |
| 6 | PSA Birth Certificate | `[REQ]` | Registration **blocked** |
| 7 | 2x2 ID Picture | `[OPT]` | To-follow, status `PENDING` |

### Student ID Format

```text
UMAK-2026-0001
  |     |    +-- 4-digit sequence (zero-padded, resumes from the highest ID in students.csv)
  |     +------- admission year (taken from the system date)
  +------------- university prefix
```

IDs are **deferred**: an applicant has no ID until they are `APPROVED`.

---

## Quick Start

**Requirements:** JDK 17 or newer. No external libraries.

```bash
# 1. Clone
git clone https://github.com/NA0r3n/SchoolAdmission-JavaBased-System.git
cd SchoolAdmission-JavaBased-System

# 2. Compile
javac *.java

# 3. Run
java Main
```

> **Tip:** The UI uses ANSI escape codes to clear the screen. Use a real terminal (Windows Terminal, PowerShell, macOS Terminal, any Linux shell). Some IDE consoles print the codes as raw text.

On first launch with no `students.csv`, the system seeds sample applicants so every module has something to show.

---

## Colleges & Programs

| College | Full name | Programs | Seats | Min. GWA range |
|:-:|---|:-:|:-:|:-:|
| **CCIS** | College of Computing and Information Sciences | 2 | 120 | 80 – 83 |
| **CBFS** | College of Business and Financial Science | 5 | 430 | 78 – 85 |
| **CTHM** | College of Tourism and Hospitality Management | 2 | 300 | 78 |
| **CCSE** | College of Construction Sciences and Engineering | 3 | 200 | 75 – 83 |
| **CET** | College of Engineering Technology | 3 | 170 | 75 – 80 |
| **CITE** | College of Innovative Teacher Education | 3 | 280 | 78 – 80 |
| **CONHS** | College of Nursing and Health Sciences | 4 | 270 | 82 – 85 |
| **CGPP** | College of Governance and Public Policy | 3 | 260 | 78 – 80 |
| **CLAS** | College of Liberal Arts and Sciences | 2 | 100 | 82 |
| **CHK** | College of Human Kinetics | 1 | 60 | 78 |
| **IAD** | Institute of Arts and Design | 3 | 170 | 78 |
| | **Total** | **31** | **2,360** | |

> The absolute floor is a GWA of **75.0**. Anything lower is rejected regardless of course.

---

## Data Files

| File | Purpose |
|---|---|
| `students.csv` | Persistent student database, auto-loaded on startup and saved after every change. |
| `Admission_Report.csv` | Optional analytics export generated from the Reports module (press **E**). |

<details>
<summary><b>students.csv columns</b></summary>

```text
studentID, fullName, age, address, contact, prevSchool, courseApplied, gwa, yearLevel, status, dateAdmitted
```

`yearLevel` is one of `FIRST_YEAR` … `FOURTH_YEAR`; `status` is one of `APPROVED`, `PENDING`, `REJECTED`, `WAITLISTED`.

</details>

---

## Java Concepts Used

| Concept | Where & how |
|---|---|
| **Encapsulation** | Private fields with public getters/setters in `Student`, `CourseRequirement`, `AdmissionService`. |
| **Method overloading** | `updateStudentStatus` and `registerNewStudent` each have ID-based/object-based variants in `AdmissionService`. |
| **Separation of concerns** | UI (`Main`), business logic + persistence (`AdmissionService`), models (`Student`, `CourseRequirement`), constants (`College`, `YearLevel`). |
| **Enums** | `College` and `YearLevel` carry custom fields (`fullName`, `displayName`) for type-safe categories. |
| **Collections** | `ArrayList` for students, colleges and search results; `HashMap` for O(1) course rules, capacities, and taken-slot counters. |
| **File I/O** | `BufferedReader`/`FileReader` and `PrintWriter`/`FileWriter` in `try-with-resources` for `students.csv` and `Admission_Report.csv`. |
| **Regex & strings** | Name (`^[a-zA-Z\s'-]+$`) and contact (`^09\d{9}$`) validation, quote-aware CSV splitting, `String.format` zero-padding, `StringBuilder`. |
| **Exception handling** | `NumberFormatException` for bad numeric input, `IOException` for file operations. |
| **Java Time API** | `LocalDate` for admission dates and the year in generated Student IDs. |

---

## Architecture

```mermaid
flowchart LR
    subgraph UI ["Presentation"]
        MAIN["Main.java<br/>menus · 2-step registration · prompts"]
        CHK["Checker.java<br/>strict y/n confirmations"]
    end
    subgraph LOGIC ["Business Logic"]
        SVC["AdmissionService.java<br/>evaluation · ID generation · CSV I/O"]
    end
    subgraph MODEL ["Models"]
        STU["Student"]
        REQ["CourseRequirement"]
        COL["College (enum)"]
        YR["YearLevel (enum)"]
    end
    subgraph DATA ["Storage"]
        DB[("students.csv")]
        REP[("Admission_Report.csv")]
    end

    MAIN --> SVC
    MAIN -.-> CHK
    SVC --> STU & REQ
    REQ --> COL
    STU --> YR
    SVC <--> DB
    SVC --> REP
```

```text
SchoolAdmission-JavaBased-System/
├── Main.java                  # Entry point, CLI menus, 2-step registration & validation
├── AdmissionService.java      # GWA/slot evaluation, deferred ID generation, CSV I/O, reports
├── Student.java               # Applicant model (nullable ID, status, profile)
├── CourseRequirement.java     # Course <-> college, minimum GWA, max slots
├── College.java               # Enum: the 11 UMak colleges
├── YearLevel.java             # Enum: 1st to 4th year
├── Checker.java               # Strict y/n confirmation + boolean input helper
├── students.csv               # Student database
├── Admission_Report.csv       # Exported report
├── System Flowcharts and Pseudocode.md
└── screenshots/               # Terminal captures used below
```

More diagrams and per-file pseudocode live in [`System Flowcharts and Pseudocode.md`](System%20Flowcharts%20and%20Pseudocode.md).

---

## Screenshots

> All captures are real output from the program running against the bundled `students.csv`.

### Main Menu
<p align="center"><img src="screenshots/main_menu.png" alt="Main menu" width="528"></p>

### Register New Student

<details open>
<summary><b>Step 1: Requirements Checker</b></summary>

<br>

<p align="center"><img src="screenshots/requirements_checker_checklist.png" alt="Requirements checklist" width="528"></p>

<p align="center"><img src="screenshots/requirements_checker_result.png" alt="Requirements complete" width="528"></p>

**Other outcomes:** missing optional documents give a *conditional pass*; missing mandatory documents *block* registration.

<p align="center">
  <img src="screenshots/requirements_result_conditional.png" alt="Requirements Result Conditional" width="528">
  <img src="screenshots/requirements_result_blocked.png" alt="Requirements Result Blocked" width="528">
</p>

</details>

<details open>
<summary><b>Step 2: Profile, Course & Application Preview</b></summary>

<br>

<p align="center"><img src="screenshots/register_student_step2.png" alt="Register student step 2" width="646"></p>

<p align="center"><img src="screenshots/application_preview.png" alt="Application preview" width="596"></p>

<p align="center"><img src="screenshots/application_preview_submit.png" alt="Application preview submit" width="596"></p>

On `y` the application is saved to `students.csv` and the outcome is shown. The ID appears only if the result is `APPROVED`.

<p align="center"><img src="screenshots/application_submitted.png" alt="Application submitted" width="596"></p>

</details>

### View All Students
<p align="center"><img src="screenshots/view_students_part1.png" alt="View Students Part1" width="705"></p>
<p align="center"><img src="screenshots/view_students_part2.png" alt="View Students Part2" width="705"></p>
<p align="center"><img src="screenshots/view_students_part3.png" alt="View Students Part3" width="705"></p>

### Search Student

<details open>
<summary><b>By Student ID</b></summary>

<br>

<p align="center"><img src="screenshots/search_student_id.png" alt="Search by ID" width="697"></p>

</details>

<details open>
<summary><b>By name (partial, case-insensitive)</b></summary>

<br>

<p align="center"><img src="screenshots/search_student_name.png" alt="Search by name" width="528"></p>

</details>

### Update Student Status

<p align="center"><img src="screenshots/update_student_select.png" alt="Select a student" width="596"></p>

<p align="center"><img src="screenshots/update_student.png" alt="Status updated" width="705"></p>

Approving a student issues their ID on the spot. **Capacity guard:** a full course refuses a promotion to `APPROVED`.

<p align="center"><img src="screenshots/update_student_blocked.png" alt="Approval blocked, course full" width="621"></p>

### Delete Student Record

<p align="center"><img src="screenshots/delete_student_select.png" alt="Select a record" width="528"></p>

<p align="center"><img src="screenshots/delete_student.png" alt="Record deleted" width="528"></p>

### Course Summary
<p align="center"><img src="screenshots/course_summary_part1.png" alt="Course Summary Part1" width="630"></p>
<p align="center"><img src="screenshots/course_summary_part2.png" alt="Course Summary Part2" width="630"></p>
<p align="center"><img src="screenshots/course_summary_part3.png" alt="Course Summary Part3" width="630"></p>

### Admission Reports & Export

<p align="center"><img src="screenshots/reports.png" alt="Admission report" width="596"></p>

<p align="center"><img src="screenshots/reports_export.png" alt="Report exported" width="604"></p>

### Exit
<p align="center"><img src="screenshots/exit.png" alt="Exit screen" width="528"></p>

<div align="center">

**University of Makati Admission Management System** · Java console application

[View on GitHub](https://github.com/NA0r3n/SchoolAdmission-JavaBased-System/)

</div>
