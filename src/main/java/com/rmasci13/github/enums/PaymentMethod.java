package com.rmasci13.github.enums;

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
