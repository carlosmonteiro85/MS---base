package br.com.projeta.gestor.views.componentes;

import java.util.List;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;

import br.com.projeta.gestor.services.UserService;
import br.com.projeta.gestor.util.Utilitarios;

public class HistoricoAlteracaoDialog extends Dialog {

  private final UserService usuarioService;
  private final Long idUsuario;

  public HistoricoAlteracaoDialog(UserService userService, Long idUsuario) {
    setHeaderTitle("Historico de Alteções");
    this.usuarioService = userService;
    this.idUsuario = idUsuario;
    Grid<HistoricoAlteracaoDTO> grid = new Grid<>(HistoricoAlteracaoDTO.class, false);
    grid.addColumn(HistoricoAlteracaoDTO::getResponsavel).setHeader("Responsável").setFlexGrow(0).setWidth("250px");
    grid.addColumn(createPersonRenderer()).setHeader("Campo").setFlexGrow(0).setWidth("250px");
    grid.addColumn(HistoricoAlteracaoDTO::getSituacaoAnterior).setHeader("Valor anterior").setFlexGrow(0).setWidth("250px");
    grid.addColumn(HistoricoAlteracaoDTO::getSituacaoAtual).setHeader("Valor Atual").setFlexGrow(0).setWidth("250px");
    grid.addColumn(d -> Utilitarios.formatData(d.getDataAlteracao())).setHeader("Data da Alteração").setFlexGrow(0).setWidth("250px");

    List<HistoricoAlteracaoDTO> historicos = userService.getHistoricoAlteracoes(idUsuario);
    GridListDataView<HistoricoAlteracaoDTO> dataView = grid.setItems(historicos);

    TextField searchField = new TextField();
    searchField.setWidth("50%");
    searchField.setPlaceholder("Pesquisar");
    searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    searchField.setValueChangeMode(ValueChangeMode.EAGER);
    searchField.addValueChangeListener(e -> dataView.refreshAll());

    dataView.addFilter(person -> {
      String searchTerm = searchField.getValue().trim();

      if (searchTerm.isEmpty()){
        return true;
      }

      boolean nomeCampo = matchesTerm(person.getCampo(), searchTerm);
      boolean dataAlteracao = matchesTerm(Utilitarios.formatData(person.getDataAlteracao()), searchTerm);
      boolean responsavel = matchesTerm(person.getResponsavel(),searchTerm);

      return nomeCampo || dataAlteracao || responsavel;
    });

    VerticalLayout layout = new VerticalLayout(searchField, grid);
    layout.setPadding(false);
    layout.setWidth("1300px");
    add(layout);
  }

  private static Renderer<HistoricoAlteracaoDTO> createPersonRenderer() {
    return LitRenderer.<HistoricoAlteracaoDTO>of(
        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
            // + " <vaadin-avatar img=\"${item.pictureUrl}\"
            // name=\"${item.fullName}\"></vaadin-avatar>"
            + "  <span> ${item.campo} </span>"
            + "</vaadin-horizontal-layout>")
        .withProperty("campo", HistoricoAlteracaoDTO::getCampo);
  }

  private boolean matchesTerm(String value, String searchTerm) {
    return value.toLowerCase().contains(searchTerm.toLowerCase());
  }
}
