package it.alessandrohan.pollbffservice.composite.dto.upstream;

import it.alessandrohan.pollbffservice.composite.dto.PollStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollDetailsResponse {
    private Long id;
    private String owner;
    private Instant expiresAt;
    private PollStatus status;
    private WinnerOptionResponse winner; //null if not expired
    private List<PollOptionResponse> options;
}
