package com.remindyoursubs.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for User responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String email;
    private String name;
    private Integer reminderDaysBefore;
    private String resendApiKey;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
