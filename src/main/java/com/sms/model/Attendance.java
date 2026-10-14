package com.sms.model;

import java.time.LocalDate;

public class Attendance {
    private int attendanceId;
    private int studentId;
    private String subject;
    private LocalDate attendanceDate;
    private String status;   // PRESENT / ABSENT / LATE

    public Attendance() {}

    public Attendance(int studentId, String subject, LocalDate attendanceDate, String status) {
        this.studentId = studentId;
        this.subject = subject;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("%-12s | %-22s | %-10s",
                attendanceDate, subject, status);
    }
}