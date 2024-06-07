package br.com.projeta.gestor.views.componentes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AvatarDialog extends Dialog {

  private Image selectedAvatar;

  public AvatarDialog(Image avatarUsuario) {

    HorizontalLayout mainLayout = new HorizontalLayout();
    add(mainLayout);

    setHeaderTitle("Avatares");
    Button saveButton = createSaveButton(avatarUsuario);
    Button cancelButton = new Button("Fechar", e -> close());
    getFooter().add(saveButton);
    getFooter().add(cancelButton);

    VerticalLayout optionsLayout = new VerticalLayout();
    optionsLayout.getStyle().set("display", "grid");
    optionsLayout.getStyle().set("grid-template-columns", "repeat(4, 1fr)");
    optionsLayout.getStyle().set("grid-gap", "50px");
    optionsLayout.getStyle().set("margin-top", "50px");

    Image avatar1 = new Image("avatar/avatar1.png", "Avatar 1");
    avatar1.setMaxWidth("80px");
    avatar1.setMaxHeight("80px");
    Image avatar2 = new Image("avatar/avatar2.png", "Avatar 2");
    avatar2.setMaxWidth("80px");
    avatar2.setMaxHeight("80px");
    Image avatar3 = new Image("avatar/avatar3.png", "Avatar 3");
    avatar3.setMaxWidth("80px");
    avatar3.setMaxHeight("80px");
    Image avatar5 = new Image("avatar/avatar4.png", "Avatar 4");
    avatar5.setMaxWidth("80px");
    avatar5.setMaxHeight("80px");
    Image avatar6 = new Image("avatar/avatar5.png", "Avatar 4");
    avatar6.setMaxWidth("80px");
    avatar6.setMaxHeight("80px");
    Image avatar7 = new Image("avatar/avatar6.png", "Avatar 4");
    avatar7.setMaxWidth("80px");
    avatar7.setMaxHeight("80px");
    Image avatar8 = new Image("avatar/avatar7.png", "Avatar 4");
    avatar8.setMaxWidth("80px");
    avatar8.setMaxHeight("80px");
    Image avatar9 = new Image("avatar/avatar8.png", "Avatar 4");
    avatar9.setMaxWidth("80px");
    avatar9.setMaxHeight("80px");
    Image avatar10 = new Image("avatar/avatar9.png", "Avatar 4");
    avatar10.setMaxWidth("80px");
    avatar10.setMaxHeight("80px");
    Image avatar11 = new Image("avatar/avatar10.png", "Avatar 4");
    avatar11.setMaxWidth("80px");
    avatar11.setMaxHeight("80px");
    Image avatar12 = new Image("avatar/avatar11.png", "Avatar 4");
    avatar12.setMaxWidth("80px");
    avatar12.setMaxHeight("80px");
    Image avatar13 = new Image("avatar/avatar12.png", "Avatar 4");
    avatar13.setMaxWidth("80px");
    avatar13.setMaxHeight("80px");

    avatar1.addClickListener(e -> setSelectedAvatar(avatar1));
    avatar2.addClickListener(e -> setSelectedAvatar(avatar2));
    avatar3.addClickListener(e -> setSelectedAvatar(avatar3));
    avatar5.addClickListener(e -> setSelectedAvatar(avatar5));
    avatar6.addClickListener(e -> setSelectedAvatar(avatar6));
    avatar7.addClickListener(e -> setSelectedAvatar(avatar7));
    avatar8.addClickListener(e -> setSelectedAvatar(avatar8));
    avatar9.addClickListener(e -> setSelectedAvatar(avatar9));
    avatar10.addClickListener(e -> setSelectedAvatar(avatar10));
    avatar11.addClickListener(e -> setSelectedAvatar(avatar11));
    avatar12.addClickListener(e -> setSelectedAvatar(avatar12));
    avatar13.addClickListener(e -> setSelectedAvatar(avatar13));

    VerticalLayout selectedLayout = new VerticalLayout();
    selectedAvatar = new Image(avatarUsuario.getSrc(), "Selected Avatar");
    selectedAvatar.setMaxWidth("300px");
    selectedAvatar.setMaxHeight("300px");
    selectedLayout.add(selectedAvatar);

    optionsLayout.add(avatar1, avatar2, avatar3, avatar5, avatar6, avatar7, avatar8, avatar9, avatar10, avatar11,
        avatar12, avatar13);
    mainLayout.add(optionsLayout);
    selectedLayout.add(selectedAvatar);
    mainLayout.add(selectedLayout);
    add(mainLayout);
  }

  private void setSelectedAvatar(Image avatar) {
    selectedAvatar.setSrc(avatar.getSrc());
    selectedAvatar.setAlt(avatar.getAlt().get());
  }

  private Button createSaveButton(Image avatar) {
    Button saveButton = new Button("Carregar");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    saveButton.addClickListener(event -> {
      avatar.setSrc(selectedAvatar.getSrc());
      close();
    });
    return saveButton;
  }
}
