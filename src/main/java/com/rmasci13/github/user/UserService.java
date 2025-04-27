package com.rmasci13.github.user;

import com.rmasci13.github.exception.ForbiddenException;
import com.rmasci13.github.exception.ItemNotFoundException;
import com.rmasci13.github.subscription.Subscription;
import com.rmasci13.github.subscription.SubscriptionDTO;
import com.rmasci13.github.subscription.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final PasswordEncoder passwordEncoder;

    //Constructor
    @Autowired
    public UserService(UserRepository userRepository, SubscriptionService subscriptionService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.subscriptionService = subscriptionService;
        this.passwordEncoder = passwordEncoder;
    }

    //Get all users
    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDTO).
                collect(Collectors.toList());
    }

    //Get single user
    public UserDTO getUser(Integer id) {
        User user = findByUserId(id);
        return mapToUserDTO(user);
    }

    //Get current user's DTO
    public UserDTO getUserByUsername(String username) {
        User user = findByUsername(username);
        return mapToUserDTO(user);
    }

    //Create a new User
    public UserDTO createUser(UserRequestDTO userRequestDTO) {
        Optional<User> userOptional = userRepository.findByUsername(userRequestDTO.username());
        if (userOptional.isPresent()) {
            throw new ForbiddenException("User with username " + userRequestDTO.username() + " already exists");
        }
        User user = mapToUser(userRequestDTO);
        User created = userRepository.save(user);
        return mapToUserDTO(created);
    }

    //Update user
    public UserDTO updateUser(@PathVariable Integer id, UserRequestDTO userRequestDTO) {
        User user = findByUserId(id);
        if (userRequestDTO.hasUsername()) {
            user.setUsername(userRequestDTO.username());
        }
        if (userRequestDTO.hasPassword()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        }
        if (userRequestDTO.hasEmail()) {
            user.setEmail(userRequestDTO.email());
        }
        User updated = userRepository.save(user);
        return mapToUserDTO(user);
    }

    //Delete user
    public void deleteUser(@PathVariable Integer id) {
        User user = findByUserId(id);
        userRepository.delete(user);
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
        return new UserDTO(user.getUsername(), user.getEmail(), subscriptionDTOs);
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
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        return user;
    }

    private User findByUserId(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("User not found with ID: " + id));
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found:" + username));
    }
}
