package location.builder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ButtonBuilder implements BBuilder {

    private Button button = new Button();

    @Override
    public void setLabelName(String labelName) {
        button.setText(labelName);
    }

    @Override
    public void setEvent(EventHandler<ActionEvent> eventHandler) {
        button.setOnAction(eventHandler);
    }

    public Button getButton() {
        return button;
    }
}