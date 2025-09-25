package edu.ccrm.service;

import edu.ccrm.domain.Student;
import java.util.List;

/**
 * Service interface for student-specific operations.
 */
public interface StudentService extends CrudService<Student, String> {
    List<Student> findByEnrolledCourseCode(String courseCode);
    List<Student> findByGpaGreaterThan(double gpa);
    void deactivateStudent(String studentId);
    List<Student> searchByName(String nameQuery);
    
    // GPA calculation methods
    void updateGpa(String studentId);
    double calculateGpa(String studentId);
}