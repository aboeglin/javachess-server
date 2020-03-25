package com.javachess.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    String port = System.getenv("PORT");
    if (port == null) {
      port = "80";
    }
    SpringApplication app = new SpringApplication(Application.class);
    app.setDefaultProperties(Collections.singletonMap("server.port", port));
    app.run(args);
  }

}
