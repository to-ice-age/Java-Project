package edu.ccrm.service.impl;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Semester;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.StudentService;
import java.util.*;
import java.util.stream.Collectors;

public class CourseServiceImpl implements CourseService {
    private final Map<String, Course> courses = new HashMap<>();
    private final StudentService studentService;

    public CourseServiceImpl(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public Course create(Course course) {
        if (exists(course.getCode())) {
            throw new IllegalArgumentException("Course already exists with code: " + course.getCode());
        }
        courses.put(course.getCode(), course);
        return course;
    }

    @Override
    public Optional<Course> findById(String code) {
        return Optional.ofNullable(courses.get(code));
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }

    @Override
    public Course update(Course course) {
        if (!exists(course.getCode())) {
            throw new IllegalArgumentException("Course not found with code: " + course.getCode());
        }
        courses.put(course.getCode(), course);
        return course;
    }

    @Override
    public void delete(String code) {
        if (!exists(code)) {
            throw new IllegalArgumentException("Course not found with code: " + code);
        }
        courses.remove(code);
    }

    @Override
    public boolean exists(String code) {
        return courses.containsKey(code);
    }

    @Override
    public List<Course> findByDepartment(String department) {
        return courses.values().stream()
            .filter(course -> course.getDepartment().equalsIgnoreCase(department))
            .collect(Collectors.toList());
    }

    @Override
    public List<Course> findBySemester(Semester semester) {
        return courses.values().stream()
            .filter(course -> course.getSemester() == semester)
            .collect(Collectors.toList());
    }

    @Override
    public List<Course> findByInstructor(String instructorId) {
        return courses.values().stream()
            .filter(course -> course.getInstructor() != null &&
                            course.getInstructor().getId().equals(instructorId))
            .collect(Collectors.toList());
    }

    @Override
    public void assignInstructor(String courseCode, String instructorId) {
        Course course = findById(courseCode)
            .orElseThrow(() -> new IllegalArgumentException("Course not found with code: " + courseCode));
        // Note: In a real implementation, would look up instructor from InstructorService
        course.setInstructor(null); // Temporary: proper implementation would set actual instructor
        update(course);
    }

    @Override
    public void deactivateCourse(String courseCode) {
        findById(courseCode).ifPresent(course -> {
            course.setActive(false);
            update(course);
        });
    }

    @Override
    public List<Course> searchByTitle(String titleQuery) {
        String query = titleQuery.toLowerCase();
        return courses.values().stream()
            .filter(course -> course.getTitle().toLowerCase().contains(query))
            .collect(Collectors.toList());
    }

    @Override
    public List<Course> filterByCredits(int minCredits, int maxCredits) {
        return courses.values().stream()
            .filter(course -> course.getCredits() >= minCredits && 
                            course.getCredits() <= maxCredits)
            .collect(Collectors.toList());
    }

    @Override
    public List<Course> filterActiveByDepartmentAndSemester(String department, Semester semester) {
        return courses.values().stream()
            .filter(Course::isActive)
            .filter(course -> course.getDepartment().equalsIgnoreCase(department))
            .filter(course -> course.getSemester() == semester)
            .collect(Collectors.toList());
    }
}