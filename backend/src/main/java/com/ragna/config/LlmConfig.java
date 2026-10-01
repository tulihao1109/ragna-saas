package com.ragna.config;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
@EnableConfigurationProperties(LlmProperties.class)
public class LlmConfig {

    @Bean
    @Primary
    public OpenAiChatModel chatModel(LlmProperties props){
        LlmProperties.EndPoint ep = props.chat();
        OpenAiApi api = OpenAiApi.builder()
                .baseUrl(ep.baseUrl())
                .apiKey(ep.apiKey())
                .build();
        return OpenAiChatModel.builder()
                .openAiApi(api)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ep.model())
                        .temperature(0.7).
                        build())
                .build();
    }

    @Bean
    public EmbeddingModel chatEmbedding(LlmProperties props){
        LlmProperties.EndPoint ep = props.embedding();
        OpenAiApi api =  OpenAiApi.builder()
                .baseUrl(ep.baseUrl())
                .apiKey(ep.apiKey())
                .build();
        return new OpenAiEmbeddingModel(
                api,
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder()
                        .model(ep.model())
                        .build(),
                RetryUtils.DEFAULT_RETRY_TEMPLATE);
    }
}
