package com.sms.dao;

import com.sms.model.Course;
import com.sms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public int addCourse(Course c) throws SQLException {
        String sql = "INSERT INTO courses(course_name, duration_years, total_semesters, fees) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getCourseName());
            ps.setInt(2, c.getDurationYears());
            ps.setInt(3, c.getTotalSemesters());
            ps.setDouble(4, c.getFees());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                c.setCourseId(keys.getInt(1));
                return c.getCourseId();
            }
        }
        return -1;
    }

    public Course getById(int id) throws SQLException {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Course> getAll() throws SQLException {
        String sql = "SELECT * FROM courses ORDER BY course_id";
        List<Course> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setCourseId(rs.getInt("course_id"));
        c.setCourseName(rs.getString("course_name"));
        c.setDurationYears(rs.getInt("duration_years"));
        c.setTotalSemesters(rs.getInt("total_semesters"));
        c.setFees(rs.getDouble("fees"));
        return c;
    }
}