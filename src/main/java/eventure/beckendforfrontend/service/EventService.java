package eventure.beckendforfrontend.service;

import eventure.beckendforfrontend.model.dto.*;
import eventure.beckendforfrontend.model.enums.RegistrationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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
    public List<ParticipantDto> getParticipants(Long eventId, Long currentUserId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", String.valueOf(currentUserId));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<ParticipantDto>> response = restTemplate.exchange(
                eventServiceUrl + "/api/v1/events/" + eventId + "/participants",
                HttpMethod.GET,
                requestEntity,
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

    public EventPageResponse getAllEventsPagination(Integer page, Integer limit, EventFiltersDto eventFilters) {
        StringBuilder urlBuilder = new StringBuilder(eventServiceUrl)
                .append("/api/v1/events?page=").append(page)
                .append("&limit=").append(limit);

        if (eventFilters != null) {
            if (eventFilters.getKeyword() != null && !eventFilters.getKeyword().isBlank()) {
                urlBuilder.append("&keyword=").append(eventFilters.getKeyword());
            }
            if (eventFilters.getCategoryId() != null) {
                urlBuilder.append("&categoryId=").append(eventFilters.getCategoryId());
            }
            if (eventFilters.getDateFrom() != null) {
                urlBuilder.append("&dateFrom=").append(eventFilters.getDateFrom());
            }
            if (eventFilters.getDateTo() != null) {
                urlBuilder.append("&dateTo=").append(eventFilters.getDateTo());
            }
            if (eventFilters.getLocation() != null && !eventFilters.getLocation().isBlank()) {
                urlBuilder.append("&location=").append(eventFilters.getLocation());
            }
            if (eventFilters.getMinAge() != null) {
                urlBuilder.append("&minAge=").append(eventFilters.getMinAge());
            }
            if (eventFilters.getMaxAge() != null) {
                urlBuilder.append("&maxAge=").append(eventFilters.getMaxAge());
            }
        }

        ResponseEntity<EventPageResponse> response = restTemplate.exchange(
                urlBuilder.toString(),
                HttpMethod.GET,
                null,
                EventPageResponse.class
        );

        return response.getBody();
    }



    public EventDto getEventById(Long id) {
        ResponseEntity<EventDto> response = restTemplate.exchange(
                eventServiceUrl + "/api/v1/events/" + id,
                HttpMethod.GET,
                null,
                EventDto.class
        );
        return response.getBody();
    }

    public EventDto createEvent(EventCreateDto eventDto, MultipartFile photo) {
        String url = eventServiceUrl + "/api/v1/events";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("event", eventDto);

        if (photo != null && !photo.isEmpty()) {
            body.add("photo", photo.getResource());
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<EventDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                EventDto.class
        );

        return response.getBody();
    }

    public EventDto updateEventById(Long id, EventUpdateDto eventDto, MultipartFile photo, Long currentUserId) {
        String url = eventServiceUrl + "/api/v1/events/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-User-Id", String.valueOf(currentUserId));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("event", eventDto);

        if (photo != null && !photo.isEmpty()) {
            body.add("photo", photo.getResource());
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<EventDto> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                EventDto.class
        );

        return response.getBody();
    }

    public void deleteEventById(Long id, Long currentUserId) {
        String url = eventServiceUrl + "/api/v1/events/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", String.valueOf(currentUserId));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );
    }

    public EventParticipantDto registerForEvent(Long eventId, Long userId) {
        String url = eventServiceUrl + "/api/v1/events/" + eventId + "/register";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", String.valueOf(userId));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<EventParticipantDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                EventParticipantDto.class
        );

        return response.getBody();
    }

    public void cancelRegistration(Long eventId, Long userId) {
        String url = eventServiceUrl + "/api/v1/events/" + eventId + "/register";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", String.valueOf(userId));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );
    }

    public List<EventDto> getUserEvents(Long userId) {
        ResponseEntity<List<EventDto>> response = restTemplate.exchange(
                eventServiceUrl + "/api/v1/events/user/" + userId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EventDto>>() {}
        );
        return response.getBody();
    }

    public List<EventDto> getUserEventsByStatus(Long userId, RegistrationStatus status) {
        String url = eventServiceUrl + "/api/v1/events/registrations?status=" + status.name();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", String.valueOf(userId));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<EventDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<EventDto>>() {}
        );

        return response.getBody();
    }

    public List<EventDto> getMyEvents(Long userId, RegistrationStatus status) {
        String url = eventServiceUrl + "/api/v1/events/my?status=" + status.name();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", String.valueOf(userId));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<EventDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<EventDto>>() {}
        );

        return response.getBody();
    }
}