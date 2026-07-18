package com.mercadona.nuriabravo.application.dto.input;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerRequestDto {

    @NotBlank(message = "worker.firstName.required")
    private String firstName;

    @NotBlank(message = "worker.lastName.required")
    private String lastName;

    @NotBlank(message = "worker.dni.required")
    @Pattern(regexp = "^\\d{8}[A-Za-z]$", message = "worker.dni.invalid")
    private String dni;

    @NotNull(message = "worker.contractHours.required")
    @Min(value = 1, message = "worker.contractHours.min")
    @Max(value = 8, message = "worker.contractHours.max")
    private Integer contractHours;

    @NotNull(message = "worker.storeId.required")
    private Long storeId;
}