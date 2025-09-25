package edu.ccrm.service.impl;

import edu.ccrm.domain.*;
import edu.ccrm.exception.*;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import java.util.*;
import java.util.stream.Collectors;

public class EnrollmentServiceImpl implements EnrollmentService {
    private final StudentService studentService;
    private final CourseService courseService;
    private final Map<String, Map<String, Grade>> grades;
    private final int maxCreditsPerSemester;

    public EnrollmentServiceImpl(StudentService studentService, CourseService courseService, int maxCreditsPerSemester) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.grades = new HashMap<>();
        this.maxCreditsPerSemester = maxCreditsPerSemester;
    }

    @Override
    public void enrollStudent(String studentId, String courseCode) {
        Student student = studentService.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        
        Course course = courseService.findById(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));
        
        if (isEnrolled(studentId, courseCode)) {
            throw new DuplicateEnrollmentException(
                String.format("Student %s is already enrolled in course %s", studentId, courseCode));
        }

        if (!hasPrerequisites(studentId, courseCode)) {
            throw new PrerequisiteNotMetException(studentId, courseCode, "Prerequisites");
        }

        int currentCredits = calculateTotalCredits(studentId, course.getSemester());
        if (currentCredits + course.getCredits() > maxCreditsPerSemester) {
            throw new MaxCreditLimitExceededException(
                "Enrolling in this course would exceed the maximum credits allowed per semester",
                currentCredits,
                maxCreditsPerSemester
            );
        }

        student.enrollInCourse(course);
        studentService.update(student);
    }

    @Override
    public void unenrollStudent(String studentId, String courseCode) {
        Student student = studentService.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        
        Course course = courseService.findById(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));
        
        if (!isEnrolled(studentId, courseCode)) {
            throw new IllegalStateException(
                String.format("Student %s is not enrolled in course %s", studentId, courseCode));
        }

        student.unenrollFromCourse(course);
        grades.computeIfPresent(studentId, (id, courseGrades) -> {
            courseGrades.remove(courseCode);
            return courseGrades;
        });
        
        studentService.update(student);
    }

    @Override
    public void recordGrade(String studentId, String courseCode, Grade grade) {
        if (!isEnrolled(studentId, courseCode)) {
            throw new IllegalStateException("Student is not enrolled in this course");
        }

        grades.computeIfAbsent(studentId, k -> new HashMap<>())
              .put(courseCode, grade);
        
        studentService.updateGpa(studentId);
    }

    @Override
    public List<Course> getEnrolledCourses(String studentId, Semester semester) {
        Student student = studentService.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));

        return student.getEnrolledCourses().stream()
            .filter(course -> course.getSemester() == semester)
            .collect(Collectors.toList());
    }

    @Override
    public List<Student> getEnrolledStudents(String courseCode) {
        courseService.findById(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));
            
        return studentService.findByEnrolledCourseCode(courseCode);
    }

    @Override
    public Grade getStudentGrade(String studentId, String courseCode) {
        if (!isEnrolled(studentId, courseCode)) {
            throw new IllegalStateException("Student is not enrolled in this course");
        }
        return grades.getOrDefault(studentId, Collections.emptyMap())
                    .get(courseCode);
    }

    @Override
    public Map<Course, Grade> getStudentGrades(String studentId, Semester semester) {
        Student student = studentService.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));

        Map<Course, Grade> semesterGrades = new HashMap<>();
        getEnrolledCourses(studentId, semester).forEach(course -> {
            Grade grade = getStudentGrade(studentId, course.getCode());
            if (grade != null) {
                semesterGrades.put(course, grade);
            }
        });
        return semesterGrades;
    }

    @Override
    public boolean canEnroll(String studentId, String courseCode) {
        try {
            Student student = studentService.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
            
            Course course = courseService.findById(courseCode)
                .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));
            
            if (isEnrolled(studentId, courseCode)) {
                return false;
            }

            if (!hasPrerequisites(studentId, courseCode)) {
                return false;
            }

            int currentCredits = calculateTotalCredits(studentId, course.getSemester());
            return currentCredits + course.getCredits() <= maxCreditsPerSemester;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int calculateTotalCredits(String studentId, Semester semester) {
        return getEnrolledCourses(studentId, semester).stream()
            .mapToInt(Course::getCredits)
            .sum();
    }

    @Override
    public boolean hasPrerequisites(String studentId, String courseCode) {
        // TODO: Implement prerequisite checking when prerequisites are added to Course
        return true;
    }

    @Override
    public Map<Grade, Long> getGradeDistribution(String courseCode) {
        courseService.findById(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));

        return grades.values().stream()
            .map(courseGrades -> courseGrades.get(courseCode))
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(
                grade -> grade,
                Collectors.counting()
            ));
    }

    @Override
    public double getAverageGrade(String courseCode) {
        courseService.findById(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));

        return grades.values().stream()
            .map(courseGrades -> courseGrades.get(courseCode))
            .filter(Objects::nonNull)
            .mapToDouble(Grade::getPoints)
            .average()
            .orElse(0.0);
    }

    @Override
    public List<Student> getTopPerformers(String courseCode, int limit) {
        courseService.findById(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));

        return grades.entrySet().stream()
            .filter(entry -> entry.getValue().containsKey(courseCode))
            .sorted((e1, e2) -> {
                Grade g1 = e1.getValue().get(courseCode);
                Grade g2 = e2.getValue().get(courseCode);
                return Double.compare(g2.getPoints(), g1.getPoints());
            })
            .limit(limit)
            .map(entry -> studentService.findById(entry.getKey())
                .orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private boolean isEnrolled(String studentId, String courseCode) {
        try {
            Student student = studentService.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
                
            return student.getEnrolledCourses().stream()
                .anyMatch(course -> course.getCode().equals(courseCode));
        } catch (Exception e) {
            return false;
        }
    }
}