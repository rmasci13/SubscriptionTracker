package com.rmasci13.github.subscription;

import com.rmasci13.github.enums.BillingCycle;
import com.rmasci13.github.exception.ForbiddenException;
import com.rmasci13.github.exception.ItemNotFoundException;
import com.rmasci13.github.user.User;
import com.rmasci13.github.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
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
        Subscription subscription = findBySubscriptionId(id);
        LocalDate nextRenewalDate = calculateNextRenewalDate(subscription);
        return new SubscriptionDTO(subscription, nextRenewalDate);
    }

    //Create new subscription
    public SubscriptionDTO createSubscription(SubscriptionDTO dto) {
        User user = findByUserId(dto.getUserID());
        Subscription subscription = mapToSubscription(dto, user);
        Subscription saved = subscriptionRepository.save(subscription);
        return convertToDTO(saved);
    }

    //Update subscription
    @Transactional
    public SubscriptionDTO updateSubscription(Integer id, SubscriptionDTO dto) {
        Subscription currentSubscription = findBySubscriptionId(id);
        if (dto.hasBillingCycle()) {
            currentSubscription.setBillingCycle(dto.getBillingCycle());
        }
        if (dto.hasLastPaymentDate()) {
            currentSubscription.setLastPaymentDate(dto.getLastPaymentDate());
        }
        if (dto.hasCategory()) {
            currentSubscription.setCategory(dto.getCategory());
        }
        if (dto.hasCost()) {
            currentSubscription.setCost(dto.getCost());
        }
        if (dto.hasPaymentMethod()) {
            currentSubscription.setPaymentMethod(dto.getPaymentMethod());
        }
        Subscription saved = subscriptionRepository.save(currentSubscription);
        return convertToDTO(saved);
    }

    public void deleteSubscription(Integer id) {
        Subscription subscription = findBySubscriptionId(id);
        subscriptionRepository.delete(subscription);
    }

//Business Logic Methods

    //Convert SubscriptionDTO to Subscription
    private Subscription mapToSubscription(SubscriptionDTO dto, User user) {
        Subscription subscription = new Subscription();
        subscription.setServiceName(dto.getServiceName());
        subscription.setCost(dto.getCost());
        subscription.setBillingCycle(dto.getBillingCycle());
        subscription.setLastPaymentDate(dto.getLastPaymentDate());
        subscription.setCategory(dto.getCategory());
        subscription.setPaymentMethod(dto.getPaymentMethod());
        subscription.setUser(user);
        return subscription;
    }

    //Convert Subscription to SubscriptionDTO
    public SubscriptionDTO convertToDTO(Subscription entity) {
        LocalDate nextRenewalDate = calculateNextRenewalDate(entity);
        return new SubscriptionDTO(entity, nextRenewalDate);
    }

    //Calculate next renewal date based on last payment date
    public LocalDate calculateNextRenewalDate(Subscription subscription) {
        LocalDate lastPaymentDate = subscription.getLastPaymentDate();
        BillingCycle billingCycle = subscription.getBillingCycle();

        return switch (billingCycle) {
            case MONTHLY -> lastPaymentDate.plusMonths(1);
            case QUARTERLY -> lastPaymentDate.plusMonths(3);
            case SEMI_ANNUALLY -> lastPaymentDate.plusMonths(6);
            case ANNUALLY -> lastPaymentDate.plusYears(1);
        };
    }

    private Subscription findBySubscriptionId(Integer id) {
        return subscriptionRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Subscription not found with ID: " + id));
    }

    private User findByUserId(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("User not found with ID: " + id));
    }

    public boolean isOwner(Integer userId, Integer subscriptionId) {
        Subscription subscription = findBySubscriptionId(subscriptionId);
        return userId.equals(subscription.getUser().getId());
    }

}
