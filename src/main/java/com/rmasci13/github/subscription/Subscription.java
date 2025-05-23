package com.rmasci13.github.subscription;

import com.rmasci13.github.enums.BillingCycle;
import com.rmasci13.github.enums.Category;
import com.rmasci13.github.enums.Status;
import com.rmasci13.github.user.User;
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
    private Double cost;
    private BillingCycle billingCycle;
    private LocalDate lastPaymentDate;
    private Category category;
    private String paymentMethod;
    private Status status;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Subscription(String serviceName,
                        double cost, BillingCycle billingCycle,
                        LocalDate lastPaymentDate, Category category,
                        String paymentMethod, User user, Status status) {
        this.serviceName = serviceName;
        this.cost = cost;
        this.billingCycle = billingCycle;
        this.lastPaymentDate = lastPaymentDate;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.user = user;
        this.status = status;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(id, that.id) && Objects.equals(serviceName, that.serviceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceName, cost, billingCycle,
                lastPaymentDate, category, paymentMethod, status, user);
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
                ", status=" + status +
                ", user=" + user +
                '}';
    }
}
