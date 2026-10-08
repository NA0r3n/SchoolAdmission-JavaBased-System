import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        AdmissionService service = new AdmissionService();
        Scanner input = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            System.out.println("====== University Of Makati Admission System ======");
            System.out.println("[1] New Student Admission");
            System.out.println("[2] View All Students");
            System.out.println("[3] Search Student");
            System.out.println("[4] Update Student Status");
            System.out.println("[5] Delete Student Record");
            System.out.println("[6] Course Summary");
            System.out.println("[7] Admission Reports");
            System.out.println("[8] Exit");
            System.out.print("Enter choice: ");
            
            int choice;
            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.\n");
                continue;
            }
            
            switch (choice) {
                case 1:
                    System.out.println("\n--- New Student Admission ---");
                    System.out.print("Enter Full Name: ");
                    String fullName = input.nextLine();
                    
                    int age = 0;
                    while (true) {
                        try {
                            System.out.print("Enter Age: ");
                            age = Integer.parseInt(input.nextLine());
                            if (age > 0) break;
                            System.out.println("❌ Age must be positive.");
                        } catch (NumberFormatException e) {
                            System.out.println("❌ Please enter a valid number.");
                        }
                    }
                    
                    System.out.print("Enter Address: ");
                    String address = input.nextLine();
                    
                    System.out.print("Enter Contact No: ");
                    String contact = input.nextLine();
                    
                    System.out.print("Enter Previous School: ");
                    String prevSchool = input.nextLine();
                    
                    System.out.print("Enter chosen course: ");
                    String courseApplied = input.nextLine();
                    
                    double gwa = 0.0;
                    while (true) {
                        try {
                            System.out.print("Enter GWA (0.0 - 100.0): ");
                            gwa = Double.parseDouble(input.nextLine());
                            if (gwa >= 0.0 && gwa <= 100.0) break;
                            System.out.println("❌ GWA must be between 0 and 100.");
                        } catch (NumberFormatException e) {
                            System.out.println("❌ Please enter a valid number.");
                        }
                    }
                    
                    String results = service.registerNewStudent(
                        fullName, age, address, contact, prevSchool, courseApplied, gwa
                    );
                    System.out.println(results + "\n");
                    break;
                
                case 2:
                    System.out.println("\n------ All Students ------");
                    List<Student> allStudents = service.getAllStudents();
                    if (allStudents.isEmpty()) {
                        System.out.println("No students registered yet.\n");
                    } else {
                        for (Student s : allStudents) {
                            System.out.println(s.getStudentID() + " | " + 
                                               s.getFullName() + " | " + 
                                               s.getCourseApplied() + " | Status: " + 
                                               s.getStatus());
                        }
                        System.out.println();
                    }
                    break;
                
                case 3:
                    System.out.println("\n--- Search Student ---");
                    System.out.print("Enter Student ID to search: ");
                    String searchId = input.nextLine();
                    Student found = service.searchByID(searchId);
                    if (found != null) {
                        System.out.println("Found: " + found.getFullName());
                        System.out.println("ID: " + found.getStudentID());
                        System.out.println("Course: " + found.getCourseApplied());
                        System.out.println("Status: " + found.getStatus() + "\n");
                    } else {
                        System.out.println("❌ Student not found.\n");
                    }
                    break;
                
                case 4:
                    System.out.println("\n--- Update Student Status ---");
                    System.out.print("Enter Existing Student ID: ");
                    String updateId = input.nextLine();
                    
                    System.out.print("Enter new status: ");
                    String newStatus = input.nextLine();
                    
                    boolean updated = service.updateStudentStatus(updateId, newStatus);
                    if (updated) {
                        Student updatedStudent = service.searchByID(updateId);
                        System.out.println("✅ Successfully updated.");
                        System.out.println(updatedStudent.getFullName());
                        System.out.println(updatedStudent.getStudentID() + " - Status: " + 
                                           updatedStudent.getStatus() + "\n");
                    } else {
                        System.out.println("❌ Student not found.\n");
                    }
                    break;
                
                case 5:
                    System.out.println("\n--- Delete Student Record ---");
                    System.out.print("Enter Existing Student ID: ");
                    String deleteId = input.nextLine();
                    
                    Student studentToDelete = service.searchByID(deleteId);
                    if (studentToDelete != null) {
                        System.out.print("Are you sure you want to delete " + 
                                         studentToDelete.getFullName() + "? (y/n): ");
                        String confirm = input.nextLine();
                        if (confirm.equalsIgnoreCase("y")) {
                            boolean deleted = service.deleteStudent(deleteId);
                            if (deleted) {
                                System.out.println("✅ Successfully deleted.\n");
                            }
                        } else {
                            System.out.println("Deletion cancelled.\n");
                        }
                    } else {
                        System.out.println("❌ Student not found.\n");
                    }
                    break;
                
                case 6:
                    System.out.println("\n--- Course Summary ---");
                    Map<String, Integer> summary = service.getCourseSummary();
                    for (Map.Entry<String, Integer> entry : summary.entrySet()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue() + " slots available");
                    }
                    System.out.println();
                    break;
                
                case 7:
                    System.out.println("\n--- Admission Reports ---");
                    System.out.println("Total Applicants: " + service.getTotalApplicants());
                    System.out.println("APPROVED: " + service.getCountByStatus("APPROVED"));
                    System.out.println("REJECTED: " + service.getCountByStatus("REJECTED"));
                    System.out.println("PENDING: " + service.getCountByStatus("PENDING"));
                    System.out.println("WAITLISTED: " + service.getCountByStatus("WAITLISTED"));
                    System.out.println();
                    break;
                
                case 8:
                    System.out.println("\nExiting system. Goodbye!");
                    running = false;
                    break;
                
                default:
                    System.out.println("❌ Invalid choice! Please try again.\n");
            }
        }
        input.close();
    }
}
