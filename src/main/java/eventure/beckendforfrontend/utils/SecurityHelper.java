package eventure.beckendforfrontend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class SecurityHelper {

    private final ObjectMapper objectMapper;

    public Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        try {
            String[] chunks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));

            JsonNode jsonNode = objectMapper.readTree(payload);

            if (jsonNode.has("id")) {
                return jsonNode.get("id").asLong();
            } else if (jsonNode.has("sub")) {
                return Long.parseLong(jsonNode.get("sub").asText());
            } else {
                throw new SecurityException("Token does not contain user ID");
            }

        } catch (Exception e) {
            throw new SecurityException("Failed to parse JWT token: " + e.getMessage());
        }
    }
}