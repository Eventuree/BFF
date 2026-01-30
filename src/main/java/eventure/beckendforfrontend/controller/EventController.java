package eventure.beckendforfrontend.controller;

import eventure.beckendforfrontend.model.dto.ParticipantDto;
import eventure.beckendforfrontend.model.dto.RateEventDto;
import eventure.beckendforfrontend.model.dto.StatusUpdateDto;
import eventure.beckendforfrontend.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<ParticipantDto>> getParticipants(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getParticipants(eventId));
    }

    @PutMapping("/{eventId}/participants/{userId}")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long eventId,
            @PathVariable Long userId,
            @RequestBody StatusUpdateDto statusDto,
            @RequestHeader("X-User-Id") Long organizerId
    ) {
        eventService.changeParticipantStatus(eventId, userId, statusDto, organizerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{eventId}/rate")
    public ResponseEntity<Void> rateEvent(
            @PathVariable Long eventId,
            @RequestBody RateEventDto request,
            HttpServletRequest httpRequest
    ) {
        Long userId = extractUserId(httpRequest);

        eventService.rateEvent(eventId, request.getScore(), userId);

        return ResponseEntity.ok().build();
    }

    private Long extractUserId(HttpServletRequest request) {
        String header = request.getHeader("X-User-Id");

        if (header != null && !header.isBlank()) {
            try {
                return Long.parseLong(header);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid User ID format in header");
            }
        }

        throw new SecurityException("User ID is missing. Please add 'X-User-Id' header.");
    }
}