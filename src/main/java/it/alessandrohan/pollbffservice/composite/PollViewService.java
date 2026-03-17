package it.alessandrohan.pollbffservice.composite;

import it.alessandrohan.pollbffservice.composite.dto.response.PollViewResponse;
import it.alessandrohan.pollbffservice.composite.dto.upstream.PollDetailsResponse;
import it.alessandrohan.pollbffservice.composite.dto.upstream.VoteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PollViewService {
    private final RestClient restClient;

    public PollViewService(RestClient restClient) {
        this.restClient = restClient;
    }

    public PollViewResponse getPollView(Long id, String authorization) {
        try {
            PollDetailsResponse details =
                    restClient.get().uri("/polls-details/{id}", id)
                            .header("Authorization", authorization)
                            .retrieve().body(PollDetailsResponse.class);
            if (details == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY, //502
                        "Upstream returned empty poll details body"
                );
            }

            VoteResponse vote =
                    restClient.get().uri("/polls/{id}/vote", id)
                            .header("Authorization", authorization)
                            .retrieve().body(VoteResponse.class);
            if (vote == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "Upstream returned empty vote body"
                );
            }

            return new PollViewResponse(details, vote.getOptionId(), vote.getId(), vote.getVotedAt());
        } catch (RestClientResponseException ex) {
            throw new ResponseStatusException(
                    HttpStatus.valueOf(ex.getStatusCode().value()),
                    ex.getStatusText(),
                    ex
            );
        }
    }
}
