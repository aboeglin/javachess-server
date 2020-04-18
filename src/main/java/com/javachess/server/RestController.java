package com.javachess.server;

import com.google.gson.Gson;
import com.javachess.logic.Game;
import com.javachess.logic.Player;
import com.javachess.server.message.PerformMove;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  @ResponseBody
  public String home() {
    return "Chess API";
  }

  @RequestMapping(value = "/health", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity health() {
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value = "/games", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity createGame(@RequestBody String payload) {
    // TODO: Register the created game to GameOrchestrator !
    Gson gson = new Gson();
    CreateGameMessage input = gson.fromJson(payload, CreateGameMessage.class);
    return new ResponseEntity(Game.of(1, Player.of(input.getUserId())), HttpStatus.OK);
  }

}

class CreateGameMessage {
  private String userId;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
