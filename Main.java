import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String DIVIDER = "------------------------------------";
    private static final String HEADER_LINE = "====================================";

    // Indices 0, 2, 5 are Mandatory; the rest are To-Follow
    private static final String[] REQUIRED_DOCS = {
        "Notice of Admission",      // [1] Mandatory
        "Medical Certificate",      // [2] To-Follow
        "Form 138 / Report Card",   // [3] Mandatory
        "Form 137",                 // [4] To-Follow
        "Good Moral Certificate",   // [5] To-Follow
        "PSA Birth Certificate",    // [6] Mandatory
        "2x2 ID Picture"            // [7] To-Follow
    };

    private static final boolean[] IS_MANDATORY = {
        true,   // Notice of Admission
        false,  // Medical Certificate
        true,   // Form 138 / Report Card
        false,  // Form 137
        false,  // Good Moral Certificate
        true,   // PSA Birth Certificate
        false   // 2x2 ID Picture
    };

    public static boolean isValidName(String name) {
        return name != null && !name.isBlank() && name.trim().matches("^[a-zA-Z\\s'-]+$");
    }

    public static boolean isValidContact(String contact) {
        return contact != null && contact.trim().matches("^09\\d{9}$");
    }

    public static String capitalizeName(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        String[] words = text.trim().split("\\s+");
        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1).toLowerCase())
                  .append(" ");
        }
        return result.toString().trim();
    }

    public static void printBanner(String title, String subtitle) {
        System.out.println(HEADER_LINE);
        System.out.println("  " + title);
        if (subtitle != null && !subtitle.isBlank()) {
            System.out.println("  " + subtitle);
        }
        System.out.println(HEADER_LINE);
        System.out.println();
    }

    public static void printSection(String title) {
        System.out.println();
        System.out.println(DIVIDER);
        System.out.println("  " + title);
        System.out.println(DIVIDER);
    }

    public static int readIntInRange(BufferedReader input, String prompt, int min, int max) throws Exception {
        while (true) {
            System.out.print(prompt);
            String line = input.readLine();
            try {
                int value = Integer.parseInt(line.trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("  [!] Enter %d to %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid input. Enter a number.");
            }
        }
    }

    private static boolean[] checkRequirementsBatch(BufferedReader input) throws Exception {
        boolean[] submitted = new boolean[REQUIRED_DOCS.length];
        for (int i = 0; i < REQUIRED_DOCS.length; i++) {
            submitted[i] = true;
        }

        System.out.println("  [REQ] = Mandatory  [OPT] = To-Follow");
        System.out.println();
        for (int i = 0; i < REQUIRED_DOCS.length; i++) {
            String tag = IS_MANDATORY[i] ? "[REQ]" : "[OPT]";
            System.out.printf("  [%d] %s %s%n", i + 1, tag, REQUIRED_DOCS[i]);
        }
        System.out.println();
        System.out.println("  Enter MISSING numbers (e.g. 2 4),");
        System.out.println("  or press Enter / 'none' if complete.");

        while (true) {
            System.out.print("Missing items: ");
            String line = input.readLine().trim();

            if (line.isEmpty() || line.equalsIgnoreCase("none") || line.equals("0")) {
                return submitted;
            }

            for (int i = 0; i < REQUIRED_DOCS.length; i++) {
                submitted[i] = true;
            }

            String[] tokens = line.split("[,\\s]+");
            boolean valid = true;
            for (String token : tokens) {
                try {
                    int num = Integer.parseInt(token);
                    if (num < 1 || num > REQUIRED_DOCS.length) {
                        valid = false;
                        break;
                    }
                    submitted[num - 1] = false;
                } catch (NumberFormatException e) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                return submitted;
            }
            System.out.printf("  [!] Enter numbers 1-%d or 'none'.%n", REQUIRED_DOCS.length);
        }
    }

    public static String readRequiredText(BufferedReader input, String prompt) throws Exception {
        while (true) {
            System.out.print(prompt);
            String text = input.readLine().trim();
            if (!text.isBlank()) {
                return text;
            }
            System.out.println("  [!] This field is required.");
        }
    }

    public static void pause(BufferedReader input) throws Exception {
        System.out.println();
        System.out.println(DIVIDER);
        System.out.print("Press Enter to continue...");
        input.readLine();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void printStudentSummary(Student s) {
        System.out.println(DIVIDER);
        System.out.printf("  %-13s: %s%n", "ID", s.getStudentID() != null ? s.getStudentID() : "Not Assigned");
        System.out.printf("  %-13s: %s%n", "Name", s.getFullName());
        System.out.printf("  %-13s: %s%n", "Year Level", s.getYearLevel().displayName);
        System.out.printf("  %-13s: %s%n", "Course", s.getCourseApplied());
        System.out.printf("  %-13s: %s%n", "GWA", s.getGrades());
        System.out.printf("  %-13s: %s%n", "Status", s.getStatus());
        System.out.printf("  %-13s: %s%n", "Date Admitted", s.getDateAdmitted());
    }

    private static void printCompactStudentRow(String prefix, Student s) {
        String sid = s.getStudentID() != null ? s.getStudentID() : "No ID";
        System.out.printf("  %s%s | %s%n", prefix, sid, s.getStatus());
        System.out.println("    " + s.getFullName());
    }

    public static void main(String[] args) throws Exception {
        AdmissionService service = new AdmissionService();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;

        while (running) {
            clearScreen();
            printBanner("UNIVERSITY OF MAKATI", "Admission Management System");
            System.out.println("  [1] Register New Student");
            System.out.println("  [2] View All Students");
            System.out.println("  [3] Search Student");
            System.out.println("  [4] Update Student Status");
            System.out.println("  [5] Delete Student Record");
            System.out.println("  [6] Course Summary");
            System.out.println("  [7] Admission Reports");
            System.out.println("  [8] Exit");
            System.out.println();

            int choice;
            try {
                System.out.print("Enter choice: ");
                choice = Integer.parseInt(input.readLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\n  [!] Invalid input. Enter a number.");
                pause(input);
                continue;
            }

            switch (choice) {
                case 1: {
                    clearScreen();
                    printBanner("REQUIREMENTS CHECKER", "Step 1 of 2: Document Verification");

                    printSection("APPLICANT NAME");
                    System.out.print("First Name: ");
                    String firstName = input.readLine();
                    while (!isValidName(firstName)) {
                        System.out.print("  [!] Invalid name. First Name: ");
                        firstName = input.readLine();
                    }

                    System.out.print("Middle Name (Enter if none): ");
                    String middleName = input.readLine().trim();
                    if (!middleName.isBlank()) {
                        while (!isValidName(middleName)) {
                            System.out.print("  [!] Invalid name. Middle Name: ");
                            middleName = input.readLine().trim();
                        }
                    }

                    System.out.print("Last Name: ");
                    String lastName = input.readLine();
                    while (!isValidName(lastName)) {
                        System.out.print("  [!] Invalid name. Last Name: ");
                        lastName = input.readLine();
                    }

                    StringBuilder fullNameBuilder = new StringBuilder();
                    fullNameBuilder.append(capitalizeName(firstName));
                    if (!middleName.isBlank()) {
                        fullNameBuilder.append(" ")
                                       .append(Character.toUpperCase(middleName.charAt(0)))
                                       .append(".");
                    }
                    fullNameBuilder.append(" ").append(capitalizeName(lastName));
                    String fullName = fullNameBuilder.toString();

                    printSection("SUBMISSION CHECKLIST");
                    boolean[] submitted = checkRequirementsBatch(input);

                    List<String> missingMandatory = new ArrayList<>();
                    List<String> toFollowDocs = new ArrayList<>();

                    for (int i = 0; i < REQUIRED_DOCS.length; i++) {
                        if (!submitted[i]) {
                            if (IS_MANDATORY[i]) {
                                missingMandatory.add(REQUIRED_DOCS[i]);
                            } else {
                                toFollowDocs.add(REQUIRED_DOCS[i]);
                            }
                        }
                    }

                    printSection("EVALUATION RESULT");
                    System.out.printf("  %-11s: %s%n", "Applicant", fullName);

                    for (String doc : missingMandatory) {
                        System.out.println("  [BLOCKED]   " + doc);
                    }
                    for (String doc : toFollowDocs) {
                        System.out.println("  [TO-FOLLOW] " + doc);
                    }

                    System.out.println();
                    if (!missingMandatory.isEmpty()) {
                        System.out.println("  [!] REGISTRATION BLOCKED");
                        System.out.println("  Submit mandatory documents first.");
                        System.out.println("  Note: Falsified documents = refusal.");
                        pause(input);
                        break;
                    } else if (!toFollowDocs.isEmpty()) {
                        System.out.println("  [!] CONDITIONAL PASS (TO-FOLLOW)");
                        System.out.println("  Proceeding; status will be PENDING");
                        System.out.println("  until to-follow items are submitted.");
                    } else {
                        System.out.println("  [OK] REQUIREMENTS COMPLETE");
                        System.out.println("  Proceed to Step 2 registration.");
                    }
                    System.out.println("  Note: Falsified documents = refusal.");

                    pause(input);
                    clearScreen();
                    printBanner("NEW STUDENT REGISTRATION", "Step 2 of 2: Profile & Course");

                    int age = readIntInRange(input, "Age (1-100): ", 1, 100);

                    String address = readRequiredText(input, "Complete Address: ");

                    System.out.print("Contact (09XXXXXXXXX): ");
                    String contact = input.readLine().trim();
                    while (!isValidContact(contact)) {
                        System.out.print("  [!] 11 digits starting with 09: ");
                        contact = input.readLine().trim();
                    }

                    String prevSchool = readRequiredText(input, "Previous School: ");

                    printSection("YEAR LEVEL");
                    System.out.println("  [1] 1st Year");
                    System.out.println("  [2] 2nd Year");
                    System.out.println("  [3] 3rd Year");
                    System.out.println("  [4] 4th Year");
                    int yearChoice = readIntInRange(input, "Enter choice: ", 1, 4);
                    YearLevel yearLevel = YearLevel.values()[yearChoice - 1];

                    printSection("SELECT COLLEGE");
                    List<College> colleges = service.getAllColleges();
                    for (int i = 0; i < colleges.size(); i++) {
                        System.out.printf("  [%d] %s - %s%n", i + 1, colleges.get(i), colleges.get(i).fullName);
                    }
                    int collegeIndex = readIntInRange(input, "Enter college number: ", 1, colleges.size()) - 1;
                    College selectedCollege = colleges.get(collegeIndex);

                    printSection("COURSES IN " + selectedCollege);
                    List<String> courses = service.getCoursesByCollege(selectedCollege);
                    for (int i = 0; i < courses.size(); i++) {
                        CourseRequirement req = service.getCourseRequirement(courses.get(i));
                        System.out.printf("  [%d] %s (Min: %.1f)%n", i + 1, courses.get(i), req.getMinGWA());
                    }
                    int courseIndex = readIntInRange(input, "Enter course number: ", 1, courses.size()) - 1;
                    String courseApplied = courses.get(courseIndex);

                    double gwa;
                    while (true) {
                        System.out.print("GWA (0.0 - 99.9): ");
                        try {
                            gwa = Double.parseDouble(input.readLine().trim());
                            if (gwa >= 0.0 && gwa <= 99.9) {
                                break;
                            }
                            System.out.println("  [!] Enter 0.0 to 99.9.");
                        } catch (NumberFormatException e) {
                            System.out.println("  [!] Enter a valid number.");
                        }
                    }

                    CourseRequirement req = service.getCourseRequirement(courseApplied);
                    int remainingSlots = service.getCourseSummary().getOrDefault(courseApplied, 0);
                    boolean hasToFollow = !toFollowDocs.isEmpty();

                    printSection("APPLICATION PREVIEW");
                    System.out.printf("  %-14s: %s%n", "Applicant", fullName);
                    System.out.printf("  %-14s: %s%n", "Course", courseApplied);
                    System.out.printf("  %-14s: %.2f%n", "Your GWA", gwa);
                    if (req != null) {
                        System.out.printf("  %-14s: %.1f%n", "Min GWA", req.getMinGWA());
                        System.out.printf("  %-14s: %d%n", "Slots Left", remainingSlots);
                    }
                    if (hasToFollow) {
                        System.out.printf("  %-14s: %s%n", "To-Follow", String.join(", ", toFollowDocs));
                    }
                    System.out.println();

                    String predicted = service.predictStatus(courseApplied, gwa, hasToFollow);
                    if (req == null) {
                        System.out.println("  Result: PENDING (Course unknown)");
                    } else if (predicted.equals("REJECTED")) {
                        System.out.println("  Result: REJECTED (Below 75.0)");
                    } else if (gwa < req.getMinGWA()) {
                        System.out.println("  Result: PENDING (Min: " + req.getMinGWA() + ")");
                    } else if (predicted.equals("WAITLISTED")) {
                        System.out.println("  Result: WAITLISTED (No slots)");
                    } else if (predicted.equals("PENDING")) {
                        System.out.println("  Result: PENDING (To-follow docs)");
                    } else {
                        System.out.println("  Result: APPROVED");
                    }

                    System.out.println();
                    if (Checker.confirm(input, "Submit application? (y/n): ")) {
                        Student saved = service.registerNewStudent(fullName, age, address, contact,
                                prevSchool, courseApplied, gwa, yearLevel, hasToFollow);
                        System.out.println();
                        System.out.println("  [OK] Application submitted.");
                        System.out.println("  Status : " + saved.getStatus());
                        System.out.println("  ID     : " + (saved.getStudentID() != null ? saved.getStudentID() : "Not Assigned"));
                    } else {
                        System.out.println("\n  Application cancelled.");
                    }
                    pause(input);
                    break;
                }

                case 2: {
                    clearScreen();
                    printBanner("ALL STUDENTS", "Registered Directory");

                    List<Student> allStudents = service.getAllStudents();
                    if (allStudents.isEmpty()) {
                        System.out.println("  No student records found.");
                    } else {
                        for (Student s : allStudents) {
                            printStudentSummary(s);
                        }
                        System.out.println(DIVIDER);
                        System.out.println("  Total Records: " + allStudents.size());
                    }
                    pause(input);
                    break;
                }

                case 3: {
                    clearScreen();
                    printBanner("SEARCH STUDENT", "By ID or Name");
                    System.out.println("  [1] Search by ID");
                    System.out.println("  [2] Search by Name");
                    System.out.println("  [3] Filter by Course");
                    System.out.println();

                    int searchChoice = readIntInRange(input, "Enter choice: ", 1, 3);
                    System.out.println();

                     if (searchChoice == 3) {
                        System.out.print("Enter course keyword: ");
                        String keyword = input.readLine().trim();
                        List<Student> results = service.searchByCourse(keyword);
                        System.out.println();
                        if (results.isEmpty()) {
                            System.out.println("  No students found for that course.");
                        } else {
                            System.out.println("  Found " + results.size() + " student(s):");
                            for (Student s : results) {
                                System.out.println(DIVIDER);
                                printCompactStudentRow("", s);
                                System.out.println("    " + s.getCourseApplied());
                            }
                            System.out.println(DIVIDER);
                        }
                    } else

                    if (searchChoice == 1) {
                        System.out.print("Enter Student ID: ");
                        String id = input.readLine().trim();
                        Student found = service.searchByID(id);
                        System.out.println();
                        if (found != null) {
                            System.out.println(DIVIDER);
                            System.out.printf("  %-13s: %s%n", "ID", found.getStudentID());
                            System.out.printf("  %-13s: %s%n", "Name", found.getFullName());
                            System.out.printf("  %-13s: %s%n", "Year Level", found.getYearLevel().displayName);
                            System.out.printf("  %-13s: %s%n", "Address", found.getAddress());
                            System.out.printf("  %-13s: %s%n", "Contact", found.getContact());
                            System.out.printf("  %-13s: %s%n", "Course", found.getCourseApplied());
                            System.out.printf("  %-13s: %s%n", "GWA", found.getGrades());
                            System.out.printf("  %-13s: %s%n", "Status", found.getStatus());
                            System.out.println(DIVIDER);
                        } else {
                            System.out.println("  Student not found.");
                        }
                    } else {
                        System.out.print("Enter name keyword: ");
                        String keyword = input.readLine().trim();
                        List<Student> results = service.searchByName(keyword);
                        System.out.println();
                        if (results.isEmpty()) {
                            System.out.println("  No matching students found.");
                        } else {
                            System.out.println("  Found " + results.size() + " student(s):");
                            for (Student s : results) {
                                System.out.println(DIVIDER);
                                printCompactStudentRow("", s);
                            } 
                            System.out.println(DIVIDER);
                        }
                    }
                    
                    pause(input);
                    break;
                }

                case 4: {
                    clearScreen();
                    printBanner("UPDATE STUDENT STATUS", "Modify Standing");
                    System.out.println("  [1] Search by ID");
                    System.out.println("  [2] Search by Name");
                    System.out.println();

                    int updateChoice = readIntInRange(input, "Enter choice: ", 1, 2);
                    Student studentToUpdate = null;

                    if (updateChoice == 1) {
                        System.out.print("Enter Student ID: ");
                        String updateId = input.readLine().trim();
                        studentToUpdate = service.searchByID(updateId);
                    } else {
                        System.out.print("Enter name keyword: ");
                        String keyword = input.readLine().trim();
                        List<Student> results = service.searchByName(keyword);
                        if (results.isEmpty()) {
                            System.out.println("\n  No matching students found.");
                            pause(input);
                            break;
                        }
                        System.out.println("\n  Found " + results.size() + " student(s):");
                        for (int i = 0; i < results.size(); i++) {
                            System.out.println(DIVIDER);
                            printCompactStudentRow("[" + (i + 1) + "] ", results.get(i));
                        }
                        System.out.println(DIVIDER);
                        int pick = readIntInRange(input, "Select student number: ", 1, results.size()) - 1;
                        studentToUpdate = results.get(pick);
                    }

                    if (studentToUpdate == null) {
                        System.out.println("\n  Student not found.");
                    } else {
                        printSection("CURRENT RECORD");
                        System.out.printf("  %-13s: %s%n", "Student", studentToUpdate.getFullName());
                        System.out.printf("  %-13s: %s%n", "Current ID",
                                studentToUpdate.getStudentID() != null ? studentToUpdate.getStudentID() : "Not Assigned");
                        System.out.printf("  %-13s: %s%n", "Old Status", studentToUpdate.getStatus());

                        System.out.println("\n  Statuses: APPROVED | PENDING");
                        System.out.println("            REJECTED | WAITLISTED");
                        System.out.print("Enter new status: ");
                        String newStatus = input.readLine().trim().toUpperCase();

                        boolean updated = service.updateStudentStatus(studentToUpdate, newStatus);
                        System.out.println();
                        if (updated) {
                            System.out.println("  [OK] Status updated.");
                            if (newStatus.equals("APPROVED") && studentToUpdate.getStudentID() != null) {
                                System.out.println("  New ID: " + studentToUpdate.getStudentID());
                            }
                        } else {
                            System.out.println("  [!] Update failed. Use a valid status and check slots.");
                        }
                    }
                    pause(input);
                    break;
                }

                case 5: {
                    while (true) {
                    clearScreen();
                    printBanner("DELETE STUDENT RECORD", "Remove Entry");
                    System.out.println("  [1] Delete by ID (Approved)");
                    System.out.println("  [2] Search by Name (All)");
                    System.out.println();

                    int deleteChoice = readIntInRange(input, "Enter choice: ", 1, 2);
                    Student studentToDelete = null;

                    if (deleteChoice == 1) {
                        System.out.print("Enter Student ID: ");
                        String deleteId = input.readLine().trim();
                        studentToDelete = service.searchByID(deleteId);
                    } else {
                        System.out.print("Enter name keyword: ");
                        String keyword = input.readLine().trim();
                        List<Student> results = service.searchByName(keyword);
                        if (results.isEmpty()) {
                            System.out.println("\n  No matching students found.");
                            pause(input);
                            break;
                        }
                        System.out.println("\n  Found " + results.size() + " student(s):");
                        for (int i = 0; i < results.size(); i++) {
                            System.out.println(DIVIDER);
                            printCompactStudentRow("[" + (i + 1) + "] ", results.get(i));
                        }
                        System.out.println(DIVIDER);
                        int pick = readIntInRange(input, "Select student number to delete: ", 1, results.size()) - 1;
                        studentToDelete = results.get(pick);
                    }

                    System.out.println();
                    if (studentToDelete == null) {
                        System.out.println("  Student not found.");
                    } else {
                        System.out.println(DIVIDER);
                        System.out.printf("  %-13s: %s%n", "ID",
                                studentToDelete.getStudentID() != null ? studentToDelete.getStudentID() : "Not Assigned");
                        System.out.printf("  %-13s: %s%n", "Student", studentToDelete.getFullName());
                        System.out.printf("  %-13s: %s%n", "Status", studentToDelete.getStatus());
                        System.out.printf("  %-13s: %s%n", "Course", studentToDelete.getCourseApplied());
                        System.out.println(DIVIDER);
                        System.out.println();
                        boolean confirmDelete = Checker.confirm(input, "Confirm deletion? (y/n): ");
                        System.out.println();
                        if (confirmDelete) {
                            if (service.deleteStudent(studentToDelete)) {
                                System.out.println("  [OK] Record deleted.");
                            } else {
                                System.out.println("  [!] Delete failed.");
                            }
                        } else {
                            System.out.println("  Deletion cancelled.");
                        }
                    }
                        if (Checker.confirm(input, "\n  Delete another record?: ")) {
                      continue;
                   } 
                 break; 
                }
                }

                case 6: {
                    clearScreen();
                    printBanner("COURSE SUMMARY", "Slot Availability by College");
                    Map<String, Integer> summary = service.getCourseSummary();
                    List<College> colleges = service.getAllColleges();

                    for (College college : colleges) {
                        printSection(college + " - " + college.fullName);
                        List<String> courses = service.getCoursesByCollege(college);

                        for (String course : courses) {
                            int slots = summary.getOrDefault(course, 0);
                            CourseRequirement req = service.getCourseRequirement(course);
                            String minGwa = (req != null) ? String.format("%.1f", req.getMinGWA()) : "N/A";
                            String statusTag = (slots > 0) ? String.format("%2d left", slots) : "FULL";

                            System.out.println("  " + course);
                            System.out.printf("    Slots: %-8s | Min GWA: %s%n", statusTag, minGwa);
                        }
                    }
                    pause(input);
                    break;
                }

                case 7: {
                    clearScreen();
                    printBanner("ADMISSION REPORTS", "Expanded System Report");
                    service.generateExpandedReport();
                    pause(input);
                    break;
                }

                case 8: {
                    clearScreen();
                    printBanner("THANK YOU FOR USING UMAK SYSTEM", "Goodbye.");
                    running = false;
                    break;
                }

                default: {
                    System.out.println("\n  [!] Invalid choice. Try again.");
                    pause(input);
                    break;
                }
            }
        }
        input.close();
    }
}
