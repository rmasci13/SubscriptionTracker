package com.rmasci13.github.user;

import com.rmasci13.github.subscription.SubscriptionDTO;

import java.util.List;

public record UserDTO(String username, String email, List<SubscriptionDTO> subscriptions) {
    public boolean hasUsername() {
        return username != null && !username.isEmpty();
    }

    public boolean hasEmail() {
        return email != null && !email.isEmpty();
    }
}
