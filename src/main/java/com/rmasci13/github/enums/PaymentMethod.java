package com.rmasci13.github.enums;

// Deprecated. Decided to use a String for Payment Method
// as its more convenient for the user to enter whatever theyd like
public enum PaymentMethod {
    CREDIT_CARD( "Credit Card"),
    PAYPAL( "PayPal");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
