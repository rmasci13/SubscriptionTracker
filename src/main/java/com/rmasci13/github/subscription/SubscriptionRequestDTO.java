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

}
