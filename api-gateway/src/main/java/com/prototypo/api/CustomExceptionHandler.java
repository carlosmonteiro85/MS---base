package com.prototypo.api;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleSignatureException(RuntimeException ex) {
    log.error("Error {}", ex);
    Problem problem = Problem.builder()
    .hora(LocalDateTime.now())
    .detalhes("Erro " + ex.getMessage())
    .status(HttpStatus.UNAUTHORIZED.value())
    .build();
    return ResponseEntity.status(problem.getStatus()).body(problem);
  }
  
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleSignatureException(Exception ex) {
    log.error("Error {}", ex);
    Problem problem = Problem.builder()
            .hora(LocalDateTime.now())
            .detalhes("Ocorreu um error interno, consulte o Administrador")
            .status(HttpStatus.BAD_REQUEST.value())
           .build();

    return ResponseEntity.status(problem.getStatus()).body(problem);
  }
}
