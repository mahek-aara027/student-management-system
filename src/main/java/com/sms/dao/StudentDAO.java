package com.sms.dao;

import com.sms.model.Student;
import com.sms.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public int addStudent(Student s) throws SQLException {
        String sql = "INSERT INTO students(roll_no, name, email, phone, gender, dob, address, course_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getRollNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getGender());
            ps.setDate(6, Date.valueOf(s.getDob()));
            ps.setString(7, s.getAddress());
            ps.setInt(8, s.getCourseId());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                s.setStudentId(keys.getInt(1));
                return s.getStudentId();
            }
        }
        return -1;
    }

    public Student getById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Student> getAll() throws SQLException {
        String sql = "SELECT * FROM students ORDER BY student_id";
        List<Student> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Student> searchByName(String keyword) throws SQLException {
        String sql = "SELECT * FROM students WHERE name LIKE ? ORDER BY name";
        List<Student> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean updateStudent(Student s) throws SQLException {
        String sql = "UPDATE students SET roll_no=?, name=?, email=?, phone=?, gender=?, " +
                     "dob=?, address=?, course_id=? WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getRollNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getGender());
            ps.setDate(6, Date.valueOf(s.getDob()));
            ps.setString(7, s.getAddress());
            ps.setInt(8, s.getCourseId());
            ps.setInt(9, s.getStudentId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getInt("student_id"));
        s.setRollNo(rs.getString("roll_no"));
        s.setName(rs.getString("name"));
        s.setEmail(rs.getString("email"));
        s.setPhone(rs.getString("phone"));
        s.setGender(rs.getString("gender"));
        Date dob = rs.getDate("dob");
        if (dob != null) s.setDob(dob.toLocalDate());
        s.setAddress(rs.getString("address"));
        s.setCourseId(rs.getInt("course_id"));
        Date adm = rs.getDate("admission_date");
        if (adm != null) s.setAdmissionDate(adm.toLocalDate());
        s.setStatus(rs.getString("status"));
        return s;
    }
}