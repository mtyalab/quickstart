package com.quickstart.dto;

public record UserDetailsDTO(
        String id,
        String firstName,
        String lastName,
        String middleName,
        String role,
        String phoneNumber,
        String username
) {
}
