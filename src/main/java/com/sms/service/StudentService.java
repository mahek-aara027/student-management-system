package com.sms.service;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDAO dao = new StudentDAO();

    public int registerStudent(Student s) throws SQLException {
        validate(s);
        return dao.addStudent(s);
    }

    public Student findById(int id) throws SQLException {
        return dao.getById(id);
    }

    public List<Student> findAll() throws SQLException {
        return dao.getAll();
    }

    public List<Student> searchByName(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank())
            throw new IllegalArgumentException("Search keyword cannot be empty");
        return dao.searchByName(keyword.trim());
    }

    public boolean update(Student s) throws SQLException {
        validate(s);
        return dao.updateStudent(s);
    }

    public boolean delete(int id) throws SQLException {
        return dao.deleteStudent(id);
    }

    private void validate(Student s) {
        if (s.getRollNo() == null || s.getRollNo().isBlank())
            throw new IllegalArgumentException("Roll number is required");
        if (s.getName() == null || s.getName().isBlank())
            throw new IllegalArgumentException("Name is required");
        if (s.getEmail() == null || !s.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("Invalid email format");
        if (s.getPhone() == null || !s.getPhone().matches("\\d{10}"))
            throw new IllegalArgumentException("Phone must be 10 digits");
        if (s.getDob() == null)
            throw new IllegalArgumentException("Date of birth is required");
        if (s.getCourseId() <= 0)
            throw new IllegalArgumentException("Course ID must be a positive number");
    }
}