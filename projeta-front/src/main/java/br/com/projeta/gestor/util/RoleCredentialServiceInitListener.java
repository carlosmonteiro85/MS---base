package br.com.projeta.gestor.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

import br.com.projeta.gestor.config.ExceptionHandler;
import br.com.projeta.gestor.data.dto.RoleRespose;
import br.com.projeta.gestor.services.AuthService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleCredentialServiceInitListener implements VaadinServiceInitListener {

    private final AuthService authService;
    private final Redirect redirectService;
    private final ExceptionHandler exceptionHandler;

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            uiEvent.getUI().addBeforeEnterListener(beforeEnterEvent -> {
                try {
                    Class<?> navigationTarget = beforeEnterEvent.getNavigationTarget();
                    if (navigationTarget.isAnnotationPresent(RoleCredential.class)) {
                        String[] requiredRoles = navigationTarget.getAnnotation(RoleCredential.class).value();
                        boolean isValid = validarRequisicao(requiredRoles);
                        if (!isValid) {
                            redirectService.login();
                        }
                    }
                } catch (Exception e) {
                    exceptionHandler.error(new ErrorEvent(e));
                }
            });
        });
    }

    private boolean validarRequisicao(String[] requiredRoles) {
        boolean isValid = false;
        for (String requiredRole : requiredRoles) {
            List<RoleRespose> userHasRole = authService.userHasRole();
            boolean permissaoPresente = userHasRole.stream()
                    .anyMatch(perfil -> perfil.getPerfil().equals(requiredRole));
            if (!userHasRole.isEmpty() && permissaoPresente) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }
}
