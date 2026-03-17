package it.alessandrohan.pollbffservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(UpstreamProperties.class)
public class RestClientConfig {
    @Bean
    public RestClient restClient(UpstreamProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
    }
}
