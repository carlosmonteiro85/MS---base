package br.com.projeta.gestor.views.componentes;

import java.util.Objects;
import java.util.Optional;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import br.com.projeta.gestor.data.dto.DominiosTelaUser;
import br.com.projeta.gestor.data.dto.PerfilResponse;
import br.com.projeta.gestor.data.dto.UsuarioResponse;
import br.com.projeta.gestor.data.dto.UsuarioResquest;
import br.com.projeta.gestor.data.exception.NegocioEsception;
import br.com.projeta.gestor.services.DominiosService;
import br.com.projeta.gestor.services.UserService;

public class ProfileDialog extends Dialog {

  private final Long idUsuario;
  private final DominiosService dominiosService;
  private final UserService usuarioService;
  private UsuarioResponse usuarioLogado;
  TextField nome;
  TextField email;
  TextField cpf;
  TextField celular;
  TextField telefone;
  DatePicker dataNascimento;
  ComboBox<ItemCombo> especialidade;
  PasswordField senha;
  PasswordField senhaConfirmacao;

  public ProfileDialog(Long idUsuario, DominiosService dominiosService, UserService usuarioService) {
      this.idUsuario = idUsuario;
      this.dominiosService = dominiosService;
      this.usuarioService = usuarioService;

      usuarioLogado = usuarioService.findById(idUsuario);

      setHeaderTitle("Profile");
      VerticalLayout dialogLayout = createDialogLayout();
      add(dialogLayout);
      Button saveButton = createSaveButton();
      Button cancelButton = new Button("Cancel", e -> close());
      getFooter().add(saveButton);
      getFooter().add(cancelButton);
      open();
  }

  private VerticalLayout createDialogLayout() {
      VerticalLayout dialogLayout = criarLayoutForm(usuarioLogado);
      dialogLayout.setPadding(false);
      dialogLayout.setSpacing(false);
      dialogLayout.setAlignItems(FlexComponent.Alignment.CENTER);
      dialogLayout.setWidthFull();

      return dialogLayout;
  }

  private Button createSaveButton() {
      Button saveButton = new Button("Salvar");
      saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

      saveButton.addClickListener(event -> {
          UsuarioResquest usuarioRequest = getUsuarioRequest();
          submeterFormulario(usuarioRequest);
      });
      return saveButton;
  }

  private UsuarioResquest getUsuarioRequest() {

      PerfilResponse perfil = usuarioLogado.getCredencial()
              .getPerfils().stream()
              .findFirst()
              .orElseThrow(
                      () -> new NegocioEsception("Este profile não possue um perfil ativo, procure o adm do sistema"));
      return UsuarioResquest.builder()
              .nome(nome.getValue())
              .cpf(cpf.getValue())
              .celular(celular.getValue())
              .telefone(telefone.getValue())
              .perfil(perfil.getId())
              .permissoes(perfil.getPermissoes().stream().map(p -> p.getId()).toList())
              .especialidade(Objects.isNull(especialidade.getValue())
                      || especialidade.getValue().value.equals("0")
                      ? null
                      : Long.valueOf(especialidade.getValue().value))
              .dataNacimento(dataNascimento.getValue())
              .email(email.getValue())
              .build();
  }

  private VerticalLayout criarLayoutForm(UsuarioResponse usuarioLogado) {
      VerticalLayout layoutColumn = new VerticalLayout();
      FormLayout formLayout1Col = new FormLayout();
      FormLayout formLayout2Col = new FormLayout();

      Image avatar = new Image("avatar/avatar.png", "Avatar");
      Anchor editarImagem = new Anchor("javascript:void(0)", "Editar Avatar");
      editarImagem.getElement().addEventListener("click", event -> {
          AvatarDialog outroDialog = new AvatarDialog(avatar);
          outroDialog.open();
      });

      avatar.setWidth("125px");
      VerticalLayout avatarLayout = new VerticalLayout();
      avatarLayout.setWidthFull();
      avatarLayout.add(avatar, editarImagem);
      layoutColumn.add(avatarLayout);

      HorizontalLayout layoutRow = new HorizontalLayout();
      layoutRow.setWidthFull();
      layoutRow.addClassName(Gap.MEDIUM);
      layoutRow.setWidth("100%");
      layoutRow.setHeight("min-content");

      nome = new TextField();
      nome.setLabel("Nome");
      nome.setWidth("100%");
      nome.setValue(usuarioLogado.getNome());
      formLayout2Col.setWidth("100%");

      email = new TextField();
      email.setLabel("Email");
      email.setWidth("20%");
      email.setValue(usuarioLogado.getCredencial().getEmail());
      formLayout1Col.add(nome, email);

      cpf = new TextField();
      cpf.setLabel("CPF");
      cpf.setWidth("min-content");
      cpf.setValue(usuarioLogado.getCredencial().getCpf());

      celular = new TextField();
      celular.setLabel("Celular");
      celular.setWidth("min-content");
      celular.setValue(usuarioLogado.getCelular());

      telefone = new TextField();
      telefone.setLabel("Telefone");
      telefone.setWidth("min-content");
      telefone.setValue(usuarioLogado.getTelefone());

      dataNascimento = new DatePicker();
      dataNascimento.setLabel("DataNascimento");
      dataNascimento.setWidth("min-content");
      dataNascimento.setValue(usuarioLogado.getDataNacimento());

      especialidade = new ComboBox<>();
      especialidade.setLabel("Especialidade");
      especialidade.setWidth("min-content");
      preencherDominios(especialidade, dominiosService.getDominios());
      validarEspecialidade(usuarioLogado, dominiosService.getDominios(), especialidade);

      layoutColumn.setWidth("200%");
      layoutColumn.getStyle().set("flex-grow", "1");
      layoutColumn.setJustifyContentMode(JustifyContentMode.START);
      layoutColumn.setAlignItems(Alignment.CENTER);
      layoutColumn.setWidthFull();
      layoutColumn.setFlexGrow(1.0, layoutColumn);
      layoutColumn.setWidth("100%");
      layoutColumn.setMaxWidth("800px");
      layoutColumn.setHeight("min-content");

      senha = new PasswordField();
      senha.setPlaceholder("Entre com sua senha");
      senha.setLabel("Senha");
      senha.setWidth("50%");

      senhaConfirmacao = new PasswordField();
      senhaConfirmacao.setPlaceholder("Confirme sua senha");
      senhaConfirmacao.setLabel("Confirmação senha");
      senhaConfirmacao.setWidth("50%");

      senhaConfirmacao.addBlurListener(event -> 
          validarSenhas()
      );

      layoutColumn.setFlexGrow(1.0, layoutRow);
      layoutColumn.add(nome);
      layoutColumn.add(formLayout2Col);
      layoutColumn.add(layoutRow);

      formLayout2Col.add(email, cpf, celular, telefone);
      formLayout2Col.add(dataNascimento);
      formLayout2Col.add(especialidade);
      formLayout2Col.add(senha, senhaConfirmacao);

      return layoutColumn;
  }

  private boolean validarSenhas() {
      if (!senha.getValue().equals(senhaConfirmacao.getValue())) {
          senha.setInvalid(true);
          senha.setErrorMessage("As senhas não coincidem!");
          return false;
      } else {
          senha.setInvalid(false);
      }
      return true;
  }

  private void preencherDominios(ComboBox<ItemCombo> select, DominiosTelaUser dominios) {
      ComponentesComum.setSelect(select, dominios.getEspecialidades());
  }

  private void validarEspecialidade(UsuarioResponse usuario, DominiosTelaUser dominios, ComboBox<ItemCombo> select) {
      Optional<PerfilResponse> perfilMedico = usuario.getCredencial().getPerfils().stream()
              .filter(p -> p.getDescricao().equals("MEDICO"))
              .findFirst();

      if (perfilMedico.isPresent()) {
          select.setVisible(true);
          ComponentesComum.setComboEspecialidades(select, dominios, perfilMedico.get().getId());
      } else {
          select.setVisible(false);
      }
  }

  private void submeterFormulario(UsuarioResquest usuarioRequest) {
      if (Boolean.FALSE.equals(validarSenhas())) {
          return;
      }
      Notificacao notificacao = usuarioService.updateUser(this.idUsuario, usuarioRequest);
      notificacao.showNotification();
      close();
  }
}
