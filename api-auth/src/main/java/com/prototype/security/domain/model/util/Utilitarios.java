package com.prototype.security.domain.model.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

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

	public static String formatarDescricao(String descricao) {
		String output = descricao.replaceAll("(\\p{Lu})", " $1").trim();
		output = output.replaceAll("(\\d)", " $1").trim().toLowerCase();
		output = output.substring(0, 1).toUpperCase() + output.substring(1);
		return adicionarAcentuacao(output);
	}

  public static String adicionarAcentuacao(String s) {
		return s
			.replace("", "");
	}

  public static LocalDate stringToLocalDate(String data){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(data, formatter);
  }

  public static String validarObjeto(Object obj){
    return Objects.isNull(obj) ? "" : obj.toString();
  }
}
