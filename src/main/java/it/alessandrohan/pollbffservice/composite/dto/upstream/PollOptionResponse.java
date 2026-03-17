package it.alessandrohan.pollbffservice.composite.dto.upstream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollOptionResponse {

    private Long id;
    private String message;
    private Instant createdAt;
}
