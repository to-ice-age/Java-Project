package edu.ccrm;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.impl.CourseServiceImpl;
import edu.ccrm.service.impl.StudentServiceImpl;
import edu.ccrm.service.impl.EnrollmentServiceImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize configuration and services
            AppConfig config = AppConfig.getInstance();
            Path dataDir = config.getDataFolderPath();
            
            // Create service instances
            StudentService studentService = new StudentServiceImpl();
            CourseService courseService = new CourseServiceImpl(studentService);
            EnrollmentService enrollmentService = new EnrollmentServiceImpl(studentService, courseService, config.getMaxCreditsPerSemester());
            
            // Initialize import/export service
            ImportExportService importExport = new ImportExportService(dataDir);

            System.out.println("Importing sample data...");
            
            // Import students and courses from CSV files
            List<Student> students = importExport.importStudents(Paths.get("test-data/students.csv"));
            List<Course> courses = importExport.importCourses(Paths.get("test-data/courses.csv"));

            // Add imported data to services
            for (Student student : students) {
                studentService.create(student);
            }
            for (Course course : courses) {
                courseService.create(course);
            }

            // Display imported data
            System.out.println("\nImported Students:");
            students.forEach(System.out::println);

            System.out.println("\nImported Courses:");
            courses.forEach(System.out::println);

            // Test enrollment
            System.out.println("\nTesting enrollment...");
            Student student = students.get(0);
            Course course = courses.get(0);
            
            enrollmentService.enrollStudent(student.getId(), course.getCode());
            System.out.println("Enrolled " + student.getFullName() + " in " + course.getTitle());

            // Record a grade
            System.out.println("\nRecording grades...");
            enrollmentService.recordGrade(student.getId(), course.getCode(), Grade.A);
            System.out.println("Recorded grade for " + student.getFullName());
            System.out.println("Current GPA: " + student.getGpa());

            System.out.println("\nDemo completed successfully!");

        } catch (Exception e) {
            System.err.println("Error running demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}