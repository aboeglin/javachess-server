package com.javachess.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestController {

  @RequestMapping("/")
  public @ResponseBody String greeting() {
    return "Create game";
  }

}
