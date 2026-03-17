package it.alessandrohan.pollbffservice.proxy;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class ProxyService {
    private final RestClient restClient;

    public ProxyService(RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<byte[]> forward(
            HttpMethod method,
            String path,
            String query,
            HttpHeaders incomingHeaders,
            byte[] body
    ) {
        String uri = (query == null || query.isEmpty()) ? path : path + "?" + query;
        HttpHeaders headers = filterRequestHeaders(incomingHeaders);

        try {   //build request
            RestClient.RequestBodySpec spec = restClient.method(method)
                    .uri(uri)
                    .headers(h -> h.addAll(headers));
            if (body != null && body.length > 0) {
                spec = spec.body(body);
            }
            return spec.retrieve().toEntity(byte[].class);  //HTTP response
        } catch (RestClientResponseException ex) {
            HttpHeaders errorHeaders = ex.getResponseHeaders();
            return ResponseEntity.status(ex.getStatusCode())
                    .headers(errorHeaders != null ? errorHeaders : HttpHeaders.EMPTY)
                    .body(ex.getResponseBodyAsByteArray());
        }
    }

    //remove host header from requests
    private HttpHeaders filterRequestHeaders(HttpHeaders incoming) {
        HttpHeaders filtered = new HttpHeaders();
        incoming.forEach((name, values) -> {
            if (!"host".equalsIgnoreCase(name)) {
                filtered.put(name, values);
            }
        });
        return filtered;
    }
}
