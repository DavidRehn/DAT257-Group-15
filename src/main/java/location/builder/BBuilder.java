package location.builder;




import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface BBuilder {

    void setLabelName(String labelName);

    void setEvent(EventHandler<ActionEvent> eventHandler);
}

