package com.rmasci13.github.user;

import com.rmasci13.github.exception.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private UserService underTest;

    User mockUser1 = new User();
    User mockUser2 = new User();

    @BeforeEach
    void setUp() {
        mockUser1.setUsername("u");
        mockUser1.setEmail("e");
        mockUser1.setSubscriptions(new ArrayList<>());
        mockUser1.setId(1);
        mockUser2.setUsername("u2");
        mockUser2.setEmail("e2");
        mockUser2.setSubscriptions(new ArrayList<>());
    }

    @Test
    void canGetUsers() {
        // Given
        List<User> mockUserList = List.of(mockUser1, mockUser2);
        UserDTO mockUserDTO1 = new UserDTO("u", "e", new ArrayList<>());
        UserDTO mockUserDTO2 = new UserDTO("u2", "e2", new ArrayList<>());
        List<UserDTO> expectedUsers = List.of(mockUserDTO1, mockUserDTO2);

        // Mock the dependency
        when(userRepository.findAll()).thenReturn(mockUserList);

        // When
        List<UserDTO> result = underTest.getUsers();

        // Then
        verify(userRepository).findAll();
        assertEquals(expectedUsers, result);
        UserDTO returnedUserDTO1 = result.get(0);
        assertEquals(mockUserDTO1, returnedUserDTO1);
    }

    @Test
    void canGetUser() {
        // Given

        // Mock the dependency
    }

    @Test
    void canGetUserByUsername() {

    }

    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void canFindUserByUserId() {
        // Given mockUser1 initialized in @BeforeEach
        Integer id = mockUser1.getId();
        // Mock the dependency
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser1));
        // When
        User result = underTest.findByUserId(id);
        // Then
        verify(userRepository).findById(id);
        assertEquals(mockUser1, result);

    }

    @Test
    void willThrowExceptionWhenUserNotFoundById() {
        // Given
        Integer notFoundId = 999;
        // Mock the dependency
        when(userRepository.findById(notFoundId)).thenReturn(Optional.empty());
        // When
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> underTest.findByUserId(notFoundId)
        );
        // Then
        verify(userRepository).findById(notFoundId);
        assertEquals("User not found with ID: " + notFoundId, thrown.getMessage());
    }

    @Test
    void canFindUserByUsername() {
        // Given mockUser1 initialized in @BeforeEach
        String username = mockUser1.getUsername();
        // Mock the dependency
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser1));
        // When
        User result = underTest.findByUsername(username);
        // Then
        verify(userRepository).findByUsername(username);
        assertEquals(mockUser1, result);
    }

    @Test
    void willThrowExceptionIfUsernameNotFoundByUsername() {
        // Given
        String notFoundUsername = "no_such_username";
        // Mock the dependency
        when(userRepository.findByUsername(notFoundUsername)).thenReturn(Optional.empty());
        // When
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class,
                () -> underTest.findByUsername(notFoundUsername)
        );
        // Then
        verify(userRepository).findByUsername(notFoundUsername);
        assertEquals("User not found with username: " + notFoundUsername, thrown.getMessage());
    }
}