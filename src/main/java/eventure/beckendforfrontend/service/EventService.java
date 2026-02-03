package eventure.beckendforfrontend.service;

import eventure.beckendforfrontend.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
    public List<ParticipantDto> getParticipants(Long eventId) {
        ResponseEntity<List<ParticipantDto>> response = restTemplate.exchange(
                eventServiceUrl + "/api/v1/events/" + eventId + "/participants",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ParticipantDto>>() {}
        );
        return response.getBody();
    }

    public void changeParticipantStatus(Long eventId, Long userId, StatusUpdateDto statusDto, Long organizerId) {
        String url = eventServiceUrl + "/api/v1/events/" + eventId + "/participants/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Id", String.valueOf(organizerId));

        HttpEntity<StatusUpdateDto> entity = new HttpEntity<>(statusDto, headers);

        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }

    public void rateEvent(Long eventId, Integer score, Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", String.valueOf(userId));

        RateEventDto body = new RateEventDto();
        body.setScore(score);

        HttpEntity<RateEventDto> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.exchange(
                eventServiceUrl + "/api/v1/events/" + eventId + "/rate",
                HttpMethod.POST,
                requestEntity,
                Void.class
        );
    }

    public Double getOrganizerRating(Long organizerId) {
        ResponseEntity<Double> response = restTemplate.exchange(
                eventServiceUrl + "/api/v1/ratings/organizer/" + organizerId,
                HttpMethod.GET,
                null,
                Double.class
        );
        return response.getBody();
    }

    public Object getArchivedEvents(Long userId, String type, int page, int limit) {
        StringBuilder urlBuilder = new StringBuilder(eventServiceUrl)
                .append("/api/v1/events/archive")
                .append("?page=").append(page)
                .append("&limit=").append(limit);

        if (type != null && !type.isBlank()) {
            urlBuilder.append("&type=").append(type);
        }

        String url = urlBuilder.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", String.valueOf(userId));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getBody();
    }
}
