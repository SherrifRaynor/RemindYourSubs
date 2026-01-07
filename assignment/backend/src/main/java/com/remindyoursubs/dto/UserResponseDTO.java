package com.remindyoursubs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String email;
    private String name;
    private Integer reminderDaysBefore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
