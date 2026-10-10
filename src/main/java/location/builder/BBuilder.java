package location.builder;

/*import javafx.scene.control.TextField;


public interface BBuilder {
    void setLabelName(String labekName );
    void setEvent(TextField textField);

 */


import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface BBuilder {

    void setLabelName(String labelName);

    void setEvent(EventHandler<ActionEvent> eventHandler);
}

