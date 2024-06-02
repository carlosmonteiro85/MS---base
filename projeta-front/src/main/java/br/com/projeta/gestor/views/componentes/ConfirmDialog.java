// package br.com.projeta.gestor.views.componentes;
package br.com.projeta.gestor.views.componentes;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.function.Consumer;

public class ConfirmDialog extends Dialog {

  public ConfirmDialog(String message, Consumer<Boolean> callback) {
    VerticalLayout content = new VerticalLayout();
    content.setAlignItems(FlexComponent.Alignment.CENTER);
    content.add(new Text(message));
    
    Button confirmButton = new Button("Confirmar");
    confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
    ButtonVariant.LUMO_SUCCESS);
    confirmButton.addClickListener(e -> {
        callback.accept(true);
        close();
    });

    Button cancelButton = new Button("Cancelar");
    cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_PRIMARY);
    cancelButton.addClickListener(e -> {
        callback.accept(false);
        close();
    });

    HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
    buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    content.add(buttonLayout);
    
    content.setPadding(true);
    content.setSpacing(true);

    add(content);
    setCloseOnOutsideClick(false); 
}
}
