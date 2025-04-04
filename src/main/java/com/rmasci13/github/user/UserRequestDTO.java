package com.rmasci13.github.user;

public record UserRequestDTO(Integer id, String username, String email, String password) {
    public boolean hasUsername() {
        return username != null && !username.isEmpty();
    }

    public boolean hasEmail() {
        return email != null && !email.isEmpty();
    }

    public boolean hasPassword() {
        return password != null && !password.isEmpty();
    }
}
