package com.jaworski.dbaccessservice.rest;

import com.jaworski.dbaccessservice.configuration.AppResources;
import com.jaworski.dbaccessservice.dto.Student;
import com.jaworski.dbaccessservice.dto.UserRestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccessRestControllerTest {

    private UriComponentsBuilder httpUrl = null;
    ParameterizedTypeReference<List<Student>> responseType = new ParameterizedTypeReference<>() {
    };

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private AppResources resources;

    @BeforeEach
    void setup() {
        httpUrl = UriComponentsBuilder.fromHttpUrl("http://localhost/api");
    }

    @Test
    void getHello_ReturnsValidContent() {
        UserRestService credentials = resources.getRestServiceCredentials();
        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth(credentials.getName(), credentials.getPassword())
                .getForEntity(httpUrl.pathSegment("hello").build().getPath(), String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertNotNull(response.getBody());
        assertNotNull(response.getHeaders().get(HttpHeaders.CONTENT_TYPE));
        String value = response.getHeaders().get(HttpHeaders.CONTENT_TYPE).stream().findAny().orElseThrow();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, value);
    }

    @Test
    void getHello_returnsStatusUnauthorized_whenWrongCredentials() {
        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("user", "pass")
                .getForEntity(httpUrl.buildAndExpand("hello").getPath(), String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getAll_returnsStatusUnauthorized_whenWrongURL() {
        UserRestService credentials = resources.getRestServiceCredentials();
        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth(credentials.getName(), credentials.getPassword())
                .getForEntity(httpUrl.build().getPath(), String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAll_returnsOK_whenValidRequest_names() {
        UserRestService credentials = resources.getRestServiceCredentials();
        ResponseEntity<List<Student>> response = testRestTemplate
                .withBasicAuth(credentials.getName(), credentials.getPassword())
                .exchange(httpUrl.pathSegment("names").build().getPath(), HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(List.class, response.getBody());
        assertInstanceOf(Student.class, response.getBody().stream().findAny().orElseThrow());
        assertNotNull(response.getHeaders().get(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_TYPE).contains(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void getAll_returnsOK_whenValidRequest_name() {
        UserRestService credentials = resources.getRestServiceCredentials();

        ResponseEntity<List<Student>> response = testRestTemplate
                .withBasicAuth(credentials.getName(), credentials.getPassword())
                .exchange(httpUrl.pathSegment("names").buildAndExpand("4").getPath(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(List.class, response.getBody());
        assertInstanceOf(Student.class, response.getBody().stream().findAny().orElseThrow());
        assertNotNull(response.getHeaders().get(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_TYPE).contains(MediaType.APPLICATION_JSON_VALUE));
    }
}