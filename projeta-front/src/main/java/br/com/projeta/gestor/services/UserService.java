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

import com.vaadin.flow.server.ErrorEvent;

import br.com.projeta.gestor.config.ExceptionHandler;
import br.com.projeta.gestor.data.dto.FiltrosRequest;
import br.com.projeta.gestor.data.dto.UsuarioResponse;
import br.com.projeta.gestor.data.dto.UsuarioResponseFilter;
import br.com.projeta.gestor.data.dto.UsuarioResquest;
import br.com.projeta.gestor.data.enuns.TipoNotificacaoEnum;
import br.com.projeta.gestor.infra.AuthFeing;
import br.com.projeta.gestor.views.componentes.HistoricoAlteracaoDTO;
import br.com.projeta.gestor.views.componentes.Notificacao;
import br.com.projeta.gestor.views.componentes.NotificacaoAlert;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final AuthFeing dominiosFeing;
  private final ExceptionHandler exceptionHandler;

  public Page<UsuarioResponse> buscarTodos(int page, int pageSize, String sortField, Sort.Direction sortDirection) {
    ResponseEntity<Page<UsuarioResponse>> response = dominiosFeing.buscaComFiltros(page, pageSize, sortField,
        sortDirection);
    return response.getBody();
  }

  public void saveUser(UsuarioResquest usuario) {
    String menssagem = "Usuário inserido com sucesso.";
    try {
      dominiosFeing.saveUser(usuario);
      NotificacaoAlert.getInstance().alerta("Sucesso", menssagem);
    } catch (Exception e) {
      exceptionHandler.error(new ErrorEvent(e));
    }
  }

  public UsuarioResponse findById(Long id) {
    try {
      return dominiosFeing.findById(id).getBody();
    } catch (Exception e) {
      exceptionHandler.error(new ErrorEvent(e));
    }
    return null;
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
      exceptionHandler.error(new ErrorEvent(e));
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
    } catch (Exception e) {
      exceptionHandler.error(new ErrorEvent(e));
    }
    return new Notificacao(menssagem, tipo);
  }

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

  public Page<UsuarioResponseFilter> buscaFiltrada(int pageSize, int currentPage, String sortField,
      Sort.Direction sortDirection, FiltrosRequest filtros) {
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

  public void reseteSenha(Long idCredencial) {
    try {
      dominiosFeing.resetPassword(idCredencial);
    } catch (Exception e) {
      exceptionHandler.error(new ErrorEvent(e));
    }
  }

  public List<HistoricoAlteracaoDTO> getHistoricoAlteracoes(Long isUduario) {
    try {
      return dominiosFeing.getHistorico(isUduario).getBody();
    } catch (Exception e) {
      exceptionHandler.error(new ErrorEvent(e));
    }
    return null;
  }
}
