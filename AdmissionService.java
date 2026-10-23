import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

public class AdmissionService {
    private List<Student> students = new ArrayList<>();
    private Map<String, CourseRequirement> courseRequirements = new HashMap<>();
    private Map<String, Integer> courseSlots = new HashMap<>();
    private Map<String, Integer> courseTakenSlots = new HashMap<>();
    private int rawIDSequence = 1;

    public AdmissionService() {
        initializeCourses();
        createSampleStudents();
    }

    private void initializeCourses() {
        // College of Computing and Information Sciences - CCIS
        addCourse(new CourseRequirement("Bachelor of Science in Information Technology (BSIT)", College.CCIS, 80.0, 150));
        addCourse(new CourseRequirement("Bachelor of Science in Computer Science (BSCS)", College.CCIS, 83.0, 120));

        // College of Business and Financial Science - CBFS
        addCourse(new CourseRequirement("Bachelor of Science in Accountancy (BSA)", College.CBFS, 85.0, 80));
        addCourse(new CourseRequirement("Bachelor of Science in Management Accounting (BSMA)", College.CBFS, 85.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Business Administration (BSBA)", College.CBFS, 80.0, 150));
        addCourse(new CourseRequirement("Bachelor of Science in Entrepreneurship (BSEnt)", College.CBFS, 78.0, 80));
        addCourse(new CourseRequirement("Bachelor of Science in Office Administration (BSOA)", College.CBFS, 78.0, 60));

        // College of Tourism and Hospitality Management - CTHM
        addCourse(new CourseRequirement("Bachelor of Science in Hospitality Management (BSHM)", College.CTHM, 78.0, 150));
        addCourse(new CourseRequirement("Bachelor of Science in Tourism Management (BSTM)", College.CTHM, 78.0, 150));

        // College of Construction Sciences and Engineering - CCSE
        addCourse(new CourseRequirement("Bachelor of Science in Civil Engineering (BSCE)", College.CCSE, 83.0, 80));
        addCourse(new CourseRequirement("Bachelor of Science in Construction Management (BSCM)", College.CCSE, 80.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Building and Property Management (BSBPM)", College.CCSE, 75.0, 60));

        // College of Engineering Technology - CET
        addCourse(new CourseRequirement("Bachelor of Science in Electrical Engineering Technology (BSEET)", College.CET, 80.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Electronics Engineering Technology (BSEleT)", College.CET, 80.0, 60));
        addCourse(new CourseRequirement("Bachelor in Automotive Technology (BAT)", College.CET, 75.0, 50));

        // College of Innovative Teacher Education - CITE
        addCourse(new CourseRequirement("Bachelor of Secondary Education (BSEd)", College.CITE, 80.0, 120));
        addCourse(new CourseRequirement("Bachelor of Elementary Education (BEEd)", College.CITE, 80.0, 100));
        addCourse(new CourseRequirement("Bachelor in Early Childhood Education (BECEd)", College.CITE, 78.0, 60));

        // College of Nursing and Health Sciences - CONHS
        addCourse(new CourseRequirement("Bachelor of Science in Nursing (BSN)", College.CONHS, 85.0, 80));
        addCourse(new CourseRequirement("Bachelor of Science in Radiologic Technology (BSRT)", College.CONHS, 83.0, 50));
        addCourse(new CourseRequirement("Bachelor of Science in Psychology (BSPsych)", College.CONHS, 82.0, 80));
        addCourse(new CourseRequirement("Bachelor of Science in Pharmacy (BSPh)", College.CONHS, 85.0, 60));

        // College of Governance and Public Policy - CGPP
        addCourse(new CourseRequirement("Bachelor of Public Administration (BPA)", College.CGPP, 78.0, 100));
        addCourse(new CourseRequirement("Bachelor of Arts in Political Science (BAPolSci)", College.CGPP, 80.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Social Work (BSSW)", College.CGPP, 78.0, 60));

        // College of Liberal Arts and Sciences - CLAS
        addCourse(new CourseRequirement("Bachelor of Science in Biology (BSBio)", College.CLAS, 82.0, 60));
        addCourse(new CourseRequirement("Bachelor of Science in Mathematics (BSMath)", College.CLAS, 82.0, 40));

        // College of Human Kinetics - CHK
        addCourse(new CourseRequirement("Bachelor of Science in Fitness and Sports Management (BSFSM)", College.CHK, 78.0, 60));

        // Institute of Arts and Design - IAD
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
        List<String> result = new ArrayList<>();
        for (CourseRequirement req : courseRequirements.values()) {
            if (req.getCollege() == college) {
                result.add(req.getCourseName());
            }
        }
        return result;
    }

    public CourseRequirement getCourseRequirement(String courseName) {
        return courseRequirements.get(courseName);
    }

    private void createSampleStudents() {
        registerPreloadedStudent(
          "Nerwen Angelo C. Ogardo", 
          18, 
          "#78 Block 4 Kalayaan Avenue Barangay West Rembo Taguig City",
          "09760895947", 
          "Tanza National Comprehensive ", 
          "Bachelor of Science in Computer Science (BSCS)",
          99.9, 
          YearLevel.FIRST_YEAR, 
          LocalDate.of(2026, 1, 23));
        registerPreloadedStudent(
          "Samantha Angela Y. Sabino", 
          18, 
          "45 Mabini Avenue, Barangay Poblacion, Santa Rosa, Laguna 4026",
          "09786642936",
          "Pateros Catholic School", "Bachelor of Science in Information Technology (BSIT)",
          99.9, 
          YearLevel.FIRST_YEAR, 
          LocalDate.of(2026, 1, 24));
        registerPreloadedStudent(
          "Ysaiah Myro Siblero",
          19,
          "388 Guava Street, Brgy. Pinagsama, Wildcat, Taguig City",
          "09241752369",
          "MCA Montessori School",
          "Bachelor of Science in Computer Science (BSCS)",
          99.9,
          YearLevel.FIRST_YEAR,
          LocalDate.of(2026, 10, 23)
        );
        registerPreloadedStudent(
          "Martin Benito E. Viñas",
          18,
          "F. Castillo, Project 4, Quezon City",
          "09310230235",
          "University of the East",
          "Bachelor of Science in Computer Science (BSCS)",
          99.9,
          YearLevel.FIRST_YEAR,
          LocalDate.of(2026, 10, 23)
        );
        // =====================================
        // TEST STUDENTS - One for each status
        // =====================================
        
        // STATUS: APPROVED
        registerPreloadedStudent(
            "Approved Student One",
            18,
            "123 Makati Street, Brgy. Poblacion, Makati City",
            "09170000001",
            "Makati High School",
            "Bachelor of Science in Computer Science (BSCS)",
            90.0,
            YearLevel.FIRST_YEAR,
            LocalDate.of(2026, 10, 24)
        );

        // STATUS: PENDING - GWA below course minimum
        registerPreloadedStudent(
            "Pending Student Two",
            18,
            "456 Ayala Avenue, Makati City",
            "09170000002",
            "Makati Science High School",
            "Bachelor of Science in Accountancy (BSA)",
            82.0,
            YearLevel.FIRST_YEAR,
            LocalDate.of(2026, 10, 24)
        );

        // STATUS: REJECTED - GWA below 75
        registerPreloadedStudent(
            "Rejected Student Three",
            19,
            "789 Rizal Avenue, Taguig City",
            "09170000003",
            "Taguig National High School",
            "Bachelor of Science in Information Technology (BSIT)",
            74.0,
            YearLevel.FIRST_YEAR,
            LocalDate.of(2026, 10, 24)
        );

        // STATUS: WAITLISTED - GWA good but no slots left
        registerPreloadedStudent(
            "Waitlisted Student Four",
            18,
            "321 Bonifacio Street, Quezon City",
            "09170000004",
            "Quezon City High School",
            "Bachelor of Science in Nursing (BSN)",
            88.0,
            YearLevel.FIRST_YEAR,
            LocalDate.of(2026, 10, 24)
        );

        // STATUS: PENDING - Course not found/invalid
        registerPreloadedStudent(
            "Pending Student Five",
            19,
            "654 Mabini Street, Pasay City",
            "09170000005",
            "Pasay City South High School",
            "Bachelor of Magic Arts",
            92.0,
            YearLevel.FIRST_YEAR,
            LocalDate.of(2026, 10, 24)
        );
    }

    private Student registerPreloadedStudent(String fullName, int age, String address, String contact,
            String prevSchool, String courseApplied, double gwa, YearLevel yearLevel, LocalDate date) {
        Student student = registerNewStudent(fullName, age, address, contact, prevSchool, courseApplied, gwa, yearLevel, true);
        if (student != null) {
            student.setDateAdmitted(date);
        }
        return student;
    }

    public Student registerNewStudent(String fullName, int age, String address, String contact,
            String prevSchool, String courseApplied, double gwa, YearLevel yearLevel) {
        return registerNewStudent(fullName, age, address, contact, prevSchool, courseApplied, gwa, yearLevel, false);
    }

    private Student registerNewStudent(String fullName, int age, String address, String contact,
            String prevSchool, String courseApplied, double gwa, YearLevel yearLevel, boolean isSilent) {

        String status;
        CourseRequirement req = courseRequirements.get(courseApplied);

        if (req == null) {
            status = "PENDING";
            if (!isSilent) {
                System.out.println("UMak does not offer this course. Status set to PENDING.");
            }
        } else if (gwa < 75.0) {
            status = "REJECTED";
            if (!isSilent) {
                System.out.println("GWA " + gwa + " is below the minimum admission requirement of 75.0. REJECTED.");
            }
        } else if (gwa < req.getMinGWA()) {
            status = "PENDING";
            if (!isSilent) {
                System.out.println("GWA " + gwa + " is below the course minimum of " + req.getMinGWA() + ". Needs review. PENDING.");
            }
        } else if (courseTakenSlots.get(courseApplied) >= courseSlots.get(courseApplied)) {
            status = "WAITLISTED";
            if (!isSilent) {
                System.out.println("No available slots for " + courseApplied + ". WAITLISTED.");
            }
        } else {
            status = "APPROVED";
            if (!isSilent) {
                System.out.println("Admission APPROVED. All requirements met.");
            }
            courseTakenSlots.put(courseApplied, courseTakenSlots.get(courseApplied) + 1);
        }

        int currentYear = LocalDate.now().getYear();
        String studentID = "UMAK-" + currentYear + "-" + String.format("%04d", rawIDSequence++);

        Student newStudent = new Student(studentID, fullName, courseApplied);
        newStudent.setStatus(status);
        newStudent.setAge(age);
        newStudent.setAddress(address);
        newStudent.setContact(contact);
        newStudent.setPrevSchool(prevSchool);
        newStudent.setGrades(gwa);
        newStudent.setYearLevel(yearLevel);
        students.add(newStudent);

        return newStudent;
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Student searchByID(String id) {
        for (Student s : students) {
            if (s.getStudentID().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public List<Student> searchByName(String keyword) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getFullName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(s);
            }
        }
        return result;
    }

    public boolean updateStudentStatus(String id, String newStatus) {
        Student s = searchByID(id);
        if (s == null) {
            return false;
        }
        String oldStatus = s.getStatus();
        String course = s.getCourseApplied();

        if (oldStatus.equals("APPROVED") && !newStatus.equals("APPROVED")) {
            courseTakenSlots.put(course, courseTakenSlots.get(course) - 1);
        } else if (!oldStatus.equals("APPROVED") && newStatus.equals("APPROVED")) {
            if (courseTakenSlots.get(course) >= courseSlots.get(course)) {
                return false;
            }
            courseTakenSlots.put(course, courseTakenSlots.get(course) + 1);
        }
        s.setStatus(newStatus);
        return true;
    }

    public boolean deleteStudent(String id) {
        Student s = searchByID(id);
        if (s != null) {
            if (s.getStatus().equals("APPROVED")) {
                courseTakenSlots.put(s.getCourseApplied(), courseTakenSlots.get(s.getCourseApplied()) - 1);
            }
            students.remove(s);
            return true;
        }
        return false;
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
}
