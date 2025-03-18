package com.rmasci13.github.subscription;

import com.rmasci13.github.enums.BillingCycle;
import com.rmasci13.github.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    //Get all subscriptions
    public List<SubscriptionDTO> getSubscriptionDTOs() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptions.stream()
                .map(subscription -> {
                    LocalDate nextRenewalDate = calculateNextRenewalDate(subscription);
                    return new SubscriptionDTO(subscription, nextRenewalDate);
        }).collect(Collectors.toList());
    }

    //Get subscription by ID
    public SubscriptionDTO getSubscriptionDTOsById(Integer id) {
        Subscription subscription = findById(id);
        LocalDate nextRenewalDate = calculateNextRenewalDate(subscription);
        return new SubscriptionDTO(subscription, nextRenewalDate);
    }

    //Create new subscription
    public SubscriptionDTO createSubscription(SubscriptionDTO dto) {
        Subscription subscription = convertToEntity(dto);
        Subscription saved = subscriptionRepository.save(subscription);
        return convertToDTO(saved);
    }

    //Update subscription
    public SubscriptionDTO updateSubscription(Integer id, SubscriptionDTO dto) {
        Subscription currentSubscription = findById(id);
        currentSubscription.setServiceName(dto.getServiceName());
        currentSubscription.setBillingCycle(dto.getBillingCycle());
        currentSubscription.setCost(dto.getCost());
        currentSubscription.setLastPaymentDate(dto.getLastPaymentDate());
        currentSubscription.setCategory(dto.getCategory());
        currentSubscription.setPaymentMethod(dto.getPaymentMethod());

        Subscription saved = subscriptionRepository.save(currentSubscription);
        return convertToDTO(saved);
    }

    public void deleteSubscription(Integer id) {
        Subscription subscription = findById(id);
        subscriptionRepository.delete(subscription);
    }

//Business Logic Methods

    //Convert SubscriptionDTO to Subscription
    private Subscription convertToEntity(SubscriptionDTO dto) {
        Subscription subscription = new Subscription();
        if (dto.getId() != null) {
            subscription.setId(dto.getId());
        }
        subscription.setServiceName(dto.getServiceName());
        subscription.setCost(dto.getCost());
        subscription.setBillingCycle(dto.getBillingCycle());
        subscription.setLastPaymentDate(dto.getLastPaymentDate());
        subscription.setCategory(dto.getCategory());
        subscription.setPaymentMethod(dto.getPaymentMethod());
        return subscription;
    }

    //Convert Subscription to SubscriptionDTO
    private SubscriptionDTO convertToDTO(Subscription entity) {
        LocalDate nextRenewalDate = calculateNextRenewalDate(entity);
        return new SubscriptionDTO(entity, nextRenewalDate);
    }

    //Calculate next renewal date based on last payment date
    private LocalDate calculateNextRenewalDate(Subscription subscription) {
        LocalDate lastPaymentDate = subscription.getLastPaymentDate();
        BillingCycle billingCycle = subscription.getBillingCycle();

        return switch (billingCycle) {
            case MONTHLY -> lastPaymentDate.plusMonths(1);
            case QUARTERLY -> lastPaymentDate.plusMonths(3);
            case SEMI_ANNUALLY -> lastPaymentDate.plusMonths(6);
            case ANNUALLY -> lastPaymentDate.plusYears(1);
        };
    }

    private Subscription findById(Integer id) {
        return subscriptionRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found with ID: " + id));
    }

}
