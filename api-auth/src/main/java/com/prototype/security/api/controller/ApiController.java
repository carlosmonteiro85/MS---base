package com.prototype.security.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class ApiController {

  @Value("${nome.sistema}")
  private String nomeSistema = "";

  @Value("${url.redirect.sucess}")
  private String urlRedirect = "";

  @GetMapping("login")
  public String login(Model model) {
    model.addAttribute("nomeSistema", nomeSistema);
    model.addAttribute("urlRedirect", urlRedirect);
    return "index";
  }

  @GetMapping("esqueci-senha")
  public String esqueciSenha(Model model) {
    model.addAttribute("nomeSistema", nomeSistema);
    model.addAttribute("urlRedirect", urlRedirect);
    return "esqueci-senha";
  }
}
