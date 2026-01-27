package eventure.beckendforfrontend.model.dto;

import lombok.Data;

@Data
public class ParticipantDto {
    private Long id;
    private String name;
    private String picture;
    private String status;
}