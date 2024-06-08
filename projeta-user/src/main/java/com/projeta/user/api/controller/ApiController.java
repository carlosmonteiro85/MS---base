package com.projeta.user.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projeta.user.domain.model.PasswordResetToken;
import com.projeta.user.domain.service.PasswordResetTokenService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class ApiController {

  private final PasswordResetTokenService resetTokenService;

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

  @GetMapping("restaurar-senha/{token}")
  public String restaurarSenha(Model model, @PathVariable(required = true) String token) {

    model.addAttribute("nomeSistema", nomeSistema);
    model.addAttribute("urlRedirect", urlRedirect);

    boolean valido = resetTokenService.isValid(token);

    if(Boolean.FALSE.equals(valido)){
      return "redirect:/login";
    }
    
    model.addAttribute("token", token);
    return "restaurar-senha";
  }
}
