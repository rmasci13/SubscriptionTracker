package com.rmasci13.github.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path="/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Get all users API call. Only ADMIN roles authorized
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    //Get user by id. Only ADMIN role or user themselves authorized
    @GetMapping(path="{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) {
        UserDTO user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    //Post request for creating new user
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequestDTO user) {
        UserDTO created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    //Put request for editing user entity. Only ADMIN role or user themselves authorized
    @PutMapping(path="{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserRequestDTO user) {
        UserDTO updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    //Delete request for deleting user entity. Only ADMIN role or user themselves authorized
    @DeleteMapping(path="{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }



}
