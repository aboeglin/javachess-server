package com.javachess.server;

import static org.assertj.core.api.Assertions.assertThat;

import com.javachess.logic.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestController controller;

    @Autowired
    private TestRestTemplate restTemplate;

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
    public void createGame() {
        // TODO: Instead of null, it should take the userId, and set it as player1 in the response.
        String expected = "{\"id\":1,\"player1\":null,\"player2\":null,\"moves\":[]}";
        assertThat(this.restTemplate.postForEntity("http://localhost:" + port + "/games", null,
          String.class).getBody()).isEqualTo(expected);
    }
}
