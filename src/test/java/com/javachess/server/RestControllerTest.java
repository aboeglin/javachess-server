package com.javachess.server;

import static org.assertj.core.api.Assertions.assertThat;

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

}
