package dev.usenkonastia.api.dto.cosmoCat;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CosmoCatDto {
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @NotBlank(message = "Name cannot be null")
    String catName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    String email;
}
