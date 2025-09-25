package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Instructor extends Person {
    private final String department;
    private final List<Course> assignedCourses;
    private final LocalDateTime joinDate;
    private String specialization;

    public Instructor(String id, String fullName, String email, String department) {
        super(id, fullName, email);
        this.department = department;
        this.assignedCourses = new ArrayList<>();
        this.joinDate = LocalDateTime.now();
    }

    @Override
    public String getRole() {
        return "Instructor";
    }

    public String getDepartment() {
        return department;
    }

    public List<Course> getAssignedCourses() {
        return Collections.unmodifiableList(assignedCourses);
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void assignCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
            course.setInstructor(this);
        }
    }

    public void unassignCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (assignedCourses.remove(course) && course.getInstructor() == this) {
            course.setInstructor(null);
        }
    }

    @Override
    public String toString() {
        return String.format("%s [Department: %s, Courses: %d, Specialization: %s]",
            super.toString(), department, assignedCourses.size(), 
            specialization != null ? specialization : "Not specified");
    }
}