package br.com.projeta.gestor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;

import br.com.projeta.gestor.data.enuns.TipoNotificacaoEnum;
import br.com.projeta.gestor.util.Redirect;
import br.com.projeta.gestor.views.componentes.Notificacao;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@NoArgsConstructor
public class ExceptionHandler implements ErrorHandler {

    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
            + "o problema persistir, entre em contato com o administrador do sistema.";
 
    private  Notificacao notificacao;
    @Autowired 
    private  Redirect redirectService;

    @Override
    public void error(ErrorEvent errorEvent) {

        Throwable throwable = errorEvent.getThrowable();
        String className = throwable.getClass().getSimpleName();

        switch (className) {
            case "Exception":
                log.error("##### IMPLEMENTAR TRATAMENTO");
                break;
            case "RuntimeException":
                log.error("##### IMPLEMENTAR TRATAMENTO");
                break;
            case "Forbidden":
                handleRuntimeBeanForbiden();
                break;
            default:
                handleOtherExceptions(throwable);
                break;
        }
    }

    private void handleOtherExceptions(Throwable throwable) {
        log.error("Error: {}", throwable);
        notificacao = new Notificacao(throwable.getMessage(),
                TipoNotificacaoEnum.ERROR);
        notificacao.showNotification();
    }

    private void handleRuntimeBeanForbiden() {
        redirectService.login();
    }
}