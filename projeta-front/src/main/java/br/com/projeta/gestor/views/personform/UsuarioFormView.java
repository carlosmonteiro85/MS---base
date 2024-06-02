package br.com.projeta.gestor.views.personform;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import br.com.projeta.gestor.config.ExceptionHandler;
import br.com.projeta.gestor.data.dto.DominiosTelaUser;
import br.com.projeta.gestor.data.dto.PerfilRequest;
import br.com.projeta.gestor.data.dto.UsuarioResquest;
import br.com.projeta.gestor.services.AuthService;
import br.com.projeta.gestor.services.DominiosService;
import br.com.projeta.gestor.services.UserService;
import br.com.projeta.gestor.util.RoleCredential;
import br.com.projeta.gestor.util.ValidaCampoUtils;
import br.com.projeta.gestor.views.MainLayout;
import br.com.projeta.gestor.views.componentes.ComponentesComum;
import br.com.projeta.gestor.views.componentes.ItemCombo;

@PageTitle("Informações de Usuário")
@Route(value = "projeta-gestor/person-form", layout = MainLayout.class)
@RoleCredential({ "ADMIN", "SUPERADMIN" })
@Uses(Icon.class)
public class UsuarioFormView extends Composite<VerticalLayout> {

    TextField fieldNome;
    EmailField textFieldEmail;
    TextField fieldCpf;
    TextField fieldCelular;
    TextField fieldTelefone;
    DatePicker datePickerNascimento;
    MultiSelectComboBox<ItemCombo> selectPermissoes;
    ComboBox<ItemCombo> selectEspecialidade;
    ComboBox<ItemCombo> selectPerfils;
    Button buttonSave;
    Button buttonCancel;
    UserService usuarioService;
    Boolean permissaoCriacao = false;
    Boolean permissaoLeitura = false;
    Binder<UsuarioResquest> binder = new Binder<>(UsuarioResquest.class);

    public UsuarioFormView(DominiosService dominiosService, UserService usuarioService, AuthService authService) {

        DominiosTelaUser dominios = dominiosService.getDominios();
        this.usuarioService = usuarioService;

        permissaoLeitura = authService.verificarPermissao("READ");
        permissaoCriacao = authService.verificarPermissao("CREATE");

        H3 h3 = new H3();
        h3.setText("Informações Pessoal");
        h3.setWidth("100%");

        VerticalLayout layoutColumn2 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();

        if (permissaoLeitura) {
            fieldNome = new TextField("Nome");
            fieldNome.setWidth("749px");
            formLayout2Col.add(fieldNome);
            fieldNome.addBlurListener(event -> ValidaCampoUtils.campoObrigatorioChangeListener(fieldNome));

            textFieldEmail = ComponentesComum.createFieldEmail("E-mail");
            textFieldEmail.setWidth("502px");
            formLayout2Col.add(textFieldEmail);
            textFieldEmail.addBlurListener(event -> ValidaCampoUtils.campoObrigatorioChangeListener(textFieldEmail));

            fieldCpf = ComponentesComum.createFieldCPF();
            fieldCpf.setWidth("218px");
            formLayout2Col.add(fieldCpf);
            fieldCpf.addBlurListener(event -> ValidaCampoUtils.campoObrigatorioCPF(fieldCpf));

            fieldCelular = ComponentesComum.createFileldTelefone("Celular");
            fieldCelular.setWidth("360px");
            formLayout2Col.add(fieldCelular);

            fieldTelefone = ComponentesComum.createFileldTelefone("Telefone");
            fieldTelefone.setWidth("360px");
            formLayout2Col.add(fieldTelefone);

            datePickerNascimento = new DatePicker("Data de Nascimento");
            datePickerNascimento.setWidth("245px");
            formLayout2Col.add(datePickerNascimento);
            datePickerNascimento.addBlurListener(event -> ValidaCampoUtils.campoObrigatorioChangeListener(datePickerNascimento));

            selectPermissoes = new MultiSelectComboBox<>("Permissões do perfil");
            selectPermissoes.setPlaceholder("Selecione as opções desejadas");
            selectPermissoes.setVisible(false);
            selectPermissoes.setWidth("736px");

            selectEspecialidade = new ComboBox<>("Especialidades");
            selectEspecialidade.setVisible(false);
            layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, selectEspecialidade);
            selectEspecialidade.setWidth("381px");

            selectPerfils = new ComboBox<>("Perfil");
            selectPerfils.setPlaceholder("Selecione as opções desejadas");
            formLayout2Col.add(selectPerfils);
            formLayout2Col.add(selectPermissoes);
            selectPerfils.setWidth("736px");
            selectPerfils.addBlurListener(event -> ValidaCampoUtils.campoObrigatorioChangeListener(selectPerfils));

            buttonSave = new Button();
            buttonSave.setText("Save");
            buttonSave.setWidth("min-content");
            buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            buttonCancel = new Button();
            buttonCancel.setText("Cancel");
            buttonCancel.setWidth("min-content");

            getContent().setWidth("100%");
            getContent().getStyle().set("flex-grow", "1");
            getContent().setJustifyContentMode(JustifyContentMode.START);
            getContent().setAlignItems(Alignment.CENTER);
            layoutColumn2.setWidth("100%");
            layoutColumn2.setMaxWidth("800px");
            layoutColumn2.setHeight("624px");
            formLayout2Col.setWidth("769px");
            layoutRow.addClassName(Gap.MEDIUM);
            layoutRow.setWidth("100%");
            layoutRow.getStyle().set("flex-grow", "1");
            layoutRow.setAlignItems(Alignment.END);
            layoutRow.setJustifyContentMode(JustifyContentMode.CENTER);
            getContent().add(layoutColumn2);
            layoutColumn2.add(h3);
            layoutColumn2.add(formLayout2Col);
            layoutColumn2.add(selectEspecialidade);
            layoutColumn2.add(layoutRow);
            layoutRow.add(buttonSave);
            layoutRow.add(buttonCancel);
            readOnly(true);

            if (permissaoCriacao) {
                readOnly(false);

                buttonCancel.addClickListener(event -> limparCampos());

                buttonSave.addClickListener(event -> {
                    UsuarioResquest usuarioRequest = getUsuarioRequest();
                    boolean camposValidos = validarCamposFormulario();
                    if (Boolean.TRUE.equals(camposValidos)) {
                        submeterFormulario(usuarioRequest);
                    }
                });
            }

            ComponentesComum.setComboBoxEspecialidade(selectEspecialidade, dominios);
            ComponentesComum.setComboPerfils(selectPerfils, dominios);

            selectPerfils.addValueChangeListener(event -> validarComboEspecialidade(dominios, event));
            buttonCancel.addClickListener(buttonClick -> limparCampos());
        }
        VaadinSession.getCurrent().setErrorHandler(new ExceptionHandler());
    }

    private UsuarioResquest getUsuarioRequest() {
        UsuarioResquest usuarioRequest = UsuarioResquest.builder()
                .nome(fieldNome.getValue())
                .cpf(fieldCpf.getValue())
                .celular(fieldCelular.getValue())
                .telefone(fieldTelefone.getValue())
                .perfil(Objects.nonNull(selectPerfils.getValue())
                        ? Long.valueOf(selectPerfils.getValue().value)
                        : null)
                .permissoes(selectPermissoes.getSelectedItems().stream().map(i -> Long.valueOf(i.value))
                        .toList())
                .especialidade(Objects.isNull(selectEspecialidade.getValue())
                        || selectEspecialidade.getValue().value.equals("0")
                                ? null
                                : Long.valueOf(selectEspecialidade.getValue().value))
                .dataNacimento(datePickerNascimento.getValue())
                .email(textFieldEmail.getValue())
                .build();
        return usuarioRequest;
    }

    private boolean validarCamposFormulario() {
        Set<Boolean> itensValidos = new HashSet<>();

        itensValidos.add(ValidaCampoUtils.campoObrigatorioChangeListener(fieldNome));
        itensValidos.add(ValidaCampoUtils.campoObrigatorioChangeListener(textFieldEmail));
        itensValidos.add(ValidaCampoUtils.campoObrigatorioCPF(fieldCpf));
        itensValidos.add(ValidaCampoUtils.campoObrigatorioChangeListener(selectPerfils));
        itensValidos.add(ValidaCampoUtils.campoObrigatorioChangeListener(datePickerNascimento));

        return itensValidos.stream().filter(i -> i.equals(Boolean.FALSE)).toList().isEmpty();
    }

    private void validarComboEspecialidade(DominiosTelaUser dominios,
            ComponentValueChangeEvent<ComboBox<ItemCombo>, ItemCombo> event) {
        ItemCombo selectedItem = event.getValue();
        if (selectedItem != null && !selectedItem.value.equals("0")) {

            Optional<PerfilRequest> perfil = dominios.getPerfils().stream()
                    .filter(p -> Objects.equals(p.getId(), Long.valueOf(selectedItem.value))).findFirst();
            if (perfil.isPresent()) {
                ComponentesComum.setComboPermissoes(selectPermissoes, perfil.get());

            }
            selectPermissoes.setVisible(true);
        } else {
            selectPermissoes.setVisible(false);
        }
        if (selectedItem != null && selectedItem.label.equals("MEDICO")) {
            selectEspecialidade.setVisible(true);
        } else {
            selectEspecialidade.setVisible(false);
        }
    }

    private void limparCampos() {
        binder.setValidationStatusHandler(status -> {});
        this.fieldNome.clear();
        this.fieldNome.setInvalid(false);
        this.textFieldEmail.clear();
        this.textFieldEmail.setInvalid(false);
        this.fieldCpf.clear();
        this.fieldCpf.setInvalid(false);
        this.datePickerNascimento.clear();
        this.datePickerNascimento.setInvalid(false);
        this.selectPermissoes.clear();
        this.selectEspecialidade.clear();
        this.selectPerfils.clear();
        this.selectPerfils.setInvalid(false);
        this.fieldCelular.clear();
        this.fieldTelefone.clear();
        fieldCelular.addValueChangeListener(this::telefoneChangeListener);
        fieldTelefone.addValueChangeListener(this::telefoneChangeListener);
    }

    private void telefoneChangeListener(ComponentValueChangeEvent<TextField, String> event) {
        String value = event.getValue();
        if (value != null && !value.isEmpty()) {
            if (value.length() < 11) {
                event.getSource().setInvalid(true);
                event.getSource().setErrorMessage("O telefone digitado é inválido.");
            } else {
                event.getSource().setInvalid(false);
                event.getSource().setErrorMessage(null);
            }
        }
    }

    private void submeterFormulario(UsuarioResquest usuarioRequest) {
        usuarioService.saveUser(usuarioRequest);
        limparCampos();
         UI.getCurrent().navigate(UsuarioFormView.class);
    }

    private void readOnly(Boolean status) {
        fieldNome.setReadOnly(status);
        textFieldEmail.setReadOnly(status);
        fieldCpf.setReadOnly(status);
        fieldCelular.setReadOnly(status);
        fieldTelefone.setReadOnly(status);
        datePickerNascimento.setReadOnly(status);
        selectPermissoes.setReadOnly(status);
        selectEspecialidade.setReadOnly(status);
        selectPerfils.setReadOnly(status);
    }
}
