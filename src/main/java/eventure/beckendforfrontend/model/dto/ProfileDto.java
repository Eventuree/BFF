package eventure.beckendforfrontend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    String name;     // firstName + lastName
    String username;
    String avatarUrl;
}
