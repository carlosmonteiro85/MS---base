package br.com.projeta.gestor.views;

import java.util.Arrays;
import java.util.Optional;

import org.vaadin.lineawesome.LineAwesomeIcon;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

import br.com.projeta.gestor.data.dto.UserProfile;
import br.com.projeta.gestor.services.AuthService;
import br.com.projeta.gestor.services.DominiosService;
import br.com.projeta.gestor.services.UserService;
import br.com.projeta.gestor.util.Redirect;
import br.com.projeta.gestor.views.addressform.AddressFormView;
import br.com.projeta.gestor.views.componentes.ProfileDialog;
import br.com.projeta.gestor.views.creditcardform.CreditCardFormView;
import br.com.projeta.gestor.views.feed.FeedView;
import br.com.projeta.gestor.views.gridwithfilters.GestaoUsuariosFiltroView;
import br.com.projeta.gestor.views.helloworld.HelloWorldView;
import br.com.projeta.gestor.views.masterdetail.MasterDetailView;
import br.com.projeta.gestor.views.personform.UsuarioFormView;

/**
 * The main view is a top-level placeholder for other views.
 */
// @Component
public class MainLayout extends AppLayout {
    private H2 viewTitle;
    private AuthService authService;
    private Redirect redirectService;
    private DominiosService dominiosService;
    private UserService usuaService;

    public MainLayout(AuthService authService, Redirect redirectService, DominiosService dominiosService,
            UserService usuaService) {
        this.authService = authService;
        this.dominiosService = dominiosService;
        this.usuaService = usuaService;
        this.redirectService = redirectService;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {

        String imagePath = "images/vaadin.png";
        Image logo = new Image(imagePath, "Descrição do Logo");
        logo.setWidth("170px");
        logo.setHeight("50px");

        VerticalLayout logoContainer = new VerticalLayout(logo);
        logoContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        logoContainer.setHeight("67px");

        Scroller scroller = new Scroller(createNavigation());
        addToDrawer(logoContainer, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (authService.validarRole(Arrays.asList("ADMIN", "CLIENTE", "MEDICO", "ROOT"))) {
            nav.addItem(
                    new SideNavItem("Bem vindo", HelloWorldView.class, LineAwesomeIcon.GLOBE_SOLID.create()));
        }
        if (authService.validarRole(Arrays.asList("ADMIN", "ROOT"))) {
            nav.addItem(
                    new SideNavItem("Cadastro usuários", UsuarioFormView.class, LineAwesomeIcon.USER.create()));
        }
        if (authService.validarRole(Arrays.asList("ADMIN", "ROOT"))) {
            nav.addItem(
                    new SideNavItem("Gestão usuários", MasterDetailView.class, LineAwesomeIcon.COLUMNS_SOLID.create()));
        }
        if (authService.validarRole(Arrays.asList("ADMIN", "CLIENTE", "MEDICO", "ROOT"))) {
            nav.addItem(
                    new SideNavItem("Listagem Usuários", GestaoUsuariosFiltroView.class,
                            LineAwesomeIcon.FILTER_SOLID.create()));
        }
        nav.addItem(
                new SideNavItem("Address Form", AddressFormView.class, LineAwesomeIcon.MAP_MARKER_SOLID.create()));
        nav.addItem(new SideNavItem("Credit Card Form", CreditCardFormView.class,
                LineAwesomeIcon.CREDIT_CARD.create()));
        nav.addItem(new SideNavItem("Feed", FeedView.class, LineAwesomeIcon.LIST_SOLID.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<UserProfile> maybeUser = Optional.ofNullable(authService.getProfileUser());

        if (maybeUser.isPresent()) {
            Div div = new Div();
            UserProfile user = maybeUser.get();

            if (user.getAvatar() != null) {

                Image imagemAvatar = getImageAvatar(user);
                imagemAvatar.setWidth("35px");
                imagemAvatar.setHeight("35px");

                Avatar avatar = new Avatar(user.getNome());

                // StreamResource resource = new StreamResource("profile-pic",
                // () -> new ByteArrayInputStream(Utilitarios.base64ToImage(user.getAvatar())));
                // avatar.setImageResource(resource);

                avatar.setThemeName("xsmall");
                avatar.getElement().setAttribute("tabindex", "-1");
                div.add(imagemAvatar);

            }

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");

            div.add(user.getNome());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);

            userName.getSubMenu().addItem("Profile", e -> {
                ProfileDialog profileDialog = new ProfileDialog(maybeUser.get().getId(), dominiosService, usuaService);
                profileDialog.open();
            });

            userName.getSubMenu().addItem("Sign out", e -> {
                authService.logout();
                redirectService.site();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    private Image getImageAvatar(UserProfile user) {
        String pathAvatar = getPathAvatar(user.getAvatar());
        return new Image(pathAvatar, user.getNome().substring(0, 1).toUpperCase());
    }

    private String getPathAvatar(String avatar) {

        if (avatar.contains("avatar/") && avatar.contains(".png")) {
            return avatar;
        }

        return "avatar/avatar.png";
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
