package com.ignaciorudyk.auth.repository.dto.request;

import com.ignaciorudyk.auth.repository.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        Role role,

        Boolean enabled,

        @Size(min = 2, max = 50)
        String firstName,

        @Size(min = 2, max = 50)
        String lastName,

        @Email(message = "El email no tiene un formato válido")
        String email
) {

    public static UpdateMeRequest toUpdateMeRequest(UpdateUserRequest updateUserRequest) {
        return new UpdateMeRequest(updateUserRequest.firstName, updateUserRequest.lastName, updateUserRequest.email);
    }

}