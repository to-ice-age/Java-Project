package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student extends Person {
    private final String regNo;
    private final List<Course> enrolledCourses;
    private final LocalDateTime enrollmentDate;
    private double gpa;

    public Student(String id, String fullName, String email, String regNo) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.enrolledCourses = new ArrayList<>();
        this.enrollmentDate = LocalDateTime.now();
        this.gpa = 0.0;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public String getRegNo() {
        return regNo;
    }

    public List<Course> getEnrolledCourses() {
        return Collections.unmodifiableList(enrolledCourses);
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        if (gpa < 0.0 || gpa > 4.3) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.3");
        }
        this.gpa = gpa;
    }

    public void enrollInCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }

    public void unenrollFromCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        enrolledCourses.remove(course);
    }

    @Override
    public String toString() {
        return String.format("%s [RegNo: %s, GPA: %.2f, Enrolled Courses: %d]", 
            super.toString(), regNo, gpa, enrolledCourses.size());
    }
}