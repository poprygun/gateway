package io.microsamples.gateway.tracksapi;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Track {
    private UUID id;
    private double latitude;
    private double longitude;
}
