package eventure.beckendforfrontend.model.dto;

import eventure.beckendforfrontend.model.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private Long organizerId;
    private String title;
    private String description;

    private EventStatus status;

    private LocalDateTime eventDate;

    private Integer maxParticipants;

    private Long categoryId;

    private String bannerPhotoUrl;

    private String location;

    private Short minAge;

    private Short maxAge;

    private String requiredGender;

    private String chatLink;

    private Boolean isAlive;

    private Long viewCount = 0L;

    private LocalDateTime createdAt;
}