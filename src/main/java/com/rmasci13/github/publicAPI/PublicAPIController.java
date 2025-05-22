package com.rmasci13.github.publicAPI;

import com.rmasci13.github.subscription.SubscriptionDTO;
import com.rmasci13.github.subscription.SubscriptionRequestDTO;
import com.rmasci13.github.subscription.SubscriptionService;
import com.rmasci13.github.user.UserDTO;
import com.rmasci13.github.user.UserRequestDTO;
import com.rmasci13.github.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;


@RestController
@RequestMapping(path="/public/api")
public class PublicAPIController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public PublicAPIController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    //Publicly accessible API call to retrieve the current User's DTO with List of Subscriptions
    @GetMapping("user/me")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        String username = principal.getName();
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    //Publicly accessible API POST to allow a new User to sign up
    @PostMapping("user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequestDTO user) {
        UserDTO created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Publicly accessible API POST call to create a new Subscription
    @PostMapping("subscription")
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestBody SubscriptionRequestDTO dto, Principal principal) {
        String username = principal.getName();
        SubscriptionDTO created = subscriptionService.createSubscriptionWithUsername(dto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
