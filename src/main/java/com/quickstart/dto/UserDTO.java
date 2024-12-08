package com.quickstart.dto;

public record UserDTO(
        String firstName,
        String lastName,
        String middleName,
        String role,
        String phoneNumber,
        String username,
        String password
) {
}
