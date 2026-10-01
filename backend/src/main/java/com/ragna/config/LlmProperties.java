package com.ragna.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ragna.llm")
public record LlmProperties(
        EndPoint chat,
        EndPoint embedding
) {
    public record EndPoint(
            String baseUrl,
            String apiKey,
            String model
    ) {
    }
}
