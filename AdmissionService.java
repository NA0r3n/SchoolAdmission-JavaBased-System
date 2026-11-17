import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class AdmissionService {
    private List<Student> students = new ArrayList<>();
    private Map<String, CourseRequirement> courseRequirements = new HashMap<>();
    private Map<String, Integer> courseSlots = new HashMap<>();
    private Map<String, Integer> courseTakenSlots = new HashMap<>();
    private int rawIDSequence = 1;
    private static final String STUDENT_CSV = "students.csv";

    public AdmissionService() {
        initializeCourses();
        loadStudentsFromCSV();
        if (students.isEmpty()) {
            createSampleStudents();
        }
        recalculateSlotCounts();
    }

    private void initializeCourses() {
        addCourse(new CourseRequirement("Bachelor of Science in Information Technology (BSIT)", College.CCIS, 80.0, 150));
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
        registerPreloadedStudent("Nerwen Angelo C. Ogardo", 18, "#78 Block 4 Kalayaan Avenue Barangay West Rembo Taguig City", "09760895947", "Tanza National Comprehensive ", "Bachelor of Science in Computer Science (BSCS)", 99.9, YearLevel.FIRST_YEAR, LocalDate.of(2026, 1, 23));
        registerPreloadedStudent("Samantha Angela Y. Sabino", 18, "45 Mabini Avenue, Barangay Poblacion, Santa Rosa, Laguna 4026", "09786642936", "Pateros Catholic School", "Bachelor of Science in Information Technology (BSIT)", 99.9, YearLevel.FIRST_YEAR, LocalDate.of(2026, 1, 24));
        registerPreloadedStudent("Ysaiah Myro Siblero", 19, "388 Guava Street, Brgy. Pinagsama, Wildcat, Taguig City", "09241752369", "MCA Montessori School", "Bachelor of Science in Computer Science (BSCS)", 99.9, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 23));
        registerPreloadedStudent("Martin Benito E. Viñas", 18, "F. Castillo, Project 4, Quezon City", "09310230235", "University of the East", "Bachelor of Science in Computer Science (BSCS)", 99.9, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 23));
        registerPreloadedStudent("Approved Student One", 18, "123 Makati Street, Brgy. Poblacion, Makati City", "09170000001", "Makati High School", "Bachelor of Science in Computer Science (BSCS)", 90.0, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 24));
        registerPreloadedStudent("Pending Student Two", 18, "456 Ayala Avenue, Makati City", "09170000002", "Makati Science High School", "Bachelor of Science in Accountancy (BSA)", 82.0, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 24));
        registerPreloadedStudent("Rejected Student Three", 19, "789 Rizal Avenue, Taguig City", "09170000003", "Taguig National High School", "Bachelor of Science in Information Technology (BSIT)", 74.0, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 24));
        registerPreloadedStudent("Waitlisted Student Four", 18, "321 Bonifacio Street, Quezon City", "09170000004", "Quezon City High School", "Bachelor of Science in Nursing (BSN)", 88.0, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 24));
        registerPreloadedStudent("Pending Student Five", 19, "654 Mabini Street, Pasay City", "09170000005", "Pasay City South High School", "Bachelor of Magic Arts", 92.0, YearLevel.FIRST_YEAR, LocalDate.of(2026, 10, 24));
        saveStudentsToCSV();
    }

    private Student registerPreloadedStudent(String fullName, int age, String address, String contact, String prevSchool, String courseApplied, double gwa, YearLevel yearLevel, LocalDate dateAdmitted) {
        Student student = registerNewStudent(fullName, age, address, contact, prevSchool, courseApplied, gwa, yearLevel, true);
        if (student != null) {
            student.setDateAdmitted(dateAdmitted);
        }
        return student;
    }

    public Student registerNewStudent(String fullName, int age, String address, String contact, String prevSchool, String courseApplied, double gwa, YearLevel yearLevel) {
        return registerNewStudent(fullName, age, address, contact, prevSchool, courseApplied, gwa, yearLevel, false);
    }

    private Student registerNewStudent(String fullName, int age, String address, String contact, String prevSchool, String courseApplied, double gwa, YearLevel yearLevel, boolean isPreloaded) {
        CourseRequirement req = courseRequirements.get(courseApplied);
        String status;

        if (req == null) {
            status = "PENDING";
        } else if (gwa < 75.0) {
            status = "REJECTED";
        } else if (gwa < req.getMinGWA()) {
            status = "PENDING";
        } else if (courseTakenSlots.get(courseApplied) >= courseSlots.get(courseApplied)) {
            status = "WAITLISTED";
        } else {
            status = "APPROVED";
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
        newStudent.setDateAdmitted(LocalDate.now());
        students.add(newStudent);
        saveStudentsToCSV();
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
        List<Student> results = new ArrayList<>();
        for (Student s : students) {
            if (s.getFullName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(s);
            }
        }
        return results;
    }

    public boolean updateStudentStatus(String id, String newStatus) {
        Student student = searchByID(id);
        if (student == null) {
            return false;
        }
        String oldStatus = student.getStatus();
        String course = student.getCourseApplied();
        if (oldStatus.equals("APPROVED") && !newStatus.equals("APPROVED")) {
            courseTakenSlots.put(course, courseTakenSlots.get(course) - 1);
        } else if (!oldStatus.equals("APPROVED") && newStatus.equals("APPROVED")) {
            if (courseTakenSlots.get(course) >= courseSlots.get(course)) {
                return false;
            }
            courseTakenSlots.put(course, courseTakenSlots.get(course) + 1);
        }
        student.setStatus(newStatus);
        saveStudentsToCSV();
        return true;
    }

    public boolean deleteStudent(String id) {
        Student student = searchByID(id);
        if (student != null) {
            if (student.getStatus().equals("APPROVED")) {
                courseTakenSlots.put(student.getCourseApplied(), courseTakenSlots.get(student.getCourseApplied()) - 1);
            }
            students.remove(student);
            saveStudentsToCSV();
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

        System.out.println("=====================================");
        System.out.println("          ADMISSION REPORTS          ");
        System.out.println("=====================================");
        System.out.println();
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
        promptExport(total, approved, pending, rejected, waitlisted, yieldRate);
    }

    public void promptExport(int total, int approved, int pending, int rejected, int waitlisted, double yieldRate) {
        Scanner scan = new Scanner(System.in);
        System.out.print("\nPress [E] to export this report to CSV, or press Enter to skip: ");
        String choice = scan.nextLine();

        if (choice.equalsIgnoreCase("E")) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("Admission_Report.csv"))) {
                writer.println("Metric,Value");
                writer.println("Total Applicants," + total);
                writer.println("Approved," + approved);
                writer.println("Pending," + pending);
                writer.println("Rejected," + rejected);
                writer.println("Waitlisted," + waitlisted);
                writer.printf("Acceptance Rate,%.2f%%%n", yieldRate);
                System.out.println("[SUCCESS] Report saved to Admission_Report.csv");
            } catch (IOException e) {
                System.out.println("[ERROR] Failed to write file: " + e.getMessage());
            }
        }
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

    private void saveStudentsToCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENT_CSV))) {
            pw.println("studentID,fullName,age,address,contact,prevSchool,courseApplied,gwa,yearLevel,status,dateAdmitted");
            for (Student s : students) {
                pw.printf("%s,\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",%.2f,%s,%s,%s%n",
                        s.getStudentID(), s.getFullName(), s.getAge(),
                        s.getAddress(), s.getContact(), s.getPrevSchool(),
                        s.getCourseApplied(), s.getGrades(), s.getYearLevel(),
                        s.getStatus(), s.getDateAdmitted());
            }
        } catch (IOException e) {
            System.out.println("[CSV SAVE ERROR] " + e.getMessage());
        }
    }

    private void loadStudentsFromCSV() {
        File file = new File(STUDENT_CSV);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String studentID = parts[0].trim();
                String fullName = parts[1].replace("\"", "").trim();
                int age = Integer.parseInt(parts[2].trim());
                String address = parts[3].replace("\"", "").trim();
                String contact = parts[4].replace("\"", "").trim();
                String prevSchool = parts[5].replace("\"", "").trim();
                String courseApplied = parts[6].replace("\"", "").trim();
                double gwa = Double.parseDouble(parts[7].trim());
                YearLevel yearLevel = YearLevel.valueOf(parts[8].trim());
                String status = parts[9].trim();
                LocalDate dateAdmitted = LocalDate.parse(parts[10].trim());

                Student loadedStudent = new Student(studentID, fullName, courseApplied);
                loadedStudent.setAge(age);
                loadedStudent.setAddress(address);
                loadedStudent.setContact(contact);
                loadedStudent.setPrevSchool(prevSchool);
                loadedStudent.setGrades(gwa);
                loadedStudent.setYearLevel(yearLevel);
                loadedStudent.setStatus(status);
                loadedStudent.setDateAdmitted(dateAdmitted);
                students.add(loadedStudent);

                String[] idSplit = studentID.split("-");
                int seq = Integer.parseInt(idSplit[2]);
                if (seq >= rawIDSequence) {
                    rawIDSequence = seq + 1;
                }
            }
        } catch (Exception e) {
            System.out.println("[CSV LOAD WARNING] " + e.getMessage());
        }
    }

}