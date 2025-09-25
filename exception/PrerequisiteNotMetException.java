package edu.ccrm.exception;

public class PrerequisiteNotMetException extends RuntimeException {
    private final String studentId;
    private final String courseCode;
    private final String prerequisiteCourse;

    public PrerequisiteNotMetException(String studentId, String courseCode, String prerequisiteCourse) {
        super(String.format("Student %s cannot enroll in %s: prerequisite %s not completed", 
                          studentId, courseCode, prerequisiteCourse));
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.prerequisiteCourse = prerequisiteCourse;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getPrerequisiteCourse() {
        return prerequisiteCourse;
    }
}