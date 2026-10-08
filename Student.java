import java.time.LocalDate;

public class Student {

    private final String studentID;
    private String fullName;
    private int age;
    private String address;
    private String contact;
    private String prevSchool;
    private String courseApplied;
    private double gwa;
    private String status; // Values: "PENDING", "APPROVED", "REJECTED", "WAITLISTED"
    private LocalDate dateAdmitted;

    public Student(String studentID, String fullName, String courseApplied) {
        this.studentID = studentID;
        this.fullName = fullName;
        this.courseApplied = courseApplied;
        this.status = "PENDING";        // FIX: was "this.status = status" (undefined variable)
        this.dateAdmitted = LocalDate.now();
    }

    // FIX: renamed from generateStudentID() -> getStudentID()
    public String getStudentID() {
        return studentID;
    }

    public LocalDate getDateAdmitted() {
        return dateAdmitted;
    }

    // Getters
    public String getFullName()      { return fullName; }
    public String getCourseApplied() { return courseApplied; }
    public double getGrades()        { return gwa; }
    public String getStatus()        { return status; }
    public int getAge()              { return age; }
    public String getContact()       { return contact; }
    public String getPrevSchool()    { return prevSchool; }
    public String getAddress()       { return address; }

    // Setters
    public void setFullName(String fullName) {
        if (fullName != null && !fullName.trim().isEmpty()) {
            this.fullName = fullName.trim();
        }
    }

    public void setCourseApplied(String courseApplied) {
        this.courseApplied = courseApplied;
    }

    public void setGrades(double gwa) {
        if (gwa < 0 || gwa > 100) {
            throw new IllegalArgumentException("Invalid GWA. Must be between 0.0 and 100.0.");
        }
        this.gwa = gwa;
    }

    public void setAddress(String address)   { this.address = address; }
    public void setContact(String contact)   { this.contact = contact; }
    public void setPrevSchool(String prevSchool) { this.prevSchool = prevSchool; }

    public void setAge(int age) {
        if (age < 15 || age > 100) {
            throw new IllegalArgumentException("Age must be between 15 and 100.");
        }
        this.age = age;
    }

    // FIX: changed return type String -> void (was always called as void)
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    public boolean isEligibleForHonors() {
        return this.gwa >= 90.0;
    }
}
