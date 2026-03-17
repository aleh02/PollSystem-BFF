package it.alessandrohan.pollbffservice.composite.dto.response;

import it.alessandrohan.pollbffservice.composite.dto.PollStatus;
import it.alessandrohan.pollbffservice.composite.dto.upstream.PollDetailsResponse;
import it.alessandrohan.pollbffservice.composite.dto.upstream.VoteResponse;
import it.alessandrohan.pollbffservice.composite.dto.upstream.WinnerOptionResponse;
import it.alessandrohan.pollbffservice.composite.dto.upstream.PollOptionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class PollViewResponse {
    //PollDetails
    private PollDetailsResponse pollDetailsResponse;
    //vote
    private String voteOptionId;
    private Long voteId;
    private Instant votedAt;
}
