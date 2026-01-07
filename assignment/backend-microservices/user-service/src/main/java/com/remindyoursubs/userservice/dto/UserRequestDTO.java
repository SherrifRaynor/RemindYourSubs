package com.remindyoursubs.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for User creation and update requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    private Integer reminderDaysBefore = 3;

    private String resendApiKey;
}
