package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Semester;
import java.util.List;
import java.util.Map;

/**
 * Service interface for generating and managing academic transcripts.
 */
public interface TranscriptService {
    // Basic transcript operations
    String generateTranscript(String studentId);
    String generateSemesterTranscript(String studentId, Semester semester);
    void exportTranscript(String studentId, String filePath);
    
    // Academic performance analysis
    double calculateSemesterGpa(String studentId, Semester semester);
    double calculateCumulativeGpa(String studentId);
    List<Course> getCompletedCourses(String studentId);
    Map<Semester, Double> getGpaProgression(String studentId);
    
    // Summary statistics
    int getTotalCreditsCompleted(String studentId);
    int getTotalCreditsPending(String studentId);
    Map<Grade, Integer> getGradeDistribution(String studentId);
    
    // Academic standing
    boolean isEligibleForGraduation(String studentId);
    boolean isOnProbation(String studentId);
    String getAcademicStanding(String studentId);
}