package com.rmasci13.github.publicUser;

import com.rmasci13.github.user.UserDTO;
import com.rmasci13.github.user.UserRequestDTO;
import com.rmasci13.github.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;


@RestController
@RequestMapping(path="/public/api/user")
public class PublicUserController {

    private final UserService userService;

    @Autowired
    public PublicUserController(UserService userService) {
        this.userService = userService;
    }

    //Publicly accessible API call to retrieve the current User's DTO with List of Subscriptions
    @GetMapping("me")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        String username = principal.getName();
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    //Publicly accessible API POST to allow a new User to sign up
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequestDTO user) {
        UserDTO created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
