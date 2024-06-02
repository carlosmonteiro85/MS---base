package br.com.projeta.gestor.views.componentes;

import java.util.Objects;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class NotificacaoAlert extends Div {

  private Span status;
  private ConfirmDialog dialog;
  HorizontalLayout layout;
  public static NotificacaoAlert instance;

  public NotificacaoAlert() {
    layout = new HorizontalLayout();
    layout.setAlignItems(FlexComponent.Alignment.CENTER);
    layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
  }

  public static NotificacaoAlert getInstance() {
    if (Objects.isNull(instance)) {
      return new NotificacaoAlert();
    }
    return instance;
  }

  public void alerta(String titulo, String texto) {
    status = new Span();
    status.setVisible(true);
    dialog = new ConfirmDialog();
    dialog.setHeader(titulo);
    dialog.setText(texto);
    dialog.setConfirmText("OK");
    dialog.open();
    layout.add(status);
    add(layout);
  }

  public void confirmacao(String titulo, String texto, ConfirmationCallback callback) {
    status = new Span();
    status.setVisible(true);
    dialog = new ConfirmDialog();
    dialog.setHeader(titulo);
    dialog.setText(texto);
    dialog.setConfirmText("Sim");
    dialog.setCancelText("Não");
    dialog.setCancelable(true);

    dialog.addConfirmListener(event -> {
      callback.onConfirm(true);
      dialog.close();
    });

    dialog.addCancelListener(event -> {
      callback.onConfirm(false);
      dialog.close();
    });

    dialog.open();
    layout.add(status);
    add(layout);
  }

  public interface ConfirmationCallback {
    void onConfirm(boolean result);
  }
}
