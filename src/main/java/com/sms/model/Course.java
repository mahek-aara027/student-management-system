package com.sms.model;

public class Course {
    private int courseId;
    private String courseName;
    private int durationYears;
    private int totalSemesters;
    private double fees;

    public Course() {}

    public Course(String courseName, int durationYears, int totalSemesters, double fees) {
        this.courseName = courseName;
        this.durationYears = durationYears;
        this.totalSemesters = totalSemesters;
        this.fees = fees;
    }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public int getDurationYears() { return durationYears; }
    public void setDurationYears(int durationYears) { this.durationYears = durationYears; }

    public int getTotalSemesters() { return totalSemesters; }
    public void setTotalSemesters(int totalSemesters) { this.totalSemesters = totalSemesters; }

    public double getFees() { return fees; }
    public void setFees(double fees) { this.fees = fees; }

    @Override
    public String toString() {
        return String.format("%-4d | %-25s | %-8d yrs | %-8d sems | Rs %,.2f",
                courseId, courseName, durationYears, totalSemesters, fees);
    }
}