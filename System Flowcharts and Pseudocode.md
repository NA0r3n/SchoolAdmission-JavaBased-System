# System Flowcharts & File-by-File Pseudocode

## 1. System Architecture & Role of Individual Files (Mermaid)

This diagram illustrates how the six `.java` source files interact with one another and with external CSV storage.

```mermaid
flowchart TD
    subgraph Presentation_Layer ["Presentation / CLI Layer"]
        MAIN["<b>main.java</b><br/>• Displays CLI menus & prompts<br/>• Validates user input (Regex/Ranges)<br/>• Formats console output"]
    end

    subgraph Service_Layer ["Business Logic & Persistence Layer"]
        SERVICE["<b>AdmissionService.java</b><br/>• Evaluates GWA & slot eligibility<br/>• Generates IDs (UMAK-YYYY-XXXX)<br/>• Manages CRUD & slot counts<br/>• Generates reports & CSV exports"]
    end

    subgraph Domain_Models ["Domain Models & Enums"]
        STUDENT["<b>Student.java</b><br/>• Stores applicant attributes<br/>• Getters & Setters"]
        COURSEREQ["<b>CourseRequirement.java</b><br/>• Stores minGWA & max slots<br/>• Links Course to College"]
        COLLEGE["<b>College.java (Enum)</b><br/>• 11 UMak Colleges<br/>• Acronym & fullName"]
        YEAR["<b>YearLevel.java (Enum)</b><br/>• FIRST_YEAR to FOURTH_YEAR<br/>• displayName"]
    end

    subgraph Storage_Layer ["File Storage (CSV)"]
        CSV_DB[("<b>students.csv</b><br/>Persistent Student Database")]
        CSV_REP[("<b>Admission_Report.csv</b><br/>Exported Analytics Report")]
    end

    MAIN -->|"Calls service methods"| SERVICE
    MAIN -.->|"Reads enum values"| COLLEGE
    MAIN -.->|"Reads enum values"| YEAR
    MAIN -.->|"Displays data from"| STUDENT
    MAIN -.->|"Checks minGWA from"| COURSEREQ

    SERVICE -->|"Creates & manages list of"| STUDENT
    SERVICE -->|"Maps & enforces rules via"| COURSEREQ
    COURSEREQ -->|"Categorized by"| COLLEGE
    STUDENT -->|"Assigned"| YEAR

    SERVICE <-->|"Reads on startup / Writes on change"| CSV_DB
    SERVICE -->|"Exports summary metrics to"| CSV_REP
```

---

## 2. Main System Execution Flowchart (Mermaid)

This flowchart details the runtime lifecycle from program startup through menu selection and automated admission evaluation.

```mermaid
flowchart TD
    START([Start Program: main]) --> INIT["Instantiate AdmissionService<br/>1. Initialize 31 courses & slot maps<br/>2. Load students.csv (or create sample data)<br/>3. Recalculate taken course slots"]
    INIT --> MENU{"Display Main Menu<br/>Read User Choice (1-8)"}

    MENU -->|Invalid Input| ERR_INPUT["Display Error Message"] --> MENU
    MENU -->|1: Register| REG_INPUT["Input & Validate Personal Details,<br/>Year Level, College, Course & GWA"]
    REG_INPUT --> EVAL_GWA{"Is GWA < 75.0?"}
    EVAL_GWA -->|Yes| STAT_REJ["Status = REJECTED"]
    EVAL_GWA -->|No| EVAL_MIN{"Is GWA < Course Min GWA?"}
    EVAL_MIN -->|Yes| STAT_PEN["Status = PENDING"]
    EVAL_MIN -->|No| EVAL_SLOT{"Are Taken Slots >= Max Slots?"}
    EVAL_SLOT -->|Yes| STAT_WAIT["Status = WAITLISTED"]
    EVAL_SLOT -->|No| STAT_APP["Status = APPROVED<br/>Increment Course Taken Slots"]

    STAT_REJ & STAT_PEN & STAT_WAIT & STAT_APP --> SAVE_NEW["Generate Student ID<br/>Save to students.csv<br/>Display Receipt"] --> MENU

    MENU -->|2: View All| VIEW_ALL["Fetch all students & print list"] --> MENU
    MENU -->|3: Search| SEARCH["Search by ID or Name keyword<br/>Display matching records"] --> MENU
    MENU -->|4: Update Status| UPDATE["Find Student by ID<br/>Verify slot capacity if APPROVED<br/>Adjust slots & save students.csv"] --> MENU
    MENU -->|5: Delete Record| DELETE["Find Student by ID & confirm<br/>Free slot if APPROVED<br/>Remove & save students.csv"] --> MENU
    MENU -->|6: Course Summary| SUMMARY["Calculate (Max Slots - Taken Slots)<br/>for all courses & display"] --> MENU
    MENU -->|7: Admission Reports| REPORT["Calculate totals, counts & yield rate<br/>Optional: Export to Admission_Report.csv"] --> MENU
    MENU -->|8: Exit| EXIT_PROG([Close Reader & Terminate])
```

---

## 3. Pseudocode for Each Source File

### 3.1 `main.java`
```text
BEGIN CLASS main

    FUNCTION isValidName(name)
        RETURN name IS NOT NULL AND name IS NOT BLANK AND name MATCHES "^[a-zA-Z\s'-]+$"
    END FUNCTION

    FUNCTION isValidContact(contact)
        RETURN contact IS NOT NULL AND contact MATCHES "^09\d{9}$"
    END FUNCTION

    FUNCTION capitalizeName(text)
        IF text IS NULL OR BLANK THEN RETURN text
        SPLIT text by whitespace into words
        FOR EACH word IN words
            APPEND uppercase(first letter) + lowercase(remaining letters) + " " to result
        END FOR
        RETURN trimmed result
    END FUNCTION

    FUNCTION pause(input)
        PRINT "Press Enter to continue..."
        READ line from input
    END FUNCTION

    FUNCTION clearScreen()
        PRINT ANSI escape codes to clear console
    END FUNCTION

    PROCEDURE main(args)
        CREATE service = NEW AdmissionService()
        CREATE input = NEW BufferedReader(System.in)
        SET running = TRUE

        WHILE running IS TRUE
            CALL clearScreen()
            DISPLAY University of Makati Admission System Menu (Options 1 to 8)
            TRY
                READ choice AS INTEGER
            CATCH NumberFormatException
                PRINT "Invalid input. Please enter a number."
                CALL pause(input)
                CONTINUE LOOP
            END TRY

            SWITCH choice
                CASE 1: // Register New Student
                    PROMPT AND VALIDATE firstName, middleName (optional), lastName using isValidName()
                    FORMAT fullName using capitalizeName() and middle initial
                    PROMPT AND VALIDATE age (1 to 100)
                    PROMPT address
                    PROMPT AND VALIDATE contact using isValidContact()
                    PROMPT prevSchool
                    DISPLAY YearLevel options (1-4) AND VALIDATE selection
                    DISPLAY all colleges from service.getAllColleges() AND VALIDATE selection
                    DISPLAY courses for selectedCollege with minGWA AND VALIDATE selection
                    PROMPT AND VALIDATE gwa (0.0 to 99.9)
                    CALL newStudent = service.registerNewStudent(...)
                    IF newStudent IS NOT NULL THEN
                        DISPLAY registration confirmation receipt
                    END IF
                    CALL pause(input)

                CASE 2: // View All Students
                    GET allStudents = service.getAllStudents()
                    IF allStudents IS EMPTY THEN
                        PRINT "No student records found."
                    ELSE
                        FOR EACH student IN allStudents
                            DISPLAY student details
                        END FOR
                    END IF
                    CALL pause(input)

                CASE 3: // Search Student
                    PROMPT searchChoice (1: Search by ID, 2: Search by Name)
                    IF searchChoice == 1 THEN
                        PROMPT id
                        CALL found = service.searchByID(id)
                        DISPLAY found student details OR "Student not found."
                    ELSE
                        PROMPT keyword
                        CALL results = service.searchByName(keyword)
                        DISPLAY matching students OR "No matching students found."
                    END IF
                    CALL pause(input)

                CASE 4: // Update Student Status
                    PROMPT updateId
                    CALL studentToUpdate = service.searchByID(updateId)
                    IF studentToUpdate IS NULL THEN
                        PRINT "Student not found."
                    ELSE
                        DISPLAY current details & valid statuses (APPROVED, PENDING, REJECTED, WAITLISTED)
                        PROMPT newStatus
                        CALL updated = service.updateStudentStatus(updateId, newStatus)
                        DISPLAY success OR capacity failure message
                    END IF
                    CALL pause(input)

                CASE 5: // Delete Student Record
                    PROMPT deleteId
                    CALL studentToDelete = service.searchByID(deleteId)
                    IF studentToDelete IS NULL THEN
                        PRINT "Student not found."
                    ELSE
                        DISPLAY student details
                        PROMPT confirmation ("y/n")
                        IF confirmation EQUALS "y" (ignore case) THEN
                            CALL service.deleteStudent(deleteId)
                            PRINT "Record deleted successfully."
                        ELSE
                            PRINT "Deletion cancelled."
                        END IF
                    END IF
                    CALL pause(input)

                CASE 6: // Course Summary
                    GET summary = service.getCourseSummary()
                    FOR EACH (courseName, availableSlots) IN summary
                        PRINT availableSlots + " slots available | " + courseName
                    END FOR
                    CALL pause(input)

                CASE 7: // Admission Reports
                    CALL service.generateExpandedReport()
                    CALL pause(input)

                CASE 8: // Exit
                    PRINT Goodbye message
                    SET running = FALSE

                DEFAULT:
                    PRINT "Invalid choice. Please try again."
                    CALL pause(input)
            END SWITCH
        END WHILE
        CLOSE input
    END PROCEDURE

END CLASS
```

---

### 3.2 `AdmissionService.java`
```text
BEGIN CLASS AdmissionService
    PRIVATE students = NEW List<Student>
    PRIVATE courseRequirements = NEW Map<String, CourseRequirement>
    PRIVATE courseSlots = NEW Map<String, Integer>
    PRIVATE courseTakenSlots = NEW Map<String, Integer>
    PRIVATE rawIDSequence = 1
    PRIVATE CONSTANT STUDENT_CSV = "students.csv"

    CONSTRUCTOR AdmissionService()
        CALL initializeCourses()
        CALL loadStudentsFromCSV()
        IF students IS EMPTY THEN
            CALL createSampleStudents()
        END IF
        CALL recalculateSlotCounts()
    END CONSTRUCTOR

    PRIVATE PROCEDURE initializeCourses()
        FOR EACH of the 31 UMak degree programs
            CALL addCourse(NEW CourseRequirement(courseName, college, minGWA, maxSlots))
        END FOR
    END PROCEDURE

    PRIVATE PROCEDURE addCourse(req)
        PUT (req.courseName, req) INTO courseRequirements
        PUT (req.courseName, req.slots) INTO courseSlots
        PUT (req.courseName, 0) INTO courseTakenSlots
    END PROCEDURE

    FUNCTION getAllColleges()
        RETURN List of all College enum values
    END FUNCTION

    FUNCTION getCoursesByCollege(college)
        INITIALIZE empty list courses
        FOR EACH req IN courseRequirements.values()
            IF req.college == college THEN ADD req.courseName TO courses
        END FOR
        RETURN courses
    END FUNCTION

    FUNCTION getCourseRequirement(courseName)
        RETURN courseRequirements.get(courseName)
    END FUNCTION

    PRIVATE PROCEDURE createSampleStudents()
        CALL registerPreloadedStudent(...) for 9 sample applicants
        CALL saveStudentsToCSV()
    END PROCEDURE

    FUNCTION registerNewStudent(fullName, age, address, contact, prevSchool, courseApplied, gwa, yearLevel, isPreloaded)
        GET req = courseRequirements.get(courseApplied)
        
        // Automated Status Evaluation Logic
        IF req IS NULL THEN
            SET status = "PENDING"
        ELSE IF gwa < 75.0 THEN
            SET status = "REJECTED"
        ELSE IF gwa < req.minGWA THEN
            SET status = "PENDING"
        ELSE IF courseTakenSlots.get(courseApplied) >= courseSlots.get(courseApplied) THEN
            SET status = "WAITLISTED"
        ELSE
            SET status = "APPROVED"
            INCREMENT courseTakenSlots for courseApplied by 1
        END IF

        GENERATE studentID = "UMAK-" + CurrentYear + "-" + ZERO_PAD(rawIDSequence++, 4)
        CREATE newStudent = NEW Student(studentID, fullName, courseApplied)
        SET newStudent attributes (status, age, address, contact, prevSchool, gwa, yearLevel, dateAdmitted = TODAY)
        ADD newStudent TO students
        CALL saveStudentsToCSV()
        RETURN newStudent
    END FUNCTION

    FUNCTION getAllStudents()
        RETURN students
    END FUNCTION

    FUNCTION searchByID(id)
        FOR EACH s IN students
            IF s.studentID EQUALS id THEN RETURN s
        END FOR
        RETURN NULL
    END FUNCTION

    FUNCTION searchByName(keyword)
        INITIALIZE empty list results
        FOR EACH s IN students
            IF lowercase(s.fullName) CONTAINS lowercase(keyword) THEN ADD s TO results
        END FOR
        RETURN results
    END FUNCTION

    FUNCTION updateStudentStatus(id, newStatus)
        GET student = searchByID(id)
        IF student IS NULL THEN RETURN FALSE
        
        SET oldStatus = student.status
        SET course = student.courseApplied

        IF oldStatus == "APPROVED" AND newStatus != "APPROVED" THEN
            DECREMENT courseTakenSlots for course by 1
        ELSE IF oldStatus != "APPROVED" AND newStatus == "APPROVED" THEN
            IF courseTakenSlots.get(course) >= courseSlots.get(course) THEN
                RETURN FALSE // Course full
            END IF
            INCREMENT courseTakenSlots for course by 1
        END IF

        SET student.status = newStatus
        CALL saveStudentsToCSV()
        RETURN TRUE
    END FUNCTION

    FUNCTION deleteStudent(id)
        GET student = searchByID(id)
        IF student IS NOT NULL THEN
            IF student.status == "APPROVED" THEN
                DECREMENT courseTakenSlots for student.courseApplied by 1
            END IF
            REMOVE student FROM students
            CALL saveStudentsToCSV()
            RETURN TRUE
        END IF
        RETURN FALSE
    END FUNCTION

    FUNCTION getCourseSummary()
        INITIALIZE summary Map
        FOR EACH course IN courseSlots.keySet()
            PUT (course, courseSlots.get(course) - courseTakenSlots.get(course)) INTO summary
        END FOR
        RETURN summary
    END FUNCTION

    PROCEDURE generateExpandedReport()
        COUNT total, approved, pending, rejected, waitlisted, and approved per course
        CALCULATE yieldRate = (total == 0) ? 0 : (approved / total) * 100
        DISPLAY formatted report summary and course breakdown
        CALL promptExport(total, approved, pending, rejected, waitlisted, yieldRate)
    END PROCEDURE

    PROCEDURE promptExport(total, approved, pending, rejected, waitlisted, yieldRate)
        PROMPT "Press [E] to export this report to CSV, or press Enter to skip"
        IF choice EQUALS "E" (ignore case) THEN
            WRITE metrics to "Admission_Report.csv"
        END IF
    END PROCEDURE

    PRIVATE PROCEDURE recalculateSlotCounts()
        RESET all courseTakenSlots values to 0
        FOR EACH s IN students
            IF s.status == "APPROVED" AND courseTakenSlots CONTAINS s.courseApplied THEN
                INCREMENT courseTakenSlots for s.courseApplied by 1
            END IF
        END FOR
    END PROCEDURE

    PRIVATE PROCEDURE saveStudentsToCSV()
        OPEN PrintWriter on STUDENT_CSV
        WRITE CSV header row
        FOR EACH s IN students
            WRITE formatted CSV row for s
        END FOR
    END PROCEDURE

    PRIVATE PROCEDURE loadStudentsFromCSV()
        IF STUDENT_CSV DOES NOT EXIST THEN RETURN
        OPEN BufferedReader on STUDENT_CSV
        SKIP header row
        WHILE line = br.readLine() IS NOT NULL
            PARSE CSV columns (handling quoted commas)
            RECONSTRUCT Student object and ADD to students
            EXTRACT sequence number from studentID and UPDATE rawIDSequence if seq >= rawIDSequence
        END WHILE
    END PROCEDURE

END CLASS
```

---

### 3.3 `Student.java`
```text
BEGIN CLASS Student
    PRIVATE studentID, fullName, address, contact, prevSchool, courseApplied, status AS STRING
    PRIVATE age AS INTEGER
    PRIVATE gwa AS DOUBLE
    PRIVATE dateAdmitted AS LocalDate
    PRIVATE yearLevel AS YearLevel

    CONSTRUCTOR Student(studentID, fullName, courseApplied)
        SET this.studentID = studentID
        SET this.fullName = fullName
        SET this.courseApplied = courseApplied
        SET this.dateAdmitted = TODAY
    END CONSTRUCTOR

    // Standard Getters and Setters for all private attributes:
    // getStudentID(), getFullName(), get/setStatus(), get/setAge(),
    // get/setAddress(), get/setContact(), get/setPrevSchool(),
    // get/setGrades() [for gwa], getCourseApplied(),
    // get/setDateAdmitted(), get/setYearLevel()
END CLASS
```

---

### 3.4 `CourseRequirement.java`
```text
BEGIN CLASS CourseRequirement
    PRIVATE courseName AS STRING
    PRIVATE college AS College
    PRIVATE minGWA AS DOUBLE
    PRIVATE slots AS INTEGER

    CONSTRUCTOR CourseRequirement(courseName, college, minGWA, slots)
        SET this.courseName = courseName
        SET this.college = college
        SET this.minGWA = minGWA
        SET this.slots = slots
    END CONSTRUCTOR

    // Standard Getters:
    // getCourseName(), getCollege(), getMinGWA(), getSlots()
END CLASS
```

---

### 3.5 `College.java`
```text
BEGIN ENUM College
    CONSTANTS:
        CCIS("College of Computing and Information Sciences"),
        CBFS("College of Business and Financial Science"),
        CTHM("College of Tourism and Hospitality Management"),
        CCSE("College of Construction Sciences and Engineering"),
        CET("College of Engineering Technology"),
        CITE("College of Innovative Teacher Education"),
        CONHS("College of Nursing and Health Sciences"),
        CGPP("College of Governance and Public Policy"),
        CLAS("College of Liberal Arts and Sciences"),
        CHK("College of Human Kinetics"),
        IAD("Institute of Arts and Design")

    PUBLIC FINAL fullName AS STRING

    CONSTRUCTOR College(fullName)
        SET this.fullName = fullName
    END CONSTRUCTOR
END ENUM
```

---

### 3.6 `YearLevel.java`
```text
BEGIN ENUM YearLevel
    CONSTANTS:
        FIRST_YEAR("1st Year"),
        SECOND_YEAR("2nd Year"),
        THIRD_YEAR("3rd Year"),
        FOURTH_YEAR("4th Year")

    PUBLIC FINAL displayName AS STRING

    CONSTRUCTOR YearLevel(displayName)
        SET this.displayName = displayName
    END CONSTRUCTOR
END ENUM
```