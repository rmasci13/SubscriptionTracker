package com.rmasci13.github.subscription;

import com.rmasci13.github.enums.BillingCycle;
import com.rmasci13.github.enums.PaymentMethod;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Subscription {
    @Id
    @SequenceGenerator(
            name = "subscription_id_sequence",
            sequenceName = "subscription_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subscription_id_sequence"
    )
    private Integer id;
    private String serviceName;
    private double cost;
    private BillingCycle billingCycle;
    private LocalDate nextRenewalDate;
    private String category;
    private PaymentMethod paymentMethod;

    public Subscription(Integer id, String serviceName, double cost, BillingCycle billingCycle, LocalDate nextRenewalDate, String category, PaymentMethod paymentMethod) {
        this.id = id;
        this.serviceName = serviceName;
        this.cost = cost;
        this.billingCycle = billingCycle;
        this.nextRenewalDate = nextRenewalDate;
        this.category = category;
        this.paymentMethod = paymentMethod;
    }

    public Subscription() {

    }

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

    public LocalDate getNextRenewalDate() {
        return nextRenewalDate;
    }

    public void setNextRenewalDate(LocalDate nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Double.compare(cost, that.cost) == 0 && Objects.equals(id, that.id) && Objects.equals(serviceName, that.serviceName) && billingCycle == that.billingCycle && Objects.equals(nextRenewalDate, that.nextRenewalDate) && Objects.equals(category, that.category) && paymentMethod == that.paymentMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceName, cost, billingCycle, nextRenewalDate, category, paymentMethod);
    }
}
