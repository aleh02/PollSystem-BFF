package it.alessandrohan.pollbffservice.composite;

import it.alessandrohan.pollbffservice.composite.dto.response.PollViewResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class PollViewServiceTest {
    @Test
    void getPollView_returnsCombinedResponse() {
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl("http://upstream");
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        RestClient restClient = restClientBuilder.build();

        String pollDetailsJson = """
                {
                  "id": 1,
                  "owner": "alice",
                  "expiresAt": "2026-01-01T00:00:00Z",
                  "status": "ACTIVE",
                  "winner": null,
                  "options": [
                    { "id": 10, "message": "Option A", "createdAt": "2026-01-01T00:00:00Z" }
                  ]
                }
                """;
        String voteJson = """
                {
                  "optionId": "10",
                  "id": 99,
                  "votedAt": "2026-01-01T00:00:00Z"
                }
                """;

        server.expect(requestTo("http://upstream/polls-details/1"))
                .andExpect(header("Authorization", "Bearer test"))
                .andRespond(withSuccess(pollDetailsJson, MediaType.APPLICATION_JSON));
        server.expect(requestTo("http://upstream/polls/1/vote"))
                .andExpect(header("Authorization", "Bearer test"))
                .andRespond(withSuccess(voteJson, MediaType.APPLICATION_JSON));

        PollViewService service = new PollViewService(restClient);
        PollViewResponse response = service.getPollView(1L, "Bearer test");

        assertNotNull(response);
        assertNotNull(response.getPollDetailsResponse());
        assertEquals("alice", response.getPollDetailsResponse().getOwner());
        assertEquals("10", response.getVoteOptionId());
        assertEquals(99L, response.getVoteId());

        server.verify();
    }

    @Test
    void getPollView_propagatesUpstreamStatus() {
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl("http://upstream");
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        RestClient restClient = restClientBuilder.build();

        server.expect(requestTo("http://upstream/polls-details/1"))
                .andExpect(header("Authorization", "Bearer test"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        PollViewService service = new PollViewService(restClient);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.getPollView(1L, "Bearer test")
        );
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        server.verify();
    }
}
