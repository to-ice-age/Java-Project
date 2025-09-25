package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Semester;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing course enrollments and grades.
 */
public interface EnrollmentService {
    void enrollStudent(String studentId, String courseCode);
    void unenrollStudent(String studentId, String courseCode);
    void recordGrade(String studentId, String courseCode, Grade grade);
    
    // Query methods
    List<Course> getEnrolledCourses(String studentId, Semester semester);
    List<Student> getEnrolledStudents(String courseCode);
    Grade getStudentGrade(String studentId, String courseCode);
    Map<Course, Grade> getStudentGrades(String studentId, Semester semester);
    
    // Validation methods
    boolean canEnroll(String studentId, String courseCode);
    int calculateTotalCredits(String studentId, Semester semester);
    boolean hasPrerequisites(String studentId, String courseCode);
    
    // Reports and analytics
    Map<Grade, Long> getGradeDistribution(String courseCode);
    double getAverageGrade(String courseCode);
    List<Student> getTopPerformers(String courseCode, int limit);
}