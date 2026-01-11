package com.remindyoursubs.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Type is required")
    private String type; // CREDIT_CARD, DEBIT_CARD, E_WALLET, BANK_ACCOUNT

    private String provider;

    @Pattern(regexp = "\\d{4}", message = "Last four digits must be 4 numbers")
    private String lastFourDigits;

    private String nickname;

    @Min(value = 1)
    @Max(value = 12)
    private Integer expiryMonth;

    @Min(value = 2024)
    private Integer expiryYear;

    private Boolean isDefault;
}
