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

    //Get User by Username, required by UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found:" + username));
    }

    //Create a new User, for POST requests
    public UserDTO createUser(UserRequestDTO userRequestDTO) {
        Optional<User> userOptional = userRepository.findByUsername(userRequestDTO.username());
        if (userOptional.isPresent()) {
            throw new ForbiddenException("User with username " + userRequestDTO.username() + " already exists");
        }
        User user = mapUserRequestDTOToUserEnt(userRequestDTO);
        User created = userRepository.save(user);
        return mapToUserDTO(created);
    }

    //Update user, for PUT requests
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

    //Delete user, for DELETE requests
    public void deleteUser(@PathVariable Integer id) {
        User user = findByUserId(id);
        userRepository.delete(user);
    }

//------------------------------------------------------------------------PRIVATE HELPER METHODS------------------------------------------------------------------------

    //Helper converting User to UserDTO,
    //utilizing helper converting Subscription to SubscriptionDTO
    public UserDTO mapToUserDTO(User user) {
        List<SubscriptionDTO> subscriptionDTOs;
        if (user.getSubscriptions() != null) {
            subscriptionDTOs = user.getSubscriptions().stream()
                    .map(this::mapSubEntToSubDTO)
                    .toList();
        }
        else {
            subscriptionDTOs = new ArrayList<SubscriptionDTO>();
        }
        return new UserDTO(user.getUsername(), user.getEmail(), subscriptionDTOs);
    }

    //Convert Subscription to SubscriptionDTO
    public SubscriptionDTO mapSubEntToSubDTO(Subscription subscription) {
        LocalDate nextRenewalDate = subscriptionService.calculateNextRenewalDate(subscription);
        return new SubscriptionDTO(subscription, nextRenewalDate);
    }

    //Convert UserRequestDTO to a User, used in creation of new User
    public User mapUserRequestDTOToUserEnt(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.username());
        user.setEmail(userRequestDTO.email());
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        return user;
    }

    //Find a User by a given ID
    public User findByUserId(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("User not found with ID: " + id));
    }

    //Find a User by a given username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
