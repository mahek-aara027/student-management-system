package com.sms;

import com.sms.model.Attendance;
import com.sms.model.Course;
import com.sms.model.Marks;
import com.sms.model.Student;
import com.sms.service.AttendanceService;
import com.sms.service.CourseService;
import com.sms.service.MarksService;
import com.sms.service.StudentService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final StudentService    studentService    = new StudentService();
    private static final CourseService     courseService     = new CourseService();
    private static final MarksService      marksService      = new MarksService();
    private static final AttendanceService attendanceService = new AttendanceService();

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   STUDENT MANAGEMENT SYSTEM");
        System.out.println("=========================================");
        while (true) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            System.out.println();
            try {
                switch (choice) {
                    case 1  -> addStudent();
                    case 2  -> viewAllStudents();
                    case 3  -> searchStudent();
                    case 4  -> updateStudent();
                    case 5  -> deleteStudent();
                    case 6  -> manageCourses();
                    case 7  -> addMarks();
                    case 8  -> viewMarksheet();
                    case 9  -> markAttendance();
                    case 10 -> viewAttendance();
                    case 11 -> viewRankList();
                    case 12 -> exportMarksheet();
                    case 0  -> {
                        System.out.println("Thank you! Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("----------- MAIN MENU -----------");
        System.out.println("1.  Add Student");
        System.out.println("2.  View All Students");
        System.out.println("3.  Search Student by Name");
        System.out.println("4.  Update Student");
        System.out.println("5.  Delete Student");
        System.out.println("6.  Manage Courses");
        System.out.println("7.  Add Marks for Student");
        System.out.println("8.  View Student Marksheet");
        System.out.println("9.  Mark Attendance");
        System.out.println("10. View Attendance");
        System.out.println("11. View Rank List");
        System.out.println("12. Export Marksheet to File");
        System.out.println("0.  Exit");
        System.out.println("---------------------------------");
    }

    // ================= STUDENT CRUD =================

    private static void addStudent() throws Exception {
        System.out.println("========== ADD STUDENT ==========");
        System.out.println("Enter Roll No           : ");
        String roll = sc.nextLine().trim();
        System.out.println("Enter Full Name         : ");
        String name = sc.nextLine().trim();
        System.out.println("Enter Email             : ");
        String email = sc.nextLine().trim();
        System.out.println("Enter Phone (10 digits) : ");
        String phone = sc.nextLine().trim();
        System.out.println("Enter Gender (M/F/O)    : ");
        String gender = sc.nextLine().trim();

        LocalDate dob = null;
        while (dob == null) {
            System.out.println("Enter DOB (yyyy-mm-dd)  : ");
            String dobStr = sc.nextLine().trim();
            try { dob = LocalDate.parse(dobStr); }
            catch (DateTimeParseException e) {
                System.out.println("  [!] Invalid date. Use yyyy-mm-dd.");
            }
        }

        System.out.println("Enter Address           : ");
        String address = sc.nextLine().trim();

        List<Course> courses = courseService.findAll();
        if (courses.isEmpty()) {
            System.out.println();
            System.out.println("[!] No courses available. Add a course first (Menu -> 6 -> 1).");
            return;
        }

        System.out.println();
        System.out.println("-------- AVAILABLE COURSES --------");
        System.out.printf("%-4s | %-25s | %-11s | %-11s | %-12s%n",
                "ID", "Course Name", "Duration", "Semesters", "Fees");
        System.out.println("-----+---------------------------+-------------+-------------+-------------");
        for (Course c : courses) System.out.println(c);
        System.out.println("-----------------------------------");

        int courseId;
        while (true) {
            courseId = readInt("Enter Course ID from above list : ");
            Course chosen = courseService.findById(courseId);
            if (chosen != null) {
                System.out.println("  Selected Course: " + chosen.getCourseName());
                break;
            }
            System.out.println("  [!] Invalid Course ID. Pick one from the list above.");
        }

        Student s = new Student(roll, name, email, phone, gender, dob, address, courseId);
        int id = studentService.registerStudent(s);
        System.out.println();
        System.out.println("[OK] Student added with ID: " + id);
        System.out.println("=================================");
    }

    private static void viewAllStudents() throws Exception {
        System.out.println("========== ALL STUDENTS ==========");
        List<Student> list = studentService.findAll();
        if (list.isEmpty()) {
            System.out.println("No students found.");
            System.out.println("==================================");
            return;
        }
        System.out.printf("%-4s | %-12s | %-22s | %-28s | %-12s | %-6s%n",
                "ID", "Roll No", "Name", "Email", "Phone", "Gender");
        System.out.println("-----+--------------+------------------------+------------------------------+--------------+-------");
        for (Student s : list) System.out.println(s);
        System.out.println("==================================");
        System.out.println("Total students: " + list.size());
    }

    private static void searchStudent() throws Exception {
        System.out.println("========== SEARCH STUDENT ==========");
        System.out.println("Enter name keyword : ");
        String kw = sc.nextLine().trim();
        List<Student> list = studentService.searchByName(kw);
        if (list.isEmpty()) {
            System.out.println("No students matched '" + kw + "'.");
            return;
        }
        System.out.printf("%-4s | %-12s | %-22s | %-28s | %-12s | %-6s%n",
                "ID", "Roll No", "Name", "Email", "Phone", "Gender");
        System.out.println("-----+--------------+------------------------+------------------------------+--------------+-------");
        for (Student s : list) System.out.println(s);
        System.out.println("Found: " + list.size() + " student(s)");
    }

    private static void updateStudent() throws Exception {
        System.out.println("========== UPDATE STUDENT ==========");
        int id = readInt("Enter Student ID to update : ");
        Student s = studentService.findById(id);
        if (s == null) {
            System.out.println("[X] No student found with ID " + id);
            return;
        }
        System.out.println();
        System.out.println("Current details:");
        System.out.println("  Name    : " + s.getName());
        System.out.println("  Email   : " + s.getEmail());
        System.out.println("  Phone   : " + s.getPhone());
        System.out.println("  Gender  : " + s.getGender());
        System.out.println("  DOB     : " + s.getDob());
        System.out.println("  Address : " + s.getAddress());
        System.out.println("  Course  : " + s.getCourseId());
        System.out.println("(Press Enter to keep current value)");

        System.out.println("Enter New Name    [" + s.getName() + "] : ");
        String v = sc.nextLine().trim(); if (!v.isEmpty()) s.setName(v);
        System.out.println("Enter New Email   [" + s.getEmail() + "] : ");
        v = sc.nextLine().trim(); if (!v.isEmpty()) s.setEmail(v);
        System.out.println("Enter New Phone   [" + s.getPhone() + "] : ");
        v = sc.nextLine().trim(); if (!v.isEmpty()) s.setPhone(v);
        System.out.println("Enter New Gender  [" + s.getGender() + "] : ");
        v = sc.nextLine().trim(); if (!v.isEmpty()) s.setGender(v);
        System.out.println("Enter New DOB     [" + s.getDob() + "] : ");
        v = sc.nextLine().trim();
        if (!v.isEmpty()) {
            try { s.setDob(LocalDate.parse(v)); }
            catch (DateTimeParseException e) { System.out.println("  [!] Invalid date. Kept old."); }
        }
        System.out.println("Enter New Address [" + s.getAddress() + "] : ");
        v = sc.nextLine().trim(); if (!v.isEmpty()) s.setAddress(v);

        List<Course> courses = courseService.findAll();
        System.out.println();
        System.out.println("-------- AVAILABLE COURSES --------");
        for (Course c : courses) System.out.println(c);
        System.out.println("-----------------------------------");
        System.out.println("Enter New Course  [" + s.getCourseId() + "] : ");
        v = sc.nextLine().trim();
        if (!v.isEmpty()) {
            try {
                int newId = Integer.parseInt(v);
                if (courseService.findById(newId) == null) {
                    System.out.println("  [!] Invalid Course ID. Keeping old value.");
                } else {
                    s.setCourseId(newId);
                }
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Keeping old value.");
            }
        }

        if (studentService.update(s)) System.out.println("[OK] Student updated.");
        else System.out.println("[X] Update failed.");
    }

    private static void deleteStudent() throws Exception {
        System.out.println("========== DELETE STUDENT ==========");
        int id = readInt("Enter Student ID to delete : ");
        Student s = studentService.findById(id);
        if (s == null) {
            System.out.println("[X] No student found with ID " + id);
            return;
        }
        System.out.println();
        System.out.println("Student to delete:");
        System.out.println("  ID      : " + s.getStudentId());
        System.out.println("  Roll No : " + s.getRollNo());
        System.out.println("  Name    : " + s.getName());
        System.out.println("  Email   : " + s.getEmail());
        System.out.println("Are you sure? (yes/no) : ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (!confirm.equals("yes") && !confirm.equals("y")) {
            System.out.println("Delete cancelled.");
            return;
        }
        if (studentService.delete(id)) System.out.println("[OK] Student deleted.");
        else System.out.println("[X] Delete failed.");
    }

    // ================= COURSES =================

    private static void manageCourses() throws Exception {
        while (true) {
            System.out.println("========== COURSE MANAGEMENT ==========");
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("0. Back to Main Menu");
            int ch = readInt("Enter choice: ");
            System.out.println();
            switch (ch) {
                case 1 -> addCourse();
                case 2 -> viewAllCourses();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addCourse() throws Exception {
        System.out.println("---- ADD COURSE ----");
        System.out.println("Enter Course Name       : ");
        String name = sc.nextLine().trim();
        int years = readInt("Enter Duration (years)  : ");
        int sems  = readInt("Enter Total Semesters   : ");
        double fees = readDouble("Enter Fees (Rs)         : ");

        Course c = new Course(name, years, sems, fees);
        int id = courseService.add(c);
        System.out.println("[OK] Course added with ID: " + id);
    }

    private static void viewAllCourses() throws Exception {
        System.out.println("---- ALL COURSES ----");
        List<Course> list = courseService.findAll();
        if (list.isEmpty()) {
            System.out.println("No courses found. Add one first.");
            return;
        }
        System.out.printf("%-4s | %-25s | %-11s | %-11s | %-12s%n",
                "ID", "Course Name", "Duration", "Semesters", "Fees");
        System.out.println("-----+---------------------------+-------------+-------------+-------------");
        for (Course c : list) System.out.println(c);
        System.out.println("Total courses: " + list.size());
    }

    // ================= MARKS =================

    private static void addMarks() throws Exception {
        System.out.println("========== ADD MARKS ==========");
        int studentId = readInt("Enter Student ID : ");

        Student s = studentService.findById(studentId);
        if (s == null) {
            System.out.println("[X] No student with ID " + studentId);
            return;
        }
        System.out.println("Student: " + s.getName() + " (" + s.getRollNo() + ")");
        System.out.println();

        String subject = "";
        while (subject.isEmpty()) {
            System.out.println("Enter Subject Name    : ");
            subject = sc.nextLine().trim();
        }

        int semester = readInt("Enter Semester (1-8)  : ");

        System.out.println("Enter Exam Type (INTERNAL/MIDTERM/FINAL) : ");
        String examType = sc.nextLine().trim().toUpperCase();
        if (!examType.equals("INTERNAL") && !examType.equals("MIDTERM") && !examType.equals("FINAL")) {
            System.out.println("[!] Invalid exam type. Defaulting to FINAL.");
            examType = "FINAL";
        }

        double maxMarks = readDouble("Enter Max Marks       : ");
        double obtained = readDouble("Enter Marks Obtained  : ");

        Marks m = new Marks(studentId, subject, semester, obtained, maxMarks, examType);
        int markId = marksService.addMarks(m);

        double pct = (obtained / maxMarks) * 100;
        System.out.println();
        System.out.println("[OK] Marks added. ID: " + markId);
        System.out.println("     Percentage: " + String.format("%.2f", pct) + "%  |  Grade: " + m.getGrade());
        System.out.println("================================");
    }

    private static void viewMarksheet() throws Exception {
        System.out.println("========== STUDENT MARKSHEET ==========");
        int studentId = readInt("Enter Student ID : ");

        Student s = studentService.findById(studentId);
        if (s == null) {
            System.out.println("[X] No student with ID " + studentId);
            return;
        }

        String sheet = buildMarksheetText(s);
        System.out.println(sheet);
    }

    // ================= ATTENDANCE =================

    private static void markAttendance() throws Exception {
        System.out.println("========== MARK ATTENDANCE ==========");
        int studentId = readInt("Enter Student ID : ");

        Student s = studentService.findById(studentId);
        if (s == null) {
            System.out.println("[X] No student with ID " + studentId);
            return;
        }
        System.out.println("Student: " + s.getName() + " (" + s.getRollNo() + ")");

        String subject = "";
        while (subject.isEmpty()) {
            System.out.println("Enter Subject Name          : ");
            subject = sc.nextLine().trim();
        }

        LocalDate date = null;
        while (date == null) {
            System.out.println("Enter Date (yyyy-mm-dd)     : ");
            String d = sc.nextLine().trim();
            try { date = LocalDate.parse(d); }
            catch (DateTimeParseException e) {
                System.out.println("  [!] Invalid date. Use yyyy-mm-dd.");
            }
        }

        System.out.println("Enter Status (PRESENT/ABSENT/LATE) : ");
        String status = sc.nextLine().trim().toUpperCase();
        if (!status.equals("PRESENT") && !status.equals("ABSENT") && !status.equals("LATE")) {
            System.out.println("[!] Invalid status. Defaulting to PRESENT.");
            status = "PRESENT";
        }

        Attendance a = new Attendance(studentId, subject, date, status);
        int attId = attendanceService.mark(a);
        System.out.println();
        System.out.println("[OK] Attendance marked. ID: " + attId);
        System.out.println("=====================================");
    }

    private static void viewAttendance() throws Exception {
        System.out.println("========== VIEW ATTENDANCE ==========");
        int studentId = readInt("Enter Student ID : ");

        Student s = studentService.findById(studentId);
        if (s == null) {
            System.out.println("[X] No student with ID " + studentId);
            return;
        }

        List<Attendance> list = attendanceService.getByStudent(studentId);
        double pct = attendanceService.calculatePercentage(studentId);
        boolean eligible = attendanceService.isEligibleForExam(studentId);

        System.out.println();
        System.out.println("Student : " + s.getName() + " (" + s.getRollNo() + ")");
        System.out.println("-----------------------------------------");

        if (list.isEmpty()) {
            System.out.println("No attendance records.");
            return;
        }

        System.out.printf("%-12s | %-22s | %-10s%n", "Date", "Subject", "Status");
        System.out.println("-------------+------------------------+-----------");
        for (Attendance a : list) System.out.println(a);
        System.out.println("-------------+------------------------+-----------");
        System.out.printf("Attendance : %.2f%%%n", pct);
        System.out.printf("Min required: %.0f%%%n", attendanceService.getMinAttendance());
        System.out.println("Exam Eligible: " + (eligible ? "YES" : "NO"));
        System.out.println("-----------------------------------------");
    }

    // ================= RANK LIST =================

    private static void viewRankList() throws Exception {
        System.out.println("========== RANK LIST ==========");
        List<Student> students = studentService.findAll();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        List<RankEntry> ranks = new ArrayList<>();
        for (Student s : students) {
            List<Marks> marks = marksService.getByStudent(s.getStudentId());
            double pct = marksService.calculatePercentage(marks);
            ranks.add(new RankEntry(s, pct, marks.size()));
        }

        // Sort by percentage descending
        ranks.sort((a, b) -> Double.compare(b.percentage, a.percentage));

        System.out.printf("%-5s | %-4s | %-12s | %-22s | %-10s | %-8s%n",
                "Rank", "ID", "Roll No", "Name", "Percentage", "Grade");
        System.out.println("------+------+--------------+------------------------+------------+---------");

        int rank = 1;
        for (RankEntry r : ranks) {
            String grade = r.markCount == 0 ? "-" : marksService.calculateGrade(r.percentage);
            String pctText = r.markCount == 0 ? "N/A" : String.format("%.2f%%", r.percentage);
            System.out.printf("%-5d | %-4d | %-12s | %-22s | %-10s | %-8s%n",
                    rank++, r.student.getStudentId(), r.student.getRollNo(),
                    r.student.getName(), pctText, grade);
        }
        System.out.println("================================");
        System.out.println("Total students ranked: " + ranks.size());
    }

    private static class RankEntry {
        Student student;
        double percentage;
        int markCount;
        RankEntry(Student s, double p, int mc) {
            this.student = s;
            this.percentage = p;
            this.markCount = mc;
        }
    }

    // ================= EXPORT =================

    private static void exportMarksheet() throws Exception {
        System.out.println("========== EXPORT MARKSHEET ==========");
        int studentId = readInt("Enter Student ID : ");

        Student s = studentService.findById(studentId);
        if (s == null) {
            System.out.println("[X] No student with ID " + studentId);
            return;
        }

        String content = buildMarksheetText(s);

        Path dir = Paths.get("exports");
        Files.createDirectories(dir);
        Path file = dir.resolve("marksheet_" + s.getRollNo() + ".txt");
        Files.writeString(file, content);

        System.out.println();
        System.out.println("[OK] Marksheet exported to:");
        System.out.println("     " + file.toAbsolutePath());
        System.out.println("======================================");
    }

    // ================= MARKSHEET BUILDER =================

    private static String buildMarksheetText(Student s) throws Exception {
        StringBuilder sb = new StringBuilder();
        Course c = courseService.findById(s.getCourseId());
        List<Marks> allMarks = marksService.getByStudent(s.getStudentId());

        sb.append("===============================================\n");
        sb.append("                 MARKSHEET\n");
        sb.append("===============================================\n");
        sb.append("Student ID   : ").append(s.getStudentId()).append("\n");
        sb.append("Roll No      : ").append(s.getRollNo()).append("\n");
        sb.append("Name         : ").append(s.getName()).append("\n");
        sb.append("Email        : ").append(s.getEmail()).append("\n");
        sb.append("Phone        : ").append(s.getPhone()).append("\n");
        sb.append("Course       : ").append(c != null ? c.getCourseName() : "N/A").append("\n");
        sb.append("-----------------------------------------------\n");

        if (allMarks.isEmpty()) {
            sb.append("No marks entered yet.\n");
            sb.append("===============================================\n");
            return sb.toString();
        }

        for (int sem = 1; sem <= 8; sem++) {
            List<Marks> semMarks = marksService.getByStudentAndSemester(s.getStudentId(), sem);
            if (semMarks.isEmpty()) continue;

            sb.append("\n>>> SEMESTER ").append(sem).append("\n");
            sb.append(String.format("%-22s | %-3s | %-8s | %-19s | %-5s%n",
                    "Subject", "Sem", "Exam", "Marks", "Grade"));
            sb.append("-----------------------+-----+----------+---------------------+------\n");
            for (Marks m : semMarks) sb.append(m).append("\n");

            double semPct = marksService.calculatePercentage(semMarks);
            sb.append("-----------------------+-----+----------+---------------------+------\n");
            sb.append(String.format("Semester %d  :  Percentage = %.2f%%  |  Grade = %s  |  %s%n",
                    sem, semPct, marksService.calculateGrade(semPct),
                    marksService.getResultStatus(semPct)));
        }

        double pct = marksService.calculatePercentage(allMarks);
        double attPct = attendanceService.calculatePercentage(s.getStudentId());
        boolean eligible = attendanceService.isEligibleForExam(s.getStudentId());

        sb.append("\n-----------------------------------------------\n");
        sb.append(String.format("OVERALL  : Percentage = %.2f%%  |  Grade = %s  |  %s%n",
                pct, marksService.calculateGrade(pct), marksService.getResultStatus(pct)));
        sb.append(String.format("ATTENDANCE : %.2f%%  |  Exam Eligible: %s%n",
                attPct, eligible ? "YES" : "NO"));
        sb.append("===============================================\n");
        return sb.toString();
    }

    // ================= HELPERS =================

    private static int readInt(String prompt) {
        while (true) {
            System.out.println(prompt);
            String line = sc.nextLine().trim();
            try { return Integer.parseInt(line); }
            catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.println(prompt);
            String line = sc.nextLine().trim();
            try { return Double.parseDouble(line); }
            catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }
}