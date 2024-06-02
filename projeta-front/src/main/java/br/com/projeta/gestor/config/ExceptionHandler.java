package br.com.projeta.gestor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;

import br.com.projeta.gestor.data.enuns.TipoNotificacaoEnum;
import br.com.projeta.gestor.util.Redirect;
import br.com.projeta.gestor.views.componentes.Notificacao;
import br.com.projeta.gestor.views.componentes.NotificacaoAlert;
import feign.FeignException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@NoArgsConstructor
public class ExceptionHandler implements ErrorHandler {

    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
            + "o problema persistir, entre em contato com o administrador do sistema.";

    @Autowired
    private Redirect redirectService;

    String nomeExeption = "";

    @Override
    public void error(ErrorEvent errorEvent) {

        Throwable throwable = errorEvent.getThrowable();

        String[] split = throwable.getClass().getCanonicalName().replace(".", "_").split("_");
        String canonicalName = throwable.getClass().getCanonicalName();
        System.out.println(canonicalName);
        String[] split2 = canonicalName.split(".");

        System.out.println(split[0]);

        if (split.length > 1) {
            nomeExeption = split[split.length - 1 ];
        } else if (split.length <= 1) {
            nomeExeption = throwable.getClass().getCanonicalName();
            System.out.println(nomeExeption);
        }

        switch (nomeExeption) {
            case "Exception":
                log.error("##### IMPLEMENTAR TRATAMENTO");
                handlethrowable(throwable);
                break;
            case "RuntimeException":
                handlethrowable(throwable);
                log.error("##### IMPLEMENTAR TRATAMENTO");
                break;
            case "Forbidden":
                handleRuntimeBeanForbiden();
                break;
            case "BadRequest":
                handleThrowableFeing(throwable);
                break;
            default:
                handlethrowable(throwable);
                break;
        }
    }

    private void handlethrowable(Throwable throwable) {
        log.error("Error: {}", throwable);
        NotificacaoAlert.getInstance().alerta("Error", MSG_ERRO_GENERICA_USUARIO_FINAL);
    }

    private void handleThrowableFeing(Throwable throwable) {
        log.error("Error: {}", throwable);
        String menssagem = "";

        int detailIndex = throwable.getMessage().indexOf("detail");

        if (detailIndex != -1) {
            detailIndex = detailIndex + "detail".length() + 2;
            int startIndex = throwable.getMessage().indexOf("\"", detailIndex);
            int endIndex = throwable.getMessage().indexOf("\"", startIndex + 1);
            String detailValue = throwable.getMessage().substring(startIndex + 1, endIndex);
            menssagem = detailValue;
        } else {
            menssagem = MSG_ERRO_GENERICA_USUARIO_FINAL;
        }
        NotificacaoAlert.getInstance().alerta("Error", menssagem);
    }

    private void handleRuntimeBeanForbiden() {
        redirectService.login();
    }
}