package br.com.projeta.gestor.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.textfield.TextField;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utilitarios {

  public static String formatData(LocalDate data) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return data.format(formatter);
  }

  public static String formatData(LocalDateTime data) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    return data.format(formatter);
  }

  public static void addMascaraTelefone(TextField textFieldCelular) {
    textFieldCelular.setPattern("\\([0-9]{2}\\) [0-9]{4,5}-[0-9]{4}");
    textFieldCelular.addValueChangeListener(e -> mascararTelefone(e));
    textFieldCelular.getElement().executeJs(
        "this.addEventListener('input', (e) => { e.target.value = e.target.value.replace(/[^0-9]/g, ''); });");
  }

  public static void mascararTelefone(ComponentValueChangeEvent<TextField, String> event) {
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

  public static void addMascaraCPF(TextField textFieldCPF) {
    textFieldCPF.setPattern("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    textFieldCPF.addValueChangeListener(Utilitarios::formatarCPF);
    textFieldCPF.getElement().executeJs(
        "this.addEventListener('input', (e) => { e.target.value = e.target.value.replace(/[^0-9]/g, ''); });");
  }

  private static void formatarCPF(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
    String cpf = event.getValue().replaceAll("[^0-9]", "");

    if (cpf.length() == 11) {
      cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    event.getSource().setValue(cpf);
  }

  public static byte[] readImageBytes(String imagePath) throws IOException {
    Path path = Paths.get(imagePath);
    return Files.readAllBytes(path);
  }

  public static byte[] base64ToImage(String base64String) {
    Decoder decoder = Base64.getDecoder();
    return decoder.decode(base64String);
  }

  public static String imageToBase64(Image image) {
    // Obtém os bytes da imagem do objeto Image
    byte[] imageData = image.getElement().getAttribute("src").getBytes();
    // Converte os bytes para uma string base64
    return Base64.getEncoder().encodeToString(imageData);
  }

  public static String removerCaracterCpf(String cpf) {
    cpf = cpf.replace(".", "");
    cpf = cpf.replace("-", "");
    return cpf;
  }

  public static boolean validarCpf(String cpf) {
    cpf = cpf.replace(".", "");
    cpf = cpf.replace("-", "");
    if(numerosInvalidos().contains(cpf)){
        return false;
    }
    try {
      Long.parseLong(cpf);
    } catch (NumberFormatException e) {
      return false;
    }
    int d1, d2;
    int digito1, digito2, resto;
    int digitoCPF;
    String nDigResult;
    d1 = d2 = 0;
    for (int nCount = 1; nCount < cpf.length() - 1; nCount++) {
      digitoCPF = Integer.valueOf(cpf.substring(nCount - 1, nCount)).intValue();
      d1 = d1 + (11 - nCount) * digitoCPF;
      d2 = d2 + (12 - nCount) * digitoCPF;
    }
    ;
    resto = (d1 % 11);
    if (resto < 2)
      digito1 = 0;
    else
      digito1 = 11 - resto;
    d2 += 2 * digito1;
    resto = (d2 % 11);
    if (resto < 2)
      digito2 = 0;
    else
      digito2 = 11 - resto;
    String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());
    nDigResult = String.valueOf(digito1) + String.valueOf(digito2);
    return nDigVerific.equals(nDigResult);
  }

  private List<String> numerosInvalidos(){
    return  Arrays.asList(
      "11111111111",
      "22222222222",
      "33333333333",
      "44444444444",
      "55555555555",
      "66666666666",
      "77777777777",
      "88888888888",
      "99999999999",
      "00000000000"
    );
  }
}
