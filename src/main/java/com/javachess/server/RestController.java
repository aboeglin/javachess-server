package com.javachess.server;

import com.google.gson.Gson;
import com.javachess.logic.Game;
import com.javachess.logic.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RestController {

  @Autowired
  private GameOrchestrator orchestrator;

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
    Gson gson = new Gson();
    UserMessage input = gson.fromJson(payload, UserMessage.class);
    Player p = Player.of(input.getUserId());
    Game response = this.orchestrator.createGame(p);
    return new ResponseEntity(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/games", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity listGames() {
    return new ResponseEntity(this.orchestrator.getGames(), HttpStatus.OK);
  }

  @RequestMapping(value = "/games/{id}", method = RequestMethod.PATCH)
  @ResponseBody
  public ResponseEntity joinGame(@PathVariable(name = "id") int gameId, @RequestBody String payload) {
    Gson gson = new Gson();
    UserMessage input = gson.fromJson(payload, UserMessage.class);
    Player p = Player.of(input.getUserId());
    return new ResponseEntity(this.orchestrator.joinGameById(p, gameId), HttpStatus.OK);
  }
}

class UserMessage {
  private String userId;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
