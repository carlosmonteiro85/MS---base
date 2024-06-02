package br.com.projeta.gestor.views.gridwithfilters;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import br.com.projeta.gestor.data.dto.DominiosTelaUser;
import br.com.projeta.gestor.data.dto.FiltrosRequest;
import br.com.projeta.gestor.data.dto.UsuarioResponseFilter;
import br.com.projeta.gestor.services.AuthService;
import br.com.projeta.gestor.services.DominiosService;
import br.com.projeta.gestor.services.UserService;
import br.com.projeta.gestor.views.MainLayout;
import br.com.projeta.gestor.views.componentes.ComponentesComum;
import br.com.projeta.gestor.views.componentes.ItemCombo;

@PageTitle("Gestão Usuários")
@Route(value = "projeta-gestor/filters", layout = MainLayout.class)
@Uses(Icon.class)
public class GestaoUsuariosFiltroView extends Div {

    private Grid<UsuarioResponseFilter> grid;
    private Filters filters;
    private final UserService usuarioService;
    private final DominiosService dominiosService;

    public GestaoUsuariosFiltroView(UserService usuarioService, AuthService authService,
            DominiosService dominiosService) {
        authService.isLogged();
        this.usuarioService = usuarioService;
        this.dominiosService = dominiosService;

        DominiosTelaUser dominios = dominiosService.getDominios();

        setSizeFull();
        addClassNames("gridwith-filters-view");

        filters = new Filters(() -> refreshGrid(), dominios);
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private HorizontalLayout createMobileFilters() {
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filters.getClassNames().contains("visible")) {
                filters.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filters.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
    }

    public static class Filters extends Div {

        private final TextField name = new TextField("Nome");
        private final TextField cpf = new TextField("CPF");
        private final TextField phone = new TextField("Telefone");
        private final DatePicker startDate = new DatePicker("Data Nascimento");
        private final DatePicker endDate = new DatePicker();
        private final MultiSelectComboBox<ItemCombo> especialidade = new MultiSelectComboBox<>("Especialidade");
        private final CheckboxGroup<ItemCombo> perfil = new CheckboxGroup<>("Perfil");

        public Filters(Runnable onSearch, DominiosTelaUser dominios) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            name.setPlaceholder("Digite o nome");

            ComponentesComum.setMultiEspecialidades(especialidade, dominios);
            ComponentesComum.setCheckComboPerfil(perfil, dominios);
            perfil.addClassName("double-width");
            especialidade.setVisible(false);

            Button resetBtn = new Button("Limpar");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                name.clear();
                phone.clear();
                startDate.clear();
                endDate.clear();
                perfil.clear();
                especialidade.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Filtrar");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            perfil.addValueChangeListener(this::validarSelecaoPerfil);

            add(name, cpf, phone, createDateRangeFilter(), especialidade, perfil, actions);
        }

        private void validarSelecaoPerfil(ComponentValueChangeEvent<CheckboxGroup<ItemCombo>, Set<ItemCombo>> event) {
            Set<ItemCombo> opcoes = event.getValue();
            Optional<ItemCombo> any = opcoes.stream()
                    .filter(i -> i.label.equals("MEDICO")).findAny();
            if (any.isPresent()) {
                for (ItemCombo item : opcoes) {
                    if (!item.label.equals("MEDICO")) {
                        perfil.deselect(item);
                    }
                }
                especialidade.setVisible(true);
            } else {
                especialidade.setVisible(false);
            }
        }

        private Component createDateRangeFilter() {
            startDate.setPlaceholder("De");
            endDate.setPlaceholder("Até");
            startDate.setAriaLabel("From date");
            endDate.setAriaLabel("To date");
            FlexLayout dateRangeComponent = new FlexLayout(startDate, new Text(" – "), endDate);
            dateRangeComponent.setAlignItems(FlexComponent.Alignment.BASELINE);
            dateRangeComponent.addClassName(LumoUtility.Gap.XSMALL);
            return dateRangeComponent;
        }
    }

    private Component createGrid() {
        grid = new Grid<>(UsuarioResponseFilter.class, false);
        
        grid.addColumn(u -> u.getNome()).setHeader("Nome").setAutoWidth(true);
        grid.addColumn(u -> u.getCelular()).setHeader("Celular").setAutoWidth(true);
        grid.addColumn(u -> u.getTelefone()).setHeader("Telefone").setAutoWidth(true);
        grid.addColumn(u -> u.getPerfil()).setHeader("Perfil").setAutoWidth(true);
        grid.addColumn(u -> u.getDataNacimento()).setHeader("Data de Nascimento").setAutoWidth(true);
        grid.addColumn(u -> u.getEmail()).setHeader("Email").setAutoWidth(true);
        grid.addColumn(u -> u.getEspecialidade()).setHeader("Especialidade").setAutoWidth(true);
        
        // int pageSize = grid.getPageSize() > 0 ? grid.getPageSize() : 50; 
    
        grid.setItems(query -> {
            FiltrosRequest filtros = bindFilter(filters);
            Page<UsuarioResponseFilter> page = usuarioService.buscaFiltrada(
                    query.getOffset() / query.getLimit(), 
                    query.getLimit(), 
                    "id",
                    Sort.Direction.ASC,
                    filtros);
            return page.stream();
        });
    
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        
        return grid;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

    private FiltrosRequest bindFilter(Filters filters){
        List<Long> especialidades = filters.especialidade.getValue().stream().map(i ->  Long.valueOf(i.value)).toList();
        List<Long> perfils = filters.perfil.getValue().stream().map(p -> Long.valueOf(p.value)).toList();
        return FiltrosRequest.builder()
            .nome(filters.name.getValue().isBlank() || filters.name.getValue() == null ? null : filters.name.getValue())
            .cpf(filters.cpf.getValue().isBlank() || filters.cpf.getValue() == null ? null : filters.cpf.getValue() )
            .celularOuTelefone(filters.phone.getValue().isBlank() || filters.phone.getValue() == null ? null : filters.phone.getValue())
            .dataInicio(filters.startDate.getValue() != null ? filters.startDate.getValue() : null)
            .dataFim(filters.endDate.getValue() != null  ? filters.endDate.getValue() : null)
            .especialidades(!especialidades.isEmpty() ? especialidades : null)
            .perfils(!perfils.isEmpty() ? perfils : null)
        .build();
    }
}
