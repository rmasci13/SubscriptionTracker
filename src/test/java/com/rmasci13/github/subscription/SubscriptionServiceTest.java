package com.rmasci13.github.subscription;
import com.rmasci13.github.enums.BillingCycle;
import com.rmasci13.github.enums.Category;
import com.rmasci13.github.enums.Status;
import com.rmasci13.github.exception.ItemNotFoundException;
import com.rmasci13.github.user.User;
import com.rmasci13.github.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    SubscriptionRepository subscriptionRepository;
    @Mock
    UserRepository userRepository;
    @Spy
    @InjectMocks
    private SubscriptionService underTest;

    @Test
    void testIsOwner_True() {
        // Given
        int subscriptionId = 5;
        int userId = 100;

        User user = new User();
        user.setId(userId);

        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setUser(user);

        Mockito.when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        //When
        boolean result = underTest.isOwner(userId, subscriptionId);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsOwner_False() {
        // Given
        int subscriptionId = 5;
        int nonOwnerId = 100;
        int ownerId = 2;

        User owner = new User();
        owner.setId(ownerId);

        User nonOwner = new User();
        nonOwner.setId(nonOwnerId);

        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setUser(owner);

        Mockito.when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        //When
        boolean result = underTest.isOwner(nonOwnerId, subscriptionId);

        // Then
        assertFalse(result);
    }

    @Test
    void canFindByUsername() {
        // Given
        String username = "test";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        User found = underTest.findByUsername(username);

        // Then
        Mockito.verify(userRepository).findByUsername(username);
        assertEquals(user, found);
    }

    @Test
    void throwsExceptionWhenUsernameNotFound() {
        // Given
        String username = "nonexistent";
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // Then
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class, () -> {
            // When
            underTest.findByUsername(username);
        });

        assertEquals("User not found with username: " + username, thrown.getMessage());
    }

    @Test
    void canFindByUserId() {
        // Given
        Integer id = 1;
        User mockUser = new User();
        mockUser.setId(id);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));

        // When
        User result = underTest.findByUserId(id);

        // Then
        Mockito.verify(userRepository).findById(id); // ✅ Verifies delegation
        assertEquals(mockUser, result);    // Optional, but good
    }

    @Test
    void findByUserIdThrowsExceptionWhenFound() {
        // Given
        Integer id = 1;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class, () -> {
            // When
            underTest.findByUserId(id);
        });

        // Then
        assertEquals("User not found with ID: " + id, thrown.getMessage());

    }

    @Test
    void willCalculateNextRenewalDate() {
        // Given
        Subscription sub1 = new Subscription();
        LocalDate lastPaymentDate = LocalDate.of(2020, 1, 1);
        BillingCycle bc1 = BillingCycle.ANNUALLY;
        sub1.setBillingCycle(bc1);
        sub1.setLastPaymentDate(lastPaymentDate);

        Subscription sub2 = new Subscription();
        BillingCycle bc2 = BillingCycle.SEMI_ANNUALLY;
        sub2.setBillingCycle(bc2);
        sub2.setLastPaymentDate(lastPaymentDate);

        Subscription sub3 = new Subscription();
        BillingCycle bc3 = BillingCycle.QUARTERLY;
        sub3.setBillingCycle(bc3);
        sub3.setLastPaymentDate(lastPaymentDate);

        Subscription sub4 = new Subscription();
        BillingCycle bc4 = BillingCycle.MONTHLY;
        sub4.setBillingCycle(bc4);
        sub4.setLastPaymentDate(lastPaymentDate);

        // When
        LocalDate result1 = underTest.calculateNextRenewalDate(sub1);
        LocalDate result2 = underTest.calculateNextRenewalDate(sub2);
        LocalDate result3 = underTest.calculateNextRenewalDate(sub3);
        LocalDate result4 = underTest.calculateNextRenewalDate(sub4);

        // Then
        LocalDate expected1 = LocalDate.of(2021, 1, 1); // 1 Year
        LocalDate expected2 = LocalDate.of(2020, 7, 1); // 6 months
        LocalDate expected3 = LocalDate.of(2020, 4, 1); // 3 months
        LocalDate expected4 = LocalDate.of(2020, 2, 1); // 1 year
        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
        assertEquals(expected3, result3);
        assertEquals(expected4, result4);
    }

    @Test
    void canConvertToDTO() {
        // Given
        User user = new User();
        user.setId(1);
        Subscription sub = new Subscription();
        sub.setBillingCycle(BillingCycle.ANNUALLY);
        sub.setLastPaymentDate(LocalDate.of(2020, 1, 1));
        sub.setServiceName("test");
        sub.setCost(10);
        sub.setCategory(Category.STREAMING);
        sub.setPaymentMethod("AMEX");
        sub.setStatus(Status.ACTIVE);
        sub.setUser(user);

        LocalDate expectedNextRenewalDate = LocalDate.of(2021, 1, 1);

        // When
        SubscriptionDTO result = underTest.convertToDTO(sub);

        // Then
        assertEquals(sub.getServiceName(), result.getServiceName());
        assertEquals(sub.getCost(), result.getCost());
        assertEquals(sub.getBillingCycle(), result.getBillingCycle());
        assertEquals(sub.getLastPaymentDate(), result.getLastPaymentDate());
        assertEquals(expectedNextRenewalDate, result.getNextRenewalDate());
        assertEquals(sub.getCategory(), result.getCategory());
        assertEquals(sub.getPaymentMethod(), result.getPaymentMethod());
        assertEquals(sub.getStatus(), result.getStatus());
        assertEquals(sub.getUser().getId(), result.getUserID());
    }

    @Test
    void canMapToSubscription() {
        // Given
        User user = new User();
        int userId = 1;
        user.setId(userId);
        SubscriptionRequestDTO dto = new SubscriptionRequestDTO("test", 10.00, BillingCycle.ANNUALLY,
                LocalDate.now(), Category.STREAMING, "AMEX", Status.ACTIVE);

        // When
        Subscription result = underTest.mapToSubscription(dto, user);

        // Then
        assertEquals(BillingCycle.ANNUALLY, result.getBillingCycle());
        assertEquals(LocalDate.now(), result.getLastPaymentDate());
        assertEquals("test", result.getServiceName());
        assertEquals(10.00, result.getCost());
        assertEquals(Category.STREAMING, result.getCategory());
        assertEquals("AMEX", result.getPaymentMethod());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(userId, result.getUser().getId());
    }

    @Test
    void getSubscriptionDTOsByIdWillCallBothMethods() {
        SubscriptionService spyService = spy(new SubscriptionService(subscriptionRepository, userRepository));

        Subscription mockSubscription = mock(Subscription.class);
        LocalDate mockDate = LocalDate.now();

        doReturn(mockSubscription).when(spyService).findBySubscriptionId(anyInt());
        doReturn(mockDate).when(spyService).calculateNextRenewalDate(any());

        try {
            spyService.getSubscriptionDTOsById(123);
        } catch (NullPointerException e) {
            // Ignore
        }
        // Verify both methods were called
        verify(spyService).findBySubscriptionId(anyInt());
        verify(spyService).calculateNextRenewalDate(any());
    }

    @Test
    void canGetSubscriptionDTOsByID() {
        // Given
        User user = new User();
        user.setId(1);
        // This is the subscription we force findBySubID to return in the when
        Subscription sub = new Subscription("test", 10.99, BillingCycle.ANNUALLY,
                LocalDate.of(2020, 1, 1), Category.STREAMING, "AMEX", user, Status.ACTIVE);
        sub.setId(1);
        // DTO we expect to be created if getSubDTOById works as intended
        SubscriptionDTO expected = new SubscriptionDTO(sub, LocalDate.of(2020, 1, 1).plusYears(1));
        // Tells next call to just return sub
        when(subscriptionRepository.findById(1)).thenReturn(Optional.of(sub));

        // When
        SubscriptionDTO result = underTest.getSubscriptionDTOsById(1);

        // Then

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void canCreateSubscriptionWithUsername() {
        // Given
        String username = "testuser";
        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO(
                "Netflix", 15.99, BillingCycle.MONTHLY,
                LocalDate.of(2020,1,1), Category.STREAMING,
                "VISA", Status.ACTIVE);

        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername(username);

        Subscription mockMappedSubscription = new Subscription("Netflix", 15.99,
                BillingCycle.MONTHLY, LocalDate.now(), Category.STREAMING, "VISA",
                mockUser, Status.ACTIVE);

        Subscription mockSavedSubscription = new Subscription("Netflix", 15.99,
                BillingCycle.MONTHLY, LocalDate.now(), Category.STREAMING, "VISA",
                mockUser, Status.ACTIVE);
        mockSavedSubscription.setId(1); // Simulating the saved subscription has an ID

        SubscriptionDTO expectedDTO = new SubscriptionDTO(mockSavedSubscription,
                LocalDate.now().plusMonths(1));

        // Mock the dependencies
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        doReturn(mockMappedSubscription).when(underTest).mapToSubscription(requestDTO, mockUser);
        when(subscriptionRepository.save(mockMappedSubscription)).thenReturn(mockSavedSubscription);
        doReturn(expectedDTO).when(underTest).convertToDTO(mockSavedSubscription);

        // When
        SubscriptionDTO result = underTest.createSubscriptionWithUsername(requestDTO, username);

        // Then
        assertNotNull(result);
        assertEquals(expectedDTO, result);

        // Verify the method calls happened in the right order
        verify(underTest).findByUsername(username);
        verify(underTest).mapToSubscription(requestDTO, mockUser);
        verify(subscriptionRepository).save(mockMappedSubscription);
        verify(underTest).convertToDTO(mockSavedSubscription);
    }

    @Test
    void canDeleteSubscription() {
        // Given
        User mockUser = new User();
        mockUser.setId(1);

        Subscription mockSub = new Subscription("Netflix", 15.99,
                BillingCycle.MONTHLY, LocalDate.now(), Category.STREAMING, "VISA",
                mockUser, Status.ACTIVE);
        int subId = 1;
        mockSub.setId(subId);

        // Mock the dependencies
        doReturn(mockSub).when(underTest).findBySubscriptionId(subId);

        // When
        underTest.deleteSubscription(subId);

        // Then
        verify(subscriptionRepository).delete(mockSub);

    }

    @Test
    void canUpdateSubscriptionWithCostChange() {
        // Given
        User mockUser = new User();
        mockUser.setId(1);

        Subscription mockOriginalSub = new Subscription("Netflix", 15.99,
                BillingCycle.MONTHLY, LocalDate.now(), Category.STREAMING, "VISA",
                mockUser, Status.ACTIVE);
        int subId = 1;
        mockOriginalSub.setId(subId);

        // Updated variables
        Double updatedCost = 20.00;

        SubscriptionRequestDTO mockUpdatedRequestDTO = new SubscriptionRequestDTO("Netflix", updatedCost,
                BillingCycle.MONTHLY, LocalDate.now(), Category.STREAMING, "VISA", Status.ACTIVE);


        // Mock the dependencies
        doReturn(mockOriginalSub).when(underTest).findBySubscriptionId(subId);
        when(subscriptionRepository.save(mockOriginalSub)).thenReturn(mockOriginalSub);

        // When
        SubscriptionDTO result = underTest.updateSubscription(subId, mockUpdatedRequestDTO);

        // Then
        verify(subscriptionRepository).save(mockOriginalSub);
        assertEquals(mockOriginalSub.getCost(), updatedCost);
        assertEquals(result.getCost(), updatedCost);
    }

    @Test
    void canUpdateSubscriptionWithCostChangeAndNulls() {
        // Given
        User mockUser = new User();
        mockUser.setId(1);

        Subscription mockOriginalSub = new Subscription("Netflix", 15.99,
                BillingCycle.MONTHLY, LocalDate.now(), Category.STREAMING, "VISA",
                mockUser, Status.ACTIVE);
        int subId = 1;
        mockOriginalSub.setId(subId);

        // Updated variables
        Double updatedCost = 20.00;

        SubscriptionRequestDTO mockUpdatedRequestDTO = new SubscriptionRequestDTO(null, updatedCost,
                null, null, null, null, null);


        // Mock the dependencies
        doReturn(mockOriginalSub).when(underTest).findBySubscriptionId(subId);
        when(subscriptionRepository.save(mockOriginalSub)).thenReturn(mockOriginalSub);

        // When
        SubscriptionDTO result = underTest.updateSubscription(subId, mockUpdatedRequestDTO);

        // Then
        verify(subscriptionRepository).save(mockOriginalSub);
        assertEquals(mockOriginalSub.getCost(), updatedCost);
        assertEquals(result.getCost(), updatedCost);
    }

    @Test
    void canUpdateSubscriptionWithMultipleChanges() {
        // Given
        User mockUser = new User();
        mockUser.setId(1);

        Subscription mockOriginalSub = new Subscription("Netflix", 15.99,
                BillingCycle.MONTHLY, LocalDate.now(), Category.STREAMING, "VISA",
                mockUser, Status.ACTIVE);
        int subId = 1;
        mockOriginalSub.setId(subId);

        // Updated variables
        String updatedName = "NETFLIX";
        String updatedPaymentMethod = "AMEX";
        Double updatedCost = 20.00;

        SubscriptionRequestDTO mockUpdatedRequestDTO = new SubscriptionRequestDTO(updatedName, updatedCost,
                BillingCycle.MONTHLY, LocalDate.now(), Category.STREAMING, updatedPaymentMethod, Status.ACTIVE);


        // Mock the dependencies
        doReturn(mockOriginalSub).when(underTest).findBySubscriptionId(subId);
        when(subscriptionRepository.save(mockOriginalSub)).thenReturn(mockOriginalSub);

        // When
        SubscriptionDTO result = underTest.updateSubscription(subId, mockUpdatedRequestDTO);

        // Then
        verify(subscriptionRepository).save(mockOriginalSub);
        assertEquals(mockOriginalSub.getCost(), updatedCost);
        assertEquals(result.getCost(), updatedCost);
        assertEquals(result.getServiceName(), updatedName);
        assertEquals(result.getPaymentMethod(), updatedPaymentMethod);
        assertEquals(mockOriginalSub.getServiceName(), updatedName);
        assertEquals(mockOriginalSub.getPaymentMethod(), updatedPaymentMethod);
    }

    @Test
    void canUpdateSubscriptionWithAllChanges() {
        // Given
        User mockUser = new User();
        mockUser.setId(1);

        Subscription mockOriginalSub = new Subscription("Netflix", 15.99,
                BillingCycle.MONTHLY, LocalDate.now(), Category.STREAMING, "VISA",
                mockUser, Status.ACTIVE);
        int subId = 1;
        mockOriginalSub.setId(subId);

        // Updated Variables
        String updatedName = "NETFLIX";
        Double updatedCost = 20.00;
        BillingCycle updatedBC = BillingCycle.ANNUALLY;
        LocalDate updatedDate = LocalDate.now().plusYears(1);
        Category updatedCategory = Category.FOOD;
        String updatedPaymentMethod = "AMEX";
        Status updatedStatus = Status.PAUSED;

        SubscriptionRequestDTO mockUpdatedRequestDTO = new SubscriptionRequestDTO(updatedName, updatedCost,
                updatedBC, updatedDate, updatedCategory, updatedPaymentMethod, updatedStatus);


        // Mock the dependencies
        doReturn(mockOriginalSub).when(underTest).findBySubscriptionId(subId);
        when(subscriptionRepository.save(mockOriginalSub)).thenReturn(mockOriginalSub);

        // When
        SubscriptionDTO result = underTest.updateSubscription(subId, mockUpdatedRequestDTO);

        // Then
        verify(subscriptionRepository).save(mockOriginalSub);
        assertEquals(mockOriginalSub.getCost(), updatedCost);
        assertEquals(result.getCost(), updatedCost);
        assertEquals(result.getServiceName(), updatedName);
        assertEquals(mockOriginalSub.getServiceName(), updatedName);
        assertEquals(result.getPaymentMethod(), updatedPaymentMethod);
        assertEquals(mockOriginalSub.getPaymentMethod(), updatedPaymentMethod);
        assertEquals(mockOriginalSub.getCategory(), updatedCategory);
        assertEquals(result.getCategory(), updatedCategory);
        assertEquals(mockOriginalSub.getBillingCycle(), updatedBC);
        assertEquals(result.getBillingCycle(), updatedBC);
        assertEquals(mockOriginalSub.getStatus(), updatedStatus);
        assertEquals(result.getStatus(), updatedStatus);
        assertEquals(mockOriginalSub.getLastPaymentDate(), updatedDate);
        assertEquals(result.getLastPaymentDate(), updatedDate);
    }

    @Test
    void willNotUpdateSubscriptionWithoutChanges() {
        // Given
        User mockUser = new User();
        mockUser.setId(1);

        Subscription mockOriginalSub = new Subscription("Netflix", 15.99,
                BillingCycle.MONTHLY, LocalDate.of(2020,1,1), Category.STREAMING, "VISA",
                mockUser, Status.ACTIVE);
        int subId = 1;
        mockOriginalSub.setId(subId);

        // Same exact variables
        SubscriptionRequestDTO mockUpdatedRequestDTO = new SubscriptionRequestDTO("Netflix", 15.99,
                BillingCycle.MONTHLY, LocalDate.of(2020, 1, 1), Category.STREAMING, "VISA", Status.ACTIVE);

        // Mock the dependencies
        doReturn(mockOriginalSub).when(underTest).findBySubscriptionId(subId);

        // When
        SubscriptionDTO result = underTest.updateSubscription(subId, mockUpdatedRequestDTO);

        // Then
        verify(subscriptionRepository, never()).save(mockOriginalSub);
    }

    @Test
    void willNotUpdateSubscriptionWithAllNulls() {
        // Given
        User mockUser = new User();
        mockUser.setId(1);

        Subscription mockOriginalSub = new Subscription("Netflix", 15.99,
                BillingCycle.MONTHLY, LocalDate.of(2020,1,1), Category.STREAMING, "VISA",
                mockUser, Status.ACTIVE);
        int subId = 1;
        mockOriginalSub.setId(subId);

        // All variables are null
        SubscriptionRequestDTO mockUpdatedRequestDTO = new SubscriptionRequestDTO(null, null,
                null, null, null, null, null);

        // Mock the dependencies
        doReturn(mockOriginalSub).when(underTest).findBySubscriptionId(subId);

        // When
        SubscriptionDTO result = underTest.updateSubscription(subId, mockUpdatedRequestDTO);

        // Then
        verify(subscriptionRepository, never()).save(mockOriginalSub);
    }

    @Test
    void canGetAllSubscriptions() {
        // Given
        User mockUser = new User();
        int userId = 1;
        mockUser.setId(userId);

        Subscription mockSub = new Subscription("Netflix", 15.99,
                BillingCycle.MONTHLY, LocalDate.of(2020,1,1), Category.STREAMING, "VISA",
                mockUser, Status.ACTIVE);
        int subId = 1;
        mockSub.setId(subId);

        LocalDate expectedNRD = LocalDate.of(2020,1,1).plusMonths(1);

        List<Subscription> mockSubList = List.of(mockSub);

        // Mock the dependency
        when(subscriptionRepository.findAll()).thenReturn(mockSubList);
        doReturn(expectedNRD).when(underTest).calculateNextRenewalDate(mockSub);
        // When
        List<SubscriptionDTO> result = underTest.getSubscriptionDTOs();
        // Then
        verify(subscriptionRepository).findAll();
        verify(underTest).calculateNextRenewalDate(mockSub);
        assertEquals(result.size(), 1);
        SubscriptionDTO dto = result.get(0);
        assertEquals(dto.getServiceName(), mockSub.getServiceName());
        assertEquals(dto.getCost(), mockSub.getCost());
        assertEquals(dto.getPaymentMethod(), mockSub.getPaymentMethod());
        assertEquals(dto.getCategory(), mockSub.getCategory());
        assertEquals(dto.getBillingCycle(), mockSub.getBillingCycle());
        assertEquals(dto.getLastPaymentDate(), mockSub.getLastPaymentDate());
        assertEquals(dto.getStatus(), mockSub.getStatus());
        assertEquals(dto.getNextRenewalDate(), expectedNRD);
    }
}