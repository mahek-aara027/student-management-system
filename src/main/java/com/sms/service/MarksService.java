package com.sms.service;

import com.sms.dao.MarksDAO;
import com.sms.model.Marks;

import java.sql.SQLException;
import java.util.List;

public class MarksService {
    private final MarksDAO dao = new MarksDAO();

    public int addMarks(Marks m) throws SQLException {
        if (m.getStudentId() <= 0)
            throw new IllegalArgumentException("Student ID is required");
        if (m.getSubject() == null || m.getSubject().isBlank())
            throw new IllegalArgumentException("Subject is required");
        if (m.getSemester() <= 0)
            throw new IllegalArgumentException("Semester must be positive");
        if (m.getMaxMarks() <= 0)
            throw new IllegalArgumentException("Max marks must be positive");
        if (m.getMarksObtained() < 0)
            throw new IllegalArgumentException("Marks cannot be negative");
        if (m.getMarksObtained() > m.getMaxMarks())
            throw new IllegalArgumentException("Marks obtained cannot exceed max marks");
        if (m.getExamType() == null || m.getExamType().isBlank())
            throw new IllegalArgumentException("Exam type is required");
        return dao.addMarks(m);
    }

    public List<Marks> getByStudent(int studentId) throws SQLException {
        return dao.getByStudent(studentId);
    }

    public List<Marks> getByStudentAndSemester(int studentId, int semester) throws SQLException {
        return dao.getByStudentAndSemester(studentId, semester);
    }

    public double calculatePercentage(List<Marks> marks) {
        if (marks.isEmpty()) return 0;
        double totalObtained = 0, totalMax = 0;
        for (Marks m : marks) {
            totalObtained += m.getMarksObtained();
            totalMax += m.getMaxMarks();
        }
        if (totalMax == 0) return 0;
        return (totalObtained / totalMax) * 100;
    }

    public String calculateGrade(double percentage) {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        if (percentage >= 40) return "E";
        return "F";
    }

    public String getResultStatus(double percentage) {
        return percentage >= 40 ? "PASS" : "FAIL";
    }
}