package br.com.projeta.gestor.views.helloworld;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;

import br.com.projeta.gestor.config.ExceptionHandler;
import br.com.projeta.gestor.services.AuthService;
import br.com.projeta.gestor.util.RoleCredential;
import br.com.projeta.gestor.views.MainLayout;

@PageTitle("Boas Vindas")
@Route(value = "projeta-gestor/home", layout = MainLayout.class)
@RouteAlias(value = "projeta-gestor", layout = MainLayout.class)
@RoleCredential({"MANAGER", "ADM", "ROOT", "CLIENTE", "MEDICO"})
public class HelloWorldView extends HorizontalLayout {

    public HelloWorldView(AuthService authService) {
        authService.isLogged();
        Div welcomeMessage = new Div();
        welcomeMessage.setText("Bem-vindo!");
        welcomeMessage.getStyle()
                .set("font-size", "3em")
                .set("margin-left", "50px");

        setAlignItems(Alignment.CENTER);
        add(welcomeMessage);

        VaadinSession.getCurrent().setErrorHandler(new ExceptionHandler());
    }
}
