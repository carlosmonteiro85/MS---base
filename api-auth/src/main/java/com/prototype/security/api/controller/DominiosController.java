package com.prototype.security.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prototype.security.api.dto.DominiosTelaUser;
import com.prototype.security.api.dto.response.HistoricoOutputDTO;
import com.prototype.security.domain.service.DominiosService;
import com.prototype.security.domain.service.HistoricoAlteracaoUsuario;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dominios")
@RequiredArgsConstructor
public class DominiosController {
  private final DominiosService dominiosService;
  private final HistoricoAlteracaoUsuario historico;

  @GetMapping("/form-user")
  public ResponseEntity<DominiosTelaUser> requestMethodName() {
    return ResponseEntity.ok(dominiosService.bindDominiosTelaUser());
  }
  @GetMapping("/teste")
  public ResponseEntity<List<HistoricoOutputDTO>> getHistorico() {
    return ResponseEntity.ok(historico.buscarHistorico(5L));
  }
}
