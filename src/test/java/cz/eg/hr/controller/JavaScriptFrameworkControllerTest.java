package cz.eg.hr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.eg.hr.data.JavaScriptFramework;
import cz.eg.hr.dto.JavaScriptFrameworkDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JavaScriptFrameworkControllerTest {

    private static final String BASE_URL = "/api/frameworks";
    private static final String SEARCH_URL = BASE_URL + "/search?text=";
    private static final String FRAMEWORK_NAME = "Test.js";
    private static final String FRAMEWORK_VERSION = "4.1";
    private static final LocalDate SUPPORT_DATE = LocalDate.of(2026, 2, 10);
    private static final BigDecimal FRAMEWORK_RATING = BigDecimal.valueOf(3);
    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTING_ID = 58L;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateFramework_Success() {
        JavaScriptFrameworkDto dto = createFrameworkDto();

        ResponseEntity<JavaScriptFramework> response = restTemplate.postForEntity(BASE_URL, dto, JavaScriptFramework.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(dto.getName());
        assertThat(response.getBody().getVersion()).isEqualTo(dto.getVersion());
        assertThat(response.getBody().getEndOfSupportDate()).isEqualTo(dto.getEndOfSupportDate());
    }


    @Test
    void testCreateFramework_Failure_Duplicate() {
        JavaScriptFrameworkDto dto = createFrameworkDto();

        restTemplate.postForEntity(BASE_URL, dto, JavaScriptFramework.class);
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, dto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("This framework already exists.");
    }

    @Test
    void testUpdateFramework_Success() {
        JavaScriptFrameworkDto dto = createFrameworkDto();
        dto.setVersion("5.0");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JavaScriptFrameworkDto> requestEntity = new HttpEntity<>(dto, headers);

        ResponseEntity<JavaScriptFramework> response = restTemplate.exchange(
            BASE_URL + "/" + EXISTING_ID,
            HttpMethod.PUT,
            requestEntity,
            JavaScriptFramework.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getVersion()).isEqualTo("5.0");
    }

    @Test
    void testUpdateFramework_NotFound() {
        JavaScriptFrameworkDto dto = createFrameworkDto();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JavaScriptFrameworkDto> requestEntity = new HttpEntity<>(dto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            BASE_URL + "/" + NON_EXISTING_ID,
            HttpMethod.PUT,
            requestEntity,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetAllFrameworks_Success() {
        JavaScriptFrameworkDto dto = createFrameworkDto();
        ResponseEntity<JavaScriptFramework> createResponse = restTemplate.postForEntity(BASE_URL, dto, JavaScriptFramework.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<JavaScriptFramework[]> response = restTemplate.getForEntity(BASE_URL, JavaScriptFramework[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    void testFullTextSearch_Success() {
        ResponseEntity<JavaScriptFramework[]> response = restTemplate.getForEntity(SEARCH_URL + FRAMEWORK_NAME, JavaScriptFramework[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testFullTextSearch_NotFound() {
        ResponseEntity<JavaScriptFramework[]> response = restTemplate.getForEntity(SEARCH_URL + "NonExistentFramework", JavaScriptFramework[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void testDeleteFramework_Success() {
        ResponseEntity<Void> response = restTemplate.exchange(
            BASE_URL + "/" + EXISTING_ID,
            HttpMethod.DELETE,
            null,
            Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteFramework_NotFound() {
        ResponseEntity<Void> response = restTemplate.exchange(
            BASE_URL + "/" + NON_EXISTING_ID,
            HttpMethod.DELETE,
            null,
            Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private JavaScriptFrameworkDto createFrameworkDto() {
        JavaScriptFrameworkDto dto = new JavaScriptFrameworkDto();
        dto.setName(FRAMEWORK_NAME);
        dto.setVersion(FRAMEWORK_VERSION);
        dto.setEndOfSupportDate(SUPPORT_DATE);
        dto.setRating(FRAMEWORK_RATING);
        return dto;
    }
}
