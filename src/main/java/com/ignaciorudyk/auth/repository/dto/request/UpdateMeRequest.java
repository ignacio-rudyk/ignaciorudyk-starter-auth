package com.ignaciorudyk.auth.repository.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateMeRequest(
        @Size(min = 2, max = 50)
        String firstName,

        @Size(min = 2, max = 50)
        String lastName,

        @Email(message = "El email no tiene un formato válido")
        String email
) {}