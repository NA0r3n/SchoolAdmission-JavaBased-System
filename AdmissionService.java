import java.time.LocalDate;
import java.util.*;

public class AdmissionService {
    private List<Student> students = new ArrayList<>();
    private Map<String, CourseRequirement> courseRequirements = new HashMap<>();
    private Map<String, Integer> courseSlots = new HashMap<>();
    private Map<String, Integer> courseTakenSlots = new HashMap<>();
    private int rawIDSequence = 1;
    private static final Set<String> VALID_STATUSES = Set.of("APPROVED", "PENDING", "REJECTED", "WAITLISTED");

    public AdmissionService() {
        initializeCourses();
        createSampleStudents();
        recalculateSlotCounts();
    }

    private void initializeCourses() {
        addCourse(new CourseRequirement("Bachelor of Science in Information Technology (BSIT)", College.CCIS, 80.0, 0));
        addCourse(new CourseRequirement("Bachelor of Science in Computer Science (BSCS)", College.CCIS, 83.0, 120));
        addCourse(new CourseRequirement("Bachelor of Science in Accountancy (BSA)", College.CBFS, 85.0, 80));
        addCourse(new CourseRequirement("Bachelor of Science in Management Accounting (BSMA)", College.CBFS, 85.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Business Administration (BSBA)", College.CBFS, 80.0, 150));
        addCourse(new CourseRequirement("Bachelor of Science in Entrepreneurship (BSEnt)", College.CBFS, 78.0, 80));
        addCourse(new CourseRequirement("Bachelor of Science in Office Administration (BSOA)", College.CBFS, 78.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Hospitality Management (BSHM)", College.CTHM, 78.0, 150));
        addCourse(new CourseRequirement("Bachelor of Science in Tourism Management (BSTM)", College.CTHM, 78.0, 150));
        addCourse(new CourseRequirement("Bachelor of Science in Civil Engineering (BSCE)", College.CCSE, 83.0, 80));
        addCourse(new CourseRequirement("Bachelor of Science in Construction Management (BSCM)", College.CCSE, 80.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Building and Property Management (BSBPM)", College.CCSE, 75.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Electrical Engineering Technology (BSEET)", College.CET, 80.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Electronics Engineering Technology (BSEleT)", College.CET, 80.0, 60));
        addCourse(new CourseRequirement("Bachelor in Automotive Technology (BAT)", College.CET, 75.0, 50));
        addCourse(new CourseRequirement("Bachelor of Secondary Education (BSEd)", College.CITE, 80.0, 120));
        addCourse(new CourseRequirement("Bachelor of Elementary Education (BEEd)", College.CITE, 80.0, 100));
        addCourse(new CourseRequirement("Bachelor in Early Childhood Education (BECEd)", College.CITE, 78.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Nursing (BSN)", College.CONHS, 85.0, 80));
        addCourse(new CourseRequirement("Bachelor of Science in Radiologic Technology (BSRT)", College.CONHS, 83.0, 50));
        addCourse(new CourseRequirement("Bachelor of Science in Psychology (BSPsych)", College.CONHS, 82.0, 80));
        addCourse(new CourseRequirement("Bachelor of Science in Pharmacy (BSPh)", College.CONHS, 85.0, 60));
        addCourse(new CourseRequirement("Bachelor of Public Administration (BPA)", College.CGPP, 78.0, 100));
        addCourse(new CourseRequirement("Bachelor of Arts in Political Science (BAPolSci)", College.CGPP, 80.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Social Work (BSSW)", College.CGPP, 78.0, 100));
        addCourse(new CourseRequirement("Bachelor of Science in Biology (BSBio)", College.CLAS, 82.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Mathematics (BSMath)", College.CLAS, 82.0, 40));
        addCourse(new CourseRequirement("Bachelor of Science in Fitness and Sports Management (BSFSM)", College.CHK, 78.0, 60));
        addCourse(new CourseRequirement("Bachelor of Multimedia Arts (BMA)", College.IAD, 78.0, 60));
        addCourse(new CourseRequirement("Bachelor of Arts in Communication (BAC)", College.IAD, 78.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Animation (BSAn)", College.IAD, 78.0, 50));
    }

    private void addCourse(CourseRequirement req) {
        courseRequirements.put(req.getCourseName(), req);
        courseSlots.put(req.getCourseName(), req.getSlots());
        courseTakenSlots.put(req.getCourseName(), 0);
    }

    public List<College> getAllColleges() {
        return new ArrayList<>(List.of(College.values()));
    }

    public List<String> getCoursesByCollege(College college) {
        List<String> courses = new ArrayList<>();
        for (CourseRequirement req : courseRequirements.values()) {
            if (req.getCollege() == college) {
                courses.add(req.getCourseName());
            }
        }
        return courses;
    }

    public CourseRequirement getCourseRequirement(String courseName) {
        return courseRequirements.get(courseName);
    }

    private void createSampleStudents() {
        registerPreloadedStudent("Nerwen Angelo C. Ogardo", 18, "#78 Block 4 Kalayaan Avenue Barangay West Rembo Taguig City", "09760895947", "Tanza National Comprehensive", "Bachelor of Science in Computer Science (BSCS)", 99.9, YearLevel.FIRST_YEAR, LocalDate.of(2026, 1, 23));
        registerPreloadedStudent("Samantha Angela Y. Sabino", 18, "45 Mabini Avenue, Barangay Poblacion, Santa Rosa, Laguna 4026", "09786642936", "Pateros Catholic School", "Bachelor of Science in Information Technology (BSIT)", 99.9, YearLevel.FIRST_YEAR, LocalDate.of(2026, 1, 24));
        registerPreloadedStudent("Ysaiah Myro Siblero", 19, "388 Guava Street, Brgy. Pinagsama, Wildcat, Taguig City", "09241752369", "MCA Montessori School", "Bachelor of Science in Computer Science (BSCS)", 99.9, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 23));
        registerPreloadedStudent("Martin Benito E. Viñas", 18, "F. Castillo, Project 4, Quezon City", "09310230235", "University of the East", "Bachelor of Science in Computer Science (BSCS)", 99.9, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 23));
        registerPreloadedStudent("Approved Student One", 18, "123 Makati Street, Brgy. Poblacion, Makati City", "09170000001", "Makati High School", "Bachelor of Science in Computer Science (BSCS)", 90.0, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 24));
        registerPreloadedStudent("Pending Student Two", 18, "456 Ayala Avenue, Makati City", "09170000002", "Makati Science High School", "Bachelor of Science in Accountancy (BSA)", 82.0, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 24));
        registerPreloadedStudent("Rejected Student Three", 19, "789 Rizal Avenue, Taguig City", "09170000003", "Taguig National High School", "Bachelor of Science in Information Technology (BSIT)", 74.0, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 24));
        registerPreloadedStudent("Waitlisted Student Four", 18, "321 Bonifacio Street, Quezon City", "09170000004", "Quezon City High School", "Bachelor of Science in Nursing (BSN)", 88.0, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 24));
        registerPreloadedStudent("Pending Student Five", 19, "654 Mabini Street, Pasay City", "09170000005", "Pasay City South High School", "Bachelor of Magic Arts", 92.0, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 24));
    }

    private Student registerPreloadedStudent(String fullName, int age, String address, String contact, String prevSchool, String courseApplied, double gwa, YearLevel yearLevel, LocalDate dateAdmitted) {
        Student student = createStudent(fullName, age, address, contact, prevSchool, courseApplied, gwa, yearLevel, false);
        if (student != null) {
            student.setDateAdmitted(dateAdmitted);
        }
        return student;
    }

    public Student registerNewStudent(String fullName, int age, String address, String contact, String prevSchool, String courseApplied, double gwa, YearLevel yearLevel) {
        return createStudent(fullName, age, address, contact, prevSchool, courseApplied, gwa, yearLevel, false);
    }

    // hasToFollowDocs = true when optional documents are still missing (caps the result at PENDING)
    public Student registerNewStudent(String fullName, int age, String address, String contact, String prevSchool, String courseApplied, double gwa, YearLevel yearLevel, boolean hasToFollowDocs) {
        return createStudent(fullName, age, address, contact, prevSchool, courseApplied, gwa, yearLevel, hasToFollowDocs);
    }

    // Single source of truth for the admission decision (first matching rule wins)
    public String predictStatus(String courseApplied, double gwa, boolean hasToFollowDocs) {
        CourseRequirement req = courseRequirements.get(courseApplied);
        if (req == null) {
            return "PENDING";
        } else if (gwa < 75.0) {
            return "REJECTED";
        } else if (gwa < req.getMinGWA()) {
            return "PENDING";
        } else if (courseTakenSlots.get(courseApplied) >= courseSlots.get(courseApplied)) {
            return "WAITLISTED";
        } else if (hasToFollowDocs) {
            return "PENDING";
        }
        return "APPROVED";
    }

    private Student createStudent(String fullName, int age, String address, String contact, String prevSchool,
                                  String courseApplied, double gwa, YearLevel yearLevel, boolean hasToFollowDocs) {
        String status = predictStatus(courseApplied, gwa, hasToFollowDocs);
        String studentID = null;

        if (status.equals("APPROVED")) {
            courseTakenSlots.put(courseApplied, courseTakenSlots.get(courseApplied) + 1);
            int currentYear = LocalDate.now().getYear();
            studentID = "UMAK-" + currentYear + "-" + String.format("%04d", rawIDSequence++);
        }

        Student newStudent = new Student(studentID, fullName, courseApplied);
        newStudent.setStatus(status);
        newStudent.setAge(age);
        newStudent.setAddress(address);
        newStudent.setContact(contact);
        newStudent.setPrevSchool(prevSchool);
        newStudent.setGrades(gwa);
        newStudent.setYearLevel(yearLevel);
        newStudent.setDateAdmitted(LocalDate.now());
        students.add(newStudent);
        return newStudent;
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Student searchByID(String id) {
        for (Student s : students) {
            String sid = s.getStudentID();
            if (sid != null && sid.equals(id)) {
                return s;
            }
        }
        return null;
    }

    public List<Student> searchByCourse(String keyword) {
        List<Student> results = new ArrayList<>();
        for (Student s : students) {
            if (s.getCourseApplied().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(s);
            }
        }
        return results;
    }

    public List<Student> searchByName(String keyword) {
        List<Student> results = new ArrayList<>();
        for (Student s : students) {
            if (s.getFullName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(s);
            }
        }
        return results;
    }

    // For students WITH ID
    public boolean updateStudentStatus(String id, String newStatus) {
        Student student = searchByID(id);
        if (student == null) {
            return false;
        }
        return updateStudentStatus(student, newStatus);
    }

    // Works for ANY student, no ID needed
    public boolean updateStudentStatus(Student student, String newStatus) {
        if (student == null) {
            return false;
        }

        if (newStatus == null || !VALID_STATUSES.contains(newStatus)) {
            return false;
        }

        String oldStatus = student.getStatus();
        String course = student.getCourseApplied();
        CourseRequirement req = courseRequirements.get(course);

        // Can't approve if course doesn't exist or is full
        if (!oldStatus.equals("APPROVED") && newStatus.equals("APPROVED")) {
            if (req == null) {
                return false;
            }
            if (courseTakenSlots.get(course) >= courseSlots.get(course)) {
                return false;
            }
            courseTakenSlots.put(course, courseTakenSlots.get(course) + 1);

            // Assign ID now if they're being approved
            if (student.getStudentID() == null) {
                int currentYear = LocalDate.now().getYear();
                String newId = "UMAK-" + currentYear + "-" + String.format("%04d", rawIDSequence++);
                student.setStudentID(newId);
            }
        }
        // If losing approval — free up the slot
        else if (oldStatus.equals("APPROVED") && !newStatus.equals("APPROVED")) {
            if (courseTakenSlots.containsKey(course)) {
                courseTakenSlots.put(course, Math.max(0, courseTakenSlots.get(course) - 1));
            }
        }

        student.setStatus(newStatus);
        return true;
    }

    public boolean deleteStudent(String id) {
        return deleteStudent(searchByID(id));
    }

    // Works for ANY student, no ID needed
    public boolean deleteStudent(Student student) {
        if (student == null || !students.remove(student)) {
            return false;
        }
        String course = student.getCourseApplied();
        if (student.getStatus().equals("APPROVED") && courseTakenSlots.containsKey(course)) {
            courseTakenSlots.put(course, Math.max(0, courseTakenSlots.get(course) - 1));
        }
        return true;
    }

    public Map<String, Integer> getCourseSummary() {
        Map<String, Integer> summary = new HashMap<>();
        for (String course : courseSlots.keySet()) {
            summary.put(course, courseSlots.get(course) - courseTakenSlots.get(course));
        }
        return summary;
    }

    public long getTotalApplicants() {
        return students.size();
    }

    public long getCountByStatus(String status) {
        long count = 0;
        for (Student s : students) {
            if (s.getStatus().equals(status)) {
                count++;
            }
        }
        return count;
    }

    public void generateExpandedReport() {
        List<Student> studentList = getAllStudents();
        int total = studentList.size();
        int approved = 0, pending = 0, rejected = 0, waitlisted = 0;
        Map<String, Integer> courseCount = new HashMap<>();

        for (Student s : studentList) {
            String status = s.getStatus().toUpperCase();
            switch (status) {
                case "APPROVED":
                    approved++;
                    courseCount.put(s.getCourseApplied(), courseCount.getOrDefault(s.getCourseApplied(), 0) + 1);
                    break;
                case "PENDING":
                    pending++;
                    break;
                case "REJECTED":
                    rejected++;
                    break;
                case "WAITLISTED":
                    waitlisted++;
                    break;
            }
        }

        double yieldRate = (total == 0) ? 0 : ((double) approved / total) * 100;

        System.out.println("Total Applicants : " + total);
        System.out.printf("Acceptance Rate  : %.2f%%%n", yieldRate);
        System.out.println("-------------------------------------");
        System.out.printf("%-12s : %d%n", "APPROVED", approved);
        System.out.printf("%-12s : %d%n", "PENDING", pending);
        System.out.printf("%-12s : %d%n", "REJECTED", rejected);
        System.out.printf("%-12s : %d%n", "WAITLISTED", waitlisted);
        System.out.println("-------------------------------------");
        System.out.println("Approved Breakdown by Course:");

        if (courseCount.isEmpty()) {
            System.out.println(" - No approved students yet.");
        } else {
            for (Map.Entry<String, Integer> entry : courseCount.entrySet()) {
                System.out.println(" - " + entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("-------------------------------------");
    }

    private void recalculateSlotCounts() {
        for (String course : courseSlots.keySet()) {
            courseTakenSlots.put(course, 0);
        }
        for (Student s : students) {
            if (s.getStatus().equals("APPROVED")) {
                String course = s.getCourseApplied();
                if (courseTakenSlots.containsKey(course)) {
                    courseTakenSlots.put(course, courseTakenSlots.get(course) + 1);
                }
            }
        }
    }
}
