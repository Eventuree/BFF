package eventure.beckendforfrontend.controller;

import eventure.beckendforfrontend.model.dto.ParticipantDto;
import eventure.beckendforfrontend.model.dto.StatusUpdateDto;
import eventure.beckendforfrontend.service.EventService;
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
}