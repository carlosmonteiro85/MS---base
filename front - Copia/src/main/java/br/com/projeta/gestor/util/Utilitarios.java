package br.com.projeta.gestor.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.textfield.TextField;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utilitarios {

  public static String formatData(LocalDate data) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return data.format(formatter);
  }

  public static void addMascaraTelefone(TextField textFieldCelular) {
    textFieldCelular.setPattern("\\([0-9]{2}\\) [0-9]{4,5}-[0-9]{4}");
    textFieldCelular.addValueChangeListener(e -> mascararTelefone(e));
    textFieldCelular.getElement().executeJs(
        "this.addEventListener('input', (e) => { e.target.value = e.target.value.replace(/[^0-9]/g, ''); });");
  }

  private static void mascararTelefone(ComponentValueChangeEvent<TextField, String> event) {
    String novoValor = event.getValue();
    String valorFormatado = "";
    if (novoValor != null && !novoValor.isEmpty()) {
      // Remove caracteres não numéricos
      novoValor = novoValor.replaceAll("[^\\d]", "");

      // Formata o número de telefone
      if (novoValor.length() >= 2) {
        valorFormatado += "(" + novoValor.substring(0, 2) + ")";
        if (novoValor.length() >= 7) {
          valorFormatado += " " + novoValor.substring(2, 7);
          if (novoValor.length() >= 11) {
            valorFormatado += "-" + novoValor.substring(7, 11);
          } else {
            valorFormatado += "-" + novoValor.substring(7);
          }
        } else {
          valorFormatado += " " + novoValor.substring(2);
        }
      } else {
        valorFormatado = novoValor;
      }
    }
    event.getSource().setValue(valorFormatado);
  }

  public static byte[] readImageBytes(String imagePath) throws IOException {
    Path path = Paths.get(imagePath);
    return Files.readAllBytes(path);
  }
}
