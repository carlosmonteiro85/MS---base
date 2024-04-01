package com.prototype.security.api.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prototype.security.api.dto.UsuarioResquest;
import com.prototype.security.api.dto.request.FiltrosRequest;
import com.prototype.security.api.dto.request.UserProfile;
import com.prototype.security.api.dto.response.UsuarioResponse;
import com.prototype.security.api.dto.response.UsuarioResponseFilter;
import com.prototype.security.domain.model.Usuario;
import com.prototype.security.domain.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

  private final UsuarioService usuarioService;

  @GetMapping()
  public ResponseEntity<Page<UsuarioResponse>> buscaComFiltros(
      @RequestParam int page,
      @RequestParam int pageSize,
      @RequestParam String sortField,
      @RequestParam Sort. Direction sortDirection) {

    PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(sortDirection, sortField));

    return ResponseEntity.ok().body(usuarioService.findAll(pageRequest));
  }

  @PostMapping()
  public ResponseEntity<Void> saveUser(@RequestBody @Valid UsuarioResquest request) {
    Usuario userPersisted = usuarioService.save(request);

    String resourceUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
        .path("/{id}")
        .buildAndExpand(userPersisted.getId())
        .toUriString();

    return ResponseEntity.created(URI.create(resourceUri)).build();
  }

  @GetMapping("{id}")
  public ResponseEntity<UsuarioResponse> findById(@PathVariable(name = "id", required = true) Long id) {
    return ResponseEntity.ok().body(usuarioService.getById(id));
  }

  @PutMapping("{id}")
  public ResponseEntity<Void> update(@PathVariable(name = "id", required = true) Long id,
      @RequestBody UsuarioResquest request) {
    usuarioService.updateUser(id, request);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id", required = true) Long id) {
    usuarioService.deleteUser(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/profile")
  public ResponseEntity<UserProfile> getUsuaProfile(@RequestParam(required = true) String token) {
    return ResponseEntity.ok().body(usuarioService.getUsuaProfile(token));
  }

  @PostMapping("/consulta-filtrada")
  public ResponseEntity<Page<UsuarioResponseFilter>> consultaFiltrada(
      @RequestParam int page,
      @RequestParam int pageSize,
      @RequestParam String sortField,
      @RequestParam Sort.Direction sortDirection,
      @RequestBody @Valid FiltrosRequest filtros) {
    PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(sortDirection, sortField));
    return ResponseEntity.ok().body(usuarioService.findAllFilter(pageRequest, filtros));
  }
}
