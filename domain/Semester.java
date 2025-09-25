package edu.ccrm.domain;

public enum Semester {
    SPRING("Spring", 1),
    SUMMER("Summer", 2),
    FALL("Fall", 3);

    private final String name;
    private final int order;

    Semester(String name, int order) {
        this.name = name;
        this.order = order;
    }

    public String getName() { return name; }
    public int getOrder() { return order; }

    @Override
    public String toString() {
        return name;
    }
}