package edu.ccrm.exception;

public class MaxCreditLimitExceededException extends RuntimeException {
    private final int currentCredits;
    private final int maxCredits;

    public MaxCreditLimitExceededException(String message, int currentCredits, int maxCredits) {
        super(message);
        this.currentCredits = currentCredits;
        this.maxCredits = maxCredits;
    }

    public int getCurrentCredits() {
        return currentCredits;
    }

    public int getMaxCredits() {
        return maxCredits;
    }
}