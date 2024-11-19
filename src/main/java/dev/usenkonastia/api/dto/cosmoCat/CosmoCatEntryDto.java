package dev.usenkonastia.api.dto.cosmoCat;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class CosmoCatEntryDto {
    UUID catId;
    String catName;
    String email;
}
