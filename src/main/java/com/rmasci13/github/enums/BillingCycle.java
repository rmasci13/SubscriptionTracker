package com.rmasci13.github.enums;

public enum BillingCycle {
    MONTHLY(1, "Monthly", 12),
    QUARTERLY(3, "Quarterly", 4),
    SEMI_ANNUALLY(6, "Semi-annually", 2),
    ANNUALLY(12, "Annually", 1);

    private final int months;
    private final String displayName;
    private final int occurrencesPerYear;

    BillingCycle(int months, String displayName, int occurrencesPerYear) {
        this.months = months;
        this.displayName = displayName;
        this.occurrencesPerYear = occurrencesPerYear;
    }

    public int getMonths() {
        return months;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getOccurrencesPerYear() {
        return occurrencesPerYear;
    }
}
