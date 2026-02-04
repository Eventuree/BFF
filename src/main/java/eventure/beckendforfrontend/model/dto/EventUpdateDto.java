package eventure.beckendforfrontend.model.dto;

import java.time.LocalDateTime;

import eventure.beckendforfrontend.model.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventUpdateDto {
    private Long organizerId;
    private String title;
    private String description;
    private Long categoryId;
    private LocalDateTime eventDate;
    private Integer maxParticipants;
    private Short minAge;
    private Short maxAge;
    private Gender requiredGender;
    private String chatLink;
    private Boolean isAlive;
    private Long viewCount;
}
