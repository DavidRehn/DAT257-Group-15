package location.builder;

public interface ComboBuilder {

    void setItems(String... items);

    void setPromptText(String promptText);

    void setWidth(double width);

    void setValue(String value);
}