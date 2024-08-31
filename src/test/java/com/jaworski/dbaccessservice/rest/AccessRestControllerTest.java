package com.jaworski.dbaccessservice.rest;

import com.jaworski.dbaccessservice.configuration.AppResources;
import com.jaworski.dbaccessservice.dto.Student;
import com.jaworski.dbaccessservice.dto.UserRestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccessRestControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private AppResources resources;

    @Test
    void getHello_ReturnsValidContent() {
        UserRestService credentials = resources.getRestServiceCredentials();
        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth(credentials.getName(), credentials.getPassword())
                .getForEntity("/api/hello", String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertNotNull(response.getBody());
    }

    @Test
    void getHello_returnsStatusUnauthorized_whenWrongCredentials() {
        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("user", "pass")
                .getForEntity("/api/hello", String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getAll_returnsStatusUnauthorized_whenWrongURL() {
        UserRestService credentials = resources.getRestServiceCredentials();
        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth(credentials.getName(), credentials.getPassword())
                .getForEntity("/api/", String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAll_returnsOK_whenWrongCredentials() {
        UserRestService credentials = resources.getRestServiceCredentials();
        ResponseEntity<List<Student>> response = testRestTemplate
                .withBasicAuth(credentials.getName(), credentials.getPassword())
                .exchange("/api/names", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(List.class, response.getBody());
        assertInstanceOf(Student.class, response.getBody().stream().findAny().orElseThrow());
    }
}