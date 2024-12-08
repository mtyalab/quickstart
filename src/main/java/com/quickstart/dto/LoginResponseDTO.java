package com.quickstart.dto;

public class LoginResponseDTO {
    private String token;
    private UserDetailsDTO user;

    public LoginResponseDTO(String token, UserDetailsDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public UserDetailsDTO getUser() {
        return user;
    }
}
