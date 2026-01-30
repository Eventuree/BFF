package eventure.beckendforfrontend.controller;

import eventure.beckendforfrontend.model.dto.ProfileRequestDTO;
import eventure.beckendforfrontend.model.dto.ProfileResponseDTO;
import eventure.beckendforfrontend.model.dto.ProfileSummaryDto;
import eventure.beckendforfrontend.service.EventService;
import eventure.beckendforfrontend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final EventService eventService;

    @GetMapping("/{userId}/summary")
    public ResponseEntity<ProfileSummaryDto> getUserProfileSummary(@PathVariable Long userId) {
        ProfileSummaryDto profileDto = profileService.getProfile(userId);
        return ResponseEntity.ok(profileDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileResponseDTO> createProfile(
            @RequestPart("data") ProfileRequestDTO request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        if (request.getUserId() == null || request.getEmail() == null) {
            throw new IllegalArgumentException("UserId and Email are required for creation");
        }

        ProfileResponseDTO response = profileService.createProfile(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileResponseDTO> updateProfile(
            @PathVariable Long userId,
            @RequestPart("data") ProfileRequestDTO request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        ProfileResponseDTO response = profileService.updateProfile(userId, request, file);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long userId) {
        profileService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponseDTO> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }

    @GetMapping("/{userId}/rating")
    public ResponseEntity<Double> getUserRating(@PathVariable Long userId) {
        Double rating = eventService.getOrganizerRating(userId);
        return ResponseEntity.ok(rating);
    }
}
