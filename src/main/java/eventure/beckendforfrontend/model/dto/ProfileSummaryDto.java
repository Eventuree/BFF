package eventure.beckendforfrontend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSummaryDto {
    String name;     // firstName + lastName
    String email;
    String avatarUrl;
}
