package location.builder;

import javafx.scene.control.ComboBox;

public class ComboBoxBuilder implements ComboBuilder {

    private ComboBox<String> comboBox = new ComboBox<>();

    @Override
    public void setItems(String... items) {
        comboBox.getItems().addAll(items);
    }

    @Override
    public void setPromptText(String promptText) {
        comboBox.setPromptText(promptText);
    }

    @Override
    public void setWidth(double width) {
        comboBox.setPrefWidth(width);
        comboBox.setMaxWidth(width);
    }

    @Override
    public void setValue(String value) {
        comboBox.setValue(value);
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }
}