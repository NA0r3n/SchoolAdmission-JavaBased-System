import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class main {

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
            result.append(word.substring(0, 1).toUpperCase())
                  .append(word.substring(1).toLowerCase())
                  .append(" ");
        }
        return result.toString().trim();
    }

    public static void pause(BufferedReader input) throws Exception {
        System.out.println("\n");
        System.out.println("---------------------------------------");
        System.out.print("Press Enter to continue...");
        input.readLine();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) throws Exception {
        AdmissionService service = new AdmissionService();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;

        while (running) {
            clearScreen();
            System.out.println("=====================================");
            System.out.println("     UNIVERSITY OF MAKATI            ");
            System.out.println("        Admission System             ");
            System.out.println("=====================================");
            System.out.println();
            System.out.println("[1] Register New Student");
            System.out.println("[2] View All Students");
            System.out.println("[3] Search Student");
            System.out.println("[4] Update Student Status");
            System.out.println("[5] Delete Student Record");
            System.out.println("[6] Course Summary");
            System.out.println("[7] Admission Reports");
            System.out.println("[8] Exit");
            System.out.println();
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(input.readLine());
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number.");
                pause(input);
                continue;
            }

            switch (choice) {
                case 1:
                    clearScreen();
                    System.out.println("=====================================");
                    System.out.println("       NEW STUDENT REGISTRATION      ");
                    System.out.println("=====================================");
                    System.out.println();

                    System.out.print("First Name: ");
                    String firstName = input.readLine();
                    while (!isValidName(firstName)) {
                        System.out.print("Invalid name. Enter First Name: ");
                        firstName = input.readLine();
                    }

                    System.out.print("Middle Name (press Enter if none): ");
                    String middleName = input.readLine().trim();
                    if (!middleName.isBlank()) {
                        while (!isValidName(middleName)) {
                            System.out.print("Invalid name. Enter Middle Name: ");
                            middleName = input.readLine().trim();
                        }
                    }

                    System.out.print("Last Name: ");
                    String lastName = input.readLine();
                    while (!isValidName(lastName)) {
                        System.out.print("Invalid name. Enter Last Name: ");
                        lastName = input.readLine();
                    }

                    StringBuilder fullNameBuilder = new StringBuilder();
                    fullNameBuilder.append(capitalizeName(firstName));
                    if (!middleName.isBlank()) {
                        fullNameBuilder.append(" ").append(middleName.trim().charAt(0)).append(".");
                    }
                    fullNameBuilder.append(" ").append(capitalizeName(lastName));
                    String fullName = fullNameBuilder.toString();

                    int age;
                
                    while (true) {
                        System.out.print("Age: ");
                        try {
                            age = Integer.parseInt(input.readLine());
                            if (age > 0 && age <= 100) {
                                break;
                            }
                            System.out.println("Please enter a valid age between 1 and 100.");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                        }
                    }

                    System.out.print("Complete Address: ");
                    String address = input.readLine();

                    System.out.print("Contact Number (start with 09, 11 digits): ");
                    String contact = input.readLine().trim();
                    while (!isValidContact(contact)) {
                        System.out.print("Invalid contact. Enter again: ");
                        contact = input.readLine().trim();
                    }

                    System.out.print("Previous School: ");
                    String prevSchool = input.readLine();

                    System.out.println("-------------------------------------");
                    System.out.println("            YEAR LEVEL               ");
                    System.out.println("-------------------------------------");
                    System.out.println("[1] 1st Year");
                    System.out.println("[2] 2nd Year");
                    System.out.println("[3] 3rd Year");
                    System.out.println("[4] 4th Year");
                    
                    int yearChoice;
                    while (true) {
                        System.out.print("Enter choice: ");
                        try {
                            yearChoice = Integer.parseInt(input.readLine());
                            if (yearChoice >= 1 && yearChoice <= 4) {
                                break;
                            }
                            System.out.println("Please enter a number between 1 and 4.");
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    }
                    YearLevel yearLevel = YearLevel.values()[yearChoice - 1];
                    System.out.println();

                    System.out.println("-------------------------------------");
                    System.out.println("           SELECT COLLEGE            ");
                    System.out.println("-------------------------------------");
                    List<College> colleges = service.getAllColleges();
                    for (int i = 0; i < colleges.size(); i++) {
                        System.out.printf("[%d] %s - %s%n", i + 1, colleges.get(i), colleges.get(i).fullName);
                    }
                    
                    int collegeIndex;
                    while (true) {
                        System.out.print("Enter college number: ");
                        try {
                            int inputNum = Integer.parseInt(input.readLine());
                            collegeIndex = inputNum - 1;
                            if (collegeIndex >= 0 && collegeIndex < colleges.size()) {
                                break;
                            }
                            System.out.println("Please enter a number between 1 and " + colleges.size() + ".");
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    }
                    College selectedCollege = colleges.get(collegeIndex);
                    System.out.println();

                    System.out.println("-------------------------------------");
                    System.out.println("  COURSES IN " + selectedCollege);
                    System.out.println("-------------------------------------");
                    List<String> courses = service.getCoursesByCollege(selectedCollege);
                    for (int i = 0; i < courses.size(); i++) {
                        CourseRequirement req = service.getCourseRequirement(courses.get(i));
                        System.out.printf("[%d] %s (Min GWA: %.1f)%n", i + 1, courses.get(i), req.getMinGWA());
                    }
                    
                    int courseIndex;
                    while (true) {
                        System.out.print("Enter course number: ");
                        try {
                            int inputNum = Integer.parseInt(input.readLine());
                            courseIndex = inputNum - 1;
                            if (courseIndex >= 0 && courseIndex < courses.size()) {
                                break;
                            }
                            System.out.println("Please enter a number between 1 and " + courses.size() + ".");
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    }
                    String courseApplied = courses.get(courseIndex);

                    System.out.print("General Weighted Average (GWA): ");
                                        double gwa;
                    while (true) {
                        System.out.print("General Weighted Average (GWA): ");
                        try {
                            gwa = Double.parseDouble(input.readLine());
                            if (gwa >= 0.0 && gwa <= 99.9) {
                                break;
                            }
                            System.out.println("Please enter a valid GWA between 0.0 and 99.9.");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                        }
                    }
                    clearScreen();
                    System.out.println("-------------------------------------\n\n");
                    Student newStudent = service.registerNewStudent(fullName, age, address, contact,
                            prevSchool, courseApplied, gwa, yearLevel);

                    System.out.println("-------------------------------------\n\n");
                
                    if (newStudent != null) {
                        System.out.println("=====================================");
                        System.out.println("        STUDENT REGISTERED           ");
                        System.out.println("=====================================");
                        System.out.println();
                        System.out.println("Student ID: " + newStudent.getStudentID());
                        System.out.println("Name:       " + newStudent.getFullName());
                        System.out.println("Year Level: " + newStudent.getYearLevel().displayName);
                        System.out.println("Course:     " + newStudent.getCourseApplied());
                        System.out.println("GWA:        " + newStudent.getGrades());
                        System.out.println("Status:     " + newStudent.getStatus());
                        System.out.println("Date:       " + newStudent.getDateAdmitted());
                        System.out.println();
                    }
                    pause(input);
                    break;

                case 2:
                    clearScreen();
                    System.out.println("=====================================");
                    System.out.println("          ALL STUDENTS               ");
                    System.out.println("=====================================");
                    System.out.println();
                    
                    List<Student> allStudents = service.getAllStudents();
                    if (allStudents.isEmpty()) {
                        System.out.println("       No student records found.");
                    } else {
                        for (Student s : allStudents) {
                            System.out.println("-------------------------------------");
                            System.out.println("ID:         " + s.getStudentID());
                            System.out.println("Name:       " + s.getFullName());
                            System.out.println("Year Level: " + s.getYearLevel().displayName);
                            System.out.println("Course:     " + s.getCourseApplied());
                            System.out.println("GWA:        " + s.getGrades());
                            System.out.println("Status:     " + s.getStatus());
                            System.out.println("Date:       " + s.getDateAdmitted());
                        }
                        System.out.println("-------------------------------------");
                    }
                    pause(input);
                    break;

                case 3:
                    clearScreen();
                    System.out.println("=====================================");
                    System.out.println("         SEARCH STUDENT              ");
                    System.out.println("=====================================");
                    System.out.println();
                    System.out.println("[1] Search by ID");
                    System.out.println("[2] Search by Name");
                    System.out.println();
                    
                    int searchChoice;
                    while (true) {
                        System.out.print("Enter choice: ");
                        try {
                            searchChoice = Integer.parseInt(input.readLine());
                            if (searchChoice == 1 || searchChoice == 2) {
                                break;
                            }
                            System.out.println("Please enter 1 or 2.");
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    }
                    System.out.println();

                    if (searchChoice == 1) {
                        System.out.print("Enter Student ID: ");
                        String id = input.readLine().trim();
                        Student found = service.searchByID(id);
                        System.out.println();
                        if (found != null) {
                            System.out.println("-------------------------------------");
                            System.out.println("ID:         " + found.getStudentID());
                            System.out.println("Name:       " + found.getFullName());
                            System.out.println("Year Level: " + found.getYearLevel().displayName);
                            System.out.println("Address:    " + found.getAddress());
                            System.out.println("Contact:    " + found.getContact());
                            System.out.println("Course:     " + found.getCourseApplied());
                            System.out.println("GWA:        " + found.getGrades());
                            System.out.println("Status:     " + found.getStatus());
                            System.out.println("-------------------------------------");
                        } else {
                            System.out.println("       Student not found.");
                        }
                    } else {
                        System.out.print("Enter name keyword: ");
                        String keyword = input.readLine().trim();
                        List<Student> results = service.searchByName(keyword);
                        System.out.println();
                        if (results.isEmpty()) {
                            System.out.println("    No matching students found.");
                        } else {
                            System.out.println("    Found " + results.size() + " student(s):");
                            System.out.println("-------------------------------------");
                            for (Student s : results) {
                                System.out.println(s.getStudentID() + "  |  " 
                                        + s.getFullName() + "  |  " + s.getStatus());
                            }
                            System.out.println("-------------------------------------");
                        }
                    }
                    pause(input);
                    break;

                case 4:
                    clearScreen();
                    System.out.println("=====================================");
                    System.out.println("        UPDATE STUDENT STATUS        ");
                    System.out.println("=====================================");
                    System.out.println();
                    System.out.print("Enter Student ID: ");
                    String updateId = input.readLine().trim();
                    Student studentToUpdate = service.searchByID(updateId);
                    System.out.println();
                    
                    if (studentToUpdate == null) {
                        System.out.println("       Student not found.");
                    } else {
                        System.out.println("-------------------------------------");
                        System.out.println("Student:      " + studentToUpdate.getFullName());
                        System.out.println("Current ID:   " + studentToUpdate.getStudentID());
                        System.out.println("Old Status:   " + studentToUpdate.getStatus());
                        System.out.println("-------------------------------------");
                        System.out.println();
                        System.out.println("Available statuses:");
                        System.out.println("  APPROVED  |  PENDING  |  REJECTED  |  WAITLISTED");
                        System.out.println();
                        System.out.print("Enter new status: ");
                        String newStatus = input.readLine().trim().toUpperCase();
                        boolean updated = service.updateStudentStatus(updateId, newStatus);
                        System.out.println();
                        if (updated) {
                            System.out.println("    Status updated successfully.");
                        } else {
                            System.out.println("    Update failed. Course may be at full capacity.");
                        }
                    }
                    pause(input);
                    break;

                case 5:
                    clearScreen();
                    System.out.println("=====================================");
                    System.out.println("       DELETE STUDENT RECORD         ");
                    System.out.println("=====================================");
                    System.out.println();
                    System.out.print("Enter Student ID: ");
                    String deleteId = input.readLine().trim();
                    Student studentToDelete = service.searchByID(deleteId);
                    System.out.println();
                    
                    if (studentToDelete == null) {
                        System.out.println("       Student not found.");
                    } else {
                        System.out.println("-------------------------------------");
                        System.out.println("Student: " + studentToDelete.getFullName());
                        System.out.println("Status:  " + studentToDelete.getStatus());
                        System.out.println("Course:  " + studentToDelete.getCourseApplied());
                        System.out.println("-------------------------------------");
                        System.out.println();
                        System.out.print("Are you sure you want to delete? (y/n): ");
                        String confirm = input.readLine().trim();
                        System.out.println();
                        if (confirm.equalsIgnoreCase("y")) {
                            service.deleteStudent(deleteId);
                            System.out.println("    Record deleted successfully.");
                        } else {
                            System.out.println("    Deletion cancelled.");
                        }
                    }
                    pause(input);
                    break;

                case 6:
                    clearScreen();
                    System.out.println("=====================================");
                    System.out.println("         COURSE SUMMARY              ");
                    System.out.println("=====================================");
                    System.out.println();
                    Map<String, Integer> summary = service.getCourseSummary();
                    for (Map.Entry<String, Integer> entry : summary.entrySet()) {
                        System.out.printf("%3d slots available  |  %s%n", entry.getValue(), entry.getKey());
                    }
                    pause(input);
                    break;

                case 7:
                    clearScreen();
                    System.out.println("=====================================");
                    System.out.println("        ADMISSION REPORTS            ");
                    System.out.println("=====================================");
                    System.out.println();
                    System.out.println("Total Applicants: " + service.getTotalApplicants());
                    System.out.println("-------------------------------------");
                    System.out.printf("%-12s : %d%n", "APPROVED", service.getCountByStatus("APPROVED"));
                    System.out.printf("%-12s : %d%n", "PENDING", service.getCountByStatus("PENDING"));
                    System.out.printf("%-12s : %d%n", "REJECTED", service.getCountByStatus("REJECTED"));
                    System.out.printf("%-12s : %d%n", "WAITLISTED", service.getCountByStatus("WAITLISTED"));
                    System.out.println("-------------------------------------");
                    pause(input);
                    break;

                case 8:
                    clearScreen();
                    System.out.println("=====================================");
                    System.out.println("  Thank you for using UMak System!   ");
                    System.out.println("            Goodbye.                 ");
                    System.out.println("=====================================");
                    running = false;
                    break;

                default:
                    System.out.println("\nInvalid choice. Please try again.");
                    pause(input);
                    break;
            }
        }
        input.close();
    }
}
