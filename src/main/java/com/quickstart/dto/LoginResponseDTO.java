package com.quickstart.dto;

import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginResponseDTO(
        @NotBlank(message = "")
        String token,
        @NotBlank(message = "")
        UserDetailsDTO user
) {}
