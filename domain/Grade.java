package edu.ccrm.domain;

public enum Grade {
    S(4.3, "Outstanding"),
    A(4.0, "Excellent"),
    B(3.0, "Good"),
    C(2.0, "Satisfactory"),
    D(1.0, "Poor"),
    F(0.0, "Fail");

    private final double points;
    private final String description;

    Grade(double points, String description) {
        this.points = points;
        this.description = description;
    }

    public double getPoints() { return points; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("%s (%s, %.1f points)", name(), description, points);
    }
}