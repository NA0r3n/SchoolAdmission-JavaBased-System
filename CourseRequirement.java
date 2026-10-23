public class CourseRequirement {
    private String courseName;
    private College college;
    private double minGWA;
    private int slots;

    public CourseRequirement(String courseName, College college, double minGWA, int slots) {
        this.courseName = courseName;
        this.college = college;
        this.minGWA = minGWA;
        this.slots = slots;
    }

    public String getCourseName() {
        return courseName;
    }

    public College getCollege() {
        return college;
    }

    public double getMinGWA() {
        return minGWA;
    }

    public int getSlots() {
        return slots;
    }
}
