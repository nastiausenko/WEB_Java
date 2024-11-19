package dev.usenkonastia.api.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CosmoCat {
    UUID catId;
    String catName;
    String email;
}
