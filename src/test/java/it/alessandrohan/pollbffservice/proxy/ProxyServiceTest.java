package it.alessandrohan.pollbffservice.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ProxyServiceTest {
    @Test
    void forward_passesMethodHeadersAndBody() {
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl("http://upstream");
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        RestClient restClient = restClientBuilder.build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer test");
        headers.add("Content-Type", "application/json");
        headers.add("Host", "bff.local");

        byte[] body = "{\"x\":1}".getBytes();

        server.expect(requestTo("http://upstream/polls?search=a"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer test"))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(withSuccess("{\"ok\":true}", MediaType.APPLICATION_JSON));

        ProxyService service = new ProxyService(restClient);
        ResponseEntity<byte[]> response = service.forward(
                org.springframework.http.HttpMethod.POST,
                "/polls",
                "search=a",
                headers,
                body
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals("{\"ok\":true}".getBytes(), response.getBody());
        assertNull(response.getHeaders().getFirst("Host"));

        server.verify();
    }

    @Test
    void forward_propagatesUpstreamError() {
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl("http://upstream");
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        RestClient restClient = restClientBuilder.build();

        server.expect(requestTo("http://upstream/polls/1"))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).body("not found"));

        ProxyService service = new ProxyService(restClient);
        ResponseEntity<byte[]> response = service.forward(
                org.springframework.http.HttpMethod.GET,
                "/polls/1",
                null,
                new HttpHeaders(),
                null
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertArrayEquals("not found".getBytes(), response.getBody());

        server.verify();
    }
}
