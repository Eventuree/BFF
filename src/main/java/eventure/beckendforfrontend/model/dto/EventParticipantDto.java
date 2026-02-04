package eventure.beckendforfrontend.model.dto;

import java.time.LocalDateTime;

import eventure.beckendforfrontend.model.enums.RegistrationStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventParticipantDto {
    private Long userId;
    private String name;
    private String avatarUrl;
    private RegistrationStatus status;
    private LocalDateTime joinedAt;
}
