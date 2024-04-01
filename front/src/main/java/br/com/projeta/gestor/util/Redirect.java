package br.com.projeta.gestor.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Route;

@Component
public class Redirect {

  @Value("${projeta.url.site}")
  private String pahtSite = "";

  @Value("${projeta.url.login}")
  private String pahtLogin = "";

  public static void criarRedirect(Class<? extends com.vaadin.flow.component.Component> clazz) {

    Route routeAnnotation = clazz.getAnnotation(Route.class);
    if (routeAnnotation != null) {
      UI.getCurrent().getPage().executeJs("window.location.href = $0",
          routeAnnotation.value().replace("/:samplePersonID?/:action?(edit)", ""));
    }
  }

  public void login() {
    UI.getCurrent().getPage().executeJs("window.location.href = $0", pahtLogin);
  }

  public void site() {
    UI.getCurrent().getPage().executeJs("window.location.href = $0", pahtSite);
  }

}
