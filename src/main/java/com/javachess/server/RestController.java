package com.javachess.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

}
