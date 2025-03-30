package com.rmasci13.github.user;

import com.rmasci13.github.subscription.Subscription;
import com.rmasci13.github.subscription.SubscriptionDTO;
import com.rmasci13.github.subscription.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    @Autowired
    public UserService(UserRepository userRepository, SubscriptionService subscriptionService) {
        this.userRepository = userRepository;
        this.subscriptionService = subscriptionService;
    }

    //Get all users
    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDTO).
                collect(Collectors.toList());
    }



    //Private helper converting User to UserDTO,
    //utilizing private helper converting Subscription to SubscriptionDTO
    private UserDTO mapToUserDTO(User user) {
        List<SubscriptionDTO> subscriptionDTOs = user.getSubscriptions().stream()
                .map(this::mapToSubscriptionDTO)
                .collect(Collectors.toList());
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), subscriptionDTOs);
    }

    //Private helper converting Subscription to SubscriptionDTO
    private SubscriptionDTO mapToSubscriptionDTO(Subscription subscription) {
        LocalDate nextRenewalDate = subscriptionService.calculateNextRenewalDate(subscription);
        return new SubscriptionDTO(subscription, nextRenewalDate);
    }

}
