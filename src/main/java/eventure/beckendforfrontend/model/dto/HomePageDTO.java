package eventure.beckendforfrontend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomePageDTO {
    private List<EventDto> trendingEvents;
    private List<CategoryDto> categories;
    private ProfileDto profile;
    private List<EventDto> events;

}
