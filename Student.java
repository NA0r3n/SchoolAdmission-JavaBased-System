import java.time.LocalDate;

public class Student {
    private String studentID;
    private String fullName;
    private int age;
    private String address;
    private String contact;
    private String prevSchool;
    private String courseApplied;
    private double gwa;
    private String status;
    private LocalDate dateAdmitted;
    private YearLevel yearLevel;

    public Student(String studentID, String fullName, String courseApplied) {
        this.studentID = studentID;
        this.fullName = fullName;
        this.courseApplied = courseApplied;
        this.dateAdmitted = LocalDate.now();
    }

    public String getStudentID() {
        return studentID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setPrevSchool(String prevSchool) {
        this.prevSchool = prevSchool;
    }

    public String getPrevSchool() {
        return prevSchool;
    }

    public void setGrades(double gwa) {
        this.gwa = gwa;
    }

    public double getGrades() {
        return gwa;
    }

    public String getCourseApplied() {
        return courseApplied;
    }

    public void setDateAdmitted(LocalDate dateAdmitted) {
        this.dateAdmitted = dateAdmitted;
    }

    public LocalDate getDateAdmitted() {
        return dateAdmitted;
    }

    public void setYearLevel(YearLevel yearLevel) {
        this.yearLevel = yearLevel;
    }

    public YearLevel getYearLevel() {
        return yearLevel;
    }
}
