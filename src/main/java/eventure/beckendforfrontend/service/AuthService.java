package eventure.beckendforfrontend.service;

import eventure.beckendforfrontend.model.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthService(
            @Qualifier("authServiceRestTemplate") RestTemplate restTemplate,
            @Value("${auth.service.url}") String authServiceUrl
    ) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    public AuthResponseDto register(RegisterRequestDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegisterRequestDto> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthResponseDto> response = restTemplate.exchange(
                authServiceUrl + "/api/auth/register",
                HttpMethod.POST,
                requestEntity,
                AuthResponseDto.class
        );

        return response.getBody();
    }

    public AuthResponseDto login(LoginRequestDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthResponseDto> response = restTemplate.exchange(
                authServiceUrl + "/api/auth/login",
                HttpMethod.POST,
                requestEntity,
                AuthResponseDto.class
        );

        return response.getBody();
    }

    public AuthResponseDto googleLogin(GoogleLoginRequestDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(
                Map.of("token", request.getToken()),
                headers
        );

        ResponseEntity<AuthResponseDto> response = restTemplate.exchange(
                authServiceUrl + "/api/auth/google/login",
                HttpMethod.POST,
                requestEntity,
                AuthResponseDto.class
        );

        return response.getBody();
    }

    public AuthResponseDto refreshToken(String refreshToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + refreshToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<AuthResponseDto> response = restTemplate.exchange(
                authServiceUrl + "/api/auth/refresh",
                HttpMethod.POST,
                requestEntity,
                AuthResponseDto.class
        );

        return response.getBody();
    }

    public Map<String, String> logout(LogoutRequestDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LogoutRequestDto> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                authServiceUrl + "/api/auth/logout",
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        return response.getBody();
    }
}
