package eventure.beckendforfrontend.controller;

import eventure.beckendforfrontend.model.dto.*;
import eventure.beckendforfrontend.model.enums.RegistrationStatus;
import eventure.beckendforfrontend.service.EventService;
import eventure.beckendforfrontend.utils.SecurityHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final SecurityHelper securityHelper;

    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<ParticipantDto>> getParticipants(
            @PathVariable Long eventId,
            HttpServletRequest request) {
        Long currentUserId = securityHelper.extractUserId(request);
        return ResponseEntity.ok(eventService.getParticipants(eventId, currentUserId));
    }

    @PutMapping("/{eventId}/participants/{userId}")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long eventId,
            @PathVariable Long userId,
            @RequestBody StatusUpdateDto statusDto,
            HttpServletRequest request
    ) {
        Long organizerId = securityHelper.extractUserId(request);

        eventService.changeParticipantStatus(eventId, userId, statusDto, organizerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{eventId}/rate")
    public ResponseEntity<Void> rateEvent(
            @PathVariable Long eventId,
            @RequestBody RateEventDto request,
            HttpServletRequest httpRequest
    ) {
        Long userId = securityHelper.extractUserId(httpRequest);

        eventService.rateEvent(eventId, request.getScore(), userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/archive")
    public ResponseEntity<Object> getArchivedEvents(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request
    ) {
        Long userId = securityHelper.extractUserId(request);

        return ResponseEntity.ok(eventService.getArchivedEvents(userId, type, page, limit));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<EventDto>> getTrendingEvents() {
        return ResponseEntity.ok(eventService.getTrendingEvents());
    }

    @GetMapping("")
    public ResponseEntity<EventPageResponse> getPaginatedEvents(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            EventFiltersDto eventFilters) {
        return ResponseEntity.ok(eventService.getAllEventsPagination(page, limit, eventFilters));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEventById(
            @PathVariable Long id,
            @RequestPart EventUpdateDto eventDto,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            HttpServletRequest request) {
        Long currentUserId = securityHelper.extractUserId(request);

        return ResponseEntity.ok(eventService.updateEventById(id, eventDto, photo, currentUserId));
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestPart("event") @Valid EventCreateDto eventDto,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        EventDto createdEvent = eventService.createEvent(eventDto, photo);
        return ResponseEntity.created(URI.create("/api/v1/events/" + createdEvent.getId()))
                .body(createdEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEventById(@PathVariable Long id,
                                             HttpServletRequest request) {
        Long currentUserId = securityHelper.extractUserId(request);
        eventService.deleteEventById(id, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<EventParticipantDto> registerForEvent(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = securityHelper.extractUserId(request);
        EventParticipantDto participant = eventService.registerForEvent(id, userId);
        return ResponseEntity.created(URI.create("/api/v1/events/" + id + "/participants"))
                .body(participant);
    }

    @DeleteMapping("/{id}/register")
    public ResponseEntity<Void> cancelRegistration(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = securityHelper.extractUserId(request);
        eventService.cancelRegistration(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventDto>> getUserEvents(
            @PathVariable Long userId) {

        return ResponseEntity.ok(eventService.getUserEvents(userId));
    }

    @GetMapping("/registrations")
    public ResponseEntity<List<EventDto>> getUserRegistrations(
            @RequestParam RegistrationStatus status,
            HttpServletRequest request) {
        Long userId = securityHelper.extractUserId(request);

        return ResponseEntity.ok(eventService.getUserEventsByStatus(userId, status));
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyEvents(
            @RequestParam(required = false, defaultValue = "APPROVED") RegistrationStatus status,
            HttpServletRequest request) {
        Long userId = securityHelper.extractUserId(request);

        return ResponseEntity.ok(eventService.getMyEvents(userId, status));
    }
}