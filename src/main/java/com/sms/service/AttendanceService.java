package com.sms.service;

import com.sms.dao.AttendanceDAO;
import com.sms.model.Attendance;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AttendanceService {
    private final AttendanceDAO dao = new AttendanceDAO();

    private static final double MIN_ATTENDANCE = 75.0;

    public int mark(Attendance a) throws SQLException {
        if (a.getStudentId() <= 0)
            throw new IllegalArgumentException("Student ID is required");
        if (a.getSubject() == null || a.getSubject().isBlank())
            throw new IllegalArgumentException("Subject is required");
        if (a.getAttendanceDate() == null)
            throw new IllegalArgumentException("Date is required");
        if (a.getAttendanceDate().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Attendance date cannot be in the future");

        String status = a.getStatus();
        if (status == null ||
            (!status.equals("PRESENT") && !status.equals("ABSENT") && !status.equals("LATE")))
            throw new IllegalArgumentException("Status must be PRESENT, ABSENT or LATE");

        return dao.mark(a);
    }

    public List<Attendance> getByStudent(int studentId) throws SQLException {
        return dao.getByStudent(studentId);
    }

    public double calculatePercentage(int studentId) throws SQLException {
        int total = dao.getTotalDays(studentId);
        if (total == 0) return 0;
        int present = dao.getPresentDays(studentId);
        return ((double) present / total) * 100;
    }

    public boolean isEligibleForExam(int studentId) throws SQLException {
        double pct = calculatePercentage(studentId);
        return pct >= MIN_ATTENDANCE;
    }

    public double getMinAttendance() {
        return MIN_ATTENDANCE;
    }
}