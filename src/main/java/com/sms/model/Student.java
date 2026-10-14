package com.sms.model;

import java.time.LocalDate;

public class Student {
    private int studentId;
    private String rollNo;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private LocalDate dob;
    private String address;
    private int courseId;
    private LocalDate admissionDate;
    private String status;

    public Student() {}

    public Student(String rollNo, String name, String email, String phone,
                   String gender, LocalDate dob, String address, int courseId) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.courseId = courseId;
        this.status = "ACTIVE";
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("%-4d | %-12s | %-20s | %-25s | %-12s | %-6s",
                studentId, rollNo, name, email, phone, gender);
    }
}