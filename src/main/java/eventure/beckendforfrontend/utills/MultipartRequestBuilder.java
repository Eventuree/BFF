package eventure.beckendforfrontend.utills;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartRequestBuilder {

    /**
     * Builds multipart/form-data request with JSON data and optional file
     */
    public <T> HttpEntity<MultiValueMap<String, Object>> build(T data, MultipartFile file) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> jsonPart = new HttpEntity<>(data, jsonHeaders);
        body.add("data", jsonPart);

        if (file != null && !file.isEmpty()) {
            addFilePart(body, file);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return new HttpEntity<>(body, headers);
    }

    private void addFilePart(MultiValueMap<String, Object> body, MultipartFile file) {
        try {
            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            body.add("file", fileResource);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + e.getMessage(), e);
        }
    }
}
