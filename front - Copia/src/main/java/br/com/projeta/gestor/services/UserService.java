package br.com.projeta.gestor.services;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.projeta.gestor.data.dto.FiltrosRequest;
import br.com.projeta.gestor.data.dto.UsuarioResponse;
import br.com.projeta.gestor.data.dto.UsuarioResponseFilter;
import br.com.projeta.gestor.data.dto.UsuarioResquest;
import br.com.projeta.gestor.data.enuns.TipoNotificacaoEnum;
import br.com.projeta.gestor.data.exception.NegocioEsception;
import br.com.projeta.gestor.infra.AuthFeing;
import br.com.projeta.gestor.views.componentes.Notificacao;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final AuthFeing dominiosFeing;

  public Page<UsuarioResponse> buscarTodos(int page, int pageSize, String sortField, Sort.Direction sortDirection) {
    ResponseEntity<Page<UsuarioResponse>> response = dominiosFeing.buscaComFiltros(page, pageSize, sortField,
        sortDirection);
    return response.getBody();
  }

  public Notificacao saveUser(UsuarioResquest usuario) {
    String menssagem = "";
    TipoNotificacaoEnum tipo = null;
    try {
      ResponseEntity<Void> responseEntity = dominiosFeing.saveUser(usuario);

      if (Objects.nonNull(responseEntity) && responseEntity.getStatusCode().is2xxSuccessful()) {
        menssagem = "O usuário " + usuario.getNome() + " foi adicionado com sucesso.";
        tipo = TipoNotificacaoEnum.SUCESSO;
      } else if (Objects.nonNull(responseEntity) && responseEntity.getStatusCode().is5xxServerError()) {
        menssagem = "Não foi possível adicionar o usuario " + usuario.getNome();
        tipo = TipoNotificacaoEnum.ALERTA;
      }
    } catch (FeignException e) {

      log.error("Error : {}", e.getMessage());
      if (e.status() == 401) {
        throw new NegocioEsception(e.getMessage());
      }
      throw new RuntimeException(e.getMessage());
    }
    return new Notificacao(menssagem, tipo);
  }

  public UsuarioResponse findById(Long id) {
    try {
      return dominiosFeing.findById(id).getBody();
    } catch (FeignException e) {

      log.error("Error : {}", e.getMessage());
      if (e.status() == 401) {
        throw new NegocioEsception(e.getMessage());
      }
      throw new RuntimeException(e.getMessage());
    }
  }

  public Notificacao updateUser(Long id, UsuarioResquest usuario) {
    String menssagem = "";
    TipoNotificacaoEnum tipo = null;
    try {
      ResponseEntity<Void> responseEntity = dominiosFeing.update(id, usuario);
      if (Objects.nonNull(responseEntity) && responseEntity.getStatusCode().is2xxSuccessful()) {
        menssagem = "O usuário " + usuario.getNome() + " foi atualizado com sucesso.";
        tipo = TipoNotificacaoEnum.SUCESSO;
      }
    } catch (Exception e) {
      log.error("Erro ao atualizar", e);
      menssagem = "Ocorreu um error interno\nDetalhes do error: " + e.getMessage();
      tipo = TipoNotificacaoEnum.ERROR;
    }
    return new Notificacao(menssagem, tipo);
  }

  public Notificacao deleteUser(Long id) {
    String menssagem = "";
    TipoNotificacaoEnum tipo = null;
    try {
      ResponseEntity<Void> responseEntity = dominiosFeing.delete(id);
      if (Objects.nonNull(responseEntity) && responseEntity.getStatusCode().is2xxSuccessful()) {
        menssagem = "O usuário deletado  com sucesso.";
        tipo = TipoNotificacaoEnum.SUCESSO;
      } else if (Objects.nonNull(responseEntity) && responseEntity.getStatusCode().is5xxServerError()) {
        menssagem = "Não foi possível deletar o usuario";
        tipo = TipoNotificacaoEnum.ALERTA;
      }
    } catch (FeignException e) {
      log.error("Error : {}", e.getMessage());
      if(e.status() == 401){
          throw new NegocioEsception(e.getMessage());
      }
      throw new RuntimeException(e.getMessage());
    }
    return new Notificacao(menssagem, tipo);
  }

  // public Optional<User> get(Long id) {
  //   return Optional.empty();
  // }

  // public User update(User entity) {
  //   return new User();
  // }

  // public void delete(Long id) {
  // }

  // public Page<User> list(Pageable pageable) {
  //   return list(pageable, null, null);
  // }

  // public Page<User> list(Pageable pageable, Specification<User> filter) {
  //   return list(pageable, filter, null);
  // }

  // public int count() {
  //   return 0;
  // }

  public <T> Page<T> list(Pageable pageable, Specification<T> filter, List<T> listaConteudo) {
    List<T> fakePageContent;
    int pageSize = pageable.getPageSize();
    int currentPage = pageable.getPageNumber();
    int startItem = currentPage * pageSize;

    if (listaConteudo.size() < startItem) {
      fakePageContent = Collections.emptyList();
    } else {
      int toIndex = Math.min(startItem + pageSize, listaConteudo.size());
      fakePageContent = listaConteudo.subList(startItem, toIndex);
    }

    return new PageImpl<>(fakePageContent, pageable, listaConteudo.size());
  }

  public Page<UsuarioResponseFilter> buscaFiltrada(int pageSize, int currentPage , String sortField, Sort.Direction sortDirection, FiltrosRequest filtros ) {
    if (pageSize < 0) {
      pageSize = 0; // Define um valor padrão mínimo para o tamanho da página
  }
    return dominiosFeing.consultaFiltrada(
      pageSize, 
        currentPage,
        sortField,
        sortDirection, 
        filtros).getBody();
  }
}
