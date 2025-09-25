package edu.ccrm.service.impl;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.StudentService;
import java.util.*;
import java.util.stream.Collectors;

public class StudentServiceImpl implements StudentService {
    private final Map<String, Student> students = new HashMap<>();
    private final EnrollmentService enrollmentService;

    public StudentServiceImpl(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @Override
    public Student create(Student student) {
        if (exists(student.getId())) {
            throw new IllegalArgumentException("Student already exists with ID: " + student.getId());
        }
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(students.get(id));
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    @Override
    public Student update(Student student) {
        if (!exists(student.getId())) {
            throw new IllegalArgumentException("Student not found with ID: " + student.getId());
        }
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public void delete(String id) {
        if (!exists(id)) {
            throw new IllegalArgumentException("Student not found with ID: " + id);
        }
        students.remove(id);
    }

    @Override
    public boolean exists(String id) {
        return students.containsKey(id);
    }

    @Override
    public List<Student> findByEnrolledCourseCode(String courseCode) {
        return students.values().stream()
            .filter(student -> student.getEnrolledCourses().stream()
                .anyMatch(course -> course.getCode().equals(courseCode)))
            .collect(Collectors.toList());
    }

    @Override
    public List<Student> findByGpaGreaterThan(double gpa) {
        return students.values().stream()
            .filter(student -> student.getGpa() > gpa)
            .collect(Collectors.toList());
    }

    @Override
    public void deactivateStudent(String studentId) {
        findById(studentId).ifPresent(student -> {
            student.setActive(false);
            update(student);
        });
    }

    @Override
    public List<Student> searchByName(String nameQuery) {
        String query = nameQuery.toLowerCase();
        return students.values().stream()
            .filter(student -> student.getFullName().toLowerCase().contains(query))
            .collect(Collectors.toList());
    }

    @Override
    public void updateGpa(String studentId) {
        findById(studentId).ifPresent(student -> {
            double newGpa = calculateGpa(studentId);
            student.setGpa(newGpa);
            update(student);
        });
    }

    @Override
    public double calculateGpa(String studentId) {
        Student student = findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        List<Course> completedCourses = student.getEnrolledCourses();
        if (completedCourses.isEmpty()) {
            return 0.0;
        }

        double totalPoints = 0.0;
        int totalCredits = 0;

        for (Course course : completedCourses) {
            Grade grade = enrollmentService.getStudentGrade(studentId, course.getCode());
            if (grade != null) {
                totalPoints += grade.getPoints() * course.getCredits();
                totalCredits += course.getCredits();
            }
        }

        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }
}