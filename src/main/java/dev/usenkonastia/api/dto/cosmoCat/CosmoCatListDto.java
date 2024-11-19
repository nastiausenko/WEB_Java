package dev.usenkonastia.api.dto.cosmoCat;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class CosmoCatListDto {
    List<CosmoCatEntryDto> cosmoCats;
}
