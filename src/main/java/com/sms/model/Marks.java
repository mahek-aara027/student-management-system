package com.sms.model;

public class Marks {
    private int markId;
    private int studentId;
    private String subject;
    private int semester;
    private double marksObtained;
    private double maxMarks;
    private String examType;   // INTERNAL / MIDTERM / FINAL

    public Marks() {}

    public Marks(int studentId, String subject, int semester,
                 double marksObtained, double maxMarks, String examType) {
        this.studentId = studentId;
        this.subject = subject;
        this.semester = semester;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
        this.examType = examType;
    }

    public int getMarkId() { return markId; }
    public void setMarkId(int markId) { this.markId = markId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public double getMarksObtained() { return marksObtained; }
    public void setMarksObtained(double marksObtained) { this.marksObtained = marksObtained; }

    public double getMaxMarks() { return maxMarks; }
    public void setMaxMarks(double maxMarks) { this.maxMarks = maxMarks; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }

    public String getGrade() {
        if (maxMarks == 0) return "-";
        double pct = (marksObtained / maxMarks) * 100;
        if (pct >= 90) return "A+";
        if (pct >= 80) return "A";
        if (pct >= 70) return "B";
        if (pct >= 60) return "C";
        if (pct >= 50) return "D";
        if (pct >= 40) return "E";
        return "F";
    }

    @Override
    public String toString() {
        return String.format("%-22s | %-3d | %-8s | %7.2f / %-6.2f | %-5s",
                subject, semester, examType, marksObtained, maxMarks, getGrade());
    }
}