package com.quickstart.dto;

public record LoginResponseDTO(
        String token,
        UserDetailsDTO user
) {

}
