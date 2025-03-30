package com.rmasci13.github.user;

import com.rmasci13.github.exception.ForbiddenException;
import com.rmasci13.github.subscription.Subscription;
import com.rmasci13.github.subscription.SubscriptionDTO;
import com.rmasci13.github.subscription.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    //Create a User
    public UserDTO createUser(UserRequestDTO userRequestDTO) {
        Optional<User> userOptional = userRepository.findByUsername(userRequestDTO.username());
        if (userOptional.isPresent()) {
            throw new ForbiddenException("User with username " + userRequestDTO.username() + " already exists");
        }
        User user = mapToUser(userRequestDTO);
        User created = userRepository.save(user);
        return mapToUserDTO(created);
    }


    //Private helper converting User to UserDTO,
    //utilizing private helper converting Subscription to SubscriptionDTO
    private UserDTO mapToUserDTO(User user) {
        List<SubscriptionDTO> subscriptionDTOs;
        if (user.getSubscriptions() != null) {
            subscriptionDTOs = user.getSubscriptions().stream()
                    .map(this::mapToSubscriptionDTO)
                    .toList();
        }
        else {
            subscriptionDTOs = new ArrayList<SubscriptionDTO>();
        }
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), subscriptionDTOs);
    }

    //Private helper converting Subscription to SubscriptionDTO
    private SubscriptionDTO mapToSubscriptionDTO(Subscription subscription) {
        LocalDate nextRenewalDate = subscriptionService.calculateNextRenewalDate(subscription);
        return new SubscriptionDTO(subscription, nextRenewalDate);
    }

    private User mapToUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.username());
        user.setEmail(userRequestDTO.email());
        user.setPassword(userRequestDTO.password());
        return user;
    }

}
