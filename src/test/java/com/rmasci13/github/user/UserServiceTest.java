package com.rmasci13.github.user;

import com.rmasci13.github.enums.BillingCycle;
import com.rmasci13.github.enums.Category;
import com.rmasci13.github.enums.Status;
import com.rmasci13.github.exception.ItemNotFoundException;
import com.rmasci13.github.subscription.Subscription;
import com.rmasci13.github.subscription.SubscriptionDTO;
import com.rmasci13.github.subscription.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private UserService underTest;

    User mockUser1 = new User();
    User mockUser2 = new User();
    Subscription mockSub1;
    Subscription mockSub2;

    @BeforeEach
    void setUp() {
        mockUser1.setUsername("u");
        mockUser1.setEmail("e");
        mockUser1.setPassword("p");
        mockUser1.setSubscriptions(new ArrayList<>());
        mockUser1.setId(1);
        mockUser2.setUsername("u2");
        mockUser2.setEmail("e2");
        mockUser2.setPassword("p2");
        mockUser2.setSubscriptions(new ArrayList<>());
        mockSub1 = new Subscription("Netflix", 10.99, BillingCycle.MONTHLY,
                LocalDate.of(2020,1,1), Category.STREAMING,
                "AMEX", mockUser1, Status.ACTIVE);
        mockSub2 = new Subscription("Factor", 99.99, BillingCycle.ANNUALLY,
                LocalDate.of(2020,1,1), Category.FOOD,
                "VISA", mockUser1, Status.ACTIVE);
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
    void canGetUserById() {
        // Given mockUser1 from BeforeEach setUp()
        Integer mockUserId = mockUser1.getId();
        UserDTO expectedDTO = new UserDTO(mockUser1.getUsername(), mockUser1.getEmail(), new ArrayList<>());

        // Mock the dependency
        doReturn(mockUser1).when(underTest).findByUserId(mockUserId);

        // When
        UserDTO result = underTest.getUser(mockUserId);

        // Then
        assertEquals(expectedDTO, result);
    }

    @Test
    void canGetUserByUsername() {
        // Given mockUser1 from BeforeEach setUp()
        String username = mockUser1.getUsername();
        UserDTO expectedDTO = new UserDTO(mockUser1.getUsername(), mockUser1.getEmail(), new ArrayList<>());

        // Mock the dependency
        doReturn(mockUser1).when(underTest).findByUsername(username);

        // When
        UserDTO result = underTest.getUserByUsername(username);

        // Then
        assertEquals(expectedDTO, result);
    }

    @Test
    void canLoadUserByUsername() {
        // Given mockUser1 from BeforeEach setUp()
        String username = mockUser1.getUsername();

        // Mock the dependency
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser1));

        // When
        UserDetails result = underTest.loadUserByUsername(username);

        // Then
        verify(userRepository).findByUsername(username);
        assertEquals(mockUser1.getUsername(), result.getUsername());
        assertEquals(mockUser1.getPassword(), result.getPassword());
    }

    @Test
    void willThrowUsernameNotFoundExceptionWhenUserDetailsNotFound() {
        // Given mockUser1 from BeforeEach setUp()
        String username = "no_such_user";

        // Mock the dependency
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () ->
                underTest.loadUserByUsername(username));

        // Then
        verify(userRepository).findByUsername(username);
        assertEquals("User not found with username: " + username, thrown.getMessage());
    }

    @Test
    void createUser() {
    }

    @Test
    void willNotUpdateUserWhenAllFieldsSame() {
        // Given mockUser1 from BeforeEach setUp()
        UserRequestDTO mockRequestDTO = new UserRequestDTO(1, mockUser1.getUsername(),
                mockUser1.getEmail(), mockUser1.getPassword());
        UserDTO expectedDTO  = new UserDTO(mockUser1.getUsername(), mockUser1.getEmail(), new ArrayList<>());

        // Mock the dependencies
        doReturn(mockUser1).when(underTest).findByUserId(1);

        // When
        UserDTO result = underTest.updateUser(1, mockRequestDTO);

        // Then
        verify(userRepository, never()).save(any(User.class));
        assertEquals(expectedDTO, result);
        assertEquals(mockUser1.getEmail(), result.email());
        assertEquals(mockUser1.getUsername(), result.username());
    }

    @Test
    void willNotUpdateUserWhenAllNull() {
        // Given mockUser1 from BeforeEach setUp()
        UserRequestDTO mockRequestDTO = new UserRequestDTO(1, null, null, null);
        UserDTO expectedDTO  = new UserDTO(mockUser1.getUsername(), mockUser1.getEmail(), new ArrayList<>());

        // Mock the dependencies
        doReturn(mockUser1).when(underTest).findByUserId(1);

        // When
        UserDTO result = underTest.updateUser(1, mockRequestDTO);

        // Then
        verify(userRepository, never()).save(any(User.class));
        assertEquals(expectedDTO, result);
        assertEquals(mockUser1.getEmail(), result.email());
        assertEquals(mockUser1.getUsername(), result.username());
    }

    @Test
    void canUpdateUserWithOneChangeAndNulls() {
        // Given mockUser1 from BeforeEach setUp()
        String originalUsername = mockUser1.getUsername();
        String newUsername = "changedUsername";
        UserRequestDTO mockRequestDTO = new UserRequestDTO(1, newUsername, null, null);
        UserDTO expectedDTO  = new UserDTO(newUsername, mockUser1.getEmail(), new ArrayList<>());

        // Mock the dependencies
        doReturn(mockUser1).when(underTest).findByUserId(1);

        // When
        UserDTO result = underTest.updateUser(1, mockRequestDTO);

        // Then
        verify(userRepository).save(mockUser1);
        assertEquals(expectedDTO, result);
        assertEquals(mockUser1.getEmail(), result.email());
        assertEquals(mockUser1.getUsername(), result.username());
        assertNotEquals(originalUsername, result.username());
    }

    @Test
    void canUpdateUserWithOneChange() {
        // Given mockUser1 from BeforeEach setUp()
        String originalUsername = mockUser1.getUsername();
        String newUsername = "changedUsername";
        UserRequestDTO mockRequestDTO = new UserRequestDTO(1, newUsername, mockUser1.getEmail(), mockUser1.getPassword());
        UserDTO expectedDTO  = new UserDTO(newUsername, mockUser1.getEmail(), new ArrayList<>());

        // Mock the dependencies
        doReturn(mockUser1).when(underTest).findByUserId(1);

        // When
        UserDTO result = underTest.updateUser(1, mockRequestDTO);

        // Then
        verify(userRepository).save(mockUser1);
        assertEquals(expectedDTO, result);
        assertEquals(mockUser1.getEmail(), result.email());
        assertEquals(mockUser1.getUsername(), result.username());
        assertNotEquals(originalUsername, result.username());
    }

    @Test
    void canUpdateUserWithMultipleChanges() {
        // Given mockUser1 from BeforeEach setUp()
        String originalUsername = mockUser1.getUsername();
        String originalEmail = mockUser1.getEmail();
        String newUsername = "changedUsername";
        String newEmail = "changedEmail";
        String newPassword = "changedPassword";
        UserRequestDTO mockRequestDTO = new UserRequestDTO(1, newUsername, newEmail, newPassword);
        UserDTO expectedDTO  = new UserDTO(newUsername, newEmail, new ArrayList<>());

        // Mock the dependencies
        doReturn(mockUser1).when(underTest).findByUserId(1);

        // When
        UserDTO result = underTest.updateUser(1, mockRequestDTO);

        // Then
        verify(userRepository).save(mockUser1);
        assertEquals(expectedDTO, result);
        assertNotEquals(originalUsername, result.username());
        assertNotEquals(originalEmail, result.email());
    }

    @Test
    void canDeleteUser() {
        // Given mockUser1 from BeforeEach setUp()

        // Mock the dependencies
        doReturn(mockUser1).when(underTest).findByUserId(1);

        // When
        underTest.deleteUser(1);

        // Then
        verify(userRepository).delete(mockUser1);
    }

    @Test
    void canMapUserToUserDTOWhenNullSubscriptions() {
        // Given using mockUser1 from BeforeEach setup with null for list of Subscriptions
        mockUser1.setSubscriptions(null);
        List<SubscriptionDTO> expectedSubDTOList = new ArrayList<>();
        UserDTO expectedUserDTO = new UserDTO(mockUser1.getUsername(), mockUser1.getEmail(), expectedSubDTOList);

        // When
        UserDTO result = underTest.mapToUserDTO(mockUser1);

        // Then
        assertEquals(expectedUserDTO, result);
    }

    @Test
    void canMapUserToUserDTOWhenMultipleSubscriptions() {
        // Given using mockUser1 from BeforeEach setup converted to DTO as an expected result
        mockUser1.setSubscriptions(List.of(mockSub1, mockSub2));
        LocalDate nrd1 = mockSub1.getLastPaymentDate().plusMonths(1);
        LocalDate nrd2 = mockSub2.getLastPaymentDate().plusYears(1);
        SubscriptionDTO mockSubDTO1 = new SubscriptionDTO(mockSub1, nrd1);
        SubscriptionDTO mockSubDTO2 = new SubscriptionDTO(mockSub2, nrd2);
        List<SubscriptionDTO> expectedSubDTOList = List.of(mockSubDTO1, mockSubDTO2);
        UserDTO expectedUserDTO = new UserDTO(mockUser1.getUsername(), mockUser1.getEmail(), expectedSubDTOList);

        // Mock the dependencies
        doReturn(mockSubDTO1).when(underTest).mapSubEntToSubDTO(mockSub1);
        doReturn(mockSubDTO2).when(underTest).mapSubEntToSubDTO(mockSub2);

        // When
        UserDTO result = underTest.mapToUserDTO(mockUser1);

        // Then
        assertEquals(expectedUserDTO, result);
    }

    @Test
    void canMapSubscriptionEntityToSubDTO() {
        // Given
        Subscription sub = new Subscription("Netflix", 10.99, BillingCycle.MONTHLY,
                LocalDate.of(2020,1,1), Category.STREAMING,
                "AMEX", mockUser1, Status.ACTIVE);
        LocalDate expectedNRD = LocalDate.of(2020, 1, 1).plusMonths(1);

        // Mock the dependencies
        when(subscriptionService.calculateNextRenewalDate(sub)).thenReturn(expectedNRD);

        // When
        SubscriptionDTO result = underTest.mapSubEntToSubDTO(sub);

        // Then
        verify(subscriptionService).calculateNextRenewalDate(sub);
        assertEquals(expectedNRD, result.getNextRenewalDate());
        assertEquals(sub.getBillingCycle(), result.getBillingCycle());
        assertEquals(sub.getCategory(), result.getCategory());
        assertEquals(sub.getUser().getId(), result.getUserID());
        assertEquals(sub.getServiceName(), result.getServiceName());
        assertEquals(sub.getCost(), result.getCost());
        assertEquals(sub.getPaymentMethod(), result.getPaymentMethod());
        assertEquals(sub.getStatus(), result.getStatus());
    }

    @Test
    void canMapUserRequestDTOTOUserEntity() {
        // Given
        UserRequestDTO mockRequestDTO = new UserRequestDTO(1, "u","e","p");
        String passwordAfterMockedEncoder = "encodedPassword";

        // Mock the dependencies
        when(passwordEncoder.encode(mockRequestDTO.password())).thenReturn(passwordAfterMockedEncoder);

        // When
        User result = underTest.mapUserRequestDTOToUserEnt(mockRequestDTO);

        // Then
        assertEquals(result.getPassword(), passwordAfterMockedEncoder);
        assertEquals(result.getUsername(), mockRequestDTO.username());
        assertEquals(result.getEmail(), mockRequestDTO.email());
        verify(passwordEncoder).encode(mockRequestDTO.password());
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