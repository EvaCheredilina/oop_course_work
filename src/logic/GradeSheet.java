package logic;

import java.util.ArrayList;

public class GradeSheet {
    private String group;
    private String subject;
    private String teacher;
    private ArrayList<MarkInfo> marks = new ArrayList<>();

    public GradeSheet(String group, String subject, String teacher) {
        this.group = group;
        this.subject = subject;
        this.teacher = teacher;
    }

    public void addMark(MarkInfo newMarkInfo) {
        marks.add(newMarkInfo);
    }

    public void removeMark(int removableMarkIndex) {
        marks.remove(removableMarkIndex);
    }

    public ArrayList<MarkInfo> getMarks() {
        return marks;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getGroup() {
        return group;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher() {
        return teacher;
    }
}
