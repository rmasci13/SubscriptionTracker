package com.rmasci13.github.subscription;

import com.rmasci13.github.enums.BillingCycle;
import com.rmasci13.github.enums.Category;
import com.rmasci13.github.enums.PaymentMethod;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Subscription {
    @Id
    @SequenceGenerator(
            name = "subscription_id_sequence",
            sequenceName = "subscription_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subscription_id_sequence"
    )
    private Integer id;
    private String serviceName;
    private double cost;
    private BillingCycle billingCycle;
    private LocalDate lastPaymentDate;
    private Category category;
    private PaymentMethod paymentMethod;

    public Subscription(Integer id, String serviceName, double cost, BillingCycle billingCycle, LocalDate lastPaymentDate, Category category, PaymentMethod paymentMethod) {
        this.id = id;
        this.serviceName = serviceName;
        this.cost = cost;
        this.billingCycle = billingCycle;
        this.lastPaymentDate = lastPaymentDate;
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

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(LocalDate lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Double.compare(cost, that.cost) == 0 && Objects.equals(id, that.id) && Objects.equals(serviceName, that.serviceName) && billingCycle == that.billingCycle && Objects.equals(lastPaymentDate, that.lastPaymentDate) && Objects.equals(category, that.category) && paymentMethod == that.paymentMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceName, cost, billingCycle, lastPaymentDate, category, paymentMethod);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", cost=" + cost +
                ", billingCycle=" + billingCycle +
                ", lastPaymentDate=" + lastPaymentDate +
                ", category='" + category + '\'' +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}
