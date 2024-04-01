package br.com.projeta.gestor.views.personform;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.vaadin.flow.component.Composite;
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
import com.vaadin.flow.component.textfield.TextField;
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
import br.com.projeta.gestor.util.Redirect;
import br.com.projeta.gestor.util.RoleCredential;
import br.com.projeta.gestor.views.MainLayout;
import br.com.projeta.gestor.views.componentes.Notificacao;

@PageTitle("Informações de Usuário")
@Route(value = "projeta-gestor/person-form", layout = MainLayout.class)
@RoleCredential({"ADMIN", "ROOT" })
@Uses(Icon.class)
public class UsuarioFormView extends Composite<VerticalLayout> {

    TextField textFieldNome;
    TextField textFieldEmail;
    TextField fieldCpf;
    TextField fieldCelular;
    TextField fieldTelefone;
    DatePicker datePickerNascimento;
    MultiSelectComboBox<SampleItem> selectPermissoes;
    ComboBox<SampleItem> selectEspecialidade;
    ComboBox<SampleItem> selectPerfils;
    Button buttonSave;
    Button buttonCancel;
    UserService usuarioService;
    Boolean permissaoCriacao = false;
    Boolean permissaoLeitura = false;

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
            textFieldNome = new TextField("Nome");
            textFieldNome.setWidth("749px");
            formLayout2Col.add(textFieldNome);

            textFieldEmail = new TextField("E-mail");
            textFieldEmail.setWidth("502px");
            formLayout2Col.add(textFieldEmail);

            fieldCpf = new TextField("CPF");
            fieldCpf.setWidth("218px");
            formLayout2Col.add(fieldCpf);

            fieldCelular = new TextField("Celular");
            fieldCelular.setWidth("360px");
            formLayout2Col.add(fieldCelular);

            fieldTelefone = new TextField("Telefone");
            fieldTelefone.setWidth("360px");
            formLayout2Col.add(fieldTelefone);

            datePickerNascimento = new DatePicker("Data de Nascimento");
            datePickerNascimento.setWidth("245px");
            formLayout2Col.add(datePickerNascimento);

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

                buttonSave.addClickListener(event -> {
                    UsuarioResquest usuarioRequest = UsuarioResquest.builder()
                            .nome(textFieldNome.getValue())
                            .cpf(fieldCpf.getValue())
                            .celular(fieldCelular.getValue())
                            .telefone(fieldTelefone.getValue())
                            .perfil(Long.valueOf(selectPerfils.getValue().value()))
                            .permissoes(selectPermissoes.getSelectedItems().stream().map(i -> Long.valueOf(i.value()))
                                    .toList())
                            .especialidade(Objects.isNull(selectEspecialidade.getValue())
                                    || selectEspecialidade.getValue().value().equals("0")
                                            ? null
                                            : Long.valueOf(selectEspecialidade.getValue().value()))
                            .dataNacimento(datePickerNascimento.getValue())
                            .email(textFieldEmail.getValue())
                            .build();

                    submeterFormulario(usuarioRequest);
                });
            }

            setComboBoxEspecialidade(selectEspecialidade, dominios);
            setComboPerfils(selectPerfils, dominios);

            selectPerfils.addValueChangeListener(event -> {
                SampleItem selectedItem = event.getValue();
                if (selectedItem != null && !selectedItem.value().equals("0")) {

                    Optional<PerfilRequest> perfil = dominios.getPerfils().stream()
                            .filter(p -> Objects.equals(p.getId(), Long.valueOf(selectedItem.value()))).findFirst();
                    if (perfil.isPresent()) {
                        setComboPermissoes(selectPermissoes, perfil.get());

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

            });
            buttonCancel.addClickListener(buttonClick -> limparCampos());
        }
        VaadinSession.getCurrent().setErrorHandler(new ExceptionHandler());
    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setComboPerfils(ComboBox<SampleItem> multiSelectComboBox, DominiosTelaUser dominios) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("0", "Selecione as opções", Boolean.TRUE));
        sampleItems.addAll(dominios.getPerfils().stream()
                .map(s -> new SampleItem(s.getId().toString(), s.getDescricao(), false)).toList());
        multiSelectComboBox.setItems(sampleItems);
        multiSelectComboBox.setItemLabelGenerator(item -> (item).label());
    }

    private void setComboPermissoes(MultiSelectComboBox<SampleItem> multiSelectComboBox, PerfilRequest perfil) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.addAll(perfil.getPermissoes().stream()
                .map(s -> new SampleItem(s.getId().toString(), s.getDescricao(), true)).toList());
        multiSelectComboBox.setItems(sampleItems);
        multiSelectComboBox.setItemLabelGenerator(item -> (item).label());
        sampleItems.forEach(multiSelectComboBox::select);
        multiSelectComboBox.setReadOnly(true);
    }

    private void setComboBoxEspecialidade(ComboBox<SampleItem> comboBox, DominiosTelaUser dominios) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("0", "Selecione uma opção", Boolean.TRUE));
        sampleItems.addAll(dominios.getEspecialidades().stream()
                .map(e -> new SampleItem(e.getId().toString(), e.getDescricao(), false)).toList());
        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(item -> (item).label());
    }

    private void limparCampos() {
        this.textFieldNome.clear();
        this.textFieldEmail.clear();
        this.fieldCpf.clear();
        this.fieldCelular.clear();
        this.fieldTelefone.clear();
        this.datePickerNascimento.clear();
        this.selectPermissoes.clear();
        this.selectEspecialidade.clear();
        this.selectPerfils.clear();
    }

    private void submeterFormulario(UsuarioResquest usuarioRequest) {
        Notificacao notificacao = usuarioService.saveUser(usuarioRequest);
        notificacao.showNotification();
        Redirect.criarRedirect(UsuarioFormView.class);
    }

    private void readOnly(Boolean status) {
        textFieldNome.setReadOnly(status);
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
