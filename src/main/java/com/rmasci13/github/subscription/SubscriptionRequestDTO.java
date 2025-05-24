package com.rmasci13.github.subscription;

import com.rmasci13.github.enums.BillingCycle;
import com.rmasci13.github.enums.Category;
import com.rmasci13.github.enums.Status;


import java.time.LocalDate;

public record SubscriptionRequestDTO(
        String serviceName,
        Double cost,
        BillingCycle billingCycle,
        LocalDate lastPaymentDate,
        Category category,
        String paymentMethod,
        Status status
) {
    public boolean hasServiceName() {
        return serviceName != null;
    }
    public boolean hasCost() {
        return cost != null;
    }
    public boolean hasBillingCycle() {
        return billingCycle != null;
    }
    public boolean hasLastPaymentDate() {
        return lastPaymentDate != null;
    }
    public boolean hasCategory() {
        return category != null;
    }
    public boolean hasPaymentMethod() {
        return paymentMethod != null;
    }
    public boolean hasStatus() {
        return status != null;
    }
}
