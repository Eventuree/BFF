package eventure.beckendforfrontend.service;

import eventure.beckendforfrontend.model.dto.CategoryDto;
import eventure.beckendforfrontend.model.dto.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class EventService {
    private final RestTemplate restTemplate;
    private final String eventServiceUrl;

    public EventService(
            RestTemplateBuilder builder,
            @Value("${event.service.url}") String url
    ) {
        this.eventServiceUrl = url;
        this.restTemplate = builder.build();

        log.info("EventService initialized | URL: {}", eventServiceUrl);
    }

    public List<EventDto> getTrendingEvents() {
        ResponseEntity<List<EventDto>> response = restTemplate.exchange(
                eventServiceUrl + "/api/v1/events/trending",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EventDto>>() {}
        );
        return response.getBody();
    }

    public List<CategoryDto> getCategories() {
        ResponseEntity<List<CategoryDto>> response = restTemplate.exchange(
                eventServiceUrl + "/api/v1/categories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CategoryDto>>() {}
        );
        return response.getBody();
    }

    public List<EventDto> getAllEvents() {
        ResponseEntity<List<EventDto>> response = restTemplate.exchange(
                eventServiceUrl + "/api/v1/events/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EventDto>>() {}
        );
        return response.getBody();
    }
}
