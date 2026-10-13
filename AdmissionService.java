import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.ArrayList;

public class AdmissionService {

	private List<Student> students = new ArrayList<>();
	private Map<String, Integer> courseSlots = new HashMap<>();
	private Map<String, Integer> courseTakenSlots = new HashMap<>();
	private int rawIDSequence = 1;

	public AdmissionService() {	
        addCourse("BSIT", 100);   // Bachelor of Science in Information Technology
        addCourse("BSCS", 100);   // Bachelor of Science in Computer Science
        addCourse("BSHM", 100);   // Bachelor of Science in Hospitality Management
        addCourse("BSCE", 100);   // Bachelor of Science in Civil Engineering
        addCourse("BSEE", 100);   // Bachelor of Science in Electrical Engineering
        addCourse("BSME", 100);   // Bachelor of Science in Mechanical Engineering
        addCourse("BSArch", 100); // Bachelor of Science in Architecture
        addCourse("BPA", 100);    // Bachelor of Public Administration
        addCourse("BSBA", 100);   // Bachelor of Science in Business Administration
    	addCourse("BSEd", 100);   // Bachelor of Secondary Education
        addCourse("BEEd", 100);   // Bachelor of Elementary Education
        addCourse("BSN", 100);    // Bachelor of Science in Nursing
        addCourse("BSCrim", 100); // Bachelor of Science in Criminology
        
        createSampleStudents();
    }
    
    	public void createSampleStudents() {
		// Preloaded — with CUSTOM dates
		registerPreloadedStudent(
		    "Nerwen Angelo C. Ogardo", 18, "Makati City", 
		    "09123456789", "Taguig National High School", 
		    "BSCS", 93.0, 
		    LocalDate.of(2026, 1, 23));
		
		registerPreloadedStudent(
		    "Samantha Angela Y. Sabino", 18, "Santa Rosa, Laguna", 
		    "09123456789", "Pateros Catholic School", 
		    "BSCS", 99.0,
		    LocalDate.of(2026, 1, 23));
	}

    
    private Student registerPreloadedStudent(
        String fullName, int age, String address, String contact,
        String prevSchool, String courseApplied, double gwa, LocalDate customDate) {
        
        Student resultMsg = registerNewStudent(fullName, age, address, contact, prevSchool, courseApplied, gwa);
        
        // Get the student added
        Student justAdded = students.get(students.size() - 1);
        
        // Override with custom date
        justAdded.setDateAdmitted(customDate);
        
        return justAdded;
    }

	public void addCourse(String courseName, int slots) {
		courseSlots.put(courseName, slots);
		courseTakenSlots.put(courseName, 0);
	}
	
	// Get all available courses
    public List<String> getAllCourses() {
        return new ArrayList<>(courseSlots.keySet());
}


	
	// Option 1: Enroll
	  public Student registerNewStudent(
		String fullName, int age, String address, String contact,
		String prevSchool, String courseApplied, double gwa) {
        String status;
            
		if (!courseSlots.containsKey(courseApplied)) {
			System.out.println("Unfortunately, UMAK does not have such course.");
			status = "PENDING";
			System.out.printf("%n──────────────────────────────%n%s is currently: %s in UMAK! %n%nReason: UMAK does not accomodate the %s course yet.%n──────────────────────────────%n", fullName, status, courseApplied);
             return null;
		}
		
        if (gwa < 75.0) {
            status = "REJECTED";
        } else if (gwa >= 75.0 && gwa < 80.0) {
            status = "PENDING";
            System.out.printf("%n──────────────────────────────%n%s is currently: %s in UMAK!%nReason: GWA %.2f needs further review.%n──────────────────────────────%n",
                fullName, status, gwa);
        } else if (courseTakenSlots.get(courseApplied) >= courseSlots.get(courseApplied)) {
            status = "WAITLISTED";
            System.out.printf("%n──────────────────────────────%n%s is currently: %s in UMAK!%nReason: %s is at full capacity.%n──────────────────────────────%n",
                fullName, status, courseApplied);
        } else {
            status = "APPROVED";
            System.out.printf("%n──────────────────────────────%n%s is now officially a student in UMAK!%n──────────────────────────────%n",
                fullName);
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

		return newStudent;
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

    // Option 8: Get Students Numbers by Status
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
