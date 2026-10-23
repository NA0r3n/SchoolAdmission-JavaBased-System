public enum College {
    CCIS("College of Computing and Information Sciences"),
    CBFS("College of Business and Financial Science"),
    CTHM("College of Tourism and Hospitality Management"),
    CCSE("College of Construction Sciences and Engineering"),
    CET("College of Engineering Technology"),
    CITE("College of Innovative Teacher Education"),
    CONHS("College of Nursing and Health Sciences"),
    CGPP("College of Governance and Public Policy"),
    CLAS("College of Liberal Arts and Sciences"),
    CHK("College of Human Kinetics"),
    IAD("Institute of Arts and Design");

    public final String fullName;

    College(String fullName) {
        this.fullName = fullName;
    }
}
