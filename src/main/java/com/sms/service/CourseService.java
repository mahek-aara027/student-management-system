package com.sms.service;

import com.sms.dao.CourseDAO;
import com.sms.model.Course;

import java.sql.SQLException;
import java.util.List;

public class CourseService {
    private final CourseDAO dao = new CourseDAO();

    public int add(Course c) throws SQLException {
        if (c.getCourseName() == null || c.getCourseName().isBlank())
            throw new IllegalArgumentException("Course name is required");
        if (c.getDurationYears() <= 0)
            throw new IllegalArgumentException("Duration must be positive");
        if (c.getTotalSemesters() <= 0)
            throw new IllegalArgumentException("Total semesters must be positive");
        if (c.getFees() < 0)
            throw new IllegalArgumentException("Fees cannot be negative");
        return dao.addCourse(c);
    }

    public Course findById(int id) throws SQLException {
        return dao.getById(id);
    }

    public List<Course> findAll() throws SQLException {
        return dao.getAll();
    }
}