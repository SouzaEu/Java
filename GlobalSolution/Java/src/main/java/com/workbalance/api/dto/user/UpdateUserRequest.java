package com.workbalance.api.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(min = 2, max = 80, message = "Name must be between 2 and 80 characters")
    private String name;

    @Pattern(
        regexp = "^(pt-BR|es-ES|en-US)$",
        message = "Preferred language must be one of: pt-BR, es-ES, en-US"
    )
    private String preferredLanguage;
}
