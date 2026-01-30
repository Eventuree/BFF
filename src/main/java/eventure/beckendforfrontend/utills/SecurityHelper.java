package eventure.beckendforfrontend.utills;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class SecurityHelper {

    public Long extractUserId(HttpServletRequest request) {
        String header = request.getHeader("X-User-Id");

        if (header != null && !header.isBlank()) {
            try {
                return Long.parseLong(header);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid User ID format in header");
            }
        }

        throw new SecurityException("User ID is missing. Please add 'X-User-Id' header.");
    }
}
