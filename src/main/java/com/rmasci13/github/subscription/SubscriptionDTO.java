package com.rmasci13.github.subscription;

import com.rmasci13.github.enums.BillingCycle;
import com.rmasci13.github.enums.Category;
import com.rmasci13.github.enums.PaymentMethod;

import java.time.LocalDate;

public class SubscriptionDTO {
    private Integer id;
    private String serviceName;
    private Double cost;
    private BillingCycle billingCycle;
    private LocalDate lastPaymentDate;
    private LocalDate nextRenewalDate; // Calculated field
    private Category category;
    private PaymentMethod paymentMethod;

    //Default Constructor
    public SubscriptionDTO() {}

    //Constructor with subscription entity and calculated nextRenewalDate
    public SubscriptionDTO(Subscription subscription, LocalDate nextRenewalDate) {
        this.id = subscription.getId();
        this.serviceName = subscription.getServiceName();
        this.cost = subscription.getCost();
        this.billingCycle = subscription.getBillingCycle();
        this.lastPaymentDate = subscription.getLastPaymentDate();
        this.nextRenewalDate = nextRenewalDate;
        this.category = subscription.getCategory();
        this.paymentMethod = subscription.getPaymentMethod();
    }

    //Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public BillingCycle getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(BillingCycle billingCycle) {
        this.billingCycle = billingCycle;
    }

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(LocalDate lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public LocalDate getNextRenewalDate() {
        return nextRenewalDate;
    }

    public void setNextRenewalDate(LocalDate nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    //"Has" Methods
    public boolean hasPaymentMethod() { return paymentMethod != null; }

    public boolean hasBillingCycle() { return billingCycle != null; }

    public boolean hasLastPaymentDate() { return lastPaymentDate != null; }

    public boolean hasCategory() { return category != null; }

    public boolean hasCost() { return cost != null; }
}
