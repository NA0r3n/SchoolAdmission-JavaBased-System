import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class AdmissionService {

    private List<Student> students = new ArrayList<>();
    private Map<String, Integer> courseSlots = new HashMap<>();
    private Map<String, Integer> courseTakenSlots = new HashMap<>();
    private int rawIDSequence = 1;

    public AdmissionService() {
        addCourse("BSIT", 0);
        addCourse("BSCS", 100);
        addCourse("BSHM", 100);
        addCourse("BSCE", 100);
        addCourse("BSEE", 100);
        addCourse("BSME", 100);
        addCourse("BSArch", 100);
        addCourse("BPA", 100);
        addCourse("BSBA", 100);
        addCourse("BSEd", 100);
        addCourse("BEEd", 100);
        addCourse("BSN", 100);
        addCourse("BSCrim", 100);
        
        createSampleStudents();
    }
    
    public void createSampleStudents() {
        registerPreloadedStudent(
            "Nerwen Angelo C. Ogardo",
            18,
            "#78 Kalayaan Avenue Barangay West Rembo Taguig City",
            "09123456789",
            "Tanza National Comprehensive High School",
            "BSCS",
            100.0,
            LocalDate.of(2026, 1, 23)
        );
        
        registerPreloadedStudent(
            "Samantha Angela Y. Sabino",
            18,
            "Santa Rosa, Laguna",
            "09123456789",
            "Pateros Catholic School",
            "BSCS",
            100.0,
            LocalDate.of(2026, 1, 23)
        );

        registerPreloadedStudent(
            "Ysaiah Myro Siblero",
            19,
            "Taguig City",
            "09123456789",
            "MCA Montessori School",
            "BSCS",
            100.0,
            LocalDate.of(2026, 1, 23)
        );

        registerPreloadedStudent(
            "Waitlisted Student",
            67,
            "His/Her Address goes here",
            "09123456789",
            "What a school",
            "BSCS",
            100.0,
            LocalDate.of(2026, 1, 23)
        );

        registerPreloadedStudent(
            "Pending Student",
            67,
            "His/Her Address goes here",
            "09123456789",
            "What a school",
            "Made up Course",
            100.0,
            LocalDate.of(2026, 1, 23)
        );

        registerPreloadedStudent(
            "Unimaginable Grade/GWA Student",
            67,
            "His/Her Address goes here",
            "09123456789",
            "What a school",
            "BSArch",
            74.0,
            LocalDate.of(2026, 1, 23)
        );

        registerPreloadedStudent(
            "Less than average Grade/GWA Student",
            67,
            "His/Her Address goes here",
            "09123456789",
            "What a school",
            "BSArch",
            77.0,
            LocalDate.of(2026, 1, 23)
        );
    }
    
    private Student registerPreloadedStudent(
        String fullName, int age, String address, String contact,
        String prevSchool, String courseApplied, double gwa, LocalDate customDate) {
        Student justAdded = registerNewStudent(fullName, age, address, contact, prevSchool, courseApplied, gwa, true);
        if (justAdded != null) {
            justAdded.setDateAdmitted(customDate);
        }
        return justAdded;
    }

    public void addCourse(String courseName, int slots) {
        courseSlots.put(courseName, slots);
        courseTakenSlots.put(courseName, 0);
    }
    
    public List<String> getAllCourses() {
        return new ArrayList<>(courseSlots.keySet());
    }
    
    public Student registerNewStudent(
        String fullName, int age, String address, String contact,
        String prevSchool, String courseApplied, double gwa) {
        return registerNewStudent(fullName, age, address, contact, prevSchool, courseApplied, gwa, false);
    }

    private Student registerNewStudent(
        String fullName, int age, String address, String contact,
        String prevSchool, String courseApplied, double gwa, boolean isSilent) {
        
        String status;
        
        if (!courseSlots.containsKey(courseApplied)) {
            status = "PENDING";
            if (!isSilent) {
                System.out.println("Unfortunately, UMAK does not have such course.");
                System.out.printf("%n──────────────────────────────%n%s is currently: %s in UMAK!%n%nReason: UMAK does not offer the %s course yet.%n──────────────────────────────%n", fullName, status, courseApplied);
            }
        }
        else if (gwa < 75.0) {
            status = "REJECTED";
        }
        else if (gwa >= 75.0 && gwa < 80.0) {
            status = "PENDING";
            if (!isSilent) {
                System.out.printf("%n──────────────────────────────%n%s is currently: %s in UMAK!%nReason: GWA %.2f needs further review.%n──────────────────────────────%n",
                    fullName, status, gwa);
            }
        }
        else if (courseTakenSlots.get(courseApplied) >= courseSlots.get(courseApplied)) {
            status = "WAITLISTED";
            if (!isSilent) {
                System.out.printf("%n──────────────────────────────%n%s is currently: %s in UMAK!%nReason: %s is at full capacity.%n──────────────────────────────%n",
                    fullName, status, courseApplied);
            }
        }
        else {
            status = "APPROVED";
            if (!isSilent) {
                System.out.printf("%n──────────────────────────────%n%s is now officially a student in UMAK!%n──────────────────────────────%n",
                    fullName);
            }
        }

        int currentYear = LocalDate.now().getYear();
        String formattedIDSequence = String.format("%04d", rawIDSequence);
        rawIDSequence++;
        String studentID = "UMAK-" + currentYear + "-" + formattedIDSequence;
        
        Student newStudent = new Student(studentID, fullName, courseApplied);
        newStudent.setStatus(status);
        newStudent.setAge(age);
        newStudent.setAddress(address);
        newStudent.setContact(contact);
        newStudent.setPrevSchool(prevSchool);
        newStudent.setGrades(gwa);
        students.add(newStudent);
        
        if (courseSlots.containsKey(courseApplied) && status.equals("APPROVED")) {
            int currentTaken = courseTakenSlots.get(courseApplied);
            courseTakenSlots.put(courseApplied, currentTaken + 1);
        }
        
        return newStudent;
    }

    public List<Student> getAllStudents() {
        return this.students;
    }

    public Student searchByID(String id) {
        for (Student student : students) {
            if (student.getStudentID().equals(id)) {
                return student;
            }
        }
        return null;
    }

    public List<Student> searchByName(String nameKeyword) {
        List<Student> foundList = new ArrayList<>();
        for (Student student : students) {
            if (student.getFullName().toLowerCase().contains(nameKeyword.toLowerCase())) {
                foundList.add(student);
            }
        }
        return foundList;
    }

    public boolean updateStudentStatus(String id, String newStatus) {
        Student student = searchByID(id);
        if (student == null) {
            return false;
        }
        String oldStatus = student.getStatus();
        String course = student.getCourseApplied();
        if (oldStatus.equals("APPROVED") && !newStatus.equals("APPROVED")) {
            int taken = courseTakenSlots.get(course);
            courseTakenSlots.put(course, taken - 1);
        } else if (!oldStatus.equals("APPROVED") && newStatus.equals("APPROVED")) {
            int taken = courseTakenSlots.get(course);
            if (taken >= courseSlots.get(course)) {
                return false;
            }
            courseTakenSlots.put(course, taken + 1);
        }
        student.setStatus(newStatus);
        return true;
    }

    public boolean deleteStudent(String id) {
        Student student = searchByID(id);
        if (student != null) {
            if (student.getStatus().equals("APPROVED")) {
                String course = student.getCourseApplied();
                int taken = courseTakenSlots.get(course);
                courseTakenSlots.put(course, taken - 1);
            }
            students.remove(student);
            return true;
        }
        return false;
    }

    public Map<String, Integer> getCourseSummary() {
        Map<String, Integer> summary = new HashMap<>();
        for (String course : courseSlots.keySet()) {
            int available = courseSlots.get(course) - courseTakenSlots.get(course);
            summary.put(course, available);
        }
        return summary;
    }

    public long getTotalApplicants() {
        return students.size();
    }

    public long getCountByStatus(String targetStatus) {
        long count = 0;
        for (Student student : students) {
            if (student.getStatus().equals(targetStatus)) {
                count++;
            }
        }
        return count;
    }
}
