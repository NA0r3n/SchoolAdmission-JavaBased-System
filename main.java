import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class main {
    public static boolean isValidName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return false;
        }
        return fullName.trim().matches("^[a-zA-Z\\s'-]+$");
    }
    
    public static boolean isValidContact(String contact) {
         if (contact == null || contact.isBlank()) {
             return false;
         }
         return contact.trim().matches("^09\\d{9}$");
    }
    
    public static String capitalizeName(String text) {
        if (text == null || text.isBlank()) return text;
        
        String[] words = text.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (!word.isBlank()) {
                String capitalized = word.substring(0, 1).toUpperCase() 
                                   + word.substring(1).toLowerCase();
                result.append(capitalized).append(" ");
            }
        }
        
        return result.toString().trim();
    }

    public static void pause(BufferedReader input) throws Exception {
        System.out.print("\nPress Enter to continue...");
        input.readLine();
    }
    
    public static void clearSystem(){
        System.out.print("\u001B[H\u001B[2J");
        System.out.flush();
    }

    public static void main(String[] args) throws Exception {
        long startSystem = System.nanoTime();
        AdmissionService service = new AdmissionService();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        
        boolean running = true;
        
        while (running) {
            clearSystem();
            System.out.printf("%n%35s%n", "=== University Of Makati ===");
            System.out.printf("%30s%n%n", "Admission System");

            System.out.println("   [1] Add Student Record");
            System.out.println("   [2] View All Students");
            System.out.println("   [3] Search Student");
            System.out.println("   [4] Update Student Status");
            System.out.println("   [5] Delete Student Record");
            System.out.println("   [6] Course Summary");
            System.out.println("   [7] Admission Reports");
            System.out.println("   [8] Exit");
            System.out.print("\nEnter choice: ");
            
            int choice;
            try {
                choice = Integer.parseInt(input.readLine());
            } catch (NumberFormatException e) {
                System.out.println("\nPlease enter a valid number.");
                pause(input);
                continue;
            }
            
            switch (choice) {
                case 1:
                    clearSystem();
                    System.out.println("\n--- New Student Admission ---");
                    
                    String firstName = "";
                    while (true) {
                        System.out.print("Enter First Name: ");
                        firstName = input.readLine();
                        if (isValidName(firstName)) break;
                        System.out.println("Invalid name! No digits or symbols allowed.\n");
                    }
                    clearSystem();
                    String middleName = "";
                    while (true) {
                        System.out.print("Enter Middle Name: ");
                        middleName = input.readLine();
                        if (isValidName(middleName)) break;
                        System.out.println("Invalid name! No digits or symbols allowed.\n");
                    }
                    clearSystem();
                    String lastName = "";
                    while (true) {
                        System.out.print("Enter Last Name: ");
                        lastName = input.readLine();
                        if (isValidName(lastName)) break;
                        System.out.println("Invalid name! No digits or symbols allowed.\n");
                    }

                    String fullName = capitalizeName(firstName) + " "
                                    + capitalizeName(middleName) + " "
                                    + capitalizeName(lastName);
                    clearSystem();
                    int age = 0;
                    while (true) {
                        try {
                            System.out.print("\nEnter Age: ");
                            age = Integer.parseInt(input.readLine());
                            if (age > 0) break;
                            System.out.println("Age must be positive.");
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    }
                    clearSystem();
                    System.out.print("\nEnter Address: ");
                    String address = input.readLine();
                    clearSystem();
                    
                    String contact = "";
                     while (true) {
                         System.out.print("\nEnter Contact No (11 digits, Ex: '09...''): ");
                         contact = input.readLine().trim();
                         if (isValidContact(contact)) break;
                         System.out.println("Invalid contact! Must be exactly 11 digits. starting with '09...'\n");
                     }
                    clearSystem();
                    System.out.print("\nEnter Previous School: ");
                    String prevSchool = input.readLine();
                    clearSystem();
                    
                    System.out.println("\n--- Available Courses ---");
                    List<String> courseList = service.getAllCourses();
                    for (int i = 0; i < courseList.size(); i++) {
                        System.out.printf("   [%d] %s%n", i + 1, courseList.get(i));
                    }
                    
                    int courseChoice;
                    String courseApplied = "";
                    while (true) {
                        System.out.print("\nEnter course number: ");
                        try {
                            courseChoice = Integer.parseInt(input.readLine());
                            if (courseChoice >= 1 && courseChoice <= courseList.size()) {
                                courseApplied = courseList.get(courseChoice - 1);
                                break;
                            } else {
                                System.out.println("Please enter a valid course number.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a number.");
                        }
                    }
                    
                    clearSystem();
                    double gwa = 0.0;
                    while (true) {
                        try {
                            System.out.print("\nEnter GWA (0.0 - 100.0): ");
                            gwa = Double.parseDouble(input.readLine());
                            if (gwa >= 0.0 && gwa <= 100.0) break;
                            System.out.println("GWA must be between 0 and 100.");
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    }
                    clearSystem();
                    Student savedStudent = service.registerNewStudent(
                        fullName, age, address, contact, prevSchool, courseApplied, gwa
                    );
                    
                    if (savedStudent != null) {
                        String message = String.format("""
            %nRecord Added Successfully:
            ──────────────────────────────
              Name:   %s
              Course: %s
              GWA:    %.2f
              Status: %s
              Date:   %s
              ID:     %s
            ──────────────────────────────""",
            savedStudent.getFullName(),
            savedStudent.getCourseApplied(),
            savedStudent.getGrades(),
            savedStudent.getStatus(),
            savedStudent.getDateAdmitted(),
            savedStudent.getStudentID());
                        clearSystem();
                        System.out.println(message);
                    }
                    pause(input);
                    break;
                    
                case 2:
                    clearSystem();
                    System.out.println("\n----- All Students ------");
                    System.out.println("═══════════════════════════════");
                     
                    List<Student> allStudents = service.getAllStudents();
                    if (allStudents.isEmpty()) {
                        System.out.println("   No students registered yet.");
                    } else {
                        for (Student s : allStudents) {
                            System.out.printf("   ID:      %s%n", s.getStudentID());
                            System.out.printf("   Name:    %s%n", s.getFullName());
                            System.out.printf("   Course:  %s%n", s.getCourseApplied());
                            System.out.printf("   Status:  %s%n", s.getStatus());
                            System.out.printf("   Date:    %s%n", s.getDateAdmitted());
                            System.out.println("   ───────────────────────────");
                        }
                    }
                    System.out.println("═══════════════════════════════");
                    pause(input);
                    break;
               
                case 3:
                    clearSystem();
                    System.out.println("\n--- Search Student ---");
                    System.out.print("Enter Student ID to search: ");
                    String searchId = input.readLine();
                    Student studentFound = service.searchByID(searchId);
                    if (studentFound != null) {
                        System.out.printf("""
    %nStudent Record Found:
    ──────────────────────────────
      Name:   %s
      Course: %s
      GWA:    %.2f
      Date:   %s
      Status: %s
      ID:     %s
    ──────────────────────────────""",
    studentFound.getFullName(),
    studentFound.getCourseApplied(),
    studentFound.getGrades(),
    studentFound.getDateAdmitted(),
    studentFound.getStatus(),
    studentFound.getStudentID());
                    } else {
                        System.out.println("\nStudent not found.");
                    }
                    pause(input);
                    break;
                
                case 4:
                    clearSystem();
                    System.out.println("\n--- Update Student Status ---");
                    System.out.print("Enter Existing Student ID: ");
                    String updateId = input.readLine();
                    
                    System.out.print("Enter new status: ");
                    String newStatus = input.readLine();
                    
                    boolean updated = service.updateStudentStatus(updateId, newStatus);
                    if (updated) {
                        Student updatedStudent = service.searchByID(updateId);
                        System.out.println("\nSuccessfully updated.");
                        System.out.println("   " + updatedStudent.getFullName());
                        System.out.println("   " + updatedStudent.getStudentID() + " - Status: " + updatedStudent.getStatus());
                    } else {
                        System.out.println("\nStudent not found.");
                    }
                    pause(input);
                    break;
                
                case 5:
                    clearSystem();
                    System.out.println("\n--- Delete Student Record ---");
                    System.out.print("Enter Existing Student ID: ");
                    String deleteId = input.readLine();
                    
                    Student studentToDelete = service.searchByID(deleteId);
                    if (studentToDelete != null) {
                        System.out.print("Are you sure you want to delete " + 
                                         studentToDelete.getFullName() + "? (y/n): ");
                        String confirm = input.readLine();
                        if (confirm.equalsIgnoreCase("y")) {
                            boolean deleted = service.deleteStudent(deleteId);
                            if (deleted) {
                                System.out.println("Successfully deleted.");
                            }
                        } else {
                            System.out.println("Deletion cancelled.");
                        }
                    } else {
                        System.out.println("\nStudent not found.");
                    }
                    pause(input);
                    break;
                
                case 6:
                    clearSystem();
                    System.out.println("\n--- Course Summary ---");
                    Map<String, Integer> summary = service.getCourseSummary();
                    for (Map.Entry<String, Integer> entry : summary.entrySet()) {
                        System.out.println("   " + entry.getKey() + " : " + entry.getValue() + " slots available");
                    }
                    pause(input);
                    break;
                
                case 7:
                    clearSystem();
                    System.out.println("\n--- Admission Reports ---");
                    System.out.println("   Total Applicants: " + service.getTotalApplicants());
                    System.out.println("   APPROVED:   " + service.getCountByStatus("APPROVED"));
                    System.out.println("   REJECTED:   " + service.getCountByStatus("REJECTED"));
                    System.out.println("   WAITLISTED: " + service.getCountByStatus("WAITLISTED"));
                    System.out.println("   PENDING:    " + service.getCountByStatus("PENDING"));
                    pause(input);
                    break;
                
                case 8:
                    System.out.println("\n\nExiting system. Goodbye!");
                    long exitTime = System.nanoTime();
        
                    long timeInMS = (exitTime - startSystem) / 1_000_000;
                    double timeInSec = timeInMS / 1000.0;
                 
                    System.out.printf("%n──────────────────────────────────────");
                    System.out.printf("%nSession Duration: %d ms (%.3f seconds)", timeInMS, timeInSec);
                    System.out.printf("%n──────────────────────────────────────%n");
                    pause(input);                   
                    running = false;
                    break;
                     
                default:
                    System.out.println("\nInvalid choice! Please try again.");
                    pause(input);
                    break;
            }           
        }
    }
}
