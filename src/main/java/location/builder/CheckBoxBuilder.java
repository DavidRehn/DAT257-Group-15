package location.builder;

import javafx.scene.control.CheckBox;

public class CheckBoxBuilder implements CBuilder {

    private CheckBox checkBox = new CheckBox();

    @Override
    public void setLabelName(String labelName) {
        checkBox.setText(labelName);
    }

    @Override
    public void setSelected(boolean selected) {
        checkBox.setSelected(selected);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}