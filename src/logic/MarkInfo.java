package logic;

public class MarkInfo {
    private String student;
    private String mark;

    public MarkInfo(String student, String mark) {
        this.student = student;
        this.mark = mark;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getStudent() {
        return student;
    }

    public String getMark() {
        return mark;
    }
}
