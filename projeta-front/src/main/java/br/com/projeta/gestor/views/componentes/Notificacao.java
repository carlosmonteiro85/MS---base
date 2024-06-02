package br.com.projeta.gestor.views.componentes;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

import br.com.projeta.gestor.data.enuns.TipoNotificacaoEnum;
import lombok.Data;

@Data
public class Notificacao {

  private String menssagem;
  private TipoNotificacaoEnum tipo;

  public Notificacao(String menssagem, TipoNotificacaoEnum tipo) {
    this.menssagem = menssagem;
    this.tipo = tipo;
  }

  public Notificacao(){}

  public void showNotification() {
    Notification notification = Notification.show(this.menssagem, 7000, Position.TOP_CENTER);
        
    switch (this.tipo) {
      case SUCESSO:
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        break;
      case ERROR:
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        break;
      case ALERTA:
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        break;
      default:
        break;
    }
  }
}
