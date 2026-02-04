package eventure.beckendforfrontend.controller;

import eventure.beckendforfrontend.model.dto.ParticipantDto;
import eventure.beckendforfrontend.model.dto.RateEventDto;
import eventure.beckendforfrontend.model.dto.StatusUpdateDto;
import eventure.beckendforfrontend.service.EventService;
import eventure.beckendforfrontend.utils.SecurityHelper;
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
    private final SecurityHelper securityHelper;

    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<ParticipantDto>> getParticipants(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getParticipants(eventId));
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
}