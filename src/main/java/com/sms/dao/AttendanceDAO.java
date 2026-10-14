package com.sms.dao;

import com.sms.model.Attendance;
import com.sms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public int mark(Attendance a) throws SQLException {
        String sql = "INSERT INTO attendance(student_id, subject, attendance_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getStudentId());
            ps.setString(2, a.getSubject());
            ps.setDate(3, Date.valueOf(a.getAttendanceDate()));
            ps.setString(4, a.getStatus());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                a.setAttendanceId(keys.getInt(1));
                return a.getAttendanceId();
            }
        }
        return -1;
    }

    public List<Attendance> getByStudent(int studentId) throws SQLException {
        String sql = "SELECT * FROM attendance WHERE student_id = ? ORDER BY attendance_date DESC";
        List<Attendance> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public int getTotalDays(int studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM attendance WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getPresentDays(int studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND status IN ('PRESENT','LATE')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Attendance mapRow(ResultSet rs) throws SQLException {
        Attendance a = new Attendance();
        a.setAttendanceId(rs.getInt("attendance_id"));
        a.setStudentId(rs.getInt("student_id"));
        a.setSubject(rs.getString("subject"));
        Date d = rs.getDate("attendance_date");
        if (d != null) a.setAttendanceDate(d.toLocalDate());
        a.setStatus(rs.getString("status"));
        return a;
    }
}