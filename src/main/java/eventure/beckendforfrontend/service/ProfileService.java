package eventure.beckendforfrontend.service;

import eventure.beckendforfrontend.model.dto.ProfileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ProfileService {
    private final RestTemplate restTemplate;
    private final String profileServiceUrl;

    public ProfileService(
            RestTemplateBuilder builder,
            @Value("${profile.service.url}") String url
    ) {
        this.profileServiceUrl = url;
        this.restTemplate = builder.build();

        log.info("EventService initialized | URL: {}", profileServiceUrl);
    }

    public ProfileDto getProfile(Long id) {
        ResponseEntity<ProfileDto> response = restTemplate.exchange(
                profileServiceUrl + "/api/v1/profiles/" + id.toString() + "/summary",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ProfileDto>(){}
        );
        return response.getBody();
    }
}
