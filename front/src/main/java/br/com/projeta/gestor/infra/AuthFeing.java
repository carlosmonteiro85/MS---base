package br.com.projeta.gestor.infra;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.projeta.gestor.data.dto.AuthRequest;
import br.com.projeta.gestor.data.dto.DominiosTelaUser;
import br.com.projeta.gestor.data.dto.FiltrosRequest;
import br.com.projeta.gestor.data.dto.JwtTokenRequest;
import br.com.projeta.gestor.data.dto.RoleRespose;
import br.com.projeta.gestor.data.dto.UserProfile;
import br.com.projeta.gestor.data.dto.UsuarioResponse;
import br.com.projeta.gestor.data.dto.UsuarioResponseFilter;
import br.com.projeta.gestor.data.dto.UsuarioResquest;
import jakarta.validation.Valid;

@FeignClient(name = "api-auth", url = "${feign.client.url.api-auth}")
public interface AuthFeing {

  @PostMapping("/auth")
  public ResponseEntity<JwtTokenRequest> auth(@RequestBody AuthRequest authRequest);

  @GetMapping("/auth/logout")
	public ResponseEntity<Void> logOut();

  @GetMapping("/auth/token")
  public ResponseEntity<JwtTokenRequest> obterToken();

  @PutMapping("/auth/reset-password")
	public ResponseEntity<Void> resetPassword(@RequestParam(required = true) Long idCredencial);

  @GetMapping("/auth")
  public ResponseEntity<Void> validarToken(@RequestParam(required = true) String token);

  @GetMapping("/auth/roles")
  public ResponseEntity<List<RoleRespose>> getRoles(@RequestParam(required = true) String token);

  @GetMapping("/users/profile")
  public ResponseEntity<UserProfile> getUsuaProfile(@RequestParam(required = true) String token);

  @GetMapping("/users")
  public ResponseEntity<Page<UsuarioResponse>> buscaComFiltros(
      @RequestParam int page,
      @RequestParam int pageSize,
      @RequestParam String sortField,
      @RequestParam Sort.Direction sortDirection);

  @PostMapping("users/consulta-filtrada")
  public ResponseEntity<Page<UsuarioResponseFilter>> consultaFiltrada(
      @RequestParam int page,
      @RequestParam int pageSize,
      @RequestParam String sortField,
      @RequestParam Sort.Direction sortDirection,
      @RequestBody @Valid FiltrosRequest filtros);

  @PostMapping("/users")
  public ResponseEntity<Void> saveUser(@RequestBody UsuarioResquest request);

  @GetMapping("/users/{id}")
  public ResponseEntity<UsuarioResponse> findById(@PathVariable(name = "id", required = true) Long id);

  @PutMapping("/users/{id}")
  public ResponseEntity<Void> update(@PathVariable(name = "id", required = true) Long id,
      @RequestBody UsuarioResquest request);

  @DeleteMapping("/users/{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id", required = true) Long id);

  @GetMapping("/dominios/form-user")
  public ResponseEntity<DominiosTelaUser> getDadosDominios();
}
