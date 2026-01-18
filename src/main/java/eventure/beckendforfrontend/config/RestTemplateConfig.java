package eventure.beckendforfrontend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${auth.service.api-key}")
    private String authServiceApiKey;

    @Bean
    public RestTemplate authServiceRestTemplate(RestTemplateBuilder builder) {
        return builder
                .interceptors((request, body, execution) -> {
                    request.getHeaders().set("X-API-Key", authServiceApiKey);
                    return execution.execute(request, body);
                })
                .build();
    }
}
