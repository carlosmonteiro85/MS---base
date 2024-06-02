package br.com.projeta.gestor.services;

import org.springframework.stereotype.Service;

import com.vaadin.flow.server.ErrorEvent;

import br.com.projeta.gestor.config.ExceptionHandler;
import br.com.projeta.gestor.data.dto.DominiosTelaUser;
import br.com.projeta.gestor.infra.AuthFeing;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DominiosService {

  private final AuthFeing dominiosFeing;
  private final ExceptionHandler exceptionHandler;

  public DominiosTelaUser getDominios() {
    try {
      return dominiosFeing.getDadosDominios().getBody();
    } catch (FeignException e) {
      exceptionHandler.error(new ErrorEvent(e));
    }
    return null;
  }
}