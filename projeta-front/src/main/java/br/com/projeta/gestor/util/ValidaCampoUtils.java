package br.com.projeta.gestor.util;

import java.time.LocalDate;
import java.util.Objects;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

import br.com.projeta.gestor.views.componentes.ItemCombo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidaCampoUtils {

  public static boolean campoObrigatorioChangeListener(TextField campo) {
    String value = campo.getValue();
    if (Objects.isNull(value) || value.isEmpty()) {
      campo.setInvalid(true);
      campo.setErrorMessage(gerarMenssagem(campo.getLabel()));
    } else {
      campo.setInvalid(false);
      campo.setErrorMessage(null);
    }
    return !campo.isInvalid();
  }

  private String gerarMenssagem(String nomeCampo) {
    return "O campo " + nomeCampo + " é obrigatório.";
  }

  public static boolean campoObrigatorioChangeListener(DatePicker campo) {
    LocalDate value = campo.getValue();
    if (Objects.isNull(value)) {
      campo.setInvalid(true);
      campo.setErrorMessage(gerarMenssagem(campo.getLabel()));
    } else {
      campo.setInvalid(false);
      campo.setErrorMessage(null);
    }
    return !campo.isInvalid();
  }

  public static boolean campoObrigatorioChangeListener(EmailField campo) {
    String value = campo.getValue();
    if (Objects.isNull(value) || value.isEmpty()) {
      campo.setInvalid(true);
      campo.setErrorMessage(gerarMenssagem(campo.getLabel()));
    } else {
      if (!campo.getValue().contains("@")) {
        campo.setInvalid(true);
        campo.setErrorMessage("O campo " + campo.getLabel() + ", não possue um formato válido. ");
      } else {
        campo.setInvalid(false);
        campo.setErrorMessage(null);
      }
    }
    return !campo.isInvalid();
  }

  public static boolean campoObrigatorioCPF(TextField cpfField) {
    cpfField.setInvalid(false);
    String cpf = cpfField.getValue();
    cpf = cpf.replaceAll("\\D", "");
    if (cpf.length() == 11) {
      boolean isValid = Utilitarios.validarCpf(cpf);
      if (Boolean.FALSE.equals(isValid)) {
        cpfField.setInvalid(true);
        cpfField.setErrorMessage("O CPF digitado é inválido.");
      } else {
        TextField cpfFields = new TextField("CPF");
        cpfField = cpfFields;
        cpfField.setInvalid(false);
        cpfField.setErrorMessage(null);
      }
    } else {
      cpfField.setInvalid(true);
      cpfField.setErrorMessage("O CPF não contém a quantidade correta de números");
    }
    if (!cpf.isBlank()) {
      cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
      cpfField.setValue(cpf);
    }
    return !cpfField.isInvalid();
  }

  public static boolean campoObrigatorioChangeListener(ComboBox<ItemCombo> comboBox) {
    ItemCombo selectedItem = comboBox.getValue();

    if (selectedItem == null || "0".equals(selectedItem.value)) {
      comboBox.setInvalid(true);
      comboBox.setErrorMessage("Selecione uma opção.");
      return false;
    } else {
      comboBox.setInvalid(false);
      comboBox.setErrorMessage(null);
      return true;
    }
  }
}
