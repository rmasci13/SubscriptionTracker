package com.rmasci13.github.user;

import com.rmasci13.github.subscription.Subscription;
import com.rmasci13.github.subscription.SubscriptionDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void canFindByUsername() {

        //Given
        User user = new User("tom_smith", "tom@example.com", "password");
        underTest.save(user);

        //When
        Optional<User> found = underTest.findByUsername("tom_smith");

        //Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("tom_smith");
    }

    @Test
    void cannotFindByUsername() {

        //Given
        String username = "dont_find";

        //When
        Optional<User> found = underTest.findByUsername(username);

        //Then
        assertThat(found).isNotPresent();
    }
}