package com.jaworski.dbaccessservice.rest;

import com.jaworski.dbaccessservice.configuration.DataSourceConfiguration;
import com.jaworski.dbaccessservice.dto.Student;
import com.jaworski.dbaccessservice.dto.UserRestService;
import com.jaworski.dbaccessservice.resources.AppResources;
import com.jaworski.dbaccessservice.service.PersonelService;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ExtendWith(SpringExtension.class)
class AccessRestControllerTest {

    private UriComponentsBuilder httpUrl = null;
    ParameterizedTypeReference<List<Student>> responseType = new ParameterizedTypeReference<>() {
    };

    private UserRestService credentials = new UserRestService();

    @Mock
    private AppResources resources;

    @LocalServerPort
    private String port;

    @BeforeEach
    void setup() {
        credentials.setName("admin");
        credentials.setPassword("admin");
        when(resources.getRestServiceCredentials()).thenReturn(credentials);

        httpUrl = UriComponentsBuilder.fromHttpUrl("https://localhost/api/").port(port);
    }

    private HttpComponentsClientHttpRequestFactory getRequestFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
        final SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        final BasicHttpClientConnectionManager connectionManager =
                new BasicHttpClientConnectionManager(socketFactoryRegistry);
        final CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    private RestTemplate getRestTemplate(UserRestService credentials) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpComponentsClientHttpRequestFactory requestFactory = getRequestFactory();

        return new RestTemplateBuilder()
                .requestFactory(() -> requestFactory)
                .basicAuthentication(credentials.getName(), credentials.getPassword())
                .build();
    }

    @Test
    void getHello_ReturnsValidContent() throws Exception {
        ResponseEntity<String> response = getRestTemplate(resources.getRestServiceCredentials())
                .getForEntity(httpUrl.pathSegment("hello").build().toUri(), String.class);
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
        UserRestService userRestService = new UserRestService();
        userRestService.setName("admin");
        userRestService.setPassword("wrong");
        HttpClientErrorException httpClientErrorException = assertThrows(HttpClientErrorException.class, () -> getRestTemplate(userRestService)
                .getForEntity(httpUrl.pathSegment("hello").toUriString(), String.class));
        assertEquals(HttpStatus.UNAUTHORIZED, httpClientErrorException.getStatusCode());
    }

    @Test
    void getAll_returnsStatusUnauthorized_whenWrongURL() {
        HttpClientErrorException httpClientErrorException = assertThrows(HttpClientErrorException.class, () -> getRestTemplate(resources.getRestServiceCredentials())
                .getForEntity(httpUrl.build().toUri(), String.class));
        assertEquals(HttpStatus.NOT_FOUND, httpClientErrorException.getStatusCode());
    }

    @Test
    @DisplayName("Get all names")
    @Disabled("Needs to be fixed - mock SQLException")
    void getAll_returnsOK_whenValidRequest_names() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        ResponseEntity<List<Student>> response = getRestTemplate(resources.getRestServiceCredentials())
                .exchange(httpUrl.pathSegment("names").toUriString(), HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(List.class, response.getBody());
        assertInstanceOf(Student.class, response.getBody().stream().findAny().orElseThrow());
        assertNotNull(response.getHeaders().get(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_TYPE).contains(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @Disabled("Needs to be fixed - mock SQLException")
    void getAll_returnsOK_whenValidRequest_name() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        ResponseEntity<List<Student>> response = getRestTemplate(resources.getRestServiceCredentials())
                .exchange(httpUrl.pathSegment("names").buildAndExpand("4").toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(List.class, response.getBody());
        assertInstanceOf(Student.class, response.getBody().stream().findAny().orElseThrow());
        assertNotNull(response.getHeaders().get(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_TYPE).contains(MediaType.APPLICATION_JSON_VALUE));
    }
}