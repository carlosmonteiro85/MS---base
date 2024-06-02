package br.com.projeta.site.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class SiteController {

  @Value("${projeta.url.login}")
  private String pahtLogin = "";

  @GetMapping({ "", "home", "/" })
  public String home(Model model) {
    model.addAttribute("currentPage", "home");
    model.addAttribute("pahtLogin", pahtLogin);
    return "index";
  }
  
  @GetMapping("about")
  public String about(Model model) {
    model.addAttribute("currentPage", "about");
    model.addAttribute("pahtLogin", pahtLogin);
    return "about";
  }
  
  @GetMapping("contact")
  public String contact(Model model) {
    model.addAttribute("currentPage", "contact");
    model.addAttribute("pahtLogin", pahtLogin);
    return "contact";
  }
  
  @GetMapping("services")
  public String services(Model model) {
    model.addAttribute("currentPage", "services");
    model.addAttribute("pahtLogin", pahtLogin);
    return "services";
  }
}
