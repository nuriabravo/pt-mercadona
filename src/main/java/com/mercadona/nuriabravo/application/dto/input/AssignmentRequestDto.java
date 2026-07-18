package com.mercadona.nuriabravo.application.dto.input;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentRequestDto {

    @NotNull(message = "assignment.sectionId.required")
    private Long sectionId;

    @NotNull(message = "assignment.hours.required")
    @Min(value = 1, message = "assignment.hours.min")
    @Max(value = 8, message = "assignment.hours.max")
    private Integer hours;
}