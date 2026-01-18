package eventure.beckendforfrontend.service;

import eventure.beckendforfrontend.model.dto.ProfileRequestDTO;
import eventure.beckendforfrontend.model.dto.ProfileResponseDTO;
import eventure.beckendforfrontend.model.dto.ProfileSummaryDto;
import eventure.beckendforfrontend.utills.MultipartRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
public class ProfileService {

    private final RestTemplate restTemplate;
    private final MultipartRequestBuilder multipartBuilder;

    private final String profileServiceUrl;

    public ProfileService(
            RestTemplateBuilder builder,
            MultipartRequestBuilder multipartBuilder,
            @Value("${profile.service.url}") String url

    ) {
        this.profileServiceUrl = url;
        this.multipartBuilder = multipartBuilder;
        this.restTemplate = builder.build();

    }

    public ProfileSummaryDto getProfile(Long id) {
        ResponseEntity<ProfileSummaryDto> response = restTemplate.exchange(
                profileServiceUrl + "/api/v1/profiles/" + id.toString() + "/summary",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ProfileSummaryDto>(){}
        );
        return response.getBody();
    }
    public ProfileResponseDTO createProfile(ProfileRequestDTO request, MultipartFile file) {

        HttpEntity<MultiValueMap<String, Object>> requestEntity = multipartBuilder.build(request, file);

        ResponseEntity<ProfileResponseDTO> response = restTemplate.exchange(
                profileServiceUrl + "/api/v1/profiles",
                HttpMethod.POST,
                requestEntity,
                ProfileResponseDTO.class
        );

        return response.getBody();
    }

    public ProfileResponseDTO updateProfile(Long userId, ProfileRequestDTO request, MultipartFile file) {

        HttpEntity<MultiValueMap<String, Object>> requestEntity = multipartBuilder.build(request, file);

        ResponseEntity<ProfileResponseDTO> response = restTemplate.exchange(
                profileServiceUrl + "/api/v1/profiles/" + userId,
                HttpMethod.PUT,
                requestEntity,
                ProfileResponseDTO.class
        );

        return response.getBody();
    }

    public ProfileResponseDTO getProfileByUserId(Long userId) {

        ResponseEntity<ProfileResponseDTO> response = restTemplate.exchange(
                profileServiceUrl + "/api/v1/profiles/" + userId,
                HttpMethod.GET,
                null,
                ProfileResponseDTO.class
        );

        return response.getBody();
    }

    public void deleteProfile(Long userId) {

        restTemplate.delete(profileServiceUrl + "/api/v1/profiles/" + userId);

    }
}
