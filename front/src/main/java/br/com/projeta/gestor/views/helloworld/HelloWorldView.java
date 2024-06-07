package br.com.projeta.gestor.views.helloworld;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import br.com.projeta.gestor.config.ExceptionHandler;
import br.com.projeta.gestor.services.AuthService;
import br.com.projeta.gestor.util.RoleCredential;
import br.com.projeta.gestor.views.MainLayout;

@PageTitle("Boas Vindas")
@Route(value = "projeta-gestor/home", layout = MainLayout.class)
@RouteAlias(value = "projeta-gestor", layout = MainLayout.class)
@RoleCredential({"MANAGER", "ADM", "ROOT", "CLIENTE", "MEDICO"})
public class HelloWorldView extends VerticalLayout {

    public HelloWorldView(AuthService authService) {
        authService.isLogged();
                Image img = new Image("images/projeta.png", "placeholder plant");
        img.setWidth("700px");
        add(img);

        H3 header = new H3("Bem vindo ao Projeta gestor");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        // setDefaultVerticalComponentAlignment(Alignment.CENTER);
        // setDefaultVerticalComponentAlignment(Alignment.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

        // Div welcomeMessage = new Div();
        // welcomeMessage.setText("Bem-vindo!");
        // welcomeMessage.getStyle()
        //         .set("font-size", "3em")
        //         .set("margin-left", "50px");

        // setAlignItems(Alignment.CENTER);
        // add(welcomeMessage);

        VaadinSession.getCurrent().setErrorHandler(new ExceptionHandler());
    }
}
