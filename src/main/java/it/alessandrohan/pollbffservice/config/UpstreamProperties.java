package it.alessandrohan.pollbffservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Setter
@Getter
@ConfigurationProperties(prefix = "bff.upstream")
public class UpstreamProperties {
    private String baseUrl;
    private Duration connectTimeout = Duration.ofSeconds(2);
    private Duration readTimeout = Duration.ofSeconds(5);
}
