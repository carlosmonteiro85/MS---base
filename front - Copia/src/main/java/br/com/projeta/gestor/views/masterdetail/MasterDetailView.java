package br.com.projeta.gestor.views.masterdetail;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import br.com.projeta.gestor.config.ExceptionHandler;
import br.com.projeta.gestor.data.dto.DominiosTelaUser;
import br.com.projeta.gestor.data.dto.PerfilRequest;
import br.com.projeta.gestor.data.dto.SampleItemRequest;
import br.com.projeta.gestor.data.dto.UsuarioResponse;
import br.com.projeta.gestor.data.dto.UsuarioResquest;
import br.com.projeta.gestor.data.enuns.TipoNotificacaoEnum;
import br.com.projeta.gestor.services.AuthService;
import br.com.projeta.gestor.services.DominiosService;
import br.com.projeta.gestor.services.UserService;
import br.com.projeta.gestor.util.AppConstants;
import br.com.projeta.gestor.util.Redirect;
import br.com.projeta.gestor.util.RoleCredential;
import br.com.projeta.gestor.views.MainLayout;
import br.com.projeta.gestor.views.componentes.ComponentesComum;
import br.com.projeta.gestor.views.componentes.ConfirmDialog;
import br.com.projeta.gestor.views.componentes.ItemCombo;
import br.com.projeta.gestor.views.componentes.Notificacao;

@PageTitle("Gestão de usuários")
@Route(value = "projeta-gestor/pessoas-detalhes/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
@RoleCredential({ "ADMIN", "ROOT" })
@Uses(Icon.class)
public class MasterDetailView extends Div implements BeforeEnterObserver {
    private final Grid<UsuarioResponse> gridUsuarios = new Grid<>(UsuarioResponse.class, false);
    private TextField nome;
    private TextField celular;
    private TextField telefone;
    private ComboBox<ItemCombo> perfil;
    private ComboBox<ItemCombo> especialidade2;
    private DatePicker dataNascimento;
    private TextField especialidade;
    private TextField email;
    private Checkbox important;
    private MultiSelectComboBox<ItemCombo> permissoes;
    private Div editorLayoutDiv = new Div();
    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button deletar = new Button("Deletar");
    Boolean permissaoAtualizar = false;
    Boolean permissaoLeitura = false;

    private final BeanValidationBinder<UsuarioResponse> binder;

    private UsuarioResponse usuario;
    private final UserService usuarioService;
    private DominiosTelaUser dominios;

    public MasterDetailView(DominiosService dominiosService, UserService usuarioService, AuthService authService) {

        permissaoAtualizar = authService.verificarPermissao("UPDATE");
        permissaoLeitura = authService.verificarPermissao("READ");

        dominios = dominiosService.getDominios();
        authService.isLogged();
        this.usuarioService = usuarioService;
        addClassNames("master-detail-view");
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        criarGridUsuarios();
        adicionarndoUsuarios(usuarioService);
        mostrarDadosUsuarioLinha();
        preencherPermissoesPerfil();

        binder = new BeanValidationBinder<>(UsuarioResponse.class);

        binder.forField(especialidade)
                .withConverter(new StringToSampleItemRequestConverter())
                .bind("especialidade");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        submeterFormulario(usuarioService);

        deletar.addClickListener(e -> {
            ConfirmDialog confirmDialog = new ConfirmDialog("Tem certeza que deseja deletar o usuário?",
                    confirmar -> {
                        if (Boolean.TRUE.equals(confirmar)) {
                            deleteUser(usuarioService, this.usuario.getId());
                        }
                    });
            confirmDialog.open();
        });
        VaadinSession.getCurrent().setErrorHandler(new ExceptionHandler());
    }

    private void submeterFormulario(UserService usuarioService) {
        if (Boolean.TRUE.equals(permissaoAtualizar)) {
            save.addClickListener(e -> {
                if (this.usuario == null) {
                    this.usuario = new UsuarioResponse();
                }

                usuarioService.updateUser(this.usuario.getId(), montarUsuarioRequest());
                clearForm();
                refreshGrid();
                Notificacao notificacao = new Notificacao("Usuario atualizado com sucesso.",
                        TipoNotificacaoEnum.SUCESSO);
                notificacao.showNotification();
                UI.getCurrent().navigate(MasterDetailView.class);
            });
        }
    }

    private void preencherPermissoesPerfil() {
        perfil.addValueChangeListener(event -> {
            ItemCombo selectedItem = event.getValue();
            if (selectedItem != null && !selectedItem.value.equals("0")) {

                Optional<PerfilRequest> perfil = dominios.getPerfils().stream()
                        .filter(p -> Objects.equals(p.getId(), Long.valueOf(selectedItem.value))).findFirst();
                if (perfil.isPresent()) {
                    ComponentesComum.setComboPermissoes(permissoes, perfil.get());

                }
                permissoes.setVisible(true);
            } else {
                permissoes.setVisible(false);
            }

            if (selectedItem != null && selectedItem.label.equals("MEDICO")) {
                especialidade2.setVisible(true);
            } else {
                especialidade2.setVisible(false);
            }

        });
    }

    private void deleteUser(UserService usuarioService, Long id) {
        Notificacao notificacao = usuarioService.deleteUser(id);
        notificacao.showNotification();
        Redirect.criarRedirect(MasterDetailView.class);
    }

    private void mostrarDadosUsuarioLinha() {
        gridUsuarios.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(
                        String.format(AppConstants.SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailView.class);
            }
        });
    }

    private void adicionarndoUsuarios(UserService samplePersonService) {
        gridUsuarios.setItems(query -> samplePersonService.buscarTodos(
                query.getPage(),
                query.getPageSize(),
                "id",
                Sort.Direction.ASC)
                .stream());
        gridUsuarios.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    private void criarGridUsuarios() {
        if (Boolean.TRUE.equals(permissaoLeitura)) {
            gridUsuarios.addColumn(UsuarioResponse::getNome).setHeader("Nome").setAutoWidth(true);
            gridUsuarios.addColumn(UsuarioResponse::getCelular).setHeader("Celular").setAutoWidth(true);
            gridUsuarios.addColumn(UsuarioResponse::getTelefone).setHeader("Telefone").setAutoWidth(true);
            gridUsuarios.addColumn(u -> u.getCredencial().getPerfils().stream().findFirst().get().getDescricao())
                    .setHeader("Perfil").setAutoWidth(true);
            gridUsuarios.addColumn("dataNacimento").setAutoWidth(true);
            gridUsuarios.addColumn(u -> u.getCredencial().getEmail()).setHeader("Email").setAutoWidth(true);
            gridUsuarios.addColumn(u -> u.getEspecialidade() != null ? u.getEspecialidade().getDescricao() : "")
                    .setHeader("Especialidade").setAutoWidth(true);

            LitRenderer<UsuarioResponse> importantRenderer = getImportantRenderer();

            gridUsuarios.addColumn(importantRenderer).setHeader("Important").setAutoWidth(true);
        }
    }

    private LitRenderer<UsuarioResponse> getImportantRenderer() {
        return LitRenderer.<UsuarioResponse>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", important -> important.getCredencial().isAtivo() ? "check" : "minus")
                .withProperty("color",
                        important -> important.getCredencial().isAtivo()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> samplePersonId = event.getRouteParameters().get(AppConstants.SAMPLEPERSON_ID)
                .map(Long::parseLong);
        if (samplePersonId.isPresent()) {
            UsuarioResponse samplePersonFromBackend = usuarioService.findById(samplePersonId.get());
            populateForm(samplePersonFromBackend, dominios);
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        editorLayoutDiv.setVisible(false);
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        nome = new TextField("Nome");
        celular = new TextField("Celular");
        telefone = new TextField("Telefone");
        perfil = new ComboBox<>("Perfil");
        permissoes = new MultiSelectComboBox<>("Permissões do perfil");
        especialidade = new TextField("Especialidade");
        especialidade2 = new ComboBox<>("Especialidade");
        dataNascimento = new DatePicker("Data Nascimento");
        email = new TextField("email");
        important = new Checkbox("Important");
        formLayout.add(nome, celular, telefone, perfil, permissoes, dataNascimento, especialidade2, email, important);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);

        if (Boolean.FALSE.equals(permissaoAtualizar)) {
            nome.setReadOnly(true);
            celular.setReadOnly(true);
            telefone.setReadOnly(true);
            perfil.setReadOnly(true);
            permissoes.setReadOnly(true);
            dataNascimento.setReadOnly(true);
            email.setReadOnly(true);
            important.setReadOnly(true);
            especialidade.setReadOnly(true);
        }
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        if (Boolean.TRUE.equals(permissaoAtualizar)) {
            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.setClassName("button-layout");
            cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            deletar.addThemeVariants(ButtonVariant.LUMO_SMALL);
            buttonLayout.add(save, cancel, deletar);
            editorLayoutDiv.add(buttonLayout);
        }
    }

    private void createGridLayout(SplitLayout splitLayout) {
        gridUsuarios.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                populateForm(event.getValue(), dominios);
                editorLayoutDiv.setVisible(true);
            } else {
                clearForm();
                editorLayoutDiv.setVisible(false);
            }
        });
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(gridUsuarios);
    }

    private void refreshGrid() {
        gridUsuarios.select(null);
        gridUsuarios.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null, dominios);
    }

    private void populateForm(UsuarioResponse value, DominiosTelaUser dominios) {
        this.usuario = value;
        binder.readBean(this.usuario);
        if (value != null) {
            nome.setValue(ComponentesComum.validateField(this.usuario.getNome()));
            celular.setValue(ComponentesComum.validateField(this.usuario.getCelular()));
            telefone.setValue(ComponentesComum.validateField(this.usuario.getTelefone()));
            especialidade.setValue(
                    value.getEspecialidade() == null ? "-1" : value.getEspecialidade().getDescricao());
            ComponentesComum.setComboPerfils(perfil, dominios);
            ComponentesComum.obterOpcaoCombo(perfil,
                    this.usuario.getCredencial().getPerfils().stream().findFirst().get().getId());
            ComponentesComum.setComboPermissoes(permissoes,
                    this.usuario.getCredencial().getPerfils().stream().findFirst().get());
            ComponentesComum.setComboBoxEspecialidade(especialidade2, dominios);

            if(Objects.nonNull(this.usuario.getEspecialidade())){
                ComponentesComum.obterOpcaoCombo(especialidade2, this.usuario.getEspecialidade().getId());
            }

            dataNascimento.setValue(this.usuario.getDataNacimento());
            email.setValue(ComponentesComum.validateField(this.usuario.getCredencial().getEmail()));
            important.setValue(this.usuario.getCredencial().isAtivo());
        } else {
            especialidade.clear();
        }
    }

    private UsuarioResquest montarUsuarioRequest() {
        return UsuarioResquest.builder()
                .id(this.usuario.getId())
                .cpf(this.usuario.getCredencial().getCpf())
                .nome(nome.getValue())
                .celular(celular.getValue())
                .telefone(telefone.getValue())
                .perfil(Long.parseLong(perfil.getValue().value))
                .especialidade(Objects.isNull(especialidade2.getValue())
                        || especialidade2.getValue().value.equals("0")
                                ? null  
                                : Long.valueOf(especialidade2.getValue().value))
                .dataNacimento(dataNascimento.getValue())
                .email(email.getValue())
                .build();
    }

    public class StringToSampleItemRequestConverter implements Converter<String, SampleItemRequest> {
        @Override
        public Result<SampleItemRequest> convertToModel(String value, ValueContext context) {
            if (value == null) {
                return Result.ok(null);
            }
            try {
                SampleItemRequest sampleItemRequest = new SampleItemRequest();
                sampleItemRequest.setId(Long.parseLong(value));
                return Result.ok(sampleItemRequest);
            } catch (NumberFormatException e) {
                return Result.error("Valor inválido");
            }
        }

        @Override
        public String convertToPresentation(SampleItemRequest value, ValueContext context) {
            return value != null ? String.valueOf(value.getId()) : "";
        }
    }
}