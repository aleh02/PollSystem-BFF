package it.alessandrohan.pollbffservice.composite.dto.upstream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteResponse {
    private String optionId;
    private Long id;
    private Instant votedAt;
}
