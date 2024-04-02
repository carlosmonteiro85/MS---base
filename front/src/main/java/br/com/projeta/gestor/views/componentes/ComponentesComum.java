package br.com.projeta.gestor.views.componentes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;

import br.com.projeta.gestor.data.dto.DominiosTelaUser;
import br.com.projeta.gestor.data.dto.PerfilRequest;
import br.com.projeta.gestor.data.dto.PerfilResponse;
import br.com.projeta.gestor.data.dto.SampleItemRequest;
import br.com.projeta.gestor.util.Utilitarios;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ComponentesComum {

  public static void setComboBoxEspecialidade(ComboBox<ItemCombo> comboBox, DominiosTelaUser dominios) {
    List<ItemCombo> sampleItems = new ArrayList<>();
    sampleItems.add(new ItemCombo("0", "Selecione uma opção", Boolean.TRUE));
    sampleItems.addAll(dominios.getEspecialidades().stream()
        .map(e -> new ItemCombo(e.getId().toString(), e.getDescricao(), false)).toList());
    comboBox.setItems(sampleItems);
    comboBox.setItemLabelGenerator(item -> item.label);
  }

  public static void setCheckComboPerfil(CheckboxGroup<ItemCombo> comboBox, DominiosTelaUser dominios) {
    List<ItemCombo> sampleItems = new ArrayList<>();
    sampleItems.addAll(dominios.getPerfils().stream()
        .map(e -> new ItemCombo(e.getId().toString(), e.getDescricao(), false)).toList());
    comboBox.setItems(sampleItems);
    comboBox.setItemLabelGenerator(item -> item.label);
  }

  public static void setComboPermissoes(MultiSelectComboBox<ItemCombo> multiSelectComboBox, PerfilResponse perfil) {
    List<ItemCombo> sampleItems = new ArrayList<>();
    sampleItems.addAll(perfil.getPermissoes().stream()
        .map(s -> new ItemCombo(s.getId().toString(), s.getDescricao(), true)).toList());
    multiSelectComboBox.setItems(sampleItems);
    multiSelectComboBox.setItemLabelGenerator(item -> item.label);
    sampleItems.forEach(multiSelectComboBox::select);
    multiSelectComboBox.setReadOnly(true);
  }

  public static void setComboEspecialidades(ComboBox<ItemCombo> selectEspecialidades, DominiosTelaUser dominios,
      Long idEspecialidade) {
    List<ItemCombo> sampleItems = new ArrayList<>();
    sampleItems.addAll(dominios.getEspecialidades().stream()
        .map(s -> new ItemCombo(s.getId().toString(), s.getDescricao(), true)).toList());

    selectEspecialidades.setItems(sampleItems);
    selectEspecialidades.setItemLabelGenerator(item -> item.label);

    sampleItems.forEach(i -> {
      if (i.value.equals(idEspecialidade.toString())) {
        selectEspecialidades.setValue(i);
      }
    });
  }

  public static void setComboPermissoes(MultiSelectComboBox<ItemCombo> multiSelectComboBox, PerfilRequest perfil) {
    List<ItemCombo> sampleItems = new ArrayList<>();
    sampleItems.addAll(perfil.getPermissoes().stream()
        .map(s -> new ItemCombo(s.getId().toString(), s.getDescricao(), true)).toList());
    multiSelectComboBox.setItems(sampleItems);
    multiSelectComboBox.setItemLabelGenerator(item -> item.label);
    sampleItems.forEach(multiSelectComboBox::select);
    multiSelectComboBox.setReadOnly(true);
  }

  public static void setMultiEspecialidades(MultiSelectComboBox<ItemCombo> multiSelectComboBox,
      DominiosTelaUser dominios) {
    List<ItemCombo> sampleItems = new ArrayList<>();
    sampleItems.addAll(dominios.getEspecialidades().stream()
        .map(s -> new ItemCombo(s.getId().toString(), s.getDescricao(), true)).toList());
    multiSelectComboBox.setItems(sampleItems);
    multiSelectComboBox.setItemLabelGenerator(item -> item.label);
  }

  public static void setSelect(ComboBox<ItemCombo> select, List<SampleItemRequest> dominios) {
    List<ItemCombo> sampleItems = new ArrayList<>();
    sampleItems.addAll(dominios.stream()
        .map(s -> new ItemCombo(s.getId().toString(), s.getDescricao(), true)).toList());
    select.setItems(sampleItems);
    select.setItemLabelGenerator(item -> item.label);
  }

  public static void setComboPerfils(ComboBox<ItemCombo> multiSelectComboBox, DominiosTelaUser dominios) {
    List<ItemCombo> sampleItems = new ArrayList<>();
    sampleItems.add(new ItemCombo("0", "Selecione as opções", Boolean.TRUE));
    sampleItems.addAll(dominios.getPerfils().stream()
        .map(s -> new ItemCombo(s.getId().toString(), s.getDescricao(), false)).toList());
    multiSelectComboBox.setItems(sampleItems);
    multiSelectComboBox.setItemLabelGenerator(item -> item.label);
  }

  public static void obterOpcaoCombo(ComboBox<ItemCombo> comboBox, Long opcao) {
    List<ItemCombo> items = comboBox.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
    items.stream()
        .filter(item -> item.value.equals(String.valueOf(opcao)))
        .findFirst()
        .ifPresent(comboBox::setValue);
  }

  public static String validateField(String value) {
    return Objects.isNull(value) ? "" : value;
  }

  public static TextField createFileldTelefone(String nomeLabel) {
    TextField telefone = new TextField(nomeLabel);

    telefone.addValueChangeListener(t -> {
      if (t.getValue().length() < 11) {
        telefone.setInvalid(true);
        telefone.setErrorMessage("O Telefone digitado é inválido.");
      } else {
        telefone.setInvalid(false);
        telefone.setErrorMessage(null);
      }
    });

    telefone.setPattern("\\([0-9]{2}\\) [0-9]{4,5}-[0-9]{4}");
    telefone.addValueChangeListener(Utilitarios::mascararTelefone);
    telefone.getElement().executeJs(
        "this.addEventListener('input', (e) => { e.target.value = e.target.value.replace(/[^0-9]/g, ''); });");

    return telefone;
  }

  public static EmailField createFieldEmail(String nomeLabel) {
    EmailField email = new EmailField(nomeLabel);

    email.addValueChangeListener(event -> {
      if (!email.getValue().contains("@")) {
        email.setErrorMessage("O formato do email nao é válido.");
      } else {
        email.setInvalid(false);
        email.setErrorMessage(null);
      }
    });
    return email;
  }

  public TextField createFieldCPF() {
    TextField cpfField = new TextField("CPF");
    cpfField.setPattern("\\[0-9]{3}\\.\\[0-9]{3}\\.\\[0-9]{3}-\\[0-9]{2}");
    cpfField.setMaxLength(14);
    cpfField.addValueChangeListener(u -> Utilitarios.addMascaraCPF(cpfField));
    cpfField.getElement().executeJs(
        "this.addEventListener('input', (e) => { e.target.value = e.target.value.replace(/[^0-9]/g, ''); });");

    cpfField.addValueChangeListener(event -> {
      String cpf = event.getValue();
      cpf = cpf.replaceAll("[^0-9]", "");
      if (cpf.length() == 11) {
        boolean isValid = Utilitarios.validarCpf(cpf);
        cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);

        if (Boolean.FALSE.equals(isValid)) {
          cpfField.setInvalid(true);
          cpfField.setErrorMessage("O CPF digitado é inválido.");
        } else {
          cpfField.setErrorMessage(null);
          cpfField.setInvalid(false);
        }
        cpfField.setValue(cpf);
      } else {
        cpfField.setErrorMessage("O CPF não contem a quantidade correta de números");
      }

    });
    return cpfField;
  }
}
