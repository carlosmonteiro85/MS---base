package br.com.projeta.gestor.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ErrorEvent;
import br.com.projeta.gestor.config.ExceptionHandler;
import br.com.projeta.gestor.data.dto.RoleRespose;
import br.com.projeta.gestor.data.dto.UserProfile;
import br.com.projeta.gestor.infra.AuthFeing;
import br.com.projeta.gestor.util.Redirect;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthFeing authFeing;
  private final HttpServletRequest request;
  private final ExceptionHandler exceptionHandler;
  private final Redirect redirectService;

  public void isLogged() {
    UI ui = UI.getCurrent();
    if (ui != null) {
      boolean loggedIn = checkIfLoggedIn();
      if (!loggedIn) {
        redirectService.login();
      }
    }
  }

  public String getTokenCookie() {
    String token = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("token".equals(cookie.getName())) {
          token = cookie.getValue();
          break;
        }
      }
    }
    return token;
  }

  private boolean checkIfLoggedIn() {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("token".equals(cookie.getName())) {
          try {
            authFeing.validarToken(cookie.getValue());
            return true;
          } catch (Exception e) {
            exceptionHandler.error(new ErrorEvent(e));
          }
        }
      }
    }
    return false;
  }

  public UserProfile getProfileUser() {
    Cookie[] cookies = request.getCookies();
    UserProfile user = null;
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("token".equals(cookie.getName())) {
          try {
            user = authFeing.getUsuaProfile(cookie.getValue()).getBody();
          } catch (Exception e) {
            exceptionHandler.error(new ErrorEvent(e));
          }
        }
      }
    }
    return user;
  }

  public List<RoleRespose> userHasRole() {
    try {
      String token = getTokenCookie();
      return authFeing.getRoles(token).getBody();
    } catch (Exception e) {
      exceptionHandler.error(new ErrorEvent(e));
    }
    return List.of();
  }

  public List<String> permissoes() {
    List<RoleRespose> userHasRole = userHasRole();
    List<String> permissoes = new ArrayList<>();
    userHasRole.stream()
        .map(RoleRespose::getPermissoes)
        .flatMap(Collection::stream)
        .forEach(permissoes::add);
    return permissoes;
  }

  public Boolean verificarPermissao(String permissao) {
    return permissoes().contains(permissao);
  }

  public void logout() {
    authFeing.logOut();
  }

  public boolean validarRole(List<String> rolesPermitidas) {
    List<RoleRespose> rolesUsuario = userHasRole();
    return rolesPermitidas.stream()
        .anyMatch(rolePermitida -> rolesUsuario.stream()
            .anyMatch(roleUsuario -> roleUsuario.getPerfil().equals(rolePermitida)));
  }
}
