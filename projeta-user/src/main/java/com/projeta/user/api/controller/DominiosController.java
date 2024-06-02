package com.projeta.user.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeta.user.api.dto.DominiosTelaUser;
import com.projeta.user.domain.service.DominiosService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dominios")
@RequiredArgsConstructor
public class DominiosController {
  private final DominiosService dominiosService;

  @GetMapping("/form-user")
  public ResponseEntity<DominiosTelaUser> requestMethodName() {
    return ResponseEntity.ok(dominiosService.bindDominiosTelaUser());
  }
}
