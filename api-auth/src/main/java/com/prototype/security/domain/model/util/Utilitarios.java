package com.prototype.security.domain.model.util;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.prototype.security.api.dto.response.UsuarioResponse;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utilitarios {

  public static Page<UsuarioResponse> getPagina(List<UsuarioResponse> lista, int pageNumber, int pageSize) {
    if (pageNumber < 1) {
      pageNumber = 1;
  }
    int fromIndex = (pageNumber - 1) * pageSize;
    int toIndex = Math.min(fromIndex + pageSize, lista.size());
    List<UsuarioResponse> content = lista.subList(fromIndex, toIndex);
    return new PageImpl<>(content, PageRequest.of(pageNumber - 1, pageSize), lista.size());
  }
}
