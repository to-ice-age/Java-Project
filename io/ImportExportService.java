package edu.ccrm.io;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Semester;
import edu.ccrm.exception.EntityNotFoundException;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportExportService {
    private static final String CSV_DELIMITER = ",";
    private final Path dataDirectory;
    private final DateTimeFormatter timestampFormat;

    public ImportExportService(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.timestampFormat = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        createDirectoryIfNotExists(dataDirectory);
    }

    // Import methods
    public List<Student> importStudents(Path filePath) throws IOException {
        List<Student> students = new ArrayList<>();
        try (Stream<String> lines = Files.lines(filePath)) {
            Iterator<String> iterator = lines.iterator();
            if (iterator.hasNext()) {
                iterator.next(); // Skip header
            }
            while (iterator.hasNext()) {
                String[] data = iterator.next().split(CSV_DELIMITER);
                if (data.length >= 5) {
                    Student student = new Student(
                        data[0].trim(), // id
                        data[2].trim() + " " + data[3].trim(), // fullName
                        data[4].trim(), // email
                        data[1].trim()  // regNo
                    );
                    students.add(student);
                }
            }
        }
        return students;
    }

    public List<Course> importCourses(Path filePath) throws IOException {
        List<Course> courses = new ArrayList<>();
        try (Stream<String> lines = Files.lines(filePath)) {
            Iterator<String> iterator = lines.iterator();
            if (iterator.hasNext()) {
                iterator.next(); // Skip header
            }
            while (iterator.hasNext()) {
                String[] data = iterator.next().split(CSV_DELIMITER);
                if (data.length >= 6) {
                    Course course = new Course.Builder(data[0].trim()) // code
                        .title(data[1].trim())
                        .credits(Integer.parseInt(data[2].trim()))
                        .semester(Semester.valueOf(data[4].trim()))
                        .department(data[5].trim())
                        .build();
                    courses.add(course);
                }
            }
        }
        return courses;
    }

    // Export methods
    public void exportStudents(List<Student> students, Path filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("id,regNo,firstName,lastName,email,status");
        
        for (Student student : students) {
            String[] names = student.getFullName().split(" ", 2);
            String firstName = names[0];
            String lastName = names.length > 1 ? names[1] : "";
            
            lines.add(String.format("%s,%s,%s,%s,%s,%s",
                student.getId(),
                student.getRegNo(),
                firstName,
                lastName,
                student.getEmail(),
                student.isActive() ? "ACTIVE" : "INACTIVE"
            ));
        }
        
        Files.write(filePath, lines);
    }

    public void exportCourses(List<Course> courses, Path filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("code,title,credits,instructor,semester,department");
        
        for (Course course : courses) {
            lines.add(String.format("%s,%s,%d,%s,%s,%s",
                course.getCode(),
                course.getTitle(),
                course.getCredits(),
                course.getInstructor() != null ? course.getInstructor().getFullName() : "TBD",
                course.getSemester(),
                course.getDepartment()
            ));
        }
        
        Files.write(filePath, lines);
    }

    public void exportEnrollments(Map<Student, List<Course>> enrollments, Path filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("studentId,regNo,courseCode,semester");
        
        for (Map.Entry<Student, List<Course>> entry : enrollments.entrySet()) {
            Student student = entry.getKey();
            for (Course course : entry.getValue()) {
                lines.add(String.format("%s,%s,%s,%s",
                    student.getId(),
                    student.getRegNo(),
                    course.getCode(),
                    course.getSemester()
                ));
            }
        }
        
        Files.write(filePath, lines);
    }

    public void exportGrades(Map<String, Map<String, Grade>> grades, Path filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("studentId,courseCode,grade,points");
        
        for (Map.Entry<String, Map<String, Grade>> studentGrades : grades.entrySet()) {
            String studentId = studentGrades.getKey();
            for (Map.Entry<String, Grade> courseGrade : studentGrades.getValue().entrySet()) {
                String courseCode = courseGrade.getKey();
                Grade grade = courseGrade.getValue();
                lines.add(String.format("%s,%s,%s,%.1f",
                    studentId,
                    courseCode,
                    grade.name(),
                    grade.getPoints()
                ));
            }
        }
        
        Files.write(filePath, lines);
    }

    private void createDirectoryIfNotExists(Path directory) {
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create directory: " + directory, e);
        }
    }
}