package com.javachess.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        /*
         * Enables PATCH requests for restTemplate
         * https://stackoverflow.com/questions/29447382/resttemplate-patch-request
         */
        restTemplate.getRestTemplate().setRequestFactory(
          new HttpComponentsClientHttpRequestFactory()
        );
    }

    @Test
    public void contexLoads() throws Exception {
      assertThat(controller).isNotNull();
    }

    @Test
    public void home() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
          String.class)).contains("Chess API");
    }

    @Test
    public void health() {
        assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/health",
          String.class).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void createGame() {
        // TODO: moves should not be part of the response
        String expected = "{\"id\":1,\"player1\":{\"id\":\"1\",\"color\":\"WHITE\"},\"player2\":null,\"moves\":[]}";
        assertThat(this.restTemplate.postForEntity("http://localhost:" + port + "/games", "{\"playerId\":\"1\"}",
          String.class).getBody()).isEqualTo(expected);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void listGames() {
        String expected = "[{\"id\":1,\"player1\":{\"id\":\"1\",\"color\":\"WHITE\"},\"player2\":null,\"moves\":[]},{\"id\":2,\"player1\":{\"id\":\"1\",\"color\":\"WHITE\"},\"player2\":null,\"moves\":[]}]";
        this.restTemplate.postForEntity("http://localhost:" + port + "/games", "{\"playerId\":\"1\"}",
          String.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/games", "{\"playerId\":\"1\"}",
          String.class);
        assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/games", String.class).getBody()).isEqualTo(expected);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void joinGame() {
        String expected = "{\"id\":1,\"player1\":{\"id\":\"1\",\"color\":\"WHITE\"},\"player2\":{\"id\":\"2\",\"color\":\"BLACK\"},\"moves\":[]}";
        this.restTemplate.postForEntity("http://localhost:" + port + "/games", "{\"playerId\":\"1\"}", String.class);
        assertThat(this.restTemplate.patchForObject("http://localhost:" + port + "/games/1", "{\"playerId\":\"2\"}", String.class)).isEqualTo(expected);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("PATCH /games should return game already full with 409 status code")
    public void joinFullGame() {
        String expected = "{\"message\":\"This game is already full !\"}";
        this.restTemplate.postForEntity("http://localhost:" + port + "/games", "{\"playerId\":\"1\"}", String.class);
        this.restTemplate.patchForObject("http://localhost:" + port + "/games/1", "{\"playerId\":\"2\"}", String.class);

        ResponseEntity<String> re = this.restTemplate.exchange("http://localhost:" + port + "/games/1", HttpMethod.PATCH, new HttpEntity<String>("{\"playerId\":\"3\"}"), String.class);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(re.getBody()).isEqualTo(expected);
    }
}
