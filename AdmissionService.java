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
        addCourse("BSIT", 50);
        addCourse("BSCS", 40);
        addCourse("BSHM", 60);
        
        greatStudents();
    }
    
    public void greatStudents(){
        registerNewStudent("Nerwen Angelo C. Ogardo",18,"makati","0912345678","TNCHS","BSCS",93);
        registerNewStudent("Samantha Angela Y. Sabino",18,"45 Mabini Avenue, Barangay Poblacion, Santa Rosa, Laguna 4026, Philippines","09786642936",
"Pateros Catholic School","BSCS",99);
    } //sanaol 99

    public void addCourse(String courseName, int slots) {
        courseSlots.put(courseName, slots);
        courseTakenSlots.put(courseName, 0);
    }

    // Option 1: Enroll
    public String registerNewStudent(
            String fullName, int age, String address, String contact,
            String prevSchool, String courseApplied, double gwa) {

        if (!courseSlots.containsKey(courseApplied)) {
            return "Error: Course does not exist.";
        }

        String status;
        if (gwa < 75.0) {
            status = "REJECTED";
        } else if (courseTakenSlots.get(courseApplied) >= courseSlots.get(courseApplied)) {
            status = "WAITLISTED";   // FIX: grades pass but course is full
        } else {
            status = "APPROVED";
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

        if (status.equals("APPROVED")) {
            int currentTaken = courseTakenSlots.get(courseApplied);
            courseTakenSlots.put(courseApplied, currentTaken + 1);
        }

        return "Success! Student registered with ID: " + studentID;
    }

    // Option 2: View all students
    public List<Student> getAllStudents() {
        return this.students;
    }

    // Option 3: Search by student ID
    public Student searchByID(String id) {
        for (Student student : students) {
            if (student.getStudentID().equals(id)) {
                return student;
            }
        }
        return null;
    }

    // Option 3: Search by name keyword
    public List<Student> searchByName(String nameKeyword) {
        List<Student> foundList = new ArrayList<>();
        for (Student student : students) {
            if (student.getFullName().toLowerCase().contains(nameKeyword.toLowerCase())) {
                foundList.add(student);
            }
        }
        return foundList;
    }

    // Option 4: Update student record
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

    // Option 5: Delete student record
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

    // Option 6: Course summary (available slots per course)
    public Map<String, Integer> getCourseSummary() {
        Map<String, Integer> summary = new HashMap<>();
        for (String course : courseSlots.keySet()) {
            int available = courseSlots.get(course) - courseTakenSlots.get(course);
            summary.put(course, available);
        }
        return summary;
    }

    // Option 7: Admission reports
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
