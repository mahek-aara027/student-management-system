package com.sms.dao;

import com.sms.model.Marks;
import com.sms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarksDAO {

    public int addMarks(Marks m) throws SQLException {
        String sql = "INSERT INTO marks(student_id, subject, semester, marks_obtained, max_marks, exam_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getStudentId());
            ps.setString(2, m.getSubject());
            ps.setInt(3, m.getSemester());
            ps.setDouble(4, m.getMarksObtained());
            ps.setDouble(5, m.getMaxMarks());
            ps.setString(6, m.getExamType());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                m.setMarkId(keys.getInt(1));
                return m.getMarkId();
            }
        }
        return -1;
    }

    public List<Marks> getByStudent(int studentId) throws SQLException {
        String sql = "SELECT * FROM marks WHERE student_id = ? ORDER BY semester, subject";
        List<Marks> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Marks> getByStudentAndSemester(int studentId, int semester) throws SQLException {
        String sql = "SELECT * FROM marks WHERE student_id = ? AND semester = ? ORDER BY subject";
        List<Marks> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, semester);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean deleteMarks(int markId) throws SQLException {
        String sql = "DELETE FROM marks WHERE mark_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, markId);
            return ps.executeUpdate() > 0;
        }
    }

    private Marks mapRow(ResultSet rs) throws SQLException {
        Marks m = new Marks();
        m.setMarkId(rs.getInt("mark_id"));
        m.setStudentId(rs.getInt("student_id"));
        m.setSubject(rs.getString("subject"));
        m.setSemester(rs.getInt("semester"));
        m.setMarksObtained(rs.getDouble("marks_obtained"));
        m.setMaxMarks(rs.getDouble("max_marks"));
        m.setExamType(rs.getString("exam_type"));
        return m;
    }
}