package eventure.beckendforfrontend.model.dto;

import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponseDTO {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String photoUrl;
    private String gender;
    private Short age;
    private String bio;
    private String location;
    private Set<String> interests;
    private Map<String, String> socialNetworks;
}